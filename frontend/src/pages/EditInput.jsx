import axios from 'axios';
import React, { useContext, useEffect, useState } from 'react';
import Instruction from '../components/input/Instruction';
import EditIo from '../components/input/EditIo';
import CurrentFile from '../components/ui/CurrentFile';
import { SET_TASKNAME, userContext } from '../context/UserContext';
import IoReference from '../components/input/IoReference';

export default function EditInput() {
    const context = useContext(userContext);
    const taskId = context.state.data.io_taskId;
    const idx = context.state.data.io_idx;

    const [data, setData] = useState();

    // CurrentFile.jsx
    // useEffect(() => {
    //     taskId && axios.get(`/api/tasks/${taskId}`)
    //     .then(function(res) {
    //         return res.data.data;
    //     })
    //     .then(function(data) {
    //         context.actions.contextDispatch({ type: SET_TASKNAME, data: data.taskTitle });
    //     })
    // }, [taskId]);
    
    // 유사 지시문
    useEffect(() => {
        axios.get(`/api/tasks/${taskId}/assignment-similar`)
        .then(function(res) {
            setData(res.data.data);
        })
    }, [taskId]);

    return (
        <div className='body'>
            <CurrentFile taskId={taskId} idx={idx}/>
            <Instruction data={data}/>
            <EditIo />
            <IoReference taskId={taskId} idx={idx}/>
        </div>
    );
}

