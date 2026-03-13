package org.uengine.kernel;

public class DefaultActivity extends Activity {
    protected void executeActivity(final ProcessInstance instance) throws Exception {
        // extension point
    }

    protected void fireComplete(final ProcessInstance instance) throws Exception {
        // no-op compatibility shim
    }
}
