package org.uengine.kernel;

public abstract class SensitiveActivityFilter implements ActivityFilter {
    @Override
    public void beforeExecute(Activity activity, ProcessInstance processInstance) throws Exception {
    }

    @Override
    public void afterExecute(Activity activity, ProcessInstance processInstance) throws Exception {
    }

    @Override
    public void afterComplete(Activity activity, ProcessInstance processInstance) throws Exception {
    }

    @Override
    public void onPropertyChange(Activity activity, ProcessInstance processInstance, String propertyName, Object value) throws Exception {
    }

    @Override
    public void onDeploy(ProcessDefinition processDefinition) throws Exception {
    }

    @Override
    public void onEvent(Activity activity, ProcessInstance processInstance, String eventName, Object payload) throws Exception {
    }
}
