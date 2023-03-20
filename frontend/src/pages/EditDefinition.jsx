import React from 'react';
import Directive from '../components/definition/Directive';
import CurrentFile from '../components/ui/CurrentFile';

export default function EditDefinition() {
    return (
        <div className='body'>
            <CurrentFile name='김다은' taskId='001' taskName='지시문' />
            <Directive />
        </div>
    );
}