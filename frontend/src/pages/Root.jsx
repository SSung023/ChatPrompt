import axios from 'axios';
import React, { useContext, useEffect } from 'react';
import { Outlet } from 'react-router-dom';
import Navigation from '../components/navigation/Navigation';
import { SET_FIRST_TASKID, SET_INST_TASKID, SET_LAST_TASKID, userContext } from '../context/UserContext';

export default function Root() {
    const context = useContext(userContext);

    useEffect(() => {
        // console.log('page loaded');
        axios.get('/api/user')
        .then(function(res) {
            // console.log(res.data.data);
            context.actions.contextDispatch({ type: SET_INST_TASKID, data: res.data.data.lastModifiedTaskNum });
            context.actions.contextDispatch({ type: SET_FIRST_TASKID, data: res.data.data.taskStartIdx });
            context.actions.contextDispatch({ type: SET_LAST_TASKID, data: res.data.data.taskEndIdx });
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

