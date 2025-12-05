import React from 'react';
import { Navigate, Route, Routes } from 'react-router-dom';
import AuthPage from './pages/AuthPage';
import DashboardPage from './pages/DashboardPage';
import ProtectedRoute from './components/ProtectedRoute';
import { useAuth } from './auth/AuthContext';
import './App.css';



/*const App = () => {
    const { isAuthenticated } = useAuth();

    return (
        <Routes>
            <Route
                path="/"
                element={<Navigate to={isAuthenticated ? '/dashboard' : '/auth'} replace />}
            />
            <Route path="/auth" element={<AuthPage />} />
            <Route
                path="/dashboard"
                element={(
                    <ProtectedRoute>
                        <DashboardPage />
                    </ProtectedRoute>
                )}
            />
            <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
    );
};
*/




import Intro from './components/Intro';


const App = () => {
    return <Intro />;
};



export default App;
