package com.nemisis.standalone.splitter_aggregation;

import java.util.List;

/**
 * Simple bean that contains a List of Strings.
 */
public class ListWrapper {

    private List<String> wrapped;

    private String wrapperName;

    public List<String> getWrapped() {
        return wrapped;
    }

    public void setWrapped(List<String> wrapped) {
        this.wrapped = wrapped;
    }

    public String getWrapperName() {
        return wrapperName;
    }

    public void setWrapperName(String wrapperName) {
        this.wrapperName = wrapperName;
    }
}