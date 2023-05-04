import React, { useContext, useEffect, useMemo, useState } from 'react';
import styles from './DuplicatedInstBody.module.css';
import { userContext } from '../../context/UserContext';
import axios from 'axios';
import InspectSection from './InspectSection';

export default function DuplicatedInstBody() {
    const context = useContext(userContext);
    // test
    const taskId = context.state.data.inst_taskId;
    const subIdx = context.state.data.sub_idx;

    const [duplicateList, setDuplicateList] = useState();
    const [originInst, setOriginInst] = useState();

    const handleLoad = () => {
        axios.get(`/api/inspect/tasks/${taskId}?targetIdx=${subIdx}`)
        .then(function(res) {
            console.log(res.data.data);
            return res.data.data;
        })
        .then(function(data) {
            setOriginInst(data.originalInstruct);
            setDuplicateList(data.duplicateList);
        })
        .catch(function(err) {
            if(err.response.status === 400){
                alert('세션이 만료되었습니다. 로그인 후 다시 시도해주세요.');
                window.localStorage.removeItem("prompt-login");
                window.location.replace(window.location.href);
            }
        })
    }

    const makeSection = useMemo(() => {
        return duplicateList && duplicateList.map((data) => {
            return <InspectSection 
                        duplicates={data.duplicates}
                        originSection={data.originSection}
                        sectionNum={data.sectionNum} 
                        originInst={originInst} 
                        key={data.sectionNum}/>
        })
    }, [duplicateList]);

    useEffect(() => {
        handleLoad();
    }, [taskId, subIdx]);

    return (
        <div>
            {makeSection}
        </div>
    );
}