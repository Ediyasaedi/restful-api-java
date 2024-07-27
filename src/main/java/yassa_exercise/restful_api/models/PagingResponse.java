package yassa_exercise.restful_api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagingResponse {
    private Integer currentPage;
    private Integer size;
    private Integer total;
}
