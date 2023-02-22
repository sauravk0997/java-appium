
package com.disney.qa.tests.disney.android.tv;

import com.disney.qa.api.client.responses.profile.DisneyProfile;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.common.utils.androidtv.AndroidTVUtils;
import com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVDiscoverPage;
import com.disney.qa.disney.android.pages.tv.utility.navhelper.NavHelper;
import com.disney.util.disney.ZebrunnerXrayLabels;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import io.appium.java_client.android.nativekey.AndroidKey;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

import static com.disney.qa.tests.disney.DisneyPlusBaseTest.STA;


public class StarPlusAndroidTVSearchTest extends DisneyPlusAndroidTVBaseTest {

    @Test
    public void searchRestrictedNoResults() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(STA, country, "XCDQA-102532"));
        SoftAssert sa = new SoftAssert();
        loginAndOpenSearchPage(sa);

        // NSFW gibberish gets no results
        disneyPlusAndroidTVSearchPage.get().typeInSearchBox("pr0n");

        UniversalUtils.captureAndUpload(getCastedDriver());
        sa.assertTrue(disneyPlusAndroidTVSearchPage.get().isContentRestrictedTitleVisible(), "Expected restricted content message to be visible...");
        sa.assertFalse(disneyPlusAndroidTVSearchPage.get().isSearchResultVisible(), "Expected no search results...");

        sa.assertAll();
    }

    @Test
    public void searchRestrictedResults() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(STA, country, "XCDQA-102533"));
        SoftAssert sa = new SoftAssert();

        // Rated TV-MOM SAID NO
        String searchTitle = "Birdman";

        // Set Rating Restrictions
        List<DisneyProfile> profileList = disneyAccountApi.get().getDisneyProfiles(entitledUser.get());
        String system = profileList.get(0).getAttributes().getParentalControls().getMaturityRating().getRatingSystem();
        List<String> ratings = profileList.get(0).getAttributes().getParentalControls().getMaturityRating().getRatingSystemValues();
        disneyAccountApi.get().editContentRatingProfileSetting(entitledUser.get(), system, ratings.get(0));

        loginAndOpenSearchPage(sa);
        disneyPlusAndroidTVSearchPage.get().typeInSearchBox(searchTitle);

        UniversalUtils.captureAndUpload(getCastedDriver());
        sa.assertTrue(disneyPlusAndroidTVSearchPage.get().isContentRestrictedTextVisible(), "Expected restricted content message to be visible...");

        sa.assertAll();
    }

    @Test
    public void noSearchCollectionResults() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(STA, country, "XCDQA-102535"));
        SoftAssert sa = new SoftAssert();
        AndroidTVUtils utils = new AndroidTVUtils(getDriver());

        String searchTitle = "Made in Latin America";

        loginAndOpenSearchPage(sa);
        disneyPlusAndroidTVSearchPage.get().typeInSearchBox(searchTitle);

        UniversalUtils.captureAndUpload(getCastedDriver());
        List<ExtendedWebElement> displayedTiles = disneyPlusAndroidTVSearchPage.get().getShelfSetElements();

        for (ExtendedWebElement displayedTile : displayedTiles) {
            String displayedTitle = utils.getContentDescription(displayedTile);

            // Verify the collection is not displayed
            sa.assertFalse(displayedTitle.contains(searchTitle),
                    "Search displays collection:");
        }

        sa.assertAll();
    }

    private void loginAndOpenSearchPage(SoftAssert sa) {
        disneyAccountApi.get().addProfile(entitledUser.get(), "test", language, null, false);
        loginWithoutHomeCheck(entitledUser.get());
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), "Profile page did not launch");
        disneyPlusAndroidTVProfilePageBase.get().selectDefaultProfileAfterFocused();
        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().isOpened(), "Homepage did not launch");

        // Goto Search page
        NavHelper navHelper = new NavHelper(this.getCastedDriver());
        navHelper.keyUntilElementFocused(() -> disneyPlusAndroidTVDiscoverPage.get().getNavHome(), AndroidKey.BACK);
        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().getNavHome().isVisible(),"Nav bar should be visible.");
        navHelper.keyUntilElementDescContains(() -> disneyPlusAndroidTVDiscoverPage.get().getFocusedNavItem(),
                AndroidKey.DPAD_UP, String.valueOf(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.SEARCH));
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();

        sa.assertTrue(disneyPlusAndroidTVSearchPage.get().isOpened(), "Search page did not open");
    }
}
