import './StatusBadge.css';

const StatusBadge = ({ status }) => {
    const getStatusClass = () => {
        switch (status?.toUpperCase()) {
            case 'OPEN':
                return 'status-open';
            case 'IN_PROGRESS':
                return 'status-progress';
            case 'COMPLETED':
                return 'status-completed';
            case 'PENDING':
                return 'status-pending';
            case 'ACCEPTED':
                return 'status-accepted';
            case 'REJECTED':
                return 'status-rejected';
            default:
                return '';
        }
    };

    const formatStatus = (status) => {
        return status?.replace(/_/g, ' ') || '';
    };

    return (
        <span className={`status-badge ${getStatusClass()}`}>
            {formatStatus(status)}
        </span>
    );
};

export default StatusBadge;
