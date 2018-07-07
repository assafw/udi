package com.assafw.udi.exceptions;

public class DuplicateTypeRegistrationException extends TypeResolutionException {
    public <T> DuplicateTypeRegistrationException(Class<T> registerType) {
        super("Duplicate type registration: " + registerType.getName());
    }
}
