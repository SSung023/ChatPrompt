export const FormattedTaskID = (id) => {
    const taskId = `${id}`;
    return taskId.padStart(3, '0');
}

export const UnformattedTaskId = (value) => {
    return parseInt(value);
}