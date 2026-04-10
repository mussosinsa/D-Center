package org.uengine.kernel;

public class ProcessDefinition {
    private ActivityFilter[] activityFilters = new ActivityFilter[0];

    public void afterDeserialization() {
    }

    public void setActivityFilters(ActivityFilter[] activityFilters) {
        this.activityFilters = activityFilters;
    }

    public ActivityFilter[] getActivityFilters() {
        return activityFilters;
    }

    public ProcessInstance createInstance() {
        if (ProcessInstance.USE_CLASS != null) {
            try {
                Object obj = ProcessInstance.USE_CLASS.getDeclaredConstructor().newInstance();
                if (obj instanceof ProcessInstance) {
                    return (ProcessInstance) obj;
                }
            } catch (Exception ignore) {
            }
        }
        return new ProcessInstance();
    }
}
