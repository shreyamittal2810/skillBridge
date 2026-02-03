import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import studentService from '../../services/studentService';
import { FiSave, FiX, FiCode } from 'react-icons/fi';
import './EditProfile.css';

const EditProfile = () => {
    const navigate = useNavigate();
    const { user, refreshUser } = useAuth();
    const [formData, setFormData] = useState({
        name: user?.name || '',
        email: user?.email || '',
        password: '',
        course: user?.course || '',
        skills: user?.skills || [],
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
            if (!formData.skills.includes(skillInput.trim())) {
                setFormData({
                    ...formData,
                    skills: [...formData.skills, skillInput.trim()],
                });
            }
            setSkillInput('');
        }
    };

    const handleRemoveSkill = (skillToRemove) => {
        setFormData({
            ...formData,
            skills: formData.skills.filter((s) => s !== skillToRemove),
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');

        try {
            // If password is empty, send update without it
            const updateData = { ...formData };
            if (!updateData.password) {
                delete updateData.password;
            }

            await studentService.updateProfile(updateData);
            await refreshUser();
            navigate('/profile');
        } catch (error) {
            setError(error.response?.data?.message || 'Failed to update profile');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="page edit-profile-page">
            <div className="page-header">
                <h1 className="page-title">Edit Profile</h1>
            </div>

            <div className="edit-profile-card">
                <form onSubmit={handleSubmit}>
                    {error && <div className="form-error-box">{error}</div>}

                    <div className="form-row">
                        <div className="form-group">
                            <label className="form-label">Full Name</label>
                            <input
                                type="text"
                                name="name"
                                className="form-input"
                                value={formData.name}
                                onChange={handleChange}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label className="form-label">Email</label>
                            <input
                                type="email"
                                name="email"
                                className="form-input"
                                value={formData.email}
                                onChange={handleChange}
                                required
                            />
                        </div>
                    </div>

                    <div className="form-row">
                        <div className="form-group">
                            <label className="form-label">New Password (leave blank to keep current)</label>
                            <input
                                type="password"
                                name="password"
                                className="form-input"
                                value={formData.password}
                                onChange={handleChange}
                                minLength={6}
                                placeholder="••••••••"
                            />
                        </div>

                        <div className="form-group">
                            <label className="form-label">Course</label>
                            <input
                                type="text"
                                name="course"
                                className="form-input"
                                value={formData.course}
                                onChange={handleChange}
                                required
                            />
                        </div>
                    </div>

                    <div className="form-group">
                        <label className="form-label">Skills (Press Enter to add)</label>
                        <div className="input-wrapper">
                            <FiCode className="input-icon-left" />
                            <input
                                type="text"
                                className="form-input with-icon-left"
                                placeholder="Add a skill..."
                                value={skillInput}
                                onChange={(e) => setSkillInput(e.target.value)}
                                onKeyDown={handleAddSkill}
                            />
                        </div>
                        {formData.skills.length > 0 && (
                            <div className="skills-list">
                                {formData.skills.map((skill, index) => (
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
                            onClick={() => navigate('/profile')}
                        >
                            <FiX /> Cancel
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
                                    <FiSave /> Save Changes
                                </>
                            )}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default EditProfile;
