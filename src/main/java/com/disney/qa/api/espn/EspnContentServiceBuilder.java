package com.disney.qa.api.espn;

import com.disney.qa.common.http.resttemplate.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

public class EspnContentServiceBuilder {

    protected EspnApiContentProvider espnApiContentProvider = new EspnApiContentProvider();

    protected RestTemplate restTemplate = new RestTemplate();

    protected EspnContentServiceBuilder() {

    }

    public static synchronized EspnContentServiceBuilder newInstance() {
        return new EspnContentServiceBuilder();
    }

    public EspnApiContentProvider withTemplateAndHost() {
        restTemplate = RestTemplateBuilder.newInstance().withDisabledSslChecking().withSpecificJsonMessageConverter().build();

        espnApiContentProvider.setRestTemplate(restTemplate);
        espnApiContentProvider.setEspnApiHost(EspnParameters.getEspnHost());

        return espnApiContentProvider;
    }

    public EspnApiContentProvider entitlementHostAndTemplate() {
        restTemplate = RestTemplateBuilder.newInstance().withDisabledSslChecking().withUtf8EncodingMessageConverter()
                .withSpecificJsonMessageConverter().build();
        espnApiContentProvider.setRestTemplate(restTemplate);
        espnApiContentProvider.setEspnApiHost(EspnParameters.ESPN_API_PROD_ENTITLEMENT.getValue());

        return espnApiContentProvider;
    }

    public EspnApiContentProvider build() {
        return espnApiContentProvider;
    }
}
