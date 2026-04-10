package org.opencloudengine.flamingo2.collector.jobs;

import org.opencloudengine.flamingo2.collector.ApplicationContextRegistry;
import org.opencloudengine.flamingo2.collector.repository.HdfsService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ObjectStorageUsageJob extends RemoteInvocation {

    private Logger logger = LoggerFactory.getLogger(ObjectStorageUsageJob.class);

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("Object Storage 사용량/연결 정보 수집을 시작합니다.");

        ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
        Map<String, SystemConfig> configs = ConfigurationHolder.getConfigs();
        HdfsService hdfsService = applicationContext.getBean(HdfsService.class);

        Collection<SystemConfig> values = configs.values();
        for (SystemConfig config : values) {
            if (!config.isObjectStorageEnabled()) {
                continue;
            }

            String address = config.getObjectAgentAddress();
            int port = config.getObjectAgentPort();

            ObjectStorageAgentService service = getObjectStorageAgentService(address, port);
            try {
                Map<String, Object> options = new HashMap<>();
                options.put("provider", config.getObjectStorageProvider());
                options.put("endpoint", config.getObjectStorageEndpoint());
                options.put("bucket", config.getObjectStorageBucket());
                options.put("timeout", config.getObjectStorageTimeout());

                Map<String, Object> storageInfo = service.getObjectStorageInfo(options);
                if (storageInfo == null) {
                    storageInfo = new HashMap<>();
                }
                storageInfo.put("type", config.getObjectStorageProvider());
                hdfsService.collect(config.getId(), config.getName(), storageInfo);
            } catch (Exception ex) {
                logger.warn("Object Storage 정보를 수집할 수 없습니다. cluster={}", config.getId(), ex);
            }
        }
    }

    private ObjectStorageAgentService getObjectStorageAgentService(String agentAddress, int agentPort) {
        String remoteServiceUrl = this.getRemoteServiceUrl(agentAddress, agentPort, "objectstorage");
        return this.getRemoteService(remoteServiceUrl, ObjectStorageAgentService.class);
    }
}
