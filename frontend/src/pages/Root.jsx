import React from 'react';
import { Outlet } from 'react-router-dom';
import Navigation from '../components/navigation/Navigation';

export default function Root() {
    const containerStyle = {
        width: `100%`,
        height: `100%`
    }
    return (
        <div style={containerStyle}>
            <Navigation />
            <div style={{marginLeft: `13em`}}>
                <Outlet />
            </div>
        </div>
    );
}

