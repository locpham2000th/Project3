package com.example.project3.service.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.StatusType;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends AbstractThrowableProblem {

    public BadRequestException(String key, String param){
        super(null, key, new StatusType() {
            @Override
            public int getStatusCode() {
                return 400;
            }

            @Override
            public String getReasonPhrase() {
                return null;
            }
        }, param);
    }

}
