package com.disney.qa.tests.nhl.api;

import com.disney.qa.api.atbat.AtBatParameters;
import com.disney.qa.common.http.resttemplate.RestTemplateBuilder;
import com.disney.qa.tests.BaseAPITest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.testng.annotations.BeforeClass;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
import java.net.URLDecoder;

/**
 * Created by boyle on 4/19/16.
 */
public class AtBatApiBaseTest extends BaseAPITest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    protected RestTemplate restTemplate = new RestTemplate();

    private static final Integer CONNECTION_TIMEOUT = 60000;

    public static final String FILE_CONTENT_DESC_STRING = "Filename (%s) ContentId (%s) Description (%s)";

    public void disableRestTemplateSsl() {
        restTemplate = RestTemplateBuilder.newInstance()
                .withDisabledSslChecking()
                .withSpecificJsonMessageConverter()
                .withUtf8EncodingMessageConverter()
                .build();
        restTemplate.setRequestFactory(setRequestFactory());
    }

    private HttpComponentsClientHttpRequestFactory setRequestFactory() {

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(CONNECTION_TIMEOUT);
        requestFactory.setReadTimeout(CONNECTION_TIMEOUT);
        requestFactory.setBufferRequestBody(true);

        return requestFactory;
    }

    @BeforeClass
    public void beforeTests() {
        disableRestTemplateSsl();
    }

    protected String safeStatus(RequestEntity<String> checkUrl, Class<?> responseType) {

        try {
            HttpStatus statusCode = restTemplate.exchange(checkUrl, responseType).getStatusCode();

            return statusCode.toString();
        } catch (HttpClientErrorException ex) {
            LOGGER.debug("Status Code Error", ex);
            return ex.getStatusCode().toString();
        } catch (Exception ex1) {
            LOGGER.debug("Status Code Error #2", ex1);
            return ex1.getMessage();
        }
    }

    public void retrieveMediaUrl(SoftAssert softAssert, String fileName, String fieldName, String url) {

        if (url != null && !url.contains("ticketmaster") && !url.contains("www.reddit.com")) {
            if (url.contains("http")) {
                executeUrl(softAssert, fileName, fieldName, url);
            } else if (url.length() > 0 && url.contains(".") && !url.contains("/")) {
                executeUrl(softAssert, fileName, fieldName, "http://" + url);
            } else if (url.length() > 0 && url.startsWith("/")) {
                executeUrl(softAssert, fileName, fieldName, "http://" + AtBatParameters.MLB_ATBAT_NEW_CONTENT_SERVER_HOST.getValue() + url);
            }
        }
    }

    public void executeUrl(SoftAssert softAssert, String fileName, String fieldName, String url) {

        String urlValue = decodeUrl(url.trim());

        LOGGER.debug(String.format("  - Checking %s URL: %s", fieldName, urlValue));

        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(urlValue.replace("#", "?")).build();

        String httpStatus = safeStatus(new RequestEntity(HttpMethod.GET, uriComponents.toUri()), String.class);

        LOGGER.debug(String.format("  - HTTP Status (%s) has been Returned.", httpStatus));
        softAssert.assertTrue("200".equalsIgnoreCase(httpStatus) || "301".equalsIgnoreCase(httpStatus) || httpStatus.contains("I/O error"),
                String.format("%n%s Field (%s) HTTP Status (%s) for URL (%s)",
                        fileName, fieldName, httpStatus, urlValue));
    }

    private String decodeUrl(String url) {
        String urlValue = url;

        try {
            urlValue = URLDecoder.decode(urlValue, "UTF-8");
        } catch (Exception ex) {
            LOGGER.error("Decode Error", ex);
        }

        return urlValue;
    }
}
