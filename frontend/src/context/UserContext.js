import React, { createContext, useReducer } from "react";

export const SET_NAME = 'user/SET_NAME';

export const SET_FIRST_TASKID = 'user/SET_FIRST_TASKID';
export const SET_LAST_TASKID = 'user/SET_LAST_TASKID';

export const SET_INST_TASKID = 'user/SET_INST_TASKID';
export const SET_IO_TASKID = 'user/SET_IO_TASKID';

export const SET_IO_IDX = 'user/SET_IO_IDX';
export const SET_FIRST_IO_IDX = 'user/SET_FIRST_IO_IDX';
export const SET_LAST_IO_IDX = 'user/SET_LAST_IO_IDX';
export const SET_TASKNAME = 'user/SET_TASKNAME';

export const userContext = createContext();

const initData = {
    name: '',
    inst_taskId: 1,
    io_taskId: 1,

    first_taskId: 1,
    last_taskId: 120,
    
    io_idx: 1,
    first_io_idx: 1,
    last_io_idx: 100,
    taskName: '',
}

const userReducer = (state, action) => {
    switch(action.type){
        case SET_NAME:
        return {
            ...state,
            name : action.data
        }
        
        case SET_FIRST_TASKID:
        return {
            ...state,
            first_taskId: action.data
        }
        case SET_LAST_TASKID:
        return {
            ...state,
            last_taskId: action.data
        }

        case SET_INST_TASKID:
        return {
            ...state,
            inst_taskId: action.data
        }
        case SET_IO_TASKID:
        return {
            ...state,
            io_taskId: action.data
        }

        case SET_IO_IDX:
        return {
            ...state,
            io_idx: action.data
        }
        case SET_FIRST_IO_IDX:
        return {
            ...state,
            first_io_idx: action.data
        }
        case SET_LAST_IO_IDX:
        return {
            ...state,
            last_io_idx: action.data
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