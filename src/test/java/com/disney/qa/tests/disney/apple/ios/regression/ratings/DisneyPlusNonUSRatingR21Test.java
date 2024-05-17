package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusPasswordIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusSearchIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusVerifyAgeDOBCollectionIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusVerifyAgeIOSPageBase;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.INVALID_CREDENTIALS_ERROR;

public class DisneyPlusNonUSRatingR21Test extends DisneyPlusRatingsBase {

    private static final String PASSWORD_PAGE_ERROR_MESSAGE = "Password page should open";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74415"})
    @Test(description = "R21: Create PIN - Enter Password - Invalid Input", groups = {"NonUS-Ratings", "R21"})
    public void verifyR21CreatePINInvalidPasswordError() {
        ratingsSetup(R21, SINGAPORE_LANG, SINGAPORE_LOCALE);
        DisneyPlusVerifyAgeIOSPageBase verifyAgePage = initPage(DisneyPlusVerifyAgeIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        String incorrectPasswordError = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SDK_ERRORS, INVALID_CREDENTIALS_ERROR.getText());
        launchR21Content();
        verifyAgePage.clickIAm21PlusButton();
        sa.assertTrue(passwordPage.isOpened(), PASSWORD_PAGE_ERROR_MESSAGE);
        passwordPage.enterPasswordNoAccount(INVALID_PASSWORD);
        sa.assertEquals(passwordPage.getErrorMessageString().replaceAll("\"", ""), incorrectPasswordError, "'We couldn't log you in' error message did not display for wrong password entered.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69767"})
    @Test(description = "R21 - Create Pin - Enter Password - Valid Input directs user to Enter Date Of Birth Screen", groups = {"NonUS-Ratings", "R21"})
    public void verifyR21CreatePINDOBScreen() {
        ratingsSetup(R21, SINGAPORE_LANG, SINGAPORE_LOCALE);
        DisneyPlusVerifyAgeIOSPageBase verifyAgePage = initPage(DisneyPlusVerifyAgeIOSPageBase.class);
        DisneyPlusVerifyAgeDOBCollectionIOSPageBase verifyAgeDOBPage = initPage(DisneyPlusVerifyAgeDOBCollectionIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        launchR21Content();
        verifyAgePage.clickIAm21PlusButton();
        sa.assertTrue(passwordPage.isOpened(), PASSWORD_PAGE_ERROR_MESSAGE);
        passwordPage.enterPassword(getAccount());
        sa.assertTrue(verifyAgeDOBPage.isOpened(), "Enter your birthdate page not opened");
        sa.assertAll();
    }

    public void launchR21Content() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVerifyAgeIOSPageBase verifyAgePage = initPage(DisneyPlusVerifyAgeIOSPageBase.class);
        String contentTitle = CONTENT_TITLE.get(0);
        homePage.clickSearchIcon();
        searchPage.searchForMedia(contentTitle);
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.clickPlayButton();
        Assert.assertTrue(verifyAgePage.isOpened(), "Verify your age page should open");
    }

}