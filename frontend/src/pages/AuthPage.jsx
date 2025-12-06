import React, { useState } from 'react';
import LoginForm from '../components/LoginForm';
import SignupForm from '../components/SignupForm';
import { useLocation } from "react-router-dom";

const AuthPage = () => {


    const location = useLocation();

    // Check what state was given, depending on the button
    const initialMode = location.state?.showSignup === true ? false : true;
    const [isLogin, setIsLogin] = useState(initialMode);

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

                </div>
            </div>
        </div>
    );
};

export default AuthPage;
