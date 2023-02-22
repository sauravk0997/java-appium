package com.disney.qa.api.atbat;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mk on 10/16/15.
 */
public class AtBatApiContentProvider implements AtBatContentProvider {

    public static final String ATBAT_CONTENT_NEWS_READER_PATH_PATTERN = "gen/content/tag/v1/%s/news/android_newsreader.json";

    protected String atBatContentServerHost;

    protected RestTemplate restTemplate;

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public <T> T getNewsReader(String team, Class<T> returnType) {
        RequestEntity<?> readRequestEntity = new RequestEntity(HttpMethod.GET, getNewsReaderUrl(team));

        ResponseEntity<T> responseEntity = restTemplate.exchange(readRequestEntity, returnType);

        return responseEntity.getBody();
    }

    @Override
    public URI getNewsReaderUrl(String team) {
        return UriComponentsBuilder.
                fromHttpUrl(atBatContentServerHost).path(String.format(ATBAT_CONTENT_NEWS_READER_PATH_PATTERN, team)).
                build().toUri();
    }

    public RequestEntity<String> buildRequestEntity(String httpType, String hostUrl, String path, HttpMethod httpMethod) {

        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme(httpType)
                .host(hostUrl)
                .path(path)
                .build();

        return new RequestEntity(setHeaders(), httpMethod, uriComponents.toUri());
    }

    public RequestEntity<String> buildRequestEntity(String httpType, String hostUrl, String path, MultiValueMap<String, String> listQueryParams, HttpMethod httpMethod) {

        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme(httpType)
                .host(hostUrl)
                .path(path)
                .queryParams(listQueryParams)
                .build();

        return new RequestEntity(setHeaders(), httpMethod, uriComponents.toUri());
    }

    private HttpHeaders setHeaders() {

        List<MediaType> acceptList = new ArrayList<MediaType>();
        acceptList.add(MediaType.APPLICATION_JSON);

        HttpHeaders httpHeader = new HttpHeaders();
        httpHeader.setAccept(acceptList);
        httpHeader.setContentType(MediaType.APPLICATION_JSON);

        return httpHeader;
    }
}
