import React, { useContext, useEffect, useState } from 'react';
import styles from './DuplicatedInstBody.module.css';
import { SET_INST_TASKID, userContext } from '../../context/UserContext';
import axios from 'axios';
import Table, { TableCell, TableHead, TableRow } from '../ui/table/Table';
import InputNumber from '../ui/input/InputNumber';

export default function DuplicatedInstBody() {
    const context = useContext(userContext);

    
    // test
    const first_taskId = context.state.data.first_taskId;
    const last_taskId = context.state.data.last_taskId;
    const subIdx = context.state.data.sub_idx;
    
    const taskId = context.state.data.inst_taskId;
    const handleTaskId = (value) => {
        context.actions.contextDispatch({ type: SET_INST_TASKID, data: parseInt(value) })
    }

    const [fakeData, ] = useState({
        originalInst: {
            part0: '의학 연구에서 치료는 연구 참가자 그룹 내에서 테스트됩니다. ',
            part1: '연구 참가자에 대한 정보를 제공하는 문구를 나열하는 것이 귀하의 임무인 연구 보고서 문장을 받게 됩니다. ',
            part2: '쉼표로 구분하여 텍스트에 나타나는 동일한 순서로 구문을 나열해야 합니다. 참가자에 대한 정보가 언급되지 않은 경우 "찾을 수 없음"만 출력합니다. ',
            part3: '참가자에 대한 관련 정보에는 성별, 건강 상태, 위치, 참가자 수가 포함됩니다. 관련 정보 없이 참가자 언급을 포함하지 마십시오.'
        },
        duplicatedList: [
            {
                instSubIdx: 2,
                duplicatedPart: [0, 1],
                partList: {
                    part0: '의학 연구에서 치료는 연구 참가자 그룹 내에서 테스트됩니다. ',
                    part1: '연구 참가자에 대한 정보를 제공하는 문구를 나열하는 것이 귀하의 임무인 연구 보고서 문장을 받게 됩니다. ',
                    part2: '쉼표로 구분하여 텍스트에 나타나는 동일한 순서로 구문을 나열해야 합니다. 참가자에 대한 정보가 언급되지 않은 경우 "찾을 수 없음"만 출력합니다. ',
                    part3: '참가자에 대한 관련 정보에는 성별, 건강 상태, 위치, 참가자 수가 포함됩니다. 관련 정보 없이 참가자 언급을 포함하지 마십시오.'
                }
            },
            {
                instSubIdx: 4,
                duplicatedPart: [0, 1],
                partList: {
                    part0: '의학 연구에서 치료는 연구 참가자 그룹 내에서 테스트됩니다. ',
                    part1: '연구 참가자에 대한 정보를 제공하는 문구를 나열하는 것이 귀하의 임무인 연구 보고서 문장을 받게 됩니다. ',
                    part2: '쉼표로 구분하여 텍스트에 나타나는 동일한 순서로 구문을 나열해야 합니다. 참가자에 대한 정보가 언급되지 않은 경우 "찾을 수 없음"만 출력합니다. ',
                    part3: '참가자에 대한 관련 정보에는 성별, 건강 상태, 위치, 참가자 수가 포함됩니다. 관련 정보 없이 참가자 언급을 포함하지 마십시오.'
                }
            }
        ]

    });

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
            <InputNumber
                context={taskId}
                setContext={handleTaskId} 
                maxNum={last_taskId} 
                minNum={first_taskId}
            />
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