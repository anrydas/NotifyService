package das.tools.notifier.notify.exceptions;

import das.tools.notifier.notify.entitys.response.ApplicationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApplicationResponse> handleException(HttpMessageNotReadableException e) {
        ApplicationResponse res = ApplicationResponse.builder()
                .status(das.tools.notifier.notify.entitys.response.ResponseStatus.ERROR)
                .errorMessage("Received Invalid Parameter(s) in Request. Error message: " + e.getLocalizedMessage())
                .build();
        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WrongRequestParameterException.class)
    public ResponseEntity<ApplicationResponse> handleException(WrongRequestParameterException e) {
        ApplicationResponse res = ApplicationResponse.builder()
                .status(das.tools.notifier.notify.entitys.response.ResponseStatus.ERROR)
                .errorMessage("Error while sending message. Error message: " + e.getLocalizedMessage())
                .build();
        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }
}
