package org.uengine.kernel.graph;

import org.uengine.kernel.Activity;

public class Transition {
    private Activity sourceActivity;

    public Activity getSourceActivity() {
        return sourceActivity;
    }

    public void setSourceActivity(Activity sourceActivity) {
        this.sourceActivity = sourceActivity;
    }
}
