import React, { useState } from 'react';
import LoginForm from '../components/LoginForm';
import SignupForm from '../components/SignupForm';

const AuthPage = () => {
    const [isLogin, setIsLogin] = useState(true);

    return (
        <div className="container">
            <div className="main-card">
                {/* Left Branding */}
                <div className="branding-section">
                    <h1 className="brand-title">Learn & Quiz</h1>
                    <p className="brand-subtitle">
                        Master any subject with interactive quizzes and personalized learning paths
                    </p>
                </div>

                {/* Right Forms */}
                <div className="form-section">
                    <div className="tab-container">
                        <button
                            className={`tab-button ${isLogin ? 'active' : ''}`}
                            onClick={() => setIsLogin(true)}
                        >
                            Login
                        </button>
                        <button
                            className={`tab-button ${!isLogin ? 'active' : ''}`}
                            onClick={() => setIsLogin(false)}
                        >
                            Sign Up
                        </button>
                    </div>

                    {isLogin ? <LoginForm /> : <SignupForm />}

                    <div className="divider">
                        <span className="divider-text">Or continue with</span>
                    </div>
                    <div className="social-buttons">
                        <button className="social-button">Google</button>
                        <button className="social-button">Facebook</button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AuthPage;
