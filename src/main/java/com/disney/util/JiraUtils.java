package com.disney.util;

import com.disney.config.DisneyParameters;
import com.zebrunner.carina.crypto.CryptoTool;
import com.zebrunner.carina.crypto.CryptoToolBuilder;
import com.zebrunner.carina.utils.R;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.testng.ITestContext;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.Objects;

import static com.zebrunner.carina.crypto.Algorithm.AES_ECB_PKCS5_PADDING;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class JiraUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final CryptoTool cryptoTool = CryptoToolBuilder.builder().chooseAlgorithm(AES_ECB_PKCS5_PADDING)
            .setKey(com.zebrunner.carina.utils.R.CONFIG.get("crypto_key_value")).build();
    private static final String JIRA_TICKET_KEY = R.CONFIG.get("reporting.tcm.xray.test-execution-key");
    private static final String API_COMMENT_URL = DisneyParameters
            .getJiraBaseUrl() + "/rest/api/2/issue/" + JIRA_TICKET_KEY + "/comment";
    private static final String REPORT_URL = String.valueOf(R.CONFIG.get("report_url"));
    private static final String JIRA_USER = "jira-zafira";
    private static final String NULL_STRING = "NULL";

    private JiraUtils() { }

    private static boolean isXrayKeySet() {
        return !Objects.equals(R.CONFIG.get("reporting.tcm.xray.test-execution-key"), NULL_STRING);
    }

    private static String getJiraAuth() {
        String jiraAuthToken = cryptoTool.decrypt(R.TESTDATA.get("jira_auth_token"));
        String auth = JIRA_USER + ":" + jiraAuthToken;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
        return "Basic " + new String(encodedAuth);
    }

    private static String testRunReportMessaging(ITestContext context) {
        return context.getCurrentXmlTest().getSuite().getName() + " RC results: " + REPORT_URL;
    }

    public static void addTestRunURLtoJiraTicketComment(ITestContext context) throws URISyntaxException, IOException {
        if (!isXrayKeySet()) {
            LOGGER.info("No Xray key set and bypassing adding test run url to Jira ticket's comment");
            return;
        }

        HttpClient httpClient = HttpClientBuilder.create().build();
        String requestBody = "{\"body\": \"" + testRunReportMessaging(context) + "\"}";

        URI uri = new URI(API_COMMENT_URL);
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setHeader(AUTHORIZATION, getJiraAuth());
        httpPost.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());

        LOGGER.info("Adding test run link to ticket {}", JIRA_TICKET_KEY);
        StringEntity entity = new StringEntity(requestBody);
        httpPost.setEntity(entity);

        HttpResponse httpResponse = httpClient.execute(httpPost);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        String responseBody = httpResponse.getEntity() != null ? EntityUtils.toString(httpResponse.getEntity()) : null;

        if (statusCode < 200 || statusCode >= 300) {
            LOGGER.info("Error attempting to add test run url to {} as comment: {}", JIRA_TICKET_KEY, responseBody);
        } else {
            LOGGER.info("Successfully added test run url as comment to {}", JIRA_TICKET_KEY);
        }
    }
}
