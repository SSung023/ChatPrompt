import React from 'react';
import { Navigate, useNavigate } from 'react-router-dom';
// import useLogin from '../hooks/useLogin';

export default function PrivateRoute({ component }) {
    const isLogin = !!window.localStorage.getItem("name");
    console.log(isLogin);
    return (
        isLogin ? component : <Navigate replace to='/login'/>
    );
}

