import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import SkillTag from '../../components/ui/SkillTag';
import { FiEdit, FiMail, FiBook, FiCode } from 'react-icons/fi';
import './Profile.css';

const Profile = () => {
    const { user, refreshUser } = useAuth();

    useEffect(() => {
        refreshUser();
    }, []);

    return (
        <div className="page profile-page">
            <div className="page-header">
                <h1 className="page-title">My Profile</h1>
                <Link to="/profile/edit" className="btn btn-secondary">
                    <FiEdit /> Edit Profile
                </Link>
            </div>

            <div className="profile-content">
                <div className="profile-card-main">
                    <div className="profile-avatar-section">
                        <div className="profile-avatar">
                            {user?.name?.charAt(0) || 'U'}
                        </div>
                        <div className="profile-name-section">
                            <h2 className="profile-name">{user?.name || 'User'}</h2>
                            <span className="profile-role">Student</span>
                        </div>
                    </div>

                    <div className="profile-info-grid">
                        <div className="profile-info-item">
                            <div className="info-icon">
                                <FiMail />
                            </div>
                            <div className="info-content">
                                <span className="info-label">Email</span>
                                <span className="info-value">{user?.email || 'Not set'}</span>
                            </div>
                        </div>

                        <div className="profile-info-item">
                            <div className="info-icon">
                                <FiBook />
                            </div>
                            <div className="info-content">
                                <span className="info-label">Course</span>
                                <span className="info-value">{user?.course || 'Not set'}</span>
                            </div>
                        </div>
                    </div>
                </div>

                <div className="profile-card">
                    <div className="card-header">
                        <FiCode />
                        <h3>Skills</h3>
                    </div>
                    <div className="skills-grid">
                        {user?.skills?.length > 0 ? (
                            user.skills.map((skill, index) => (
                                <SkillTag key={index} skill={skill} />
                            ))
                        ) : (
                            <p className="no-skills">No skills added yet</p>
                        )}
                    </div>
                </div>

                <div className="profile-stats">
                    <div className="stat-item">
                        <span className="stat-number">{user?.skills?.length || 0}</span>
                        <span className="stat-name">Skills</span>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Profile;
