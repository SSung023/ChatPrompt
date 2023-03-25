import React, { useContext, useEffect, useState } from 'react';
import { Outlet } from 'react-router-dom';
import Navigation from '../components/navigation/Navigation';
import { metaDataContext, SET_HEIGHT } from '../context/MetaDataContext';

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

