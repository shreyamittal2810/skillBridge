import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { FiUser, FiMail, FiLock, FiBook, FiCode, FiUserPlus } from 'react-icons/fi';
import './Auth.css';

// Predefined skills matching the backend Skill enum
const AVAILABLE_SKILLS = ['JAVA', 'PYTHON', 'REACT', 'SPRING_BOOT', 'MYSQL'];

const Register = () => {
    const navigate = useNavigate();
    const { register } = useAuth();
    const [formData, setFormData] = useState({
        name: '',
        email: '',
        password: '',
        course: '',
        skills: [],
    });
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
        setError('');
    };

    const handleSkillToggle = (skill) => {
        if (formData.skills.includes(skill)) {
            setFormData({
                ...formData,
                skills: formData.skills.filter(s => s !== skill)
            });
        } else {
            setFormData({
                ...formData,
                skills: [...formData.skills, skill]
            });
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (formData.skills.length === 0) {
            setError('Please select at least one skill');
            return;
        }

        setLoading(true);
        setError('');

        const result = await register(formData);

        if (result.success) {
            navigate('/login', { state: { message: 'Registration successful! Please login.' } });
        } else {
            setError(result.error);
        }
        setLoading(false);
    };

    return (
        <div className="auth-container">
            <div className="auth-background">
                <div className="auth-shape auth-shape-1"></div>
                <div className="auth-shape auth-shape-2"></div>
                <div className="auth-shape auth-shape-3"></div>
            </div>

            <div className="auth-card auth-card-lg">
                <div className="auth-header">
                    <div className="auth-logo">🌉</div>
                    <h1 className="auth-title">Create Account</h1>
                    <p className="auth-subtitle">Join Skill Bridge and start collaborating</p>
                </div>

                <form onSubmit={handleSubmit} className="auth-form">
                    {error && <div className="auth-error">{error}</div>}

                    <div className="form-row">
                        <div className="form-group">
                            <label className="form-label">Full Name</label>
                            <div className="input-wrapper">
                                <FiUser className="input-icon" />
                                <input
                                    type="text"
                                    name="name"
                                    className="form-input with-icon"
                                    placeholder="John Doe"
                                    value={formData.name}
                                    onChange={handleChange}
                                    required
                                />
                            </div>
                        </div>

                        <div className="form-group">
                            <label className="form-label">Email Address</label>
                            <div className="input-wrapper">
                                <FiMail className="input-icon" />
                                <input
                                    type="email"
                                    name="email"
                                    className="form-input with-icon"
                                    placeholder="you@example.com"
                                    value={formData.email}
                                    onChange={handleChange}
                                    required
                                />
                            </div>
                        </div>
                    </div>

                    <div className="form-row">
                        <div className="form-group">
                            <label className="form-label">Password</label>
                            <div className="input-wrapper">
                                <FiLock className="input-icon" />
                                <input
                                    type="password"
                                    name="password"
                                    className="form-input with-icon"
                                    placeholder="Min. 6 characters"
                                    value={formData.password}
                                    onChange={handleChange}
                                    minLength={6}
                                    required
                                />
                            </div>
                        </div>

                        <div className="form-group">
                            <label className="form-label">Course / Program</label>
                            <div className="input-wrapper">
                                <FiBook className="input-icon" />
                                <input
                                    type="text"
                                    name="course"
                                    className="form-input with-icon"
                                    placeholder="e.g., Computer Science"
                                    value={formData.course}
                                    onChange={handleChange}
                                    required
                                />
                            </div>
                        </div>
                    </div>

                    <div className="form-group">
                        <label className="form-label">
                            <FiCode style={{ marginRight: '8px' }} />
                            Select Your Skills
                        </label>
                        <div className="skills-grid">
                            {AVAILABLE_SKILLS.map((skill) => (
                                <button
                                    key={skill}
                                    type="button"
                                    className={`skill-toggle ${formData.skills.includes(skill) ? 'active' : ''}`}
                                    onClick={() => handleSkillToggle(skill)}
                                >
                                    {skill.replace('_', ' ')}
                                </button>
                            ))}
                        </div>
                        {formData.skills.length > 0 && (
                            <div className="selected-skills">
                                Selected: {formData.skills.map(s => s.replace('_', ' ')).join(', ')}
                            </div>
                        )}
                    </div>

                    <button
                        type="submit"
                        className="btn btn-primary btn-lg auth-btn"
                        disabled={loading}
                    >
                        {loading ? (
                            <span className="btn-loader"></span>
                        ) : (
                            <>
                                <FiUserPlus />
                                Create Account
                            </>
                        )}
                    </button>
                </form>

                <div className="auth-footer">
                    <p>Already have an account? <Link to="/login">Sign In</Link></p>
                </div>
            </div>
        </div>
    );
};

export default Register;

