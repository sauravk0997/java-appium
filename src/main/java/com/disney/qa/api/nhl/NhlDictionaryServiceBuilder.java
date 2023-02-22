package com.disney.qa.api.nhl;

import com.disney.qa.api.nhl.statsapi.NhlStatsApiDictionaryProvider;
import org.springframework.web.client.RestTemplate;

public class NhlDictionaryServiceBuilder {

    protected NhlDictionaryService nhlDictionaryService = new NhlDictionaryService();

    protected NhlStatsApiDictionaryProvider nhlStatsApiDictionaryProvider = new NhlStatsApiDictionaryProvider();


    protected NhlDictionaryServiceBuilder() {
    }

    public static synchronized NhlDictionaryServiceBuilder newInstance() {
        return new NhlDictionaryServiceBuilder();
    }

    public NhlDictionaryServiceBuilder withAllParametersForStatsApi(RestTemplate restTemplate) {
        nhlStatsApiDictionaryProvider.setRestTemplate(restTemplate);
        nhlStatsApiDictionaryProvider.setNhlStatsApiHost(NhlParameters.getNhlDictionaryHost());
        nhlDictionaryService.setNhlDictionaryProvider(nhlStatsApiDictionaryProvider);
        return this;
    }

    public NhlDictionaryService build() {
        return nhlDictionaryService;
    }
}
