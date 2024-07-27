package yassa_exercise.restful_api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class WebResponse <T>{

    private boolean success;
    private T data;
    private PagingResponse paging;
}

