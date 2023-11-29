package com.bci.reactive.exception;

import javax.persistence.NonUniqueResultException;

public class EmailNonUniqueResultException extends NonUniqueResultException {

    public EmailNonUniqueResultException(String message) {
        super(message);
    }
}
