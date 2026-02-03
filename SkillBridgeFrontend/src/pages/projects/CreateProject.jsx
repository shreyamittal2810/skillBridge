import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import projectService from '../../services/projectService';
import { FiPlus, FiCode } from 'react-icons/fi';
import './CreateProject.css';

const CreateProject = () => {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        title: '',
        description: '',
        requiredSkills: [],
    });
    const [skillInput, setSkillInput] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
        setError('');
    };

    const handleAddSkill = (e) => {
        if (e.key === 'Enter' && skillInput.trim()) {
            e.preventDefault();
            if (!formData.requiredSkills.includes(skillInput.trim())) {
                setFormData({
                    ...formData,
                    requiredSkills: [...formData.requiredSkills, skillInput.trim()],
                });
            }
            setSkillInput('');
        }
    };

    const handleRemoveSkill = (skillToRemove) => {
        setFormData({
            ...formData,
            requiredSkills: formData.requiredSkills.filter((s) => s !== skillToRemove),
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (formData.requiredSkills.length === 0) {
            setError('Please add at least one required skill');
            return;
        }

        setLoading(true);
        setError('');

        try {
            const project = await projectService.create(formData);
            navigate(`/projects/${project.projectId}`);
        } catch (error) {
            setError(error.response?.data?.message || 'Failed to create project');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="page create-project-page">
            <div className="page-header">
                <h1 className="page-title">Create New Project</h1>
                <p className="page-subtitle">Share your project idea and find collaborators</p>
            </div>

            <div className="create-project-card">
                <form onSubmit={handleSubmit}>
                    {error && <div className="form-error-box">{error}</div>}

                    <div className="form-group">
                        <label className="form-label">Project Title *</label>
                        <input
                            type="text"
                            name="title"
                            className="form-input"
                            placeholder="e.g., E-Commerce Platform, AI Chatbot..."
                            value={formData.title}
                            onChange={handleChange}
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label className="form-label">Description *</label>
                        <textarea
                            name="description"
                            className="form-input form-textarea"
                            placeholder="Describe your project idea, goals, and what you're looking for in team members..."
                            value={formData.description}
                            onChange={handleChange}
                            rows={6}
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label className="form-label">Required Skills (Press Enter to add) *</label>
                        <div className="input-wrapper">
                            <FiCode className="input-icon-left" />
                            <input
                                type="text"
                                className="form-input with-icon-left"
                                placeholder="e.g., React, Node.js, Python..."
                                value={skillInput}
                                onChange={(e) => setSkillInput(e.target.value)}
                                onKeyDown={handleAddSkill}
                            />
                        </div>
                        {formData.requiredSkills.length > 0 && (
                            <div className="skills-list">
                                {formData.requiredSkills.map((skill, index) => (
                                    <span key={index} className="skill-chip">
                                        {skill}
                                        <button
                                            type="button"
                                            onClick={() => handleRemoveSkill(skill)}
                                            className="skill-chip-remove"
                                        >
                                            ×
                                        </button>
                                    </span>
                                ))}
                            </div>
                        )}
                    </div>

                    <div className="form-actions">
                        <button
                            type="button"
                            className="btn btn-secondary"
                            onClick={() => navigate(-1)}
                        >
                            Cancel
                        </button>
                        <button
                            type="submit"
                            className="btn btn-primary"
                            disabled={loading}
                        >
                            {loading ? (
                                <span className="btn-loader"></span>
                            ) : (
                                <>
                                    <FiPlus /> Create Project
                                </>
                            )}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default CreateProject;
