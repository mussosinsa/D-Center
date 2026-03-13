package org.opencloudengine.flamingo2.agent.objectstorage;

import java.util.Map;

public interface ObjectStorageAgentService {

    Map<String, Object> getObjectStorageInfo(Map<String, Object> options);
}
