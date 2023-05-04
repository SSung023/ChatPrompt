import React from 'react';
import StatusBar from '../components/inspect/StatusBar';
import IoPairs from '../components/inspect/IoPairs';
import InspectInstruction from '../components/inspect/InspectInstruction';

export default function Inspect() {
    return (
        <div className='body'>
            <StatusBar/>
            <InspectInstruction />
            <IoPairs/>
        </div>
    );
}

