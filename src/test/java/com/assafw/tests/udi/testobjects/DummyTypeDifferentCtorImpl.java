package com.assafw.tests.udi.testobjects;

import com.assafw.tests.udi.testobjects.interfaces.DummyType;

public class DummyTypeDifferentCtorImpl implements DummyType {
    private String str;

    public DummyTypeDifferentCtorImpl(String str) {

        this.str = str;
    }
}
