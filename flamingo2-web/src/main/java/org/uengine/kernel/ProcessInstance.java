package org.uengine.kernel;

import java.util.HashMap;
import java.util.Map;

public class ProcessInstance {
    public static Class<?> USE_CLASS;

    private final Map<String, Object> values = new HashMap<>();
    private String instanceId;

    public Object get(String key) {
        return values.get(key);
    }

    public void set(String key, Object value) {
        values.put(key, value);
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public void execute() throws Exception {
        // no-op compatibility shim
    }
}
