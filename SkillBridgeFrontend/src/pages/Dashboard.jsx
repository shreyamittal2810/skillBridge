import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import projectService from '../services/projectService';
import applicationService from '../services/applicationService';
import { FiFolder, FiBriefcase, FiClipboard, FiPlus, FiArrowRight } from 'react-icons/fi';
import './Dashboard.css';

const Dashboard = () => {
    const { user } = useAuth();
    const [stats, setStats] = useState({
        totalProjects: 0,
        myProjects: 0,
        pendingApplications: 0,
    });
    const [recentProjects, setRecentProjects] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const [projects, myApplications] = await Promise.all([
                    projectService.getAll(),
                    applicationService.getMyApplications(),
                ]);

                const myProjects = projects.filter(p => p.createdBy === user?.studentId);
                const pendingApps = myApplications.filter(a => a.applicationStatus === 'PENDING');

                setStats({
                    totalProjects: projects.length,
                    myProjects: myProjects.length,
                    pendingApplications: pendingApps.length,
                });

                setRecentProjects(projects.slice(0, 4));
            } catch (error) {
                console.error('Failed to fetch dashboard data:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, [user]);

    const statCards = [
        {
            icon: FiFolder,
            label: 'Total Projects',
            value: stats.totalProjects,
            color: 'primary',
            link: '/projects'
        },
        {
            icon: FiBriefcase,
            label: 'My Projects',
            value: stats.myProjects,
            color: 'success',
            link: '/my-projects'
        },
        {
            icon: FiClipboard,
            label: 'Pending Applications',
            value: stats.pendingApplications,
            color: 'warning',
            link: '/my-applications'
        },
    ];

    return (
        <div className="page dashboard">
            <div className="dashboard-header">
                <div className="welcome-section">
                    <h1 className="welcome-title">
                        Welcome back, <span className="text-gradient">{user?.name || 'Student'}</span>! 👋
                    </h1>
                    <p className="welcome-subtitle">
                        Ready to collaborate on amazing projects? Here's your overview.
                    </p>
                </div>
                <Link to="/create-project" className="btn btn-primary">
                    <FiPlus /> Create Project
                </Link>
            </div>

            <div className="stats-grid">
                {statCards.map((stat, index) => (
                    <Link to={stat.link} key={index} className={`stat-card stat-${stat.color}`}>
                        <div className="stat-icon">
                            <stat.icon />
                        </div>
                        <div className="stat-info">
                            <span className="stat-value">{loading ? '...' : stat.value}</span>
                            <span className="stat-label">{stat.label}</span>
                        </div>
                    </Link>
                ))}
            </div>

            <div className="dashboard-section">
                <div className="section-header">
                    <h2 className="section-title">Recent Projects</h2>
                    <Link to="/projects" className="section-link">
                        View All <FiArrowRight />
                    </Link>
                </div>

                {loading ? (
                    <div className="loader-container">
                        <div className="loader"></div>
                    </div>
                ) : recentProjects.length > 0 ? (
                    <div className="recent-projects-grid">
                        {recentProjects.map((project) => (
                            <Link
                                to={`/projects/${project.projectId}`}
                                key={project.projectId}
                                className="recent-project-card"
                            >
                                <div className="project-status-dot" data-status={project.status}></div>
                                <h3 className="project-name">{project.title}</h3>
                                <p className="project-desc">
                                    {project.description?.substring(0, 80)}...
                                </p>
                                <div className="project-meta">
                                    <span className="project-skills-count">
                                        {project.requiredSkills?.length || 0} skills required
                                    </span>
                                    <span className="project-members-count">
                                        👥 {project.teamMembers?.length || 0}
                                    </span>
                                </div>
                            </Link>
                        ))}
                    </div>
                ) : (
                    <div className="empty-state">
                        <div className="empty-state-icon">📁</div>
                        <h3 className="empty-state-title">No projects yet</h3>
                        <p>Be the first to create a project!</p>
                        <Link to="/create-project" className="btn btn-primary mt-2">
                            <FiPlus /> Create Project
                        </Link>
                    </div>
                )}
            </div>

            <div className="dashboard-section">
                <h2 className="section-title">Quick Actions</h2>
                <div className="quick-actions">
                    <Link to="/projects" className="quick-action-card">
                        <FiFolder className="quick-action-icon" />
                        <span>Browse Projects</span>
                    </Link>
                    <Link to="/create-project" className="quick-action-card">
                        <FiPlus className="quick-action-icon" />
                        <span>Create Project</span>
                    </Link>
                    <Link to="/my-applications" className="quick-action-card">
                        <FiClipboard className="quick-action-icon" />
                        <span>My Applications</span>
                    </Link>
                </div>
            </div>
        </div>
    );
};

export default Dashboard;
