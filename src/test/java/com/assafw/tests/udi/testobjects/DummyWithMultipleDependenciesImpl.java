package com.assafw.tests.udi.testobjects;

import com.assafw.tests.udi.testobjects.interfaces.DummyType;
import com.assafw.tests.udi.testobjects.interfaces.DummyWithDependency;
import com.assafw.tests.udi.testobjects.interfaces.DummyWithMultipleDependencies;

public class DummyWithMultipleDependenciesImpl implements DummyWithMultipleDependencies {
    private final DummyType dummy;
    private final DummyWithDependency dummyDep;

    public DummyWithMultipleDependenciesImpl(DummyType dummy, DummyWithDependency dummyDep) {

        this.dummy = dummy;
        this.dummyDep = dummyDep;
    }

    @Override
    public DummyType getDummy() {
        return dummy;
    }

    @Override
    public DummyWithDependency getDummyWithDependency() {
        return dummyDep;
    }
}
