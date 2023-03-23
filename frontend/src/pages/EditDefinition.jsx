import axios from 'axios';
import React, { useContext, useEffect, useState } from 'react';
import Directive from '../components/definition/Directive';
import EditDirective from '../components/definition/EditDirective';
import References from '../components/definition/References';
import CurrentFile from '../components/ui/CurrentFile';
import { userContext } from '../context/UserContext';

export default function EditDefinition() {
    const context = useContext(userContext);
    const taskId = context.state.data.inst_taskId;
    const [defData, setDef] = useState();

    useEffect(() => {
        axios.get(`/api/tasks/${taskId}/definitions`)
        .then(function(res) {
            setDef(res.data.data);
        })
        .catch(function(err) {
            if(err.response.status === 400){
                window.localStorage.removeItem("name");
                window.location.replace(window.location.href);
            }
        })
    }, [taskId]);

    // useEffect(() => {
    //     console.log(defData);
    // }, [defData]);

    return (
        defData && 
        <div className='body'>
            <CurrentFile ptaskName='지시문' taskId={taskId}/>
            <Directive data={defData}/>
            <EditDirective />
            <References taskId={taskId}/>
        </div>
    );
}