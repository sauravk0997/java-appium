package com.disney.qa.tests.disney.web.appex.playback;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.appex.DisneyAppExUtil;
import com.disney.qa.disney.web.appex.homepage.DisneyPlusHomePageSearchPage;
import com.disney.qa.disney.web.appex.media.DisneyPlusBaseDetailsPage;
import com.disney.qa.disney.web.appex.playback.DisneyPlusPlaybackPage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.entities.Contents;
import com.disney.qa.disney.web.entities.PageUrl;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusPlaybackAdsTest extends DisneyPlusBaseTest {

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<DisneyAccount>();

    @AfterMethod(alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-31184"})
    @Test(description = "Verify ad markers in playback", groups = {TestGroup.DISNEY_APPEX,TestGroup.PLAYER_APPEX,"US"})
    public void verifyADMarkers() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusPlaybackPage playbackPage = new DisneyPlusPlaybackPage(getDriver());
        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        DisneyPlusBaseDetailsPage detailsPage = new DisneyPlusBaseDetailsPage(getDriver());

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.navigateToMain();
        appExUtil.dBaseUniversalLogin(account.getEmail(), account.getUserPass());
        appExUtil.setPlayerQaMode();

        homepageSearchPage
                .clickOnSearchBar()
                .verifyUrlText(softAssert, PageUrl.SEARCH);
        homepageSearchPage
                .enterTextOnSearchBar(Contents.AD_SVOD);
        detailsPage
                .clickDetailsPagePlayBtn()
                .waitForSeconds(15);
        playbackPage
                .hoverHudsonVideoPlayer()
                .clickVideoPlayerPauseBtn();
        softAssert.assertTrue(playbackPage.isAdElementPresent(), "Ad elements are not present");
    }

}
