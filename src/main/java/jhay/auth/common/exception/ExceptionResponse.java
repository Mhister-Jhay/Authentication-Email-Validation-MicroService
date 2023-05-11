package jhay.auth.common.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Builder
public class ExceptionResponse {
    private LocalDateTime time;
    private String message;
    private String path;
    private Integer statusCode;
}
