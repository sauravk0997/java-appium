package com.disney.listeners;

import com.disney.heath_check.clients.DynamoDBClient;
import com.disney.heath_check.clients.MultiverseDevicesClient;
import com.disney.heath_check.response.MultiverseDevicesResponse;
import com.qaprosoft.carina.core.foundation.utils.R;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.annotations.Listeners;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Listeners
public class MultiverseDevicesListener implements ISuiteListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final MultiverseDevicesClient CLIENT = new MultiverseDevicesClient();

    private String platform = "";

    @Override
    public void onStart(ISuite suite) {
        String triggerType = StringUtils.isBlank(System.getProperty("ci_build_cause")) ? "" : System.getProperty("ci_build_cause");
        String jobName = StringUtils.isBlank(System.getProperty("JOB_BASE_NAME")) ? "" : System.getProperty("JOB_BASE_NAME").toLowerCase();
        if ((triggerType.equalsIgnoreCase("timertrigger") || triggerType.equalsIgnoreCase("upstreamtrigger")) && isValidJob(jobName)) {
            LOGGER.info("Multiverse Devices Listener -> Starting device health check for {}", jobName);
            String version = System.getProperty("build");
            MultiverseDevicesResponse response = CLIENT.requestHealthCheck(platform, version);
            LOGGER.info("Health Check started for: {}  platform: {} Job ID: {}", jobName, platform, response.getId());
            int retryCount = 0;
            boolean isCompleted = false;
            while (!isCompleted && retryCount < 12) {
                try {
                    TimeUnit.SECONDS.sleep(20);
                    isCompleted = CLIENT.getHealthCheckStatus(response.getId()).getStatus().equalsIgnoreCase("completed");
                    retryCount++;
                } catch (Exception e) {
                    LOGGER.error("Thread was interrupted while sleeping due to: {}", e.getMessage());
                    retryCount++;
                }
            }
            List<String> deviceName = DynamoDBClient.getInstance()
                    .getAvailableDevicesForPlatform(platform).stream().map(item -> item.get("deviceName").getS()).collect(Collectors.toList());
            int threadCount = deviceName.size();
            R.CONFIG.put("capabilities.deviceName", String.join(",", deviceName));
            suite.getXmlSuite().setThreadCount(threadCount);
            R.CONFIG.put("thread_count", String.valueOf(threadCount));
            LOGGER.info("Thread count Suite: {} \nThread count R.CONFIG: {}", suite.getXmlSuite().getThreadCount(), R.CONFIG.get("thread_count"));
            LOGGER.info("Updated devices List: {}", R.CONFIG.get("capabilities.deviceName"));
            LOGGER.info("Multiverse Devices Listener -> Health Check Completion: {}", isCompleted);
        }
    }
    public boolean isValidJob(String jobName) {
        if (jobName.contains("nightly")) {
            if (jobName.contains("partner")) {
                platform = "android-tv-partner";
            } else {
                platform = jobName.contains("fire") ? "fire-tv" : "android-tv";
            }
            return true;
        }

        if (jobName.contains("regression")) {
            if (jobName.contains("handset")) {
                platform = "android";
                return true;
            } else if (jobName.contains("tablet")) {
                platform = jobName.contains("fire") ? "fire-tab" : "android-tab";
                return true;
            }
        }

        return false;
    }
}
