import React, { useState } from 'react';
import StatusBar from '../components/inspect/StatusBar';
import Instruction from '../components/input/Instruction';
import IoPairs from '../components/inspect/IoPairs';

export default function Inspect() {
    return (
        <div className='body'>
            <StatusBar/>
            <Instruction />
            <IoPairs/>
        </div>
    );
}

