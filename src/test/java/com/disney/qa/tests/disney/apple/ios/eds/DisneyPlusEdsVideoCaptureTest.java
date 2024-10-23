package com.disney.qa.tests.disney.apple.ios.eds;

import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusSearchIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import org.testng.annotations.Test;

import static com.disney.qa.common.constant.IConstantHelper.US;

public class DisneyPlusEdsVideoCaptureTest extends DisneyBaseTest {

    @Test(description = "Capture data for the following video sequence: Play -> Pause -> Resume -> Back", groups = {TestGroup.PRE_CONFIGURATION, US})
    public void capturePauseResume() {
        DisneyPlusDetailsIOSPageBase detailsIOSPageBase = onboardAndOpenMedia();
        DisneyPlusVideoPlayerIOSPageBase videoPlayerIOSPageBase = detailsIOSPageBase.clickPlayButton().waitForVideoToStart();
        videoPlayerIOSPageBase
                .clickPauseButton()
                .displayVideoController()
                .clickPlayButton()
                .clickBackButton()
                .isOpened();
        harValidation();
    }

    @Test(description = "Capture data for the following video sequence: Play -> Pause -> Resume -> Scrub -> Back", groups = {TestGroup.PRE_CONFIGURATION, US})
    public void capturePauseResumeScrub() {
        DisneyPlusDetailsIOSPageBase detailsIOSPageBase = onboardAndOpenMedia();
        detailsIOSPageBase.clickPlayButton();
        DisneyPlusVideoPlayerIOSPageBase videoPlayerIOSPageBase = detailsIOSPageBase.clickPlayButton().waitForVideoToStart();
        videoPlayerIOSPageBase.clickPauseButton()
                .displayVideoController()
                .clickPlayButton()
                .scrubToPlaybackPercentage(50)
                .clickBackButton()
                .isOpened();
        harValidation();
    }

    @Test(description = "Capture data for the following video sequence: Play -> Pause -> Scrub -> Resume -> Back", groups = {TestGroup.PRE_CONFIGURATION, TestGroup.PROXY, US})
    public void capturePauseScrubResume() {
        DisneyPlusDetailsIOSPageBase detailsIOSPageBase = onboardAndOpenMedia();
        detailsIOSPageBase.clickPlayButton();
        DisneyPlusVideoPlayerIOSPageBase videoPlayerIOSPageBase = detailsIOSPageBase.clickPlayButton().waitForVideoToStart();
        videoPlayerIOSPageBase.clickPauseButton()
                .scrubToPlaybackPercentage(50)
                .clickPlayButton()
                .clickBackButton()
                .isOpened();
        harValidation();
    }

    private DisneyPlusDetailsIOSPageBase onboardAndOpenMedia() {
        restartDriver(true);
        setAppToHomeScreen(getAccount());

        //Open SEARCH page and open the first result for query 'Mickey'
        DisneyPlusSearchIOSPageBase searchIOSPageBase = initPage(DisneyPlusSearchIOSPageBase.class);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.SEARCH);
        searchIOSPageBase.searchForMedia("Wall-E");
        searchIOSPageBase.getDisplayedTitles().get(0).click();
        return initPage(DisneyPlusDetailsIOSPageBase.class);
    }

    private void harValidation() {
        pause(10);
//        HARUtils harUtils = new HARUtils(proxy.get());
//        SoftAssert sa = new SoftAssert();
//        sa.assertFalse(proxy.get().getHar().getLog().getEntries().isEmpty(),
//                "Proxy capture failed. There are no har entries to scan.");
//        Map<HARUtils.RequestDataType, List<String>> harFilter = new HashMap<>();
//        harFilter.put(HARUtils.RequestDataType.URL, List.of("bamgrid.com/dust"));
//        harFilter.put(HARUtils.RequestDataType.POST_DATA,
//                List.of("urn:dss:event:fed:media:playback:requested",
//                        "urn:dss:event:fed:media:payload:fetched",
//                        "urn:dss:event:fed:media:playlist:master:fetched",
//                        "urn:dss:event:fed:media:playback:started",
//                        "urn:dss:event:fed:media:playback:paused",
//                        "urn:dss:event:fed:media:playback:resumed",
//                        "urn:dss:event:fed:media:playback:rebuffering:started",
//                        "urn:dss:event:fed:media:playback:rebuffering:ended",
//                        "urn:dss:event:fed:media:playback:ended"));
//        harUtils.printFilteredHarDetails(harFilter);
//
//        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmm");
//        dateFormat.setTimeZone(TimeZone.getTimeZone("EST"));
//
//        harUtils.publishHAR(String.format("Mobile_Traffic_%s", dateFormat.format(new Date())));
//        sa.assertAll();
    }
}
