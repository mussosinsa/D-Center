package org.uengine.processpublisher;

import org.uengine.kernel.ProcessDefinition;

import java.io.InputStream;

public class BPMNUtil {
    public static ProcessDefinition adapt(InputStream inputStream) {
        return new ProcessDefinition();
    }
}
