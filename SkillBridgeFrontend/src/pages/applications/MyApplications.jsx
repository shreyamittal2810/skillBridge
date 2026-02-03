import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import applicationService from '../../services/applicationService';
import StatusBadge from '../../components/ui/StatusBadge';
import Loader from '../../components/ui/Loader';
import { FiExternalLink, FiTrash2 } from 'react-icons/fi';
import './MyApplications.css';

const MyApplications = () => {
    const [applications, setApplications] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchApplications();
    }, []);

    const fetchApplications = async () => {
        try {
            const data = await applicationService.getMyApplications();
            setApplications(data);
        } catch (error) {
            console.error('Failed to fetch applications:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async (id) => {
        if (window.confirm('Are you sure you want to withdraw this application?')) {
            try {
                await applicationService.delete(id);
                fetchApplications();
            } catch (error) {
                alert(error.response?.data?.message || 'Failed to delete application');
            }
        }
    };

    const getStatusMessage = (status) => {
        switch (status) {
            case 'PENDING':
                return 'Waiting for project owner to review';
            case 'ACCEPTED':
                return 'Congratulations! You\'ve been accepted to the team';
            case 'REJECTED':
                return 'Your application was not accepted';
            default:
                return '';
        }
    };

    return (
        <div className="page my-applications-page">
            <div className="page-header">
                <h1 className="page-title">My Applications</h1>
                <p className="page-subtitle">Track your project applications and their status</p>
            </div>

            {loading ? (
                <Loader text="Loading applications..." />
            ) : applications.length > 0 ? (
                <div className="applications-list">
                    {applications.map((app) => (
                        <div key={app.applicationId} className="application-card">
                            <div className="application-card-header">
                                <div className="application-project">
                                    <h3>Project #{app.projectId}</h3>
                                    <Link to={`/projects/${app.projectId}`} className="view-project-link">
                                        <FiExternalLink /> View Project
                                    </Link>
                                </div>
                                <StatusBadge status={app.applicationStatus} />
                            </div>

                            <p className="application-message-text">{app.message}</p>

                            <div className="application-footer">
                                <span className="application-date">
                                    Applied: {new Date(app.createdOn).toLocaleDateString()}
                                </span>
                                <span className="status-message">{getStatusMessage(app.applicationStatus)}</span>
                            </div>

                            {app.applicationStatus === 'PENDING' && (
                                <button
                                    className="btn btn-danger btn-sm delete-btn"
                                    onClick={() => handleDelete(app.applicationId)}
                                >
                                    <FiTrash2 /> Withdraw
                                </button>
                            )}
                        </div>
                    ))}
                </div>
            ) : (
                <div className="empty-state">
                    <div className="empty-state-icon">📋</div>
                    <h3 className="empty-state-title">No applications yet</h3>
                    <p>Browse projects and apply to join exciting teams!</p>
                    <Link to="/projects" className="btn btn-primary mt-2">
                        Browse Projects
                    </Link>
                </div>
            )}
        </div>
    );
};

export default MyApplications;
