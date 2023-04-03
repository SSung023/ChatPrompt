import axios from 'axios';
import React, { useContext, useEffect } from 'react';
import { Outlet } from 'react-router-dom';
import Navigation from '../components/navigation/Navigation';
import { SET_FIRST_TASKID, SET_INST_TASKID, SET_IO_TASKID, SET_LAST_TASKID, userContext } from '../context/UserContext';

export default function Root() {
    const context = useContext(userContext);

    useEffect(() => {
        // console.log('page loaded');
        axios.get('/api/user')
        .then(function(res) {
            return res.data.data;
        })
        .then(function(data) {
            if(data.lastModifiedTaskNum < data.taskStartIdx || data.lastModifiedTaskNum > data.taskEndIdx){
                context.actions.contextDispatch({ type: SET_INST_TASKID, data: data.taskStartIdx });
                context.actions.contextDispatch({ type: SET_IO_TASKID, data: data.taskEndIdx });    
            }
            else {
                context.actions.contextDispatch({ type: SET_INST_TASKID, data: data.lastModifiedTaskNum });
                context.actions.contextDispatch({ type: SET_IO_TASKID, data: data.lastModifiedTaskNum });
            }
            context.actions.contextDispatch({ type: SET_FIRST_TASKID, data: data.taskStartIdx });
            context.actions.contextDispatch({ type: SET_LAST_TASKID, data: data.taskEndIdx });
        })
    }, []);

    return (
        <div className='root-wrapper'>
            <Navigation />
            <div>
                <Outlet />
            </div>
        </div>
    );
}

