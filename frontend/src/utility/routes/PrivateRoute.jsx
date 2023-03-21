import React from 'react';
import { Navigate } from 'react-router-dom';
// import useLogin from '../hooks/useLogin';

export default function PrivateRoute({ component }) {
    const isLogin = !!window.localStorage.getItem("name");
    return (
        isLogin ? component : <Navigate replace to='/login'/>
    );
}

