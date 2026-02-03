import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import {
    FiHome,
    FiFolder,
    FiBriefcase,
    FiClipboard,
    FiUser,
    FiLogOut,
    FiPlus,
    FiUsers,
    FiMessageCircle,
    FiShield
} from 'react-icons/fi';
import './Sidebar.css';

const Sidebar = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const { logout, user } = useAuth();

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    const menuItems = [
        { path: '/dashboard', icon: FiHome, label: 'Dashboard' },
        { path: '/projects', icon: FiFolder, label: 'Browse Projects' },
        { path: '/my-projects', icon: FiBriefcase, label: 'My Projects' },
        { path: '/create-project', icon: FiPlus, label: 'Create Project' },
        { path: '/my-applications', icon: FiClipboard, label: 'My Applications' },
        { path: '/messages', icon: FiMessageCircle, label: 'Messages' },
        { path: '/profile', icon: FiUser, label: 'Profile' },
        { path: '/admin', icon: FiShield, label: 'Admin Panel' },
    ];

    return (
        <aside className="sidebar">
            <div className="sidebar-header">
                <div className="sidebar-logo">
                    <span className="logo-icon">🌉</span>
                    <span className="logo-text">Skill Bridge</span>
                </div>
            </div>

            <nav className="sidebar-nav">
                <ul className="nav-list">
                    {menuItems.map((item) => (
                        <li key={item.path}>
                            <Link
                                to={item.path}
                                className={`nav-link ${location.pathname === item.path ? 'active' : ''}`}
                            >
                                <item.icon className="nav-icon" />
                                <span>{item.label}</span>
                            </Link>
                        </li>
                    ))}
                </ul>
            </nav>

            <div className="sidebar-footer">
                <div className="user-info">
                    <div className="user-avatar">
                        {user?.name?.charAt(0) || 'U'}
                    </div>
                    <div className="user-details">
                        <span className="user-name">{user?.name || 'User'}</span>
                        <span className="user-email">{user?.email || ''}</span>
                    </div>
                </div>
                <button className="logout-btn" onClick={handleLogout}>
                    <FiLogOut />
                    <span>Logout</span>
                </button>
            </div>
        </aside>
    );
};

export default Sidebar;
