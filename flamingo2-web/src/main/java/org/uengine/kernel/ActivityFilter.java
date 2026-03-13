package org.uengine.kernel;

public interface ActivityFilter {
    void beforeExecute(Activity activity, ProcessInstance processInstance) throws Exception;

    void afterExecute(Activity activity, ProcessInstance processInstance) throws Exception;

    void afterComplete(Activity activity, ProcessInstance processInstance) throws Exception;

    void onPropertyChange(Activity activity, ProcessInstance processInstance, String propertyName, Object value) throws Exception;

    void onDeploy(ProcessDefinition processDefinition) throws Exception;

    void onEvent(Activity activity, ProcessInstance processInstance, String eventName, Object payload) throws Exception;
}
