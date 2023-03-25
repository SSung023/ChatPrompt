import React from 'react';
import { Outlet } from 'react-router-dom';
import Navigation from '../components/navigation/Navigation';

export default function Root() {
    return (
        <div className='root-wrapper'>
            <Navigation />
            <div>
                <Outlet />
            </div>
        </div>
    );
}

