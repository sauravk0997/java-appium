package com.disney.qa.tests.disney.apple.ios.regression.moremenu;

import com.disney.qa.api.explore.response.*;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.*;
import com.disney.util.*;
import com.zebrunner.agent.core.annotation.*;
import org.testng.*;
import org.testng.annotations.*;

import java.util.*;
import static com.disney.qa.common.constant.IConstantHelper.US;
import static com.disney.qa.common.constant.RatingConstant.*;

public class DisneyPlusMoreMenuLiveToggleTest extends DisneyBaseTest {

    @DataProvider(name = "emeaCountries")
    public Object[][] emeaCountries() {
        return new Object[][]{
                {"US", "US"},
                /*{FRANCE, "fr"},
                {SPAIN, "ES"},
                {SWEDEN, "SE"}*/
        };
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-78533"})
    @Test(dataProvider = "emeaCountries", groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyLiveAndUnratedToggleUI(String country, String countryCode) {
        String trioBasicPlan = "Disney Bundle Trio Premium - 26.99 USD - Monthly";
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(trioBasicPlan, countryCode, "en")));
        loginToHome(getUnifiedAccount());

        //Get the live data
        ArrayList<Container> container = getDisneyAPIPageUnifiedAccount();
        String espnContentTitle = container.get(4).getItems().get(0).getVisuals().getTitle();
        LOGGER.info("Espn content title " + espnContentTitle);

        Assert.assertTrue(swipe(homePage.getStaticTextByLabel(espnContentTitle), Direction.UP, 2, 800),
                "live toggle section is not present for the account");

        homePage.clickMoreTab();
        moreMenu.clickEditProfilesBtn();
        editProfile.clickEditModeProfile(getAccount().getFirstName());
        Assert.assertTrue(swipe(editProfile.getProfileSettingLiveUnratedHeader(), Direction.UP, 2, 500),
                "Unable to swipe to live toggle section");

        //Toggle is ON by default
        Assert.assertEquals(editProfile.getLiveAndUnratedToggleState(), "On");

        // Turn Toggle OFF
        editProfile.tapLiveAndUnratedToggle();
        editProfile.waitForUpdatedToastToDisappear();
        editProfile.getDoneButton().click();

        //Live and unrated content will be turned off
        Assert.assertTrue(homePage.getHomeNav().isPresent(), "Home page did not open");
        Assert.assertFalse(swipe(homePage.getStaticTextByLabel(espnContentTitle), Direction.UP, 5, 800),
                "live toggle section was present even after turning off the live toggle");
    }

}
