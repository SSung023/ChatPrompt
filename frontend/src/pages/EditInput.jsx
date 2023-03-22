import axios from 'axios';
import React, { useContext, useEffect, useState } from 'react';
import Instruction from '../components/input/Instruction';
import IoEdit from '../components/input/IoEdit';
import CurrentFile from '../components/ui/CurrentFile';
import { userContext } from '../context/UserContext';

export default function EditInput() {
    const context = useContext(userContext);
    const taskId = context.state.data.io_taskId;

    const [data, setData] = useState();
    
    useEffect(() => {
        axios.get(`/api/tasks/${taskId}/assignment-similar`)
        .then(function(res) {
            setData(res.data.data);
        })
    }, [taskId]);

    return (
        <div className='body'>
            <CurrentFile ptaskName={`입출력`} taskId={taskId}/>
            <Instruction data={data}/>
            <IoEdit />
        </div>
    );
}

