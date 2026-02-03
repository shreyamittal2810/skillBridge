import { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import projectService from '../../services/projectService';
import applicationService from '../../services/applicationService';
import studentService from '../../services/studentService';
import StatusBadge from '../../components/ui/StatusBadge';
import SkillTag from '../../components/ui/SkillTag';
import Modal from '../../components/ui/Modal';
import Loader from '../../components/ui/Loader';
import { FiEdit, FiTrash2, FiUsers, FiSend, FiCheck, FiX, FiArrowLeft } from 'react-icons/fi';
import './ProjectDetail.css';

const ProjectDetail = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const { user } = useAuth();
    const [project, setProject] = useState(null);
    const [applications, setApplications] = useState([]);
    const [loading, setLoading] = useState(true);
    const [showApplyModal, setShowApplyModal] = useState(false);
    const [showApplicationsModal, setShowApplicationsModal] = useState(false);
    const [applyMessage, setApplyMessage] = useState('');
    const [applying, setApplying] = useState(false);
    const [error, setError] = useState('');
    const [memberDetails, setMemberDetails] = useState({});

    const isOwner = Number(project?.createdBy) === Number(user?.studentId);
    const isMember = project?.teamMembers?.some(m => Number(m) === Number(user?.studentId));

    useEffect(() => {
        const fetchProject = async () => {
            try {
                const data = await projectService.getById(id);
                setProject(data);
                // Fetch member details
                if (data.teamMembers?.length > 0) {
                    const details = {};
                    for (const memberId of data.teamMembers) {
                        try {
                            const member = await studentService.getById(memberId);
                            details[memberId] = member;
                        } catch (err) {
                            console.error(`Failed to fetch member ${memberId}:`, err);
                            details[memberId] = { name: `Member #${memberId}` };
                        }
                    }
                    setMemberDetails(details);
                }
            } catch (error) {
                console.error('Failed to fetch project:', error);
                setError('Project not found');
            } finally {
                setLoading(false);
            }
        };

        fetchProject();
    }, [id]);

    const fetchApplications = async () => {
        try {
            const data = await projectService.getApplications(id);
            setApplications(data);
        } catch (error) {
            console.error('Failed to fetch applications:', error);
        }
    };

    const handleApply = async (e) => {
        e.preventDefault();
        setApplying(true);
        try {
            await applicationService.apply({
                projectId: parseInt(id),
                messageText: applyMessage,
            });
            setShowApplyModal(false);
            setApplyMessage('');
            alert('Application submitted successfully!');
        } catch (error) {
            alert(error.response?.data?.message || 'Failed to apply');
        } finally {
            setApplying(false);
        }
    };

    const handleApplicationAction = async (appId, status) => {
        try {
            await applicationService.updateStatus(appId, status);
            fetchApplications();
            if (status === 'ACCEPTED') {
                // Refresh project to show new member
                const updatedProject = await projectService.getById(id);
                setProject(updatedProject);
                // Fetch details for new members
                if (updatedProject.teamMembers?.length > 0) {
                    const details = { ...memberDetails };
                    for (const memberId of updatedProject.teamMembers) {
                        if (!details[memberId]) {
                            try {
                                const member = await studentService.getById(memberId);
                                details[memberId] = member;
                            } catch (err) {
                                details[memberId] = { name: `Member #${memberId}` };
                            }
                        }
                    }
                    setMemberDetails(details);
                }
            }
        } catch (error) {
            alert(error.response?.data?.message || 'Failed to update application');
        }
    };

    const handleStatusChange = async (newStatus) => {
        try {
            const updated = await projectService.updateStatus(id, newStatus);
            setProject(updated);
        } catch (error) {
            alert(error.response?.data?.message || 'Failed to update status');
        }
    };

    const handleDelete = async () => {
        if (window.confirm('Are you sure you want to delete this project?')) {
            try {
                await projectService.delete(id);
                navigate('/my-projects');
            } catch (error) {
                alert(error.response?.data?.message || 'Failed to delete project');
            }
        }
    };

    if (loading) {
        return <Loader text="Loading project..." />;
    }

    if (error) {
        return (
            <div className="page">
                <div className="empty-state">
                    <div className="empty-state-icon">❌</div>
                    <h3 className="empty-state-title">{error}</h3>
                    <Link to="/projects" className="btn btn-primary mt-2">
                        <FiArrowLeft /> Back to Projects
                    </Link>
                </div>
            </div>
        );
    }

    return (
        <div className="page project-detail-page">
            <Link to="/projects" className="back-link">
                <FiArrowLeft /> Back to Projects
            </Link>

            <div className="project-detail-header">
                <div className="project-detail-info">
                    <div className="project-title-row">
                        <h1 className="project-detail-title">{project.title}</h1>
                        <StatusBadge status={project.status} />
                    </div>
                    <p className="project-owner">Created by User #{project.createdBy}</p>
                </div>

                <div className="project-actions">
                    {isOwner && (
                        <>
                            <button
                                className="btn btn-secondary"
                                onClick={() => {
                                    fetchApplications();
                                    setShowApplicationsModal(true);
                                }}
                            >
                                <FiUsers /> View Applications
                            </button>
                            <Link to={`/projects/${id}/edit`} className="btn btn-secondary">
                                <FiEdit /> Edit
                            </Link>
                            <button className="btn btn-danger" onClick={handleDelete}>
                                <FiTrash2 /> Delete
                            </button>
                        </>
                    )}
                    {!isOwner && !isMember && project.status === 'OPEN' && (
                        <button className="btn btn-primary" onClick={() => setShowApplyModal(true)}>
                            <FiSend /> Apply to Join
                        </button>
                    )}
                    {isMember && !isOwner && (
                        <span className="member-badge">✓ You're a member</span>
                    )}
                </div>
            </div>

            <div className="project-detail-content">
                <div className="project-main">
                    <section className="detail-section">
                        <h2 className="section-heading">Description</h2>
                        <p className="project-description">{project.description}</p>
                    </section>

                    <section className="detail-section">
                        <h2 className="section-heading">Required Skills</h2>
                        <div className="skills-container">
                            {project.requiredSkills?.map((skill, index) => (
                                <SkillTag key={index} skill={skill} />
                            ))}
                        </div>
                    </section>

                    {isOwner && (
                        <section className="detail-section">
                            <h2 className="section-heading">Project Status</h2>
                            <div className="status-actions">
                                {project.status === 'OPEN' && (
                                    <button
                                        className="btn btn-secondary"
                                        onClick={() => handleStatusChange('IN_PROGRESS')}
                                    >
                                        Start Project
                                    </button>
                                )}
                                {project.status === 'IN_PROGRESS' && (
                                    <button
                                        className="btn btn-success"
                                        onClick={() => handleStatusChange('COMPLETED')}
                                    >
                                        Mark as Completed
                                    </button>
                                )}
                            </div>
                        </section>
                    )}
                </div>

                <aside className="project-sidebar">
                    <div className="sidebar-card">
                        <h3 className="sidebar-title">Team Members</h3>
                        <div className="team-members">
                            <div className="team-member owner">
                                <span className="member-avatar">👑</span>
                                <span>{memberDetails[project.createdBy]?.name || `Owner #${project.createdBy}`}</span>
                            </div>
                            {project.teamMembers?.filter(m => Number(m) !== Number(project.createdBy)).map((memberId) => (
                                <div key={memberId} className="team-member">
                                    <span className="member-avatar">👤</span>
                                    <span>{memberDetails[memberId]?.name || `Member #${memberId}`}</span>
                                </div>
                            ))}
                            {project.teamMembers?.filter(m => Number(m) !== Number(project.createdBy)).length === 0 && (
                                <p className="no-members">No other members yet</p>
                            )}
                        </div>
                    </div>

                    <div className="sidebar-card">
                        <h3 className="sidebar-title">Project Info</h3>
                        <div className="info-list">
                            <div className="info-item">
                                <span className="info-label">Status</span>
                                <StatusBadge status={project.status} />
                            </div>
                            <div className="info-item">
                                <span className="info-label">Team Size</span>
                                <span>{project.teamMembers?.length || 1} members</span>
                            </div>
                            <div className="info-item">
                                <span className="info-label">Skills Required</span>
                                <span>{project.requiredSkills?.length || 0}</span>
                            </div>
                        </div>
                    </div>
                </aside>
            </div>

            {/* Apply Modal */}
            <Modal
                isOpen={showApplyModal}
                onClose={() => setShowApplyModal(false)}
                title="Apply to Join Project"
            >
                <form onSubmit={handleApply}>
                    <div className="form-group">
                        <label className="form-label">Why do you want to join this project?</label>
                        <textarea
                            className="form-input form-textarea"
                            placeholder="Describe your interest and skills..."
                            value={applyMessage}
                            onChange={(e) => setApplyMessage(e.target.value)}
                            required
                        />
                    </div>
                    <div className="modal-actions">
                        <button
                            type="button"
                            className="btn btn-secondary"
                            onClick={() => setShowApplyModal(false)}
                        >
                            Cancel
                        </button>
                        <button
                            type="submit"
                            className="btn btn-primary"
                            disabled={applying}
                        >
                            {applying ? 'Submitting...' : 'Submit Application'}
                        </button>
                    </div>
                </form>
            </Modal>

            {/* Applications Modal */}
            <Modal
                isOpen={showApplicationsModal}
                onClose={() => setShowApplicationsModal(false)}
                title="Project Applications"
                size="lg"
            >
                {applications.length > 0 ? (
                    <div className="applications-list">
                        {applications.map((app) => (
                            <div key={app.applicationId} className="application-item">
                                <div className="application-header">
                                    <span className="applicant-id">User #{app.senderId}</span>
                                    <StatusBadge status={app.applicationStatus} />
                                </div>
                                <p className="application-message">{app.message}</p>
                                <p className="application-date">
                                    Applied: {new Date(app.createdOn).toLocaleDateString()}
                                </p>
                                {app.applicationStatus === 'PENDING' && (
                                    <div className="application-actions">
                                        <button
                                            className="btn btn-success btn-sm"
                                            onClick={() => handleApplicationAction(app.applicationId, 'ACCEPTED')}
                                        >
                                            <FiCheck /> Accept
                                        </button>
                                        <button
                                            className="btn btn-danger btn-sm"
                                            onClick={() => handleApplicationAction(app.applicationId, 'REJECTED')}
                                        >
                                            <FiX /> Reject
                                        </button>
                                    </div>
                                )}
                            </div>
                        ))}
                    </div>
                ) : (
                    <div className="empty-state">
                        <p>No applications yet</p>
                    </div>
                )}
            </Modal>
        </div>
    );
};

export default ProjectDetail;
