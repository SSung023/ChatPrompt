import React, { useContext } from 'react';
import IoEdit from '../components/input/IoEdit';
import CurrentFile from '../components/ui/CurrentFile';
import { userContext } from '../context/UserContext';

export default function EditInput() {
    const context = useContext(userContext);
    const taskId = context.state.data.io_taskId;
    return (
        <div className='body'>
            <CurrentFile ptaskName={`입출력`} taskId={taskId}/>
            <IoEdit />
        </div>
    );
}

