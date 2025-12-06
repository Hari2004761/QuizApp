import React, { useEffect, useMemo, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { FaChartLine, FaClock, FaHistory, FaSignOutAlt, FaUserCircle } from 'react-icons/fa';
import axios from 'axios';
import { useAuth } from '../auth/AuthContext';

const DashboardPage = () => {
    const { userEmail, firstName, lastName, username, country, logout } = useAuth();
    const [summary, setSummary] = useState({ loading: true, data: null, error: '' });
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/auth', { replace: true });
    };

    const buildDashboardUrl = (path = '/') => {
        const params = new URLSearchParams();
        if (userEmail) params.set('userEmail', userEmail);
        if (firstName) params.set('firstName', firstName);
        if (lastName) params.set('lastName', lastName);
        if (username) params.set('username', username);
        if (country) params.set('country', country);
        return `http://localhost:8081${path}?${params.toString()}`;
    };

    const displayName = [firstName, lastName].filter(Boolean).join(' ') || username || userEmail;

    useEffect(() => {
        if (!userEmail) {
            setSummary({ loading: false, data: null, error: '' });
            return;
        }

        let cancelled = false;
        const load = async () => {
            setSummary((prev) => ({ ...prev, loading: true, error: '' }));
            try {
                const res = await axios.get('http://localhost:8081/api/summary', {
                    params: { userEmail }
                });
                if (!cancelled) {
                    setSummary({ loading: false, data: res.data, error: '' });
                }
            } catch (err) {
                if (!cancelled) {
                    setSummary({ loading: false, data: null, error: 'Could not load your progress.' });
                }
            }
        };

        load();
        return () => { cancelled = true; };
    }, [userEmail]);

    const stats = useMemo(() => {
        const data = summary.data || {};
        const history = data.history || [];
        const recent = data.recentResults || [];
        const overallProgress = data.overallProgress ?? 0;
        const progressDegrees = data.progressDegrees ?? Math.round((overallProgress || 0) * 3.6);
        return {
            data,
            history,
            recent,
            overallProgress,
            progressDegrees,
            best: data.bestScorePercent ?? 0,
            avg: data.avgScorePercent ?? 0,
            attempts: data.totalAttempts ?? 0,
            totalQuizzes: data.totalQuizzes ?? 0,
            totalQuestions: data.totalQuestions ?? 0
        };
    }, [summary.data]);

    const formatDate = (value) => {
        if (!value) return '';
        const date = new Date(value);
        return date.toLocaleString(undefined, { month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' });
    };

    return (
        <div className="dashboard-wrapper">
            <header className="dashboard-header">
                <div className="dashboard-brand">QuizMe! Dashboard</div>
                <div className="dashboard-user">
                    <FaUserCircle size={24} />
                    <span>{displayName}</span>
                    <button className="secondary-button" onClick={handleLogout}>
                        <FaSignOutAlt aria-hidden="true" /> Sign out
                    </button>
                </div>
            </header>

            <main className="dashboard-main">
                <section className="dashboard-hero">
                    <div>
                        <p className="eyebrow">Welcome back</p>
                        <h2>Stay on track, {displayName}</h2>
                        <p className="muted">
                            Your progress and history stay in sync. Jump back into your quizzes or create a new one.
                        </p>
                        <div className="hero-actions">
                            <a
                                className="primary-button"
                                href={buildDashboardUrl('/')}
                                target="_blank"
                                rel="noreferrer"
                            >
                                <FaChartLine aria-hidden="true" /> Open quiz workspace
                            </a>
                            <a
                                className="secondary-button"
                                href={buildDashboardUrl('/quiz/new')}
                                target="_blank"
                                rel="noreferrer"
                            >
                                Create a quiz
                            </a>
                        </div>
                    </div>

                    <div className="hero-progress">
                        <div
                            className="progress-circle"
                            style={{
                                background: `conic-gradient(#ffda23 0deg ${stats.progressDegrees}deg, rgba(255,255,255,0.12) ${stats.progressDegrees}deg 360deg)`
                            }}
                        >
                            <div className="progress-inner">
                                <span>{stats.overallProgress ?? 0}%</span>
                            </div>
                        </div>
                        <p className="muted small">Average score across all quizzes.</p>
                    </div>
                </section>

                <section className="grid stats-grid">
                    <div className="card stat-card">
                        <div className="card-title">Best score</div>
                        <p className="stat-value">{stats.best}%</p>
                        <p className="muted small">Highest percentage you&apos;ve reached.</p>
                    </div>
                    <div className="card stat-card">
                        <div className="card-title">Avg score</div>
                        <p className="stat-value">{stats.avg}%</p>
                        <p className="muted small">Average across all attempts.</p>
                    </div>
                    <div className="card stat-card">
                        <div className="card-title">Attempts</div>
                        <p className="stat-value">{stats.attempts}</p>
                        <p className="muted small">Total quiz attempts by this account.</p>
                    </div>
                    <div className="card stat-card">
                        <div className="card-title">Quiz bank</div>
                        <p className="stat-value">{stats.totalQuizzes} quizzes</p>
                        <p className="muted small">{stats.totalQuestions} questions available.</p>
                    </div>
                </section>

                <section className="card history-card">
                    <div className="section-header">
                        <div>
                            <h3>Past quizzes &amp; scores</h3>
                            <p className="section-subtitle">
                                {summary.loading
                                    ? 'Loading your history...'
                                    : stats.history.length > 0
                                        ? `${stats.history.length} attempts saved`
                                        : 'No attempts yet. Start a quiz to track progress.'}
                            </p>
                        </div>
                        <div className="history-meta">
                            <FaHistory aria-hidden="true" />
                            <span>{stats.history.length} recorded</span>
                        </div>
                    </div>

                    {summary.error && <div className="error-tile">{summary.error}</div>}

                    {summary.loading && (
                        <div className="loading-tile">
                            <FaClock /> Fetching latest data...
                        </div>
                    )}

                    {!summary.loading && stats.history.length > 0 && (
                        <div className="history-list">
                            {stats.history.map((item, idx) => (
                                <div className="history-item" key={`${item.subjectName}-${idx}`}>
                                    <div>
                                        <div className="history-title">{item.subjectName}</div>
                                        <p className="muted small">{formatDate(item.completedAt)}</p>
                                    </div>
                                    <div className="history-score">
                                        <span className="score-main">{item.score}/{item.total}</span>
                                        <span className="history-percent">{item.percentage}%</span>
                                    </div>
                                </div>
                            ))}
                        </div>
                    )}
                </section>
            </main>
        </div>
    );
};

export default DashboardPage;
