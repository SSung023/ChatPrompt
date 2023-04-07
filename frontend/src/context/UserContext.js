import React, { createContext, useReducer } from "react";

export const SET_NAME = 'user/SET_NAME';
export const SET_TASKNAME = 'user/SET_TASKNAME';

export const SET_FIRST_TASKID = 'user/SET_FIRST_TASKID';
export const SET_LAST_TASKID = 'user/SET_LAST_TASKID';
export const SET_IO_FIRST_TASKID = 'user/SET_IO_FIRST_TASKID';
export const SET_IO_LAST_TASKID = 'user/SET_IO_LAST_TASKID';

export const SET_INST_TASKID = 'user/SET_INST_TASKID';
export const SET_IO_TASKID = 'user/SET_IO_TASKID';

export const SET_IO_IDX = 'user/SET_IO_IDX';
export const SET_SUB_IDX = 'user/SET_SUB_IDX';

export const SET_IO_PROGRESS = 'user/SET_IO_PROGRESS';

export const userContext = createContext();

const initData = {
    taskName: '',
    name: '',

    inst_taskId: 1,
    io_taskId: 1,

    first_taskId: 1,
    last_taskId: 120,
    io_first_taskId: 1,
    io_last_taskId: 120,
    
    io_idx: 1,
    sub_idx: 1,

    io_progress: 0,
}

const userReducer = (state, action) => {
    switch(action.type){
        // basic info
        case SET_NAME:
        return {
            ...state,
            name : action.data
        }
        case SET_TASKNAME:
        return {
            ...state,
            taskName: action.data
        }

        // range of id
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
        case SET_IO_FIRST_TASKID:
        return {
            ...state,
            io_first_taskId: action.data
        }
        case SET_IO_LAST_TASKID:
        return {
            ...state,
            io_last_taskId: action.data
        }

        // id
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
        
        // index
        case SET_IO_IDX:
        return {
            ...state,
            io_idx: action.data
        }
        case SET_SUB_IDX:
        return {
            ...state,
            sub_idx: action.data
        }

        case SET_IO_PROGRESS:
        return {
            ...state,
            io_progress: action.data
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