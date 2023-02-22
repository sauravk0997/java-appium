package com.disney.qa.tests.disney.android.mobile.videoplayer;

import com.disney.pojo.ConcurrencyAccount;
import com.disney.pojo.DisneyPlusAsset;
import com.disney.qa.api.disney.DisneyContentApiChecker;
import com.disney.qa.api.disney.DisneyParameters;
import com.disney.qa.disney.android.pages.common.DisneyPlusCommonPageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusMediaPageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusSearchPageBase;
import com.disney.qa.tests.disney.android.BaseDisneyTest;
import com.disney.requests.ConcurrencyRequest;
import com.disney.selenium.ConcurrencyGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DisneyPlusAndroidConcurrencyTest extends BaseDisneyTest {

    private static final String CONCURRENCY_ITEM = "Big Hero 6";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68652"})
    @Test(description = "Concurrency Limit test across platforms", groups = {"Concurrency"})
    public void testCheckConcurrentUsers() {
        PAGEFACTORY_LOGGER.info("Preparing concurrent streams via Selenoid...");

        DisneyPlusAsset asset = new DisneyPlusAsset();
        asset.setId("9171161e-4a99-4b74-9b0d-c3f5ae14c8d5");
        asset.setType("movies");
        asset.setName("assembled-the-making-of-eternals");
        asset.setEncodedId("AXyD2BUrdXo8");

        ConcurrencyGenerator concurrencyGenerator = new ConcurrencyGenerator();
        ConcurrencyRequest concurrencyRequest = new ConcurrencyRequest();
        concurrencyRequest.setNumberOfPlays(4);
        concurrencyRequest.setPlayMultipleTimes(true);
        concurrencyRequest.addDisneyAsset(asset);
        concurrencyRequest.setEnvironment(DisneyParameters.getEnv());
        concurrencyRequest.setPartner(PARTNER);

        ConcurrencyAccount concurrencyAccount = new ConcurrencyAccount();
        concurrencyAccount.setFromDisneyAccount(disneyAccount.get());
        concurrencyRequest.setAccount(concurrencyAccount);

        concurrencyGenerator.playVideos(concurrencyRequest);
        concurrencyGenerator.waitForCompletion(60);
        restartDriver(true);
        verifyConnectionIsGood();
        login(disneyAccount.get(), false);
        dismissChromecastOverlay();

        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        JsonNode appDictionary = getFullDictionary(disneyAccount.get().getProfileLang());
        DisneyContentApiChecker apiChecker = new DisneyContentApiChecker();

        commonPageBase.navigateToPage(apiChecker.getDictionaryItemValue(
                appDictionary, DisneyPlusCommonPageBase.MenuItem.SEARCH.getText()));

        DisneyPlusSearchPageBase searchPageBase = initPage(DisneyPlusSearchPageBase.class);
        searchPageBase.searchForMedia(CONCURRENCY_ITEM);
        searchPageBase.openSearchResult(CONCURRENCY_ITEM);

        initPage(DisneyPlusMediaPageBase.class).startPlayback();

        Assert.assertTrue(commonPageBase.isErrorButtonPresent(),
                "Concurrency error not displayed");
    }
}
