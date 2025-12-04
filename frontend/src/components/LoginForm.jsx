import React, { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import axios from 'axios';
import { useAuth } from '../auth/AuthContext';

const LoginForm = () => {
    const [message, setMessage] = useState('');
    const navigate = useNavigate();
    const location = useLocation();
    const { login } = useAuth();

    const handleLogin = async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const email = formData.get('email');
        const password = formData.get('password');

        try {
            const res = await axios.post('http://localhost:8080/api/auth/login', {
                email,
                password
            });

            setMessage(res.data.message);
            if (res.data.status === 'success') {
                login(email);
                const redirectPath = location.state?.from?.pathname || '/dashboard';
                navigate(redirectPath, { replace: true });
                e.target.reset();
            }
        } catch (err) {
            console.error(err);
            setMessage('❌ Login failed. Try again.');
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
            {message && (
                <p className={`message ${message.includes('❌') ? 'error' : 'success'}`}>
                    {message}
                </p>
            )}
        </div>
    );
};

export default LoginForm;
