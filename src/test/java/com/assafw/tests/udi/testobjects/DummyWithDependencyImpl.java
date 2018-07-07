package com.assafw.tests.udi.testobjects;

import com.assafw.tests.udi.testobjects.interfaces.DummyType;
import com.assafw.tests.udi.testobjects.interfaces.DummyWithDependency;

public class DummyWithDependencyImpl implements DummyWithDependency {

    private DummyType dummy;

    public DummyWithDependencyImpl(DummyType dummy) {
        this.dummy = dummy;
    }

    @Override
    public DummyType getDummy() {
        return dummy;
    }
}
