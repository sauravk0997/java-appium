package com.disney.qa.carina;

import com.disney.qa.api.disney.DisneyParameters;
import com.qaprosoft.carina.core.foundation.utils.Configuration;
import com.qaprosoft.carina.core.foundation.utils.R;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.params.SetParams;

import java.lang.invoke.MethodHandles;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.Locale;

public class AllowListManager {

    private static final String REDIS_HOST = "primary.verified-ips.apiservices.%s.%s.bamgrid.net";
    private static final String EDGE_THROTTLE_HOSTS = "https://%sprivate-edge.%s.bamgrid.net";
    private static final String TWENTY_FOUR_HOURS = "86400";
    private static final String OK = "OK";
    private static final String QA_ENV = "QA";
    private static final boolean ENABLE_ALLOW_LIST = R.CONFIG.get("enable_allow_list").equals("true");

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public void addIpToAllowList(String ip) {

        if (isIpValidated(ip) && isIpAllowListedAlready(ip) && ENABLE_ALLOW_LIST) {
            for (String region: DisneyParameters.retrieveAwsRegions()) {
                String host = String.format(REDIS_HOST, DisneyParameters.getEnvironmentType(Configuration.get(Configuration.Parameter.ENV)).toLowerCase(), region);
                JedisPool jedisPool = new JedisPool(getJedisPoolConfig(), host);
                try(Jedis jedis = jedisPool.getResource()) {
                    jedis.connect();
                    LOGGER.info("Connected to {}", host);
                    addToProperList(jedis, "allowlist", ip);
                    addToProperList(jedis, "whitelist", ip);
                    addToRateLimitExemption(ip, region);
                } catch (JedisConnectionException ex) {
                    LOGGER.error(String.format("Failed to Connect to Redis Host: %s", host), ex);
                } catch (Exception e1) {
                    LOGGER.error(String.format("Unknown Redis Error: %s", e1.getMessage()), e1);
                } finally {
                    jedisPool.close();
                    R.CONFIG.put("ip_allow_listed_" + ip, "true");
                }
            }
        } else {
            LOGGER.debug("IP not valid to be AllowListed or already has been: {}", ip);
        }
    }

    private void addToProperList(Jedis jedisConnection, String listType, String ip) {
        if (OK.equalsIgnoreCase(jedisConnection.set(String.format("%s#%s", listType, ip), TWENTY_FOUR_HOURS, setJedisParams()))) {
            String success = String.format("Successfully Added IP (%s) to %s", ip, listType.toUpperCase());
            LOGGER.info(success);
        } else {
            String failure = String.format("Failed to Add IP (%s) to %s", ip, listType.toUpperCase());
            LOGGER.info(failure);
        }
    }

    private void addToRateLimitExemption(String ip, String region) {
        try {
            URL url = new URL(getRateLimitHost(region) + "/private/throttle/exempt?ip=" + ip);
            LOGGER.info("Connecting to Rate Exemption Host: {}", url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(0);
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (HttpStatus.SC_OK == responseCode) {
                LOGGER.info("Successfully Added IP ({}) to Exemption", ip);
            } else {
                LOGGER.error("Failed to Add IP ({}) to Exemption", ip);
                LOGGER.error("Error Code: {}", responseCode);
                LOGGER.error("Error Status: {}", connection.getResponseMessage());
            }
        } catch (Exception e) {
            LOGGER.error("Error Encountered: {}", e.getMessage(), e);
        }
    }

    private String getRateLimitHost(String region) {
        String acceptableEnvironment = DisneyParameters.getEnvironmentType(Configuration.get(Configuration.Parameter.ENV));
        if (QA_ENV.equalsIgnoreCase(acceptableEnvironment)) {
            return String.format(EDGE_THROTTLE_HOSTS, acceptableEnvironment.toLowerCase(Locale.ROOT) + "-", region);
        }
        return String.format(EDGE_THROTTLE_HOSTS, "", region);
    }

    private JedisPoolConfig getJedisPoolConfig() {
        final JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(128);
        jedisPoolConfig.setMaxIdle(128);
        jedisPoolConfig.setMinIdle(16);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnReturn(true);
        jedisPoolConfig.setTestWhileIdle(true);
        jedisPoolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
        jedisPoolConfig.setNumTestsPerEvictionRun(3);
        jedisPoolConfig.setBlockWhenExhausted(true);
        return jedisPoolConfig;
    }

    private SetParams setJedisParams() {
        SetParams params = SetParams.setParams();
        params.ex(Integer.parseInt(TWENTY_FOUR_HOURS));
        return params;
    }

    private boolean isIpValidated(String ip) {
        return !ip.startsWith("192");
    }

    private boolean isIpAllowListedAlready(String ip) {
        return !R.CONFIG.containsKey("ip_allow_listed_" + ip);
    }
}
