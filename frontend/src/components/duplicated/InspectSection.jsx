import React, { useContext, useMemo } from 'react';
import styles from './InspectSection.module.css';
import Table, { TableBody, TableCell, TableHead, TableRow } from '../ui/table/Table';
import { SET_SUB_IDX, userContext } from '../../context/UserContext';
import { useNavigate } from 'react-router-dom';
import Divider from '../ui/divider/Divider';

export default function InspectSection({ duplicates, originSection, sectionNum, originInst }) {
    const context = useContext(userContext);
    const subIdx = context.state.data.sub_idx;

    const makeDuplicatedInst = useMemo(() => {
        return duplicates.map((inst, idx) => {
            return <DuplicatedInst 
                        partIdx={inst.partIdx} 
                        partList={inst.partList} 
                        targetSubIdx={inst.targetSubIdx}
                        sectionNum={sectionNum}
                        key={idx}/>
        })
    }, [duplicates])
    
    return (
        <div className={styles.section}>
            <div className={styles.wrapper}>
                <p className={styles.sectionNum}>
                    <span>{`Section ${sectionNum+ 1}`}</span>
                    <span style={{ 
                        color: `var(--red-color)`, 
                        marginLeft: `1em`, 
                        fontWeight: `var(--regular)`,
                        fontSize: `14px`,    
                    }}>{duplicates.length !== 0 && `중복 ${duplicates.length}건이 발견되었어요.`}</span>
                    {/* {duplicates.length !== 0 
                    ? <span style={{ 
                        color: `var(--red-color)`, 
                        marginLeft: `0.5em`, 
                        fontWeight: `var(--regular)`,
                        fontSize: `14px`,    
                    }}>{`중복 ${duplicates.length}건이 발견되었어요.`}</span>
                    : <span style={{ 
                        color: `var(--main-color)`, 
                        marginLeft: `0.5em`, 
                        fontWeight: `var(--regular)`,
                        fontSize: `16px`,    
                    }}>{`✓`}</span>} */}
                </p>
                <OriginalInst subIdx={subIdx} originInst={originInst} originSection={originSection} sectionNum={sectionNum}/>
                {duplicates.length === 0 
                ? <p className={styles.none}>중복 지시문이 없어요 🎉</p> 
                : makeDuplicatedInst}
                {/* {duplicates.length !== 0 && makeDuplicatedInst} */}
            </div>
            <Divider />
        </div>
    );
}

function OriginalInst({ subIdx, originInst, originSection, sectionNum }) {
    return (
        <Table>
            <TableBody>
                <TableRow>
                    <TableHead>{`지시문 ${subIdx}`}</TableHead>
                    <TableCell>
                        <MakeHighlightedSentence sentence={originInst} subString={originSection} sectionNum={sectionNum}/>
                    </TableCell>
                </TableRow>
            </TableBody>
        </Table>
    );
}

function DuplicatedInst({ partIdx, partList, targetSubIdx, sectionNum }){
    const context = useContext(userContext);
    const taskId = context.state.data.inst_taskId;

    const navigate = useNavigate();

    return (
        <div className={styles.duplicateSection}>
            <div className={styles.duplicateHeader}>
                <p className={styles.duplicateInfo}>{`task: ${taskId} sub index: ${targetSubIdx}`}</p>
                <p 
                    className={styles.modifyBtn}
                    onClick={() => {
                        context.actions.contextDispatch({ type: SET_SUB_IDX, data: targetSubIdx });
                        navigate('../');
                    }}    
                >{`수정하러 가기 >`}</p>
            </div>
            <Table>
                <TableBody>
                    <TableRow>
                        <TableHead>{`지시문 ${targetSubIdx}`}</TableHead>
                        <TableCell>
                            <MakeDuplicatedHighlightedSentence partList={partList} partIdx={partIdx} sectionNum={sectionNum}/>
                        </TableCell>
                    </TableRow>
                </TableBody>
            </Table>
        </div>
    );
}

function MakeHighlightedSentence({ sentence, subString, sectionNum }) {
    const index = sentence.indexOf(subString);
    const before = sentence.slice(0, index);
    const matched = sentence.slice(index, index + subString.length);
    const after = sentence.slice(index + subString.length);
        
    const highlightStyle = {
        background: `linear-gradient(to top, var(--h-color-${sectionNum}) 30%, transparent 40%)`,
        borderRadius: `5px`,
    };

    return (
        <p>
            {before && <span>{before}</span>}
            <span style={highlightStyle}>{matched}</span>
            {after && <span>{after}</span>}
        </p>
    );
}

function MakeDuplicatedHighlightedSentence({ partList, partIdx, sectionNum }) {
    const highlightStyle = {
        background: `linear-gradient(to top, var(--h-color-${sectionNum}) 30%, transparent 40%)`,
        borderRadius: `5px`,
    };

    const makehighlightedSentence = useMemo(() => {
        return partList.map((part, idx) => {
            if (partIdx.includes(idx))
                return <span style={highlightStyle} key={idx}>{part}</span>
            return <span key={idx}>{part}</span>
        })
    }, [partList]);

    return (
        <p>
            {makehighlightedSentence}
        </p>
    );
}