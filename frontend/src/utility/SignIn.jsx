// import { useContext } from "react";
// import { SET_ID, SET_NAME, SET_INST_TASKID, SET_IO_TASKID, SET_TASKNAME, userContext } from "../context/UserContext";
// import { GetUserId } from "./GetUserId";

// login을 처리
// localStorage에 정보가 있으면 그걸로 context 초기화
// 구축자명을 받아서 GetUserId를 이용해 context 초기화
function SignIn({ name }) {
    // const context = useContext(userContext);
    // const lname = window.localStorage.getItem('name');
    // const inst_taskid = window.localStorage.getItem('inst_taskid');
    // const io_taskid = window.localStorage.getItem('io_taskid');
    
    // // localStorage에 정보가 없을 때
    // if(!lname) {
    //     context.actions.contextDispatch({ type: SET_NAME, data: `${name}`});
    //     context.actions.contextDispatch({ type: SET_INST_TASKID, data: 1});
    //     context.actions.contextDispatch({ type: SET_IO_TASKID, data: 1});
    //     context.actions.contextDispatch({ type: SET_TASKNAME, data: `지시문`});
    // }
    // else{
    //     context.actions.contextDispatch({ type: SET_INST_TASKID, data: inst_taskid});
    //     context.actions.contextDispatch({ type: SET_IO_TASKID, data: io_taskid});
    // }
    // context.actions.contextDispatch({ type: SET_TASKNAME, data: `지시문`});
    // context.actions.contextDispatch({ type: SET_ID, data: GetUserId(name)});

    // // localStorage에 아이템이 존재하면 로그인 된 것.
    // return (!!lname);
}

export default SignIn;