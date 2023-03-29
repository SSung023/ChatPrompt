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
    const subIdx = context.state.data.sub_idx;

    const [data, setData] = useState();
    
    // 유사 지시문
    useEffect(() => {
        axios.get(`/api/tasks/${taskId}/assignment/${subIdx}`)
        .then(function(res) {
            setData(res.data.data);
            context.actions.contextDispatch({ type: SET_TASKNAME, data: res.data.data.taskTitle});
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

