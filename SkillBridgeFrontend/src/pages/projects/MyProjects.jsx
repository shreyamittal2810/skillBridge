import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import projectService from '../../services/projectService';
import ProjectCard from '../../components/ui/ProjectCard';
import Loader from '../../components/ui/Loader';
import { FiPlus } from 'react-icons/fi';
import './MyProjects.css';

const MyProjects = () => {
    const { user } = useAuth();
    const [projects, setProjects] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchMyProjects = async () => {
            try {
                const allProjects = await projectService.getAll();
                const myProjects = allProjects.filter(
                    (p) => p.createdBy === user?.studentId
                );
                setProjects(myProjects);
            } catch (error) {
                console.error('Failed to fetch projects:', error);
            } finally {
                setLoading(false);
            }
        };

        if (user) {
            fetchMyProjects();
        }
    }, [user]);

    return (
        <div className="page my-projects-page">
            <div className="page-header">
                <div>
                    <h1 className="page-title">My Projects</h1>
                    <p className="page-subtitle">Projects you've created and manage</p>
                </div>
                <Link to="/create-project" className="btn btn-primary">
                    <FiPlus /> Create New
                </Link>
            </div>

            {loading ? (
                <Loader text="Loading your projects..." />
            ) : projects.length > 0 ? (
                <div className="projects-grid">
                    {projects.map((project) => (
                        <ProjectCard key={project.projectId} project={project} />
                    ))}
                </div>
            ) : (
                <div className="empty-state">
                    <div className="empty-state-icon">📁</div>
                    <h3 className="empty-state-title">No projects yet</h3>
                    <p>Create your first project and start collaborating!</p>
                    <Link to="/create-project" className="btn btn-primary mt-2">
                        <FiPlus /> Create Project
                    </Link>
                </div>
            )}
        </div>
    );
};

export default MyProjects;
