import './Loader.css';

const Loader = ({ size = 'md', text = '' }) => {
    return (
        <div className="loader-wrapper">
            <div className={`loader loader-${size}`}></div>
            {text && <p className="loader-text">{text}</p>}
        </div>
    );
};

export default Loader;
