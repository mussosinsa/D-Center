package org.uengine.kernel;

import org.uengine.kernel.graph.Transition;

import java.util.ArrayList;
import java.util.List;

public class Activity {
    public static final String ACTIVITY_STOPPED = "ACTIVITY_STOPPED";

    private String tracingTag;
    private String name;
    private final List<Transition> incomingTransitions = new ArrayList<>();

    public String getTracingTag() {
        return tracingTag;
    }

    public void setTracingTag(String tracingTag) {
        this.tracingTag = tracingTag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Transition> getIncomingTransitions() {
        return incomingTransitions;
    }
}
