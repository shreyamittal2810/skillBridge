import { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import adminService from '../../services/adminService';
import Loader from '../../components/ui/Loader';
import { FiUsers, FiFolder, FiTrash2, FiShield, FiUser, FiX } from 'react-icons/fi';
import './AdminDashboard.css';

const AdminDashboard = () => {
    const { user } = useAuth();
    const navigate = useNavigate();
    const [activeTab, setActiveTab] = useState('students');
    const [students, setStudents] = useState([]);
    const [projects, setProjects] = useState([]);
    const [loading, setLoading] = useState(true);
    const [actionLoading, setActionLoading] = useState(null);

    useEffect(() => {
        // Check if user is admin - handle both string and case variations
        const userRole = user?.role?.toUpperCase?.() || '';
        if (userRole !== 'ADMIN') {
            navigate('/dashboard');
            return;
        }
        fetchData();
    }, [user, navigate]);

    const fetchData = async () => {
        setLoading(true);
        try {
            const [studentsData, projectsData] = await Promise.all([
                adminService.getAllStudents(),
                adminService.getAllProjects()
            ]);
            setStudents(studentsData);
            setProjects(projectsData);
        } catch (error) {
            console.error('Failed to fetch admin data:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleRoleChange = async (studentId, newRole) => {
        setActionLoading(`role-${studentId}`);
        try {
            await adminService.updateRole(studentId, newRole);
            setStudents(students.map(s =>
                s.studentId === studentId ? { ...s, role: newRole } : s
            ));
        } catch (error) {
            alert('Failed to update role: ' + (error.response?.data?.message || error.message));
        } finally {
            setActionLoading(null);
        }
    };

    const handleDeleteStudent = async (studentId) => {
        if (!window.confirm('Are you sure you want to delete this student?')) return;
        setActionLoading(`delete-student-${studentId}`);
        try {
            await adminService.deleteStudent(studentId);
            setStudents(students.filter(s => s.studentId !== studentId));
        } catch (error) {
            alert('Failed to delete student: ' + (error.response?.data?.message || error.message));
        } finally {
            setActionLoading(null);
        }
    };

    const handleStatusChange = async (projectId, newStatus) => {
        setActionLoading(`status-${projectId}`);
        try {
            await adminService.updateProjectStatus(projectId, newStatus);
            setProjects(projects.map(p =>
                p.projectId === projectId ? { ...p, status: newStatus } : p
            ));
        } catch (error) {
            alert('Failed to update status: ' + (error.response?.data?.message || error.message));
        } finally {
            setActionLoading(null);
        }
    };

    const handleDeleteProject = async (projectId) => {
        if (!window.confirm('Are you sure you want to delete this project?')) return;
        setActionLoading(`delete-project-${projectId}`);
        try {
            await adminService.deleteProject(projectId);
            setProjects(projects.filter(p => p.projectId !== projectId));
        } catch (error) {
            alert('Failed to delete project: ' + (error.response?.data?.message || error.message));
        } finally {
            setActionLoading(null);
        }
    };

    if (loading) {
        return <Loader text="Loading admin dashboard..." />;
    }

    return (
        <div className="page admin-dashboard">
            <div className="admin-header">
                <h1><FiShield /> Admin Dashboard</h1>
                <p>Manage students and projects</p>
            </div>

            <div className="admin-stats">
                <div className="stat-card">
                    <FiUsers />
                    <div>
                        <h3>{students.length}</h3>
                        <span>Total Students</span>
                    </div>
                </div>
                <div className="stat-card">
                    <FiFolder />
                    <div>
                        <h3>{projects.length}</h3>
                        <span>Total Projects</span>
                    </div>
                </div>
            </div>

            <div className="admin-tabs">
                <button
                    className={`tab-btn ${activeTab === 'students' ? 'active' : ''}`}
                    onClick={() => setActiveTab('students')}
                >
                    <FiUsers /> Students
                </button>
                <button
                    className={`tab-btn ${activeTab === 'projects' ? 'active' : ''}`}
                    onClick={() => setActiveTab('projects')}
                >
                    <FiFolder /> Projects
                </button>
            </div>

            {activeTab === 'students' && (
                <div className="admin-table-container">
                    <table className="admin-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Name</th>
                                <th>Email</th>
                                <th>Course</th>
                                <th>Role</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {students.map(student => (
                                <tr key={student.studentId}>
                                    <td>{student.studentId}</td>
                                    <td>{student.name}</td>
                                    <td>{student.email}</td>
                                    <td>{student.course || '-'}</td>
                                    <td>
                                        <select
                                            value={student.role || 'STUDENT'}
                                            onChange={(e) => handleRoleChange(student.studentId, e.target.value)}
                                            disabled={actionLoading === `role-${student.studentId}`}
                                            className={`role-select ${student.role?.toLowerCase()}`}
                                        >
                                            <option value="STUDENT">Student</option>
                                            <option value="ADMIN">Admin</option>
                                        </select>
                                    </td>
                                    <td>
                                        <button
                                            className="btn-icon danger"
                                            onClick={() => handleDeleteStudent(student.studentId)}
                                            disabled={actionLoading === `delete-student-${student.studentId}`}
                                            title="Delete Student"
                                        >
                                            <FiTrash2 />
                                        </button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            )}

            {activeTab === 'projects' && (
                <div className="admin-table-container">
                    <table className="admin-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Title</th>
                                <th>Created By</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {projects.map(project => (
                                <tr key={project.projectId}>
                                    <td>{project.projectId}</td>
                                    <td>{project.title}</td>
                                    <td>{project.createdBy}</td>
                                    <td>
                                        <select
                                            value={project.status || 'OPEN'}
                                            onChange={(e) => handleStatusChange(project.projectId, e.target.value)}
                                            disabled={actionLoading === `status-${project.projectId}`}
                                            className={`status-select ${project.status?.toLowerCase()}`}
                                        >
                                            <option value="OPEN">Open</option>
                                            <option value="CLOSED">Closed</option>
                                            <option value="CANCELLED">Cancelled</option>
                                        </select>
                                    </td>
                                    <td>
                                        <button
                                            className="btn-icon danger"
                                            onClick={() => handleDeleteProject(project.projectId)}
                                            disabled={actionLoading === `delete-project-${project.projectId}`}
                                            title="Delete Project"
                                        >
                                            <FiTrash2 />
                                        </button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
};

export default AdminDashboard;
