import axios from 'axios';
import React, { useContext, useEffect, useState } from 'react';
import Directive from '../components/definition/Directive';
import EditDirective from '../components/definition/EditDirective';
import References from '../components/definition/References';
import CurrentFile from '../components/ui/CurrentFile';
import { userContext } from '../context/UserContext';

export default function EditDefinition() {
    const context = useContext(userContext);
    const taskId = context.state.data.taskId;
    const [defData, setDef] = useState();

    useEffect(() => {
        return () => {
            window.localStorage.setItem("name", context.state.data.name);
            window.localStorage.setItem("taskId", context.state.data.taskId);
            window.localStorage.setItem("taskName", context.state.data.taskName);
        }
    }, []);

    useEffect(() => {
        axios.get(`/api/tasks/${taskId}`)
        .then(function(res) {
            setDef(res.data.data);
        });
    }, [taskId]);

    return (
        defData && 
        <div className='body'>
            <CurrentFile ptaskName='지시문' />
            <Directive data={defData}/>
            <EditDirective />
            <References taskId={taskId} defData={defData}/>
        </div>
    );
}