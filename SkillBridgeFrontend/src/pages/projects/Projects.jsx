import { useState, useEffect } from 'react';
import projectService from '../../services/projectService';
import ProjectCard from '../../components/ui/ProjectCard';
import Loader from '../../components/ui/Loader';
import { FiSearch, FiFilter } from 'react-icons/fi';
import './Projects.css';

const Projects = () => {
    const [projects, setProjects] = useState([]);
    const [filteredProjects, setFilteredProjects] = useState([]);
    const [loading, setLoading] = useState(true);
    const [searchTerm, setSearchTerm] = useState('');
    const [statusFilter, setStatusFilter] = useState('ALL');

    useEffect(() => {
        const fetchProjects = async () => {
            try {
                const data = await projectService.getAll();
                setProjects(data);
                setFilteredProjects(data);
            } catch (error) {
                console.error('Failed to fetch projects:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchProjects();
    }, []);

    useEffect(() => {
        let result = projects;

        // Search filter
        if (searchTerm) {
            result = result.filter(
                (p) =>
                    p.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
                    p.description.toLowerCase().includes(searchTerm.toLowerCase()) ||
                    p.requiredSkills?.some(s => s.toLowerCase().includes(searchTerm.toLowerCase()))
            );
        }

        // Status filter
        if (statusFilter !== 'ALL') {
            result = result.filter((p) => p.status === statusFilter);
        }

        setFilteredProjects(result);
    }, [searchTerm, statusFilter, projects]);

    return (
        <div className="page projects-page">
            <div className="page-header">
                <h1 className="page-title">Browse Projects</h1>
                <p className="page-subtitle">
                    Discover exciting projects and find opportunities to collaborate
                </p>
            </div>

            <div className="projects-filters">
                <div className="search-box">
                    <FiSearch className="search-icon" />
                    <input
                        type="text"
                        className="search-input"
                        placeholder="Search projects, skills..."
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                    />
                </div>

                <div className="filter-group">
                    <FiFilter className="filter-icon" />
                    <select
                        className="filter-select"
                        value={statusFilter}
                        onChange={(e) => setStatusFilter(e.target.value)}
                    >
                        <option value="ALL">All Status</option>
                        <option value="OPEN">Open</option>
                        <option value="IN_PROGRESS">In Progress</option>
                        <option value="COMPLETED">Completed</option>
                    </select>
                </div>
            </div>

            <div className="projects-count">
                Showing {filteredProjects.length} of {projects.length} projects
            </div>

            {loading ? (
                <Loader text="Loading projects..." />
            ) : filteredProjects.length > 0 ? (
                <div className="projects-grid">
                    {filteredProjects.map((project) => (
                        <ProjectCard key={project.projectId} project={project} />
                    ))}
                </div>
            ) : (
                <div className="empty-state">
                    <div className="empty-state-icon">🔍</div>
                    <h3 className="empty-state-title">No projects found</h3>
                    <p>Try adjusting your search or filters</p>
                </div>
            )}
        </div>
    );
};

export default Projects;
