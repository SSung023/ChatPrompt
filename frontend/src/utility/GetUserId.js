const userId = [
    { name: '박소영' },
    { name: '김다은' },
    { name: '성희연' },
    { name: '홍길동' },
    { name: '권경란' },
]

export const GetUserId = (name) => {
    return userId.findIndex(i => i.name == `${name}`) + 1;
}