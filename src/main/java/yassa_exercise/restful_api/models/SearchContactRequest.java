package yassa_exercise.restful_api.models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class SearchContactRequest {

    private String name;

    private String phone;

    private String email;

    @NotBlank
    private Integer page;

    @NotBlank
    private Integer size;
}
