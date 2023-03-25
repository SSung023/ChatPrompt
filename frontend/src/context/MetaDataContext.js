import React, { createContext, useReducer } from "react";

export const SET_HEIGHT = 'user/SET_HEIGHT';

export const metaDataContext = createContext();

const initData = {
    scrollHeight: 0,
}

const metaDataReducer = (state, action) => {
    switch(action.type){
        case SET_HEIGHT:
        return {
            ...state,
            scrollHeight : action.data
        }
        default:
            return;
    }
}

const MetaDataProvider = ({ children }) => {
    const [data, contextDispatch] = useReducer(metaDataReducer, initData);

    const value = {
        state: { data },
        actions: { contextDispatch }
    }

    return (
        <metaDataContext.Provider value = {value}>
            {children}
        </metaDataContext.Provider>
    );
}

export default MetaDataProvider;