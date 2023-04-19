import React, { useContext, useEffect } from 'react';
import styles from './DuplicatedInstBody.module.css';
import { userContext } from '../../context/UserContext';
import axios from 'axios';
import Table, { TableCell, TableHead, TableRow } from '../ui/table/Table';

export default function DuplicatedInstBody() {
    const context = useContext(userContext);

    const taskId = context.state.data.inst_taskId;
    const subIdx = context.state.data.sub_idx;

    const handleLoad = () => {
        axios.get(`/api/inspect/tasks/${taskId}?targetIdx=${subIdx}`)
        .then(function(res) {
            console.log(res.data);
        })
        .catch(function(err) {
            console.log(err);
        })
    }

    useEffect(() => {
        handleLoad();
    }, [taskId, subIdx]);

    return (
        <div>
            
        </div>
    );
}

function OriginalInst() {
    return (
        <Table>

        </Table>
    );
}

function ShowDuplicatedInst({ duplicatedList, subIdx }) {
    return (
        <TableRow>
            <TableHead>{`지시문${subIdx}`}</TableHead>
            <TableCell>
                
            </TableCell>
        </TableRow>
    );
}