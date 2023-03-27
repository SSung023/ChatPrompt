import axios from 'axios';
import React, { useCallback, useContext, useEffect, useMemo, useState } from 'react';
import { userContext } from '../../context/UserContext';
import Table, { TableBody, TableCell, TableHead, TableRow } from '../ui/table/Table';
import styles from './ShowInst.module.css';

export default function ShowInst() {
    const context = useContext(userContext);
    const first_taskId = context.state.data.first_taskId;
    const last_taskId = context.state.data.last_taskId;

    const [taskNum, setTaskNum] = useState(context.state.data.inst_taskId);
    const [taskId, setTaskId] = useState(taskNum);

    const [data, setData] = useState();

    const makeDefinitions = useMemo(() => {
        return (
            data && 
            data.map((definitions, idx) => {
                const row1 = (
                    <TableRow key={idx*2}>
                        <TableHead>{`지시문 1`}</TableHead>
                        <TableCell>
                            <span>지시문 1</span>
                        </TableCell>
                    </TableRow>
                )
                const row2 = (
                    <TableRow key={idx*2+1}>
                        <TableHead>{`지시문 2`}</TableHead>
                        <TableCell>
                            <span>지시문 2</span>
                        </TableCell>
                    </TableRow>
                );
                return [row1, row2];
            })
            
        );
    }, [data]);

    const load = () => {
        axios.get(`/api/tasks/${taskNum}/assignment-similar/lists`)
        .then(function(res) {
            console.log(res);
            return res.data.dataList;
        })
        .then(function(data) {
            setData(data);
            // console.log(data);
        })
        .catch(function(err) {
            if(err.response.status === 400){
                window.localStorage.removeItem("prompt-login");
                window.location.replace(window.location.href);
            }
        })
    }

    const handleSubmit = (e) => {
        e.preventDefault();
        setTaskId(taskNum);
    }

    useEffect(() => {
        load();
    }, [taskId]);

    return (
        <div className={styles.showInst}>
            <form
                onSubmit={handleSubmit}>
                <label>task: </label>
                <input 
                    type="number" 
                    value={taskNum}
                    onChange={(e) => {
                        const value = e.target.value;
                        value >= first_taskId && value <= last_taskId && setTaskNum(parseInt(e.target.value))
                    }}
                    max="120"
                    min="1"
                />
            </form>
            
            <Table>
                <TableBody>
                    <TableRow>
                        <TableHead></TableHead>
                    </TableRow>
                    {makeDefinitions}
                </TableBody>
            </Table>
        </div>
    );
}

