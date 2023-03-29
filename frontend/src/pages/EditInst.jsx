import axios from 'axios';
import React, { useContext, useEffect, useState } from 'react';
import Directive from '../components/definition/Directive';
import EditSimilarInst from '../components/definition/EditSimilarInst';
import References from '../components/definition/References';
import CurrentFile from '../components/ui/CurrentFile';
import { SET_TASKNAME, userContext } from '../context/UserContext';

export default function EditInst() {
    const context = useContext(userContext);
    const taskId = context.state.data.inst_taskId;
    const [originalDefData, setOrginal] = useState(); // 번역문, 원문
    const [defData, setDef] = useState(); // 지시문 1, 2

    // CurrentFile.jsx
    useEffect(() => {
        taskId && axios.get(`/api/tasks/${taskId}`)
        .then(function(res) {
            return res.data.data;
        })
        .then(function(data) {
            setOrginal(data);
            context.actions.contextDispatch({ type: SET_TASKNAME, data: data.taskTitle });
        })
    }, [taskId]);

    // Directive.jsx 지시문1, 지시문2
    useEffect(() => {
        axios.get(`/api/tasks/${taskId}/definitions`)
        .then(function(res) {
            setDef(res.data.data);
        })
        .catch(function(err) {
            if(err.response.status === 400){
                window.localStorage.removeItem("prompt-login");
                window.location.replace(window.location.href);
            }
        })
    }, [taskId]);


    return (
        defData && 
        <div className='body'>
            <CurrentFile ptaskName='지시문' taskId={taskId}/>
            <Directive defData={defData} originalDefData={originalDefData}/>
            <EditSimilarInst />
            <References taskId={taskId} originalDefData={originalDefData}/>
        </div>
    );
}