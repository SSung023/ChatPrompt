import React, { useContext, useEffect, useState } from 'react';
import { TableCell, TableHead, TableRow } from '../ui/table/Table';
import styles from './IoInspect.module.css';

import { VscEyeClosed, VscEye } from 'react-icons/vsc';
import axios from 'axios';
import { SET_IO_PROGRESS, userContext } from '../../context/UserContext';
import InfoBox from '../ui/information/InfoBox';

export default function IoInspect({ data, idx }) {
    const row1 = (
        <Input data={data.input} idx={idx} key={(idx-1)*2}/>
    )
    const row2 = (
        <Output data={data.output} idx={idx} key={(idx-1)*2+1} isValidated={data.isValidated}/>
    );
    return (
        data && [row1, row2]
    );
}

function Input({ data, idx }) {
    return (
        <TableRow>
            <TableHead>{`입력 ${idx}`}</TableHead>
            <TableCell>
                {!data
                    ? <span style={{color:"var(--placeholder-txt-color)"}} className="noDrag">작성한 입력이 없습니다.</span>
                    : <span>{`${data}`}</span>}
            </TableCell>
        </TableRow>
    );
}

function Output({ data, idx, isValidated }) {
    const context = useContext(userContext);
    const taskId = context.state.data.io_taskId;

    const [isChecked, setChecked] = useState(isValidated);
    
    const handleChange = (e) => {
        console.log(typeof(isChecked));
        axios.patch(`/api/verifications/tasks/${taskId}/io/${idx}?verify=${isChecked ? 'no' : 'yes'}`)
        .then(function(res) {
            return res.data.data;
        })
        .then(function(data) {
            setChecked(Boolean(data.isValidated));
            context.actions.contextDispatch({ type: SET_IO_PROGRESS, data: data.validatedCnt });
        })
        .catch(function(err) {
            if(err.response.status === 400){
                alert('세션이 만료되었습니다. 로그인 후 다시 시도해주세요.');
                window.localStorage.removeItem("prompt-login");
                window.location.replace(window.location.href);
            }
        })
    }

    useEffect(() => {
        setChecked(Boolean(isValidated));
    }, [isValidated]);

    return (
        <TableRow>
            <TableHead>{`출력 ${idx}`}</TableHead>
            <TableCell>
                <div className={styles.output}>
                    {!data
                        ? <span style={{color:"var(--placeholder-txt-color)"}} className="noDrag">작성한 출력이 없습니다.</span>
                        : <span className={isChecked ? `${styles.open}` : `${styles.hidden}`}>{`${data}`}</span>}

                    <input 
                        className={styles.checkbox}
                        type="checkbox" 
                        id={`verifi${idx}`}
                        checked={isChecked ? true : false}
                        onChange={handleChange}
                    />
                    {data && 
                    <label 
                        className={styles.label} 
                        htmlFor={`verifi${idx}`}
                    >
                        {(isChecked 
                        ? <VscEye color='var(--main-color)'/> 
                        : <VscEyeClosed color='var(--placeholder-txt-color)'/>) }
                    </label>}

                    <InfoBox>
                        <p style={{ fontWeight: `var(--bold)`, color: `var(--main-color)`, marginBottom: `6px`, }}>✅  도움말</p>
                        <p><VscEyeClosed/> 버튼을 활성화하면 달성률이 올라갑니다.</p>
                        <p style={{ color: `var(--main-color)`, fontSize: `12px`, }}>버튼이 안 보인다면 입출력을 작성해 주세요!</p>
                    </InfoBox>
                </div>
            </TableCell>
        </TableRow>
    );
}

