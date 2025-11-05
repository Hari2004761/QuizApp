import React, { useState } from 'react';
import Select from 'react-select';
import { getData } from 'country-list';

const countries = getData().map(country => ({
    value: country.code,
    label: country.name
}));

const customSelectStyles = {
    control: (provided) => ({
        ...provided,
        width: '109%',
        height: '3rem',
        minHeight: '3rem',
        borderRadius: '0.5rem',
        border: '2px solid #d1d5db',
        padding: '0 0.5rem',
        boxShadow: 'none',
        fontSize: '1rem',
    }),
    menu: (provided) => ({
        ...provided,
        fontSize: '1rem'
    }),
    singleValue: (provided) => ({
        ...provided,
        fontSize: '1rem'
    })
};

const SignupForm = () => {
    const [selectedCountry, setSelectedCountry] = useState(null);

    const handleSignup = (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const email = formData.get('email');
        const password = formData.get('password');
        const confirmPassword = formData.get('confirmPassword');

        if (!selectedCountry) {
            alert("Please select your country!");
            return;
        }

        if (password !== confirmPassword) {
            alert("Passwords do not match!");
            return;
        }

        alert(`Demo signup for ${email} from ${selectedCountry.label}`);
        e.target.reset();
        setSelectedCountry(null);
    };

    return (
        <div className="form-container">
            <h2 className="form-title">Join Learn & Quiz</h2>
            <form className="form" onSubmit={handleSignup}>
                <input type="text" name="firstName" placeholder="First Name" className="form-input" required />
                <input type="text" name="lastName" placeholder="Last Name" className="form-input" required />
                <input type="email" name="email" placeholder="Email" className="form-input" required />
                <input type="password" name="password" placeholder="Password" className="form-input" required />
                <input type="password" name="confirmPassword" placeholder="Confirm Password" className="form-input" required />

                {/* Country dropdown */}
                <Select
                    options={countries}
                    value={selectedCountry}
                    onChange={setSelectedCountry}
                    placeholder="Select your country"
                    styles={customSelectStyles}
                />

                <button type="submit" className="primary-button">Sign Up</button>
            </form>
        </div>
    );
};

export default SignupForm;
