package com.bci.reactive.exception;

import javax.persistence.NonUniqueResultException;

public class PhoneNonUniqueResultException extends NonUniqueResultException {

    public PhoneNonUniqueResultException(String message) {
        super(message);
    }
}
