package com.disney.qa.tests.disney.apple.ios.regression.Hulk;

import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusHulkDetailsTest extends DisneyBaseTest {
    private static final String BABY_YODA = "f11d21b5-f688-50a9-8b85-590d6ec26d0c";

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74543"})
    @Test(description = "On Junior Profile verify unavailable details page", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifyJuniorProfileDetailsUnavailableState() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        disneyAccount.set(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
        disneyAccountApi.get().addProfile(disneyAccount.get(), JUNIOR_PROFILE, KIDS_DOB, disneyAccount.get().getProfileLang(), BABY_YODA, true, true);

        setAppToHomeScreen(disneyAccount.get(), JUNIOR_PROFILE);
        launchDeeplink(true, R.TESTDATA.get("disney_prod_generic_unavailable_deeplink"), 10);
        homePage.clickOpenButton();

        sa.assertTrue(homePage.getUnavailableContentError().isPresent(), "Unavailable content error not present.");
        sa.assertTrue(homePage.getUnavailableOkButton().isPresent(), "Unavailable content error button not present.");

        homePage.getUnavailableOkButton().click();
        sa.assertTrue(homePage.isOpened(), "Home page not present");
        homePage.clickRandomCollectionTile(DisneyPlusHomeIOSPageBase.Collection.ANIMATED_MOVIES_QA, 3, Direction.UP);
        sa.assertTrue(detailsPage.isOpened(), "Details page did not open.");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74568"})
    @Test(description = "On Adult profile verify unavailable details page", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifyAdultProfileDetailsUnavailableState() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        disneyAccount.set(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));

        setAppToHomeScreen(disneyAccount.get());
        launchDeeplink(true, R.TESTDATA.get("disney_prod_generic_unavailable_deeplink"), 10);
        homePage.clickOpenButton();

        sa.assertTrue(homePage.getUnavailableContentError().isPresent(), "Unavailable content error not present.");
        sa.assertTrue(homePage.getUnavailableOkButton().isPresent(), "Unavailable content error button not present.");

        homePage.getUnavailableOkButton().click();
        sa.assertTrue(homePage.isOpened(), "Home page not present");

        homePage.clickRandomCollectionTile(DisneyPlusHomeIOSPageBase.Collection.NEW_TO_DISNEY_QA, 3, Direction.UP);
        sa.assertTrue(detailsPage.isOpened(), "Details page did not open.");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74568"})
    @Test(description = "On Adult profile verify unavailable details page", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifyHulkDetailsTab() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        disneyAccount.set(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));

        setAppToHomeScreen(disneyAccount.get());
        launchDeeplink(true, R.TESTDATA.get("disney_prod_generic_unavailable_deeplink"), 10);
        homePage.clickOpenButton();

        sa.assertTrue(homePage.getUnavailableContentError().isPresent(), "Unavailable content error not present.");
        sa.assertTrue(homePage.getUnavailableOkButton().isPresent(), "Unavailable content error button not present.");

        homePage.getUnavailableOkButton().click();
        sa.assertTrue(homePage.isOpened(), "Home page not present");

        homePage.clickRandomCollectionTile(DisneyPlusHomeIOSPageBase.Collection.NEW_TO_DISNEY_QA, 3, Direction.UP);
        sa.assertTrue(detailsPage.isOpened(), "Details page did not open.");
        sa.assertAll();
    }
}
