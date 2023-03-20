import React from 'react';
import { Outlet } from 'react-router-dom';
import Navigation from '../components/navigation/Navigation';

export default function Root() {
    const containerStyle = {
        display: `flex`,
        alignItems: `center`,
        width: `100vw`,
        height: `100vh`,
    }
    return (
        <div style={containerStyle}>
            <Navigation />
            <Outlet />
        </div>
    );
}

