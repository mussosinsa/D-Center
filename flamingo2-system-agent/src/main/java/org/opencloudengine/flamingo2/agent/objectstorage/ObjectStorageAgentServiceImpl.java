package org.opencloudengine.flamingo2.agent.objectstorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ObjectStorageAgentServiceImpl implements ObjectStorageAgentService {

    private final Logger logger = LoggerFactory.getLogger(ObjectStorageAgentServiceImpl.class);

    @Override
    public Map<String, Object> getObjectStorageInfo(Map<String, Object> options) {
        Map<String, Object> result = new HashMap<>();

        String endpoint = stringValue(options, "endpoint", "");
        String provider = stringValue(options, "provider", "object");
        String bucket = stringValue(options, "bucket", "");

        boolean reachable = ping(endpoint, intValue(options, "timeout", 3000));

        result.put("type", provider);
        result.put("provider", provider);
        result.put("bucket", bucket);
        result.put("endpoint", endpoint);
        result.put("reachable", reachable);

        result.put("all", 0L);
        result.put("dead", reachable ? 0L : 1L);
        result.put("live", reachable ? 1L : 0L);
        result.put("decommisioning", 0L);
        result.put("blocksTotal", 0L);
        result.put("corrupt", 0L);
        result.put("underReplicatedBlocks", 0L);
        result.put("totalFiles", 0L);
        result.put("totalBlocks", 0L);
        result.put("totalLoad", 0L);
        result.put("capacityRemaining", 0L);
        result.put("capacityRemainingPercent", 0);
        result.put("capacityTotal", 0L);
        result.put("capacityUsed", 0L);
        result.put("capacityUsedNonDFS", 0L);
        result.put("capacityUsedPercent", 0);
        result.put("editLogSize", 0L);
        result.put("free", 0L);
        result.put("used", 0L);
        result.put("total", 0L);
        result.put("threads", 0);
        result.put("jvmMaxMemory", 0L);
        result.put("jvmTotalMemory", 0L);
        result.put("jvmFreeMemory", 0L);
        result.put("jvmUsedMemory", 0L);
        result.put("timestamp", new Date());

        return result;
    }

    private boolean ping(String endpoint, int timeout) {
        if (endpoint == null || endpoint.trim().isEmpty()) {
            return false;
        }
        HttpURLConnection conn = null;
        try {
            URL url = new URL(endpoint);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout);
            conn.setRequestMethod("GET");
            int code = conn.getResponseCode();
            return code >= 200 && code < 500;
        } catch (Exception ex) {
            logger.debug("Object storage endpoint check failed: {}", endpoint, ex);
            return false;
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    private String stringValue(Map<String, Object> map, String key, String defaultValue) {
        Object value = map == null ? null : map.get(key);
        return value == null ? defaultValue : value.toString();
    }

    private int intValue(Map<String, Object> map, String key, int defaultValue) {
        Object value = map == null ? null : map.get(key);
        if (value == null) return defaultValue;
        try {
            return Integer.parseInt(value.toString());
        } catch (Exception ex) {
            return defaultValue;
        }
    }
}
