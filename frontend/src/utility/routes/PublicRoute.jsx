import React from 'react';
import { Navigate } from 'react-router-dom';
import useLogin from '../SignIn';

export default function PublicRoute({ component, restricted }) {
    const isLogin = !!window.localStorage.getItem('name');
    // console.log(isLogin);
    //const isLogin = false;
    return (
        isLogin && restricted ? <Navigate replace to='/'/> : component
    );
}

