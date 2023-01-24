package com.softura.skillup.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class RecordNotFoundException extends SkillUpException {
    public RecordNotFoundException(Long id) {
        super("Record with given id " + id + " is not found");
    }

}
