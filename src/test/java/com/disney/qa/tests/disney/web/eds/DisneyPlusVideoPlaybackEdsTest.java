package com.disney.qa.tests.disney.web.eds;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.common.web.LocalStorageUtils;
import com.disney.qa.disney.web.appex.homepage.DisneyPlusHomePageSearchPage;
import com.disney.qa.disney.web.appex.media.DisneyPlusBaseDetailsPage;
import com.disney.qa.disney.web.appex.playback.DisneyPlusPlaybackPage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.HARUtils;
import com.qaprosoft.carina.browsermobproxy.ProxyPool;
import net.lightbody.bmp.proxy.CaptureType;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusVideoPlaybackEdsTest extends DisneyPlusBaseTest {

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<>();

    @AfterMethod(alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
    }

    @Test(description = "Play - Pause - Backout", groups = {"US", "MX"})
    public void playbackBackout() {
        SoftAssert sa = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyPlusPlaybackPage playbackPage = new DisneyPlusPlaybackPage(getDriver());
        playbackPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));

        proxy.set(ProxyPool.getProxy());
        proxy.get().enableHarCaptureTypes(CaptureType.RESPONSE_CONTENT,CaptureType.RESPONSE_CONTENT);
        HARUtils harUtils = new HARUtils(proxy.get());

        playbackPage.dBaseUniversalLogin(account.getEmail() , account.getUserPass());

        LocalStorageUtils localStorage = new LocalStorageUtils(getDriver());
        localStorage.setItemInLocalStorage("enableQaMode", "true");

        sa.assertTrue(localStorage.isItemPresentInLocalStorage("enableQaMode"), "Expected 'enableQaMode' to be set");

        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        homepageSearchPage.clickOnSearchBar();
        homepageSearchPage.verifyUrlText(sa,"search");
        homepageSearchPage.enterMovieOnSearchBar();
        DisneyPlusBaseDetailsPage detailsPage = new DisneyPlusBaseDetailsPage(getDriver());
        proxy.get().newHar();
        detailsPage.clickDetailsPagePlayBtn();
        pause(10); //ensure the video will continue to play for the next step

        playbackPage.clickVideoPlayerPauseBtn();
        pause(10); //ensure the video will continue to play for the next step

        playbackPage.clickVideoPlayerPlayBtn();
        pause(10); //ensure the video will continue to play for the next step

        playbackPage.clickVideoPlayerBackArrow();
        harUtils.publishHarConfiguredName("Backout");
        proxy.get().endHar();

    }

    @Test(description = "Pause - Resume - Scrubber", groups = {"US", "MX"})
    public void playbackScubber() {
        SoftAssert sa = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyPlusPlaybackPage playbackPage = new DisneyPlusPlaybackPage(getDriver());
        playbackPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));

        proxy.set(ProxyPool.getProxy());
        proxy.get().enableHarCaptureTypes(CaptureType.RESPONSE_CONTENT,CaptureType.RESPONSE_CONTENT);
        HARUtils harUtils = new HARUtils(proxy.get());

        playbackPage.dBaseUniversalLogin(account.getEmail() , account.getUserPass());

        LocalStorageUtils localStorage = new LocalStorageUtils(getDriver());
        localStorage.setItemInLocalStorage("enableQaMode", "true");

        sa.assertTrue(localStorage.isItemPresentInLocalStorage("enableQaMode"), "Expected 'enableQaMode' to be set");

        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        homepageSearchPage.clickOnSearchBar();
        homepageSearchPage.verifyUrlText(sa,"search");
        homepageSearchPage.enterMovieOnSearchBar();
        DisneyPlusBaseDetailsPage detailsPage = new DisneyPlusBaseDetailsPage(getDriver());
        proxy.get().newHar();
        detailsPage.clickDetailsPagePlayBtn();
        pause(10); //ensure the video will continue to play for the next step

        playbackPage.clickVideoPlayerPauseBtn();
        pause(10); //ensure the video will continue to play for the next step

        playbackPage.clickVideoPlayerPlayBtn();
        pause(10); //ensure the video will continue to play for the next step

        playbackPage.mediaPlayerSeek(500);
        pause(10); //ensure the video will continue to play for the next step

        harUtils.publishHarConfiguredName("Scrubber");
        proxy.get().endHar();
    }

    @Test(description = "Javascript Trigger - 3x Play", groups = {"US", "MX"})
    public void playbackPlay() {
        SoftAssert sa = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyPlusPlaybackPage playbackPage = new DisneyPlusPlaybackPage(getDriver());
        playbackPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));

        proxy.set(ProxyPool.getProxy());
        proxy.get().enableHarCaptureTypes(CaptureType.RESPONSE_CONTENT,CaptureType.RESPONSE_CONTENT);
        HARUtils harUtils = new HARUtils(proxy.get());

        playbackPage.dBaseUniversalLogin(account.getEmail() , account.getUserPass());

        LocalStorageUtils localStorage = new LocalStorageUtils(getDriver());
        localStorage.setItemInLocalStorage("enableQaMode", "true");

        sa.assertTrue(localStorage.isItemPresentInLocalStorage("enableQaMode"), "Expected 'enableQaMode' to be set");

        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        homepageSearchPage.clickOnSearchBar();
        homepageSearchPage.verifyUrlText(sa,"search");
        homepageSearchPage.enterMovieOnSearchBar();

        DisneyPlusBaseDetailsPage detailsPage = new DisneyPlusBaseDetailsPage(getDriver());
        proxy.get().newHar();

        detailsPage.clickDetailsPagePlayBtn();
        pause(10); //ensure the video will continue to play for the next step

        playbackPage.pauseMediaPlayer();
        pause(10);

        playbackPage.playMediaPlayer();
        pause(10);

        playbackPage.playMediaPlayer();
        pause(10);

        playbackPage.playMediaPlayer();
        pause(10);

        harUtils.publishHarConfiguredName("Play_3x");
        proxy.get().endHar();
    }

    @Test(description = "Javascript Trigger - 3x Pause", groups = {"US", "MX"})
    public void playbackPause() {
        SoftAssert sa = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyPlusPlaybackPage playbackPage = new DisneyPlusPlaybackPage(getDriver());
        playbackPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));

        proxy.set(ProxyPool.getProxy());
        proxy.get().enableHarCaptureTypes(CaptureType.RESPONSE_CONTENT,CaptureType.RESPONSE_CONTENT);
        HARUtils harUtils = new HARUtils(proxy.get());

        playbackPage.dBaseUniversalLogin(account.getEmail() , account.getUserPass());

        LocalStorageUtils localStorage = new LocalStorageUtils(getDriver());
        localStorage.setItemInLocalStorage("enableQaMode", "true");

        sa.assertTrue(localStorage.isItemPresentInLocalStorage("enableQaMode"), "Expected 'enableQaMode' to be set");

        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        homepageSearchPage.clickOnSearchBar();
        homepageSearchPage.verifyUrlText(sa,"search");
        homepageSearchPage.enterMovieOnSearchBar();

        DisneyPlusBaseDetailsPage detailsPage = new DisneyPlusBaseDetailsPage(getDriver());
        proxy.get().newHar();

        detailsPage.clickDetailsPagePlayBtn();
        pause(10); //ensure the video will continue to play for the next step

        playbackPage.pauseMediaPlayer();
        pause(10);

        playbackPage.pauseMediaPlayer();
        pause(10);

        playbackPage.pauseMediaPlayer();
        pause(10);

        harUtils.publishHarConfiguredName("Pause_3x");
        proxy.get().endHar();
    }

}
