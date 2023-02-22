package com.disney.qa.disney.android.pages.common;

import com.disney.qa.api.client.requests.content.SearchPageRequest;

import com.disney.qa.api.client.responses.content.ContentSeries;
import com.disney.qa.api.dictionary.DisneyLocalizationUtils;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.search.DisneySearchApi;
import org.openqa.selenium.WebDriver;

import java.util.List;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusContentMetadataPageBase extends DisneyPlusCommonPageBase {

    public DisneyPlusContentMetadataPageBase(WebDriver driver) {
        super(driver);
    }

    public ContentSeries getSeriesObject(String mediaTitle, DisneyAccount disneyAccount, DisneySearchApi searchApi, DisneyLocalizationUtils localizationUtils) {
        var searchQuery = searchApi.getContentSearchPageResults(searchPageBuilder(disneyAccount, mediaTitle));
        String encodedSeriesId = null;

        List<String> titles = searchQuery.getContentTitlesFull();
        for (int i = 0; i < titles.size(); i++) {
            if (titles.get(i).equals(mediaTitle)) {
                encodedSeriesId = searchQuery.getEncodedSeriesId(i);
                break;
            }
        }
        return searchApi.getSeries(encodedSeriesId, localizationUtils.getLocale(), localizationUtils.getUserLanguage());
    }

    private SearchPageRequest searchPageBuilder(DisneyAccount disneyAccount, String mediaTitle) {
        return SearchPageRequest.builder()
                .region(disneyAccount.getCountryCode())
                .language(disneyAccount.getProfileLang())
                .account(disneyAccount)
                .query(mediaTitle)
                .build();
    }
}
