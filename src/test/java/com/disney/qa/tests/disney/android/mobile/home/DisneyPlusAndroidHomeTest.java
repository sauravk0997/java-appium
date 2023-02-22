package com.disney.qa.tests.disney.android.mobile.home;

import com.disney.qa.disney.android.pages.common.DisneyPlusCommonPageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusDiscoverPageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusMaturityPageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusMediaPageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusMoreMenuPageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusWelcomePageBase;
import com.disney.qa.tests.disney.android.BaseDisneyTest;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.disney.util.disney.ZebrunnerXrayLabels;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusAndroidHomeTest extends BaseDisneyTest {

    private static final String TEST_PROFILE_NAME = "No Continue Watching";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67375"})
    @Test(description = "User taps on a single content to details", groups = {"Home"})
    @Maintainer("jdemelle")
    public void testTapSingleContentToDetailsPage() {
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1638"));

        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);
        DisneyPlusDiscoverPageBase discoverPageBase = initPage(DisneyPlusDiscoverPageBase.class);
        SoftAssert sa = new SoftAssert();

        login(disneyAccount.get(), false);
        commonPageBase.dismissTravelingDialog();

        Assert.assertTrue(discoverPageBase.isOpened(),
                "App did not land on Home page");

        discoverPageBase.selectVisibleMediaPosterByIndex(6);

        Assert.assertTrue(mediaPageBase.isOpened(),
                "App did not land on Details Page");

        commonPageBase.tapBlackBackButton();

        Assert.assertTrue(discoverPageBase.isOpened(),
                "App did not land on Home page after back navigation");

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68155"})
    @Test(description = "Home - Verify Continue Watching not displayed to new user/profile", groups = {"Home"})
    @Maintainer("jdemelle")
    public void testContinueWatchingNotDisplayed() {
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1639"));
        SoftAssert sa = new SoftAssert();

        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusWelcomePageBase.class);
        DisneyPlusDiscoverPageBase discoverPageBase = initPage(DisneyPlusDiscoverPageBase.class);
        DisneyPlusMaturityPageBase maturityPageBase = initPage(DisneyPlusMaturityPageBase.class);
        DisneyPlusMoreMenuPageBase moreMenuPageBase = initPage(DisneyPlusMoreMenuPageBase.class);

        login(disneyAccount.get(), false);
        commonPageBase.dismissTravelingDialog();

        Assert.assertTrue(discoverPageBase.isOpened(), "App did not land on Home page");

        sa.assertFalse(commonPageBase.isContinueWatchingDisplayed(), "Continue Watching should not be displayed");

        commonPageBase.swipeUpOnScreen(2);

        sa.assertFalse(commonPageBase.isContinueWatchingDisplayed(), "Continue Watching should not be displayed");

        addNewProfile(TEST_PROFILE_NAME);

        sa.assertEquals(maturityPageBase.getMaturitySettingProfileName(), TEST_PROFILE_NAME,
                "Profile Name not displayed correctly");

        maturityPageBase.clickMaturityNotNowButton();

        Assert.assertTrue(moreMenuPageBase.isProfileVisible(TEST_PROFILE_NAME),
                "New profile not visible on 'Who's Watching' page");

        moreMenuPageBase.clickOnGenericTextElement(TEST_PROFILE_NAME);
        maturityPageBase.clickMaturityRatingConfirmationButton();

        Assert.assertTrue(discoverPageBase.isOpened(), "App did not land on Home page");

        sa.assertFalse(commonPageBase.isContinueWatchingDisplayed(), "Continue Watching should not be displayed");

        commonPageBase.swipeUpOnScreen(2);

        sa.assertFalse(commonPageBase.isContinueWatchingDisplayed(), "Continue Watching should not be displayed");

        checkAssertions(sa);
    }
}