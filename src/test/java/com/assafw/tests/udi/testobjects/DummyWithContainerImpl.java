package com.assafw.tests.udi.testobjects;

import com.assafw.udi.ObjectsContainer;
import com.assafw.udi.exceptions.TypeResolutionException;
import com.assafw.tests.udi.testobjects.interfaces.DummyType;
import com.assafw.tests.udi.testobjects.interfaces.DummyWithContainer;

public class DummyWithContainerImpl implements DummyWithContainer {

    private DummyType dummy;

    public DummyWithContainerImpl(ObjectsContainer container) throws TypeResolutionException {
        dummy = container.resolve(DummyType.class);
    }

    public DummyType getDummy() {
        return dummy;
    }
}

