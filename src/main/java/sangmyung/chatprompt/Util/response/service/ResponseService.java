package sangmyung.chatprompt.Util.response.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import sangmyung.chatprompt.Util.response.dto.CommonResponse;
import sangmyung.chatprompt.Util.response.dto.ListResponse;
import sangmyung.chatprompt.Util.response.dto.PagingResponse;
import sangmyung.chatprompt.Util.response.dto.SingleResponse;

import java.util.List;

/**
 * @Author : Jeeseob
 * @CreateAt : 2022/10/04
 */

@Service
public class ResponseService {

    private void setSuccessResult(CommonResponse response) {
        response.setStatus(response.getStatus());
        response.setMessage(response.getMessage());
    }

    private void setFailResult(CommonResponse response) {
        response.setStatus(response.getStatus());
        response.setMessage(response.getMessage());
    }

    public CommonResponse successResult() {
        CommonResponse response = new CommonResponse();
        this.setSuccessResult(response);

        return response;
    }

    public SingleResponse<String> failResult(String message) {
        SingleResponse<String> response = new SingleResponse<>(message);
        this.setFailResult(response);

        return response;
    }

    public <T> SingleResponse<T> singleResult(T data) {
        SingleResponse<T> response = new SingleResponse<>(data);
        this.setSuccessResult(response);

        return response;
    }

    public <T> ListResponse<T> listResult(List<T> dataList) {
        ListResponse<T> response = new ListResponse<>(dataList);
        this.setSuccessResult(response);

        return response;
    }

    public <T> PagingResponse<T> pagingResult(Page<T> data) {
        PagingResponse<T> response = new PagingResponse<T>(data);
        this.setSuccessResult(response);

        return response;
    }
}
