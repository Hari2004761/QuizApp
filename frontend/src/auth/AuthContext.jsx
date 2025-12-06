import React, { createContext, useContext, useEffect, useMemo, useState } from 'react';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(() => {
        const stored = localStorage.getItem('quizapp:user');
        return stored ? JSON.parse(stored) : null;
    });

    useEffect(() => {
        if (user) {
            localStorage.setItem('quizapp:user', JSON.stringify(user));
        } else {
            localStorage.removeItem('quizapp:user');
        }
    }, [user]);

    const value = useMemo(() => ({
        user,
        userEmail: user?.email || '',
        firstName: user?.firstName || '',
        lastName: user?.lastName || '',
        username: user?.username || '',
        country: user?.country || '',
        isAuthenticated: Boolean(user?.email),
        login: (userData) => setUser(userData),
        logout: () => setUser(null)
    }), [user]);

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => {
    const ctx = useContext(AuthContext);
    if (!ctx) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return ctx;
};
