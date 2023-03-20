import React, { createContext, useReducer } from "react";

export const SET_NAME = 'user/SET_NAME';
export const SET_TASKID = 'user/SET_TASKID';
export const SET_TASKNAME = 'user/SET_TASKNAME';

export const userContext = createContext();

const initData = {
    name: '박소영',
    taskId: '001',
    taskName: '지시문',
}

const userReducer = (state, action) => {
    switch(action.type){
        case SET_NAME:
            return {
                ...state,
                name : action.data
            }
        case SET_TASKID:
            return {
                ...state,
                taskId: action.data
            }
        case SET_TASKNAME:
            return {
                ...state,
                taskName: action.data
            }
        default:
            return;
    }
}

const UserProvider = ({ children }) => {
    const [data, contextDispatch] = useReducer(userReducer, initData);

    const value = {
        state: { data },
        actions: { contextDispatch }
    }

    return (
        <userContext.Provider value = {value}>
            {children}
        </userContext.Provider>
    );
}

export default UserProvider;