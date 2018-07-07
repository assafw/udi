package com.assafw.udi.exceptions;

public class TypeResolutionInstantiationException extends TypeResolutionException {

    public <T> TypeResolutionInstantiationException(Class<T> requestedType) {
        super("Failed to instantiate type: " + requestedType.getName());
    }
}
