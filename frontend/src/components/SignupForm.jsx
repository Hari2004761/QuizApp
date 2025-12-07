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
    const [message, setMessage] = useState('');

    const handleSignup = async (e) => {
        e.preventDefault();
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
            setMessage('❌ Please select your country!');
            return;
        }

        if (user.password !== user.confirmPassword) {
            setMessage('❌ Passwords do not match!');
            return;
        }

        try {
            const res = await axios.post('http://localhost:8080/api/auth/signup', user);
            setMessage(res.data);
            e.target.reset();
            setSelectedCountry(null);
        } catch (err) {
            setMessage('Signup failed. Try again.');
        }
    };

    return (
        <div className="form-container">
            <h2 className="form-title">Join Learn & Quiz</h2>
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

            {message && (
                <p className={`message ${message.startsWith('✅') ? 'success' : 'error'}`}>
                    {message}
                </p>
            )}
        </div>
    );
};

export default SignupForm;
