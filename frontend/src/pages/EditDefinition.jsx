import React, { useContext, useEffect } from 'react';
import Directive from '../components/definition/Directive';
import EditDirective from '../components/definition/EditDirective';
import CurrentFile from '../components/ui/CurrentFile';
import { SET_NAME, SET_TASKID, SET_TASKNAME, userContext } from '../context/UserContext';

export default function EditDefinition() {
    const context = useContext(userContext);

    useEffect(() => {
        // if(window.localStorage.getItem("name")){
        //     const name = window.localStorage.getItem("name");
        //     context.actions.contextDispatch({ type: SET_NAME, data: `${name}` });
        //     const taskId = window.localStorage.getItem("taskId");
        //     context.actions.contextDispatch({ type: SET_TASKID, data: `${taskId}` });
        //     const taskName = window.localStorage.getItem("taskName");
        //     context.actions.contextDispatch({ type: SET_TASKNAME, data: `${taskName}` });
        // }
        return () => {
            window.localStorage.setItem("name", context.state.data.name);
            window.localStorage.setItem("taskId", context.state.data.taskId);
            window.localStorage.setItem("taskName", context.state.data.taskName);
        }
    }, []);
    return (
        <div className='body'>
            <CurrentFile ptaskName='지시문' />
            <Directive />
            <EditDirective />
        </div>
    );
}