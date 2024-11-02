package uz.tsue.ricoin.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ValidationErrorResponse {

    private Map<String, List<String>> validationErrors;

}