package com.assafw.udi.exceptions;

public class TypeRegistrationException extends TypeResolutionException {

    public <T> TypeRegistrationException(Class<T> resolveType) {
        super("Missing type registration: " + resolveType.getName());
    }
}
