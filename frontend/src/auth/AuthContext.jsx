import React, { createContext, useContext, useEffect, useMemo, useState } from 'react';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    const [userEmail, setUserEmail] = useState(() =>
        localStorage.getItem('quizapp:user') || ''
    );

    useEffect(() => {
        if (userEmail) {
            localStorage.setItem('quizapp:user', userEmail);
        } else {
            localStorage.removeItem('quizapp:user');
        }
    }, [userEmail]);

    const value = useMemo(() => ({
        userEmail,
        isAuthenticated: Boolean(userEmail),
        login: (email) => setUserEmail(email),
        logout: () => {
            setUserEmail('');
            localStorage.removeItem('quizapp:user');
        }
    }), [userEmail]);

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => {
    const ctx = useContext(AuthContext);
    if (!ctx) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return ctx;
};
