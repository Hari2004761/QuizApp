import React from 'react';
import { useNavigate } from 'react-router-dom';
import { FaChartLine, FaCheckCircle, FaSignOutAlt, FaUserCircle } from 'react-icons/fa';
import { useAuth } from '../auth/AuthContext';

const DashboardPage = () => {
    const { userEmail, logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/intro', { replace: true, state: { fromLogout: true } });
    };

    return (
        <div className="dashboard-wrapper">
            <header className="dashboard-header">
                <div className="dashboard-brand">Learn &amp; Quiz</div>
                <div className="dashboard-user">
                    <FaUserCircle size={24} />
                    <span>{userEmail}</span>
                    <button className="secondary-button" onClick={handleLogout}>
                        <FaSignOutAlt aria-hidden="true" /> Sign out
                    </button>
                </div>
            </header>

            <main className="dashboard-main">
                <section className="card highlight">
                    <div>
                        <p className="eyebrow">Welcome back</p>
                        <h2>Jump into today&apos;s quiz plan</h2>
                        <p className="muted">
                            Your progress, recent attempts, and new practice questions are all in one place.
                        </p>
                        <div className="dashboard-button-wrapper">
                            <a
                                className="primary-button dashboard-button"
                                href="http://localhost:8081/"
                                target="_blank"
                                rel="noreferrer"
                            >
                                Open quiz dashboard
                            </a>
                        </div>
                    </div>
                    <FaChartLine size={72} className="accent-icon" />
                </section>

                <section className="grid">
                    <div className="card">
                        <div className="card-title">Profile</div>
                        <p className="muted">Use the navigation above to keep studying without logging in again.</p>
                        <ul className="list">
                            <li><FaCheckCircle className="success" /> Signed in as {userEmail}</li>
                            <li><FaCheckCircle className="success" /> Account verified</li>
                            <li><FaCheckCircle className="success" /> Session active</li>
                        </ul>
                    </div>
                    <div className="card">
                        <div className="card-title">Need to start fresh?</div>
                        <p className="muted">If you signed up recently, your new quizzes will appear in the dashboard tab.</p>
                        <a className="link" href="http://localhost:8081/quiz/new" target="_blank" rel="noreferrer">
                            Add a new quiz now
                        </a>
                    </div>
                </section>
            </main>
        </div>
    );
};

export default DashboardPage;
