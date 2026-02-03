import './SkillTag.css';

const SkillTag = ({ skill, onRemove, removable = false }) => {
    return (
        <span className="skill-tag">
            {skill}
            {removable && (
                <button
                    type="button"
                    className="skill-tag-remove"
                    onClick={() => onRemove(skill)}
                >
                    ×
                </button>
            )}
        </span>
    );
};

export default SkillTag;
