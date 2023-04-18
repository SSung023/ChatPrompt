import React from 'react';
import InstHeader from '../components/duplicated/InstHeader';
import Divider from '../components/ui/divider/Divider';
import DuplicatedInstBody from '../components/duplicated/DuplicatedInstBody';

export default function DuplicatedInst() {
    return (
        <div className='body'>
            <InstHeader/>
            <Divider/>
            <DuplicatedInstBody />
        </div>
    );
}