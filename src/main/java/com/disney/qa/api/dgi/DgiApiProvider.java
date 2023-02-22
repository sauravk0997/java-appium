package com.disney.qa.api.dgi;

import com.disney.qa.common.http.resttemplate.RestTemplateBuilder;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.qaprosoft.carina.core.foundation.utils.factory.ICustomTypePageFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public interface DgiApiProvider extends ICustomTypePageFactory {

    <T> ResponseEntity<T> queryEndpoint(HttpMethod httpMethod, T[] body);

    <T> ResponseEntity<T> queryEndpoint(HttpMethod httpMethod, boolean[] flags);

    <T> ResponseEntity<T> queryEndpoint(HttpMethod httpMethod, T[] body, boolean[] flags);

    default RestTemplate buildRestTemplate() {
        return RestTemplateBuilder
                .newInstance()
                .withSpecificJsonMessageConverter()
                .withUtf8EncodingMessageConverter()
                .build();
    }

    default Configuration buildJsonPathJacksonConfiguration() {
        return Configuration.builder().
                mappingProvider(new JacksonMappingProvider()).
                jsonProvider(new JacksonJsonNodeJsonProvider()).
                build();
    }

}
