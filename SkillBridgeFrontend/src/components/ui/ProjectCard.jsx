import { Link } from 'react-router-dom';
import StatusBadge from './StatusBadge';
import SkillTag from './SkillTag';
import './ProjectCard.css';

const ProjectCard = ({ project }) => {
    const { projectId, title, description, status, requiredSkills, createdBy, teamMembers } = project;

    return (
        <Link to={`/projects/${projectId}`} className="project-card">
            <div className="project-card-header">
                <StatusBadge status={status} />
                <span className="project-members">
                    👥 {teamMembers?.length || 0} members
                </span>
            </div>

            <h3 className="project-title">{title}</h3>

            <p className="project-description">
                {description?.length > 120 ? `${description.substring(0, 120)}...` : description}
            </p>

            <div className="project-skills">
                {requiredSkills?.slice(0, 4).map((skill, index) => (
                    <SkillTag key={index} skill={skill} />
                ))}
                {requiredSkills?.length > 4 && (
                    <span className="skill-more">+{requiredSkills.length - 4}</span>
                )}
            </div>

            <div className="project-footer">
                <span className="view-more">View Details →</span>
            </div>
        </Link>
    );
};

export default ProjectCard;
