export const getId = ( taskId, pageName ) => {
    if(pageName == "inquire-inst") {
        if(taskId >= 1 && taskId < 31) return 'C'
        else if(taskId >= 31 && taskId < 61) return 'D'
        else if(taskId >= 61 && taskId < 91) return 'E'
        else if(taskId >= 91 && taskId < 121) return 'F'
    }
    else if(pageName == "inquire-io") {
        if(taskId >= 1 && taskId < 7) return 'C'
        else if(taskId >= 7 && taskId < 31) return 'B'
        else if(taskId >= 31 && taskId < 39) return 'D'
        else if(taskId >= 39 && taskId < 61) return 'B'
        else if(taskId >= 61 && taskId < 69) return 'E'
        else if(taskId >= 69 && taskId < 91) return 'B'
        else if(taskId >= 91 && taskId < 99) return 'F'
        else if(taskId >= 99 && taskId < 121) return 'B'
    }
    return;
}