package com.assafw.udi;

import com.assafw.udi.exceptions.DuplicateTypeRegistrationException;
import com.assafw.udi.exceptions.TypeRegistrationException;
import com.assafw.udi.exceptions.TypeResolutionException;
import com.assafw.udi.exceptions.TypeResolutionInstantiationException;
import com.assafw.udi.utils.CollectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class ObjectsContainer {

    private Map<Class, Class> typeCacheMap;
    private Map<Class, Object> resolvedTypeCache;

    public ObjectsContainer() {
        typeCacheMap = new HashMap<>();
        resolvedTypeCache = new HashMap<>();
    }

    public <T1, T2> void registerType(Class<T1> registerType, Class<T2> instanceType) throws DuplicateTypeRegistrationException {
        if (!typeCacheMap.containsKey(registerType)) {
            typeCacheMap.put(registerType, instanceType);
        } else {
            throw new DuplicateTypeRegistrationException(registerType);
        }
    }

    public <T> T resolve(Class<T> resolveType) throws TypeResolutionException {
        return resolveInternal(resolveType);
    }

    public <T> void registerInstance(Class<T> registerType, Object instance) throws DuplicateTypeRegistrationException {
        if (typeCacheMap.containsKey(registerType)) {
            throw new DuplicateTypeRegistrationException(registerType);
        }

        typeCacheMap.put(registerType, instance.getClass());
        resolvedTypeCache.put(registerType, instance);
    }

    private <T> T resolveInternal(Class resolveType) throws TypeResolutionException {
        if (typeCacheMap.containsKey(resolveType)) {

            if (resolvedTypeCache.containsKey(resolveType)) {
                return (T) resolvedTypeCache.get(resolveType);
            } else {
                return instantiateAndRegisterType(resolveType);
            }
        }

        throw new TypeRegistrationException(resolveType);
    }

    private <T> T instantiateAndRegisterType(Class resolveType) throws TypeResolutionException {
        Class targetType;

        try {
            targetType = Class.forName(typeCacheMap.get(resolveType).getName());
        } catch (ClassNotFoundException e) {
            throw new TypeRegistrationException(resolveType);
        }

        Optional<T> instance = tryInstantiate(targetType);

        if (!instance.isPresent()) {
            throw new TypeResolutionInstantiationException(resolveType);
        }

        resolvedTypeCache.put(resolveType, instance.get());
        return instance.get();
    }

    private <T> Optional<T> tryInstantiate(Class<T> targetType) {
        Optional<T> instance = tryInstantiateWithContainer(targetType);

        if (!instance.isPresent()) {
            instance = tryInstantiateWithDefaultCtor(targetType);
        }

        return instance;
    }

    private <T> Optional<T> tryInstantiateWithDefaultCtor(Class targetType) {
        try {
            return Optional.of((T) targetType.newInstance());

        } catch (InstantiationException | IllegalAccessException e) {

            return Optional.empty();
        }
    }

    private <T> Optional<T> tryInstantiateWithContainer(Class targetType) {

        try {
            Constructor[] candidateCtors = targetType.getDeclaredConstructors();
            for (Constructor ctor : candidateCtors) {
                if (ctor.getParameterCount() > 0) {
                    List<Object> argMatches = getCtorArgs(ctor);

                    if (foundAllArgsForCtor(ctor, argMatches)) {
                        return Optional.of((T) ctor.newInstance(argMatches.toArray()));
                    }
                }
            }
            return Optional.empty();
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            return Optional.empty();
        }
    }

    private List<Object> getCtorArgs(Constructor ctor) {
        return Arrays.stream(ctor.getParameterTypes())
                .map(param -> resolveCtorParam(param))
                .filter(arg -> arg.isPresent())
                .map(arg -> arg.get())
                .collect(Collectors.toList());
    }

    private boolean foundAllArgsForCtor(Constructor ctor, List<Object> argMatches) {
        return !CollectionUtils.isNullOrEmpty(argMatches) && argMatches.size() == ctor.getParameterCount();
    }

    private Optional<Object> resolveCtorParam(Class param) {
        try {
            return Optional.of(resolveInternal(param));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }
}