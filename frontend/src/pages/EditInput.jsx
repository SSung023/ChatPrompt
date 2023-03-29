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

    return (
        <div className='body'>
            <CurrentFile />
            <Instruction />
            <EditIo />
            <IoReference taskId={taskId} idx={idx}/>
        </div>
    );
}

