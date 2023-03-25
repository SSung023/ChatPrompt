import React from 'react';
import { Navigate } from 'react-router-dom';
// import useLogin from '../hooks/useLogin';

export default function PrivateRoute({ component }) {
    const isLogin = !!window.localStorage.getItem("prompt-login");
    // console.log(isLogin);
    return (
        isLogin ? component : <Navigate replace to='/login'/>
    );
}

