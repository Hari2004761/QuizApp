import React, { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import axios from 'axios';
import { useAuth } from '../auth/AuthContext';

const LoginForm = () => {
    const [feedback, setFeedback] = useState({ type: '', text: '' });
    const navigate = useNavigate();
    const location = useLocation();
    const { login } = useAuth();

    const handleLogin = async (e) => {
        e.preventDefault();
        setFeedback({ type: '', text: '' });
        const formData = new FormData(e.target);
        const email = formData.get('email').trim().toLowerCase();
        const password = formData.get('password');

        try {
            const res = await axios.post('http://localhost:8080/api/auth/login', {
                email,
                password
            });

            const { status, message: serverMessage, firstName, lastName, username, country } = res.data;
            const type = status === 'success' ? 'success' : 'error';
            setFeedback({
                type,
                text: serverMessage || (type === 'success' ? 'Logged in successfully.' : 'Login failed. Try again.')
            });

            if (type === 'success') {
                login({
                    email,
                    firstName,
                    lastName,
                    username,
                    country
                });
                const redirectPath = location.state?.from?.pathname || '/dashboard';
                navigate(redirectPath, { replace: true });
                e.target.reset();
            }
        } catch (err) {
            console.error(err);
            const serverMessage = err.response?.data?.message || 'Login failed. Try again.';
            setFeedback({ type: 'error', text: serverMessage });
        }
    };

    return (
        <div className="form-container">
            <h2 className="form-title">Welcome Back!</h2>
            <p className="form-subtitle">Continue your learning journey</p>
            <form className="form" onSubmit={handleLogin}>
                <div className="form-group">
                    <label>Email</label>
                    <input type="email" name="email" placeholder="Enter your email" className="form-input" required />
                </div>
                <div className="form-group">
                    <label>Password</label>
                    <input type="password" name="password" placeholder="Enter your password" className="form-input" required />
                </div>
                <button type="submit" className="primary-button">Sign In</button>
            </form>
            {feedback.text && (
                <p className={`message ${feedback.type === 'success' ? 'success' : 'error'}`}>
                    {feedback.text}
                </p>
            )}
        </div>
    );
};

export default LoginForm;
