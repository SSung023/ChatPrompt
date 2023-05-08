import axios from 'axios';
import React, { useContext, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { userContext } from '../../context/UserContext';
import Table, { TableBody } from '../ui/table/Table';
import IoInspect from './IoInspect';
import styles from './IoPairs.module.css';

export default function IoPairs() {
    const navigate = useNavigate();

    const context = useContext(userContext);

    // store 정보
    const taskId = context.state.data.io_taskId;
    const idx = context.state.data.io_idx;

    // 내부 관리용
    const [taskIdx, setIdx] = useState(1);
    const [io, setIo] = useState({});

    const handleLoad = (e) => {
        taskId && axios.get(`/api/verifications/tasks/${taskId}/io/${idx}`)
        .then(function(res) {
            return res.data.data;
        })
        .then(function(data) {
            setIo(data);
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
        // 입출력 작성 폼에 불러오기
        handleLoad();
        setIdx(idx);
    }, [taskId, idx]);

    return (
        <div>
            <Table>
                <TableBody>
                    <IoInspect data={io} idx={idx}/>
                </TableBody>
            </Table>

            <p 
                className={styles.modifyBtn}
                onClick={() => {
                    navigate('../input');
                }}
            >{`입출력 수정하러 가기 >`}</p>
        </div>
    );
}

