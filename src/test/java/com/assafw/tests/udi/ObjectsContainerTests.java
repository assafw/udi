package com.assafw.tests.udi;

import com.assafw.udi.ObjectsContainer;
import com.assafw.udi.exceptions.DuplicateTypeRegistrationException;
import com.assafw.udi.exceptions.TypeRegistrationException;
import com.assafw.udi.exceptions.TypeResolutionException;
import com.assafw.udi.exceptions.TypeResolutionInstantiationException;
import com.assafw.tests.udi.testobjects.*;
import com.assafw.tests.udi.testobjects.interfaces.DummyType;
import com.assafw.tests.udi.testobjects.interfaces.DummyWithContainer;
import com.assafw.tests.udi.testobjects.interfaces.DummyWithDependency;
import com.assafw.tests.udi.testobjects.interfaces.DummyWithMultipleDependencies;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ObjectsContainerTests {

    private ObjectsContainer target;

    @Before
    public void setUp() {
        target = new ObjectsContainer();
    }

    @Test
    public void shouldResolveSuccessfullyWhenTypeIsRegistered()
            throws TypeResolutionException {

        registerDummy();
        DummyType resolved = target.resolve(DummyType.class);

        assertThat(resolved, notNullValue());
        assertThat(resolved, instanceOf(DummyTypeImpl.class));
    }

    @Test
    public void shouldResolveAndReturnTheSameInstanceWhenTryingToResolveMultipleTimes()
            throws TypeResolutionException {

        registerDummy();
        DummyType instance1 = target.resolve(DummyType.class);
        DummyType instance2 = target.resolve(DummyType.class);

        assertThat(instance1, sameInstance(instance2));
    }

    @Test(expected = TypeRegistrationException.class)
    public void shouldThrowExceptionWhenTryingToResolveAnUnregisteredType()
            throws TypeResolutionException {

        registerDummy();
        target.resolve(Object.class);
    }

    @Test
    public void shouldResolveSuccessfullyWhenTargetTypeCtorNeedsTheContainerInstance()
            throws TypeResolutionException {

        registerDummy();
        target.registerType(DummyWithContainer.class, DummyWithContainerImpl.class);
        target.registerInstance(ObjectsContainer.class, target);
        DummyWithContainer resolvedInstance = target.resolve(DummyWithContainer.class);

        assertThat(resolvedInstance, notNullValue());
        assertThat(resolvedInstance.getDummy(), notNullValue());
    }

    @Test
    public void shouldResolveSuccessfullyWhenTargetTypeDependsOnAlreadyRegisteredTypes()
            throws TypeResolutionException {

        registerDummy();
        target.registerType(DummyWithDependency.class, DummyWithDependencyImpl.class);
        DummyWithDependency resolvedInstance = target.resolve(DummyWithDependency.class);

        assertThat(resolvedInstance, notNullValue());
        assertThat(resolvedInstance.getDummy(), notNullValue());
    }

    @Test
    public void shouldResolveSuccessfullyWhenTargetTypeHasMultipleDependenciesThatAreAlreadyRegisteredTypes()
            throws TypeResolutionException {

        registerDummy();
        target.registerType(DummyWithDependency.class, DummyWithDependencyImpl.class);
        target.registerType(DummyWithMultipleDependencies.class, DummyWithMultipleDependenciesImpl.class);
        DummyWithMultipleDependencies resolvedInstance = target.resolve(DummyWithMultipleDependencies.class);

        assertThat(resolvedInstance, notNullValue());
        assertThat(resolvedInstance.getDummy(), notNullValue());
        assertThat(resolvedInstance.getDummy(), notNullValue());
    }

    @Test(expected = TypeResolutionInstantiationException.class)
    public void shouldThrowExceptionWhenTargetTypeHasMultipleDependenciesAndSomeAreMissing()
            throws TypeResolutionException {

        // Missing DummyType registration
        target.registerType(DummyWithDependency.class, DummyWithDependencyImpl.class);
        target.registerType(DummyWithMultipleDependencies.class, DummyWithMultipleDependenciesImpl.class);

        target.resolve(DummyWithMultipleDependencies.class);
    }

    @Test(expected = TypeResolutionInstantiationException.class)
    public void shouldThrowExceptionWhenTargetTypeDependsOnTypesThatAreNotRegistered()
            throws TypeResolutionException {

        target.registerType(DummyWithDependency.class, DummyWithDependencyImpl.class);
        target.resolve(DummyWithDependency.class);
    }

    @Test(expected = TypeResolutionInstantiationException.class)
    public void shouldThrowExceptionWhenTryingToResolveTypeWithMissingCtor()
            throws TypeResolutionException {

        target.registerType(DummyType.class, DummyTypePrivateCtorImpl.class);
        target.resolve(DummyType.class);
    }

    @Test(expected = TypeResolutionInstantiationException.class)
    public void shouldThrowExceptionWhenTryingToResolveTypeWithUnmatchingCtor()
            throws TypeResolutionException {

        target.registerType(DummyType.class, DummyTypeDifferentCtorImpl.class);
        target.resolve(DummyType.class);
    }

    @Test
    public void shouldReturnSameInstanceWhenRegisteringTypeWithSpecificInstance()
            throws TypeResolutionException {

        DummyTypeImpl instance = new DummyTypeImpl();
        target.registerInstance(DummyType.class, instance);

        assertThat(target.resolve(DummyType.class), notNullValue());
        assertThat(target.resolve(DummyType.class), sameInstance(instance));
    }

    @Test(expected = DuplicateTypeRegistrationException.class)
    public void shouldThrowExceptionWhenTryingToRegisterTheSameInstanceMoreThanOnce()
            throws DuplicateTypeRegistrationException {

        target.registerInstance(DummyType.class, new DummyTypeImpl());
        target.registerInstance(DummyType.class, new DummyTypeImpl());
    }

    @Test(expected = DuplicateTypeRegistrationException.class)
    public void shouldThrowExceptionWhenTryingToRegisterTheSameTypeMoreThanOnce()
            throws DuplicateTypeRegistrationException {

        registerDummy();
        registerDummy();
    }

    private void registerDummy() throws DuplicateTypeRegistrationException {
        target.registerType(DummyType.class, DummyTypeImpl.class);
    }
}