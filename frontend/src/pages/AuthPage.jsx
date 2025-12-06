import React, { useState } from 'react';
import LoginForm from '../components/LoginForm';
import SignupForm from '../components/SignupForm';

const AuthPage = () => {
    const [isLogin, setIsLogin] = useState(true);

    return (
        <div className="landing-shell">
            {/* Navigation */}
            <header className="hero-nav">
                <div className="nav-brand">QuizMe!</div>
            </header>

            {/* Centered Hero Section */}
            <main className="hero-content">
                <div className="hero-copy centered">
                    <h1 className="main-title">QuizMe!</h1>
                    <p className="hero-lede">
                        A fun and interactive platform created by students, for students.
                        Practice smarter with customizable quizzes, track your progress,
                        and improve your learning at your own pace.
                    </p>

                    <a className="cta-button" href="#auth-card">Get Started</a>
                </div>
            </main>

            {/* Login / Register Panel */}
            <section className="auth-panel" id="auth-card">
                <div className="tab-switcher">
                    <button
                        className={isLogin ? 'tab active' : 'tab'}
                        onClick={() => setIsLogin(true)}
                    >
                        Sign In
                    </button>
                    <button
                        className={!isLogin ? 'tab active' : 'tab'}
                        onClick={() => setIsLogin(false)}
                    >
                        Register
                    </button>
                </div>

                <div className="auth-forms">
                    {isLogin ? <LoginForm /> : <SignupForm />}
                </div>
            </section>
        </div>
    );
};

export default AuthPage;
