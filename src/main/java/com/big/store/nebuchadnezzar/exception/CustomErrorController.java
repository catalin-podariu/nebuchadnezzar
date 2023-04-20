package com.big.store.nebuchadnezzar.exception;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomErrorController extends AbstractErrorController {

    @Autowired
    public CustomErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @RequestMapping("/error")
    public ResponseEntity<?> handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            HttpStatus httpStatus = HttpStatus.valueOf(statusCode);

            if (httpStatus == HttpStatus.NOT_FOUND) {
                Object exceptionAttribute = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
                if (exceptionAttribute == null) {
                    return ResponseEntity.status(httpStatus).body("Endpoint not found");
                }
            } else if (httpStatus == HttpStatus.UNAUTHORIZED) {
                return ResponseEntity.status(httpStatus).body("Unauthorized");
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unknown error occurred. Initial error status was [" + status + "]");
    }
}
