import React, { useState } from 'react';
import Select from 'react-select';
import { getData } from 'country-list';
import axios from 'axios';

const normalizeCountryName = (name) => name
    .replace(/\s*\(the\)/gi, ' ')
    .replace(/\s+/g, ' ')
    .trim();

const countries = getData().map(country => ({
    value: country.code,
    label: normalizeCountryName(country.name)
}));

const SignupForm = () => {
    const [selectedCountry, setSelectedCountry] = useState(null);
    const [feedback, setFeedback] = useState({ type: '', text: '' });

    const handleSignup = async (e) => {
        e.preventDefault();
        setFeedback({ type: '', text: '' });
        const formData = new FormData(e.target);
        const user = {
            username: formData.get('username'),
            firstName: formData.get('firstName'),
            lastName: formData.get('lastName'),
            email: formData.get('email').trim().toLowerCase(),
            password: formData.get('password'),
            confirmPassword: formData.get('confirmPassword'),
            country: selectedCountry?.label
        };

        if (!selectedCountry) {
            setFeedback({ type: 'error', text: 'Please select your country.' });
            return;
        }

        if (user.password !== user.confirmPassword) {
            setFeedback({ type: 'error', text: 'Passwords do not match.' });
            return;
        }

        try {
            const res = await axios.post('http://localhost:8080/api/auth/signup', user);
            const { status, message } = res.data;
            const type = status === 'success' ? 'success' : 'error';
            setFeedback({ type, text: message || 'Signup completed.' });
            if (type === 'success') {
                e.target.reset();
                setSelectedCountry(null);
            }
        } catch (err) {
            const serverMessage = err.response?.data?.message || 'Signup failed. Try again.';
            setFeedback({ type: 'error', text: serverMessage });
        }
    };

    return (
        <div className="form-container">
            <h2 className="form-title">Join Learn &amp; Quiz</h2>
            <form className="form" onSubmit={handleSignup}>
                <input type="text" name="firstName" placeholder="First Name" className="form-input" required />
                <input type="text" name="lastName" placeholder="Last Name" className="form-input" required />
                <input type="email" name="email" placeholder="Email" className="form-input" required />
                <input type="text" name="username" placeholder="Username" className="form-input" required />
                <input type="password" name="password" placeholder="Password" className="form-input" required />
                <input type="password" name="confirmPassword" placeholder="Confirm Password" className="form-input" required />

                <Select
                    classNamePrefix="react-select"
                    options={countries}
                    value={selectedCountry}
                    onChange={setSelectedCountry}
                    placeholder="Select your country"
                />

                <button type="submit" className="primary-button">Sign Up</button>
            </form>

            {feedback.text && (
                <p className={`message ${feedback.type === 'success' ? 'success' : 'error'}`}>
                    {feedback.text}
                </p>
            )}
        </div>
    );
};

export default SignupForm;
