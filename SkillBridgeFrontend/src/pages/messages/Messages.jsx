import { useState, useEffect, useRef } from 'react';
import { useAuth } from '../../context/AuthContext';
import messageService from '../../services/messageService';
import Loader from '../../components/ui/Loader';
import { FiSend, FiMessageCircle, FiUser, FiSearch } from 'react-icons/fi';
import './Messages.css';

const Messages = () => {
    const { user } = useAuth();
    const [students, setStudents] = useState([]);
    const [messages, setMessages] = useState([]);
    const [selectedStudent, setSelectedStudent] = useState(null);
    const [newMessage, setNewMessage] = useState('');
    const [loading, setLoading] = useState(true);
    const [sending, setSending] = useState(false);
    const [searchTerm, setSearchTerm] = useState('');
    const [error, setError] = useState('');
    const messagesEndRef = useRef(null);

    useEffect(() => {
        fetchData();
    }, []);

    useEffect(() => {
        scrollToBottom();
    }, [messages, selectedStudent]);

    const fetchData = async () => {
        try {
            setError('');
            // Fetch students first
            let studentsData = [];
            try {
                studentsData = await messageService.getAllStudents();
                console.log('Students fetched:', studentsData);
            } catch (studentError) {
                console.error('Failed to fetch students:', studentError);
                setError('Failed to load students. Make sure SMS Service is running.');
            }

            // Fetch messages (might fail if no direct_messages table)
            let messagesData = [];
            try {
                messagesData = await messageService.getMyMessages();
                console.log('Messages fetched:', messagesData);
            } catch (msgError) {
                console.error('Failed to fetch messages:', msgError);
                // Don't set error for messages, just log it
            }

            // Filter out current user from students list
            setStudents(studentsData.filter(s => s.studentId !== user?.studentId));
            setMessages(messagesData);
        } catch (error) {
            console.error('Failed to fetch data:', error);
            setError('Failed to load data. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
    };

    const getConversation = (studentId) => {
        return messages
            .filter(
                (m) =>
                    (m.senderStudentId === user?.studentId && m.receiverStudentId === studentId) ||
                    (m.senderStudentId === studentId && m.receiverStudentId === user?.studentId)
            )
            .sort((a, b) => new Date(a.createdAt) - new Date(b.createdAt));
    };

    const getLastMessage = (studentId) => {
        const conversation = getConversation(studentId);
        return conversation[conversation.length - 1];
    };

    const getUnreadCount = (studentId) => {
        return messages.filter(
            (m) => m.senderStudentId === studentId && m.receiverStudentId === user?.studentId
        ).length;
    };

    const handleSend = async (e) => {
        e.preventDefault();
        if (!newMessage.trim() || !selectedStudent) return;

        setSending(true);
        try {
            await messageService.send(selectedStudent.studentId, newMessage);
            setNewMessage('');
            // Refresh messages
            const messagesData = await messageService.getMyMessages();
            setMessages(messagesData);
        } catch (error) {
            console.error('Failed to send message:', error);
            alert('Failed to send message');
        } finally {
            setSending(false);
        }
    };

    const filteredStudents = students.filter((s) =>
        s.name?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        s.email?.toLowerCase().includes(searchTerm.toLowerCase())
    );

    // Sort students by last message time
    const sortedStudents = [...filteredStudents].sort((a, b) => {
        const lastA = getLastMessage(a.studentId);
        const lastB = getLastMessage(b.studentId);
        if (!lastA && !lastB) return 0;
        if (!lastA) return 1;
        if (!lastB) return -1;
        return new Date(lastB.createdAt) - new Date(lastA.createdAt);
    });

    if (loading) {
        return <Loader text="Loading messages..." />;
    }

    return (
        <div className="page messages-page">
            <div className="messages-container">
                {/* Sidebar - Student List */}
                <div className="messages-sidebar">
                    <div className="sidebar-header">
                        <h2><FiMessageCircle /> Messages</h2>
                    </div>
                    <div className="search-box">
                        <FiSearch className="search-icon" />
                        <input
                            type="text"
                            placeholder="Search students..."
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                        />
                    </div>
                    <div className="students-list">
                        {sortedStudents.length > 0 ? (
                            sortedStudents.map((student) => {
                                const lastMessage = getLastMessage(student.studentId);
                                const unreadCount = getUnreadCount(student.studentId);
                                return (
                                    <div
                                        key={student.studentId}
                                        className={`student-item ${selectedStudent?.studentId === student.studentId ? 'active' : ''}`}
                                        onClick={() => setSelectedStudent(student)}
                                    >
                                        <div className="student-avatar">
                                            <FiUser />
                                        </div>
                                        <div className="student-info">
                                            <div className="student-name">{student.name}</div>
                                            <div className="last-message">
                                                {lastMessage?.message?.substring(0, 30) || 'No messages yet'}
                                                {lastMessage?.message?.length > 30 && '...'}
                                            </div>
                                        </div>
                                        {unreadCount > 0 && (
                                            <span className="unread-badge">{unreadCount}</span>
                                        )}
                                    </div>
                                );
                            })
                        ) : (
                            <div className="no-students">
                                {error ? (
                                    <>
                                        <p style={{ color: '#ef4444' }}>⚠️ {error}</p>
                                        <button
                                            className="btn btn-secondary"
                                            style={{ marginTop: '10px' }}
                                            onClick={fetchData}
                                        >
                                            Retry
                                        </button>
                                    </>
                                ) : (
                                    <p>No students found</p>
                                )}
                            </div>
                        )}
                    </div>
                </div>

                {/* Chat Area */}
                <div className="chat-area">
                    {selectedStudent ? (
                        <>
                            <div className="chat-header">
                                <div className="chat-user">
                                    <div className="user-avatar"><FiUser /></div>
                                    <div className="user-info">
                                        <h3>{selectedStudent.name}</h3>
                                        <span>{selectedStudent.email}</span>
                                    </div>
                                </div>
                            </div>

                            <div className="chat-messages">
                                {getConversation(selectedStudent.studentId).length > 0 ? (
                                    getConversation(selectedStudent.studentId).map((msg) => (
                                        <div
                                            key={msg.id}
                                            className={`message ${msg.senderStudentId === user?.studentId ? 'sent' : 'received'}`}
                                        >
                                            <div className="message-content">
                                                <p>{msg.message}</p>
                                                <span className="message-time">
                                                    {new Date(msg.createdAt).toLocaleTimeString([], {
                                                        hour: '2-digit',
                                                        minute: '2-digit',
                                                    })}
                                                </span>
                                            </div>
                                        </div>
                                    ))
                                ) : (
                                    <div className="no-messages">
                                        <FiMessageCircle size={48} />
                                        <p>No messages yet</p>
                                        <span>Start a conversation with {selectedStudent.name}</span>
                                    </div>
                                )}
                                <div ref={messagesEndRef} />
                            </div>

                            <form className="chat-input" onSubmit={handleSend}>
                                <input
                                    type="text"
                                    placeholder="Type a message..."
                                    value={newMessage}
                                    onChange={(e) => setNewMessage(e.target.value)}
                                    disabled={sending}
                                />
                                <button type="submit" disabled={sending || !newMessage.trim()}>
                                    <FiSend />
                                </button>
                            </form>
                        </>
                    ) : (
                        <div className="no-chat-selected">
                            <FiMessageCircle size={64} />
                            <h3>Select a conversation</h3>
                            <p>Choose a student from the list to start chatting</p>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default Messages;
