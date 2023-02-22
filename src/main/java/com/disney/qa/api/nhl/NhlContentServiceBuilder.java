package com.disney.qa.api.nhl;

import com.disney.qa.api.nhl.feed.NhlFeedContentProvider;
import com.disney.qa.api.nhl.statsapi.MlbNhlStatsApiContentProvider;
import com.disney.qa.api.nhl.statsapi.NhlStatsApiContentProvider;
import org.springframework.web.client.RestTemplate;

public class NhlContentServiceBuilder {

    protected NhlContentService nhlContentService = new NhlContentService();

    protected NhlStatsApiContentProvider nhlStatsApiContentProvider = new NhlStatsApiContentProvider();

    protected MlbNhlStatsApiContentProvider mlbNhlStatsApiContentProvider = new MlbNhlStatsApiContentProvider();

    protected NhlFeedContentProvider nhlFeedContentProvider = new NhlFeedContentProvider();

    protected NhlContentServiceBuilder() {
    }

    public static synchronized NhlContentServiceBuilder newInstance() {
        return new NhlContentServiceBuilder();
    }

    public NhlContentServiceBuilder withAllParametersForStatsApi(RestTemplate restTemplate) {
        nhlStatsApiContentProvider.setRestTemplate(restTemplate);
        nhlStatsApiContentProvider.setNhlStatsApiHost(NhlParameters.getNhlStatsApiHost());

        nhlContentService.setNhlContentProvider(nhlStatsApiContentProvider);

        return this;
    }


    public NhlContentServiceBuilder withAllParametersForFeed(RestTemplate restTemplate) {
        nhlFeedContentProvider.setRestTemplate(restTemplate);
        nhlFeedContentProvider.setNhlFeedHost(NhlParameters.NHL_FEED_HOST.getValue());
        nhlFeedContentProvider.setSecretApiKey(NhlParameters.NHL_FEED_AUTH_SECRET_API_KEY.getValue());
        nhlFeedContentProvider.setPartnerName(NhlParameters.NHL_FEED_PARTNER_NAME.getValue());

        nhlContentService.setNhlContentProvider(nhlFeedContentProvider);

        return this;
    }

    public NhlContentServiceBuilder withAllParametersForFeedHostF(RestTemplate restTemplate) {
        nhlFeedContentProvider.setRestTemplate(restTemplate);
        nhlFeedContentProvider.setNhlFeedHost(NhlParameters.NHL_FEED_HOST_F.getValue());
        nhlFeedContentProvider.setSecretApiKey(NhlParameters.NHL_FEED_AUTH_SECRET_API_KEY.getValue());
        nhlFeedContentProvider.setPartnerName(NhlParameters.NHL_FEED_PARTNER_NAME.getValue());

        nhlContentService.setNhlContentProvider(nhlFeedContentProvider);

        return this;
    }

    public NhlContentServiceBuilder withAllParametersForStatsApiHostMlb(RestTemplate restTemplate) {
        mlbNhlStatsApiContentProvider.setRestTemplate(restTemplate);
        mlbNhlStatsApiContentProvider.setNhlStatsApiHost(NhlParameters.MLB_NHL_STATS_API_HOST.getValue());

        nhlContentService.setNhlContentProvider(mlbNhlStatsApiContentProvider);

        return this;
    }

    public NhlContentServiceBuilder withSpecificNhlStatsApiHost(String host) {
        nhlStatsApiContentProvider.setNhlStatsApiHost(host);

        return this;
    }

    public NhlContentService build() {
        return nhlContentService;
    }

}
