package com.assafw.tests.udi.testobjects;

import com.assafw.tests.udi.testobjects.interfaces.DummyType;

public class DummyTypePrivateCtorImpl implements DummyType {

    private DummyTypePrivateCtorImpl() {
    }

    public static DummyTypePrivateCtorImpl get() {
        return new DummyTypePrivateCtorImpl();
    }
}