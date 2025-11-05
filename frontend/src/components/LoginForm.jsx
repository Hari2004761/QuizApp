import React from 'react';

const LoginForm = () => {
    const handleLogin = (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const email = formData.get('email');
        const password = formData.get('password');
        alert(`Demo login for ${email}`);
        e.target.reset();
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
        </div>
    );
};

export default LoginForm;
