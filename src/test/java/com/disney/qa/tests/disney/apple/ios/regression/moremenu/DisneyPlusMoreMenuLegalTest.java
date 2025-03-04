package com.disney.qa.tests.disney.apple.ios.regression.moremenu;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.dictionary.DisneyLocalizationUtils;
import com.disney.config.DisneyParameters;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import io.appium.java_client.remote.MobilePlatform;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;

import static com.disney.qa.common.constant.IConstantHelper.*;

public class DisneyPlusMoreMenuLegalTest extends DisneyBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String DISNEY_TERMS_OF_USE = "Disney Terms of Use";
    private static final String SUBSCRIBER_AGREEMENT = "Subscriber Agreement";
    private static final String PRIVACY_POLICY = "Privacy Policy";
    private static final String US_STATE_PRIVACY_RIGHTS_NOTICE = "US State Privacy Rights Notice";
    private static final String DO_NOT_SELL_OR_SHARE_MY_PERSONAL_INFORMATION = "Do Not Sell or Share My Personal Information";
    private static final String LEGAL_PAGE_HEADER_NOT_DISPLAYED = "Legal Page Header not displayed";
    private static final String ONE_TRUST_PAGE_NOT_DISPLAYED = "One Trust Page not displayed";
    private static final String TOGGLE_NOT_TURNED_OFF = "Toggle was not Turned Off";
    private static final String TOGGLE_DID_NOT_TURN_OFF = "Toggle did not turn OFF after selecting";
    private static final String TOGGLE_SHOULD_NOT_SAVE_VALUE = "Toggle should not save value unless confirm button is tapped";

    @DataProvider
    private Object[] fallbackLanguages() {
        return new String[]{"TUID: en", "TUID: es", "TUID: fr"};
    }

    @DataProvider
    private Object[] impressumCountries() {
        return new String[]{"TUID: DE", "TUID: AT", "TUID: CH"};
    }

    /**
     * Navigation is a required test case, so hard asserting it in each test provides coverage
     * and a readable error log in case navigation while opening the page in any test fails.
     */
    private void confirmLegalPageOpens() {
        DisneyplusLegalIOSPageBase disneyPlusLegalIOSPageBase = initPage(DisneyplusLegalIOSPageBase.class);
        Assert.assertTrue(disneyPlusLegalIOSPageBase.isOpened(), "Legal Page did not open on navigation");
        Assert.assertTrue(disneyPlusLegalIOSPageBase.getBackButton().isElementPresent(), "Back button not displayed");
        getLocalizationUtils().getLegalHeaders().forEach(header -> {
            LOGGER.info("Verifying header is present: {}", header);
            Assert.assertTrue(disneyPlusLegalIOSPageBase.isLegalHeadersPresent(header),
                    String.format("Header '%s' was not displayed", header));
        });
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-76671"})
    @Test(dataProvider = "fallbackLanguages", description = "Verify the displays in Legal only show in the profile language if the account's country supports it", groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyLegalUsesFallbackDictionary(String TUID) {
        SoftAssert sa = new SoftAssert();
        String lang = StringUtils.substringAfter(TUID, "TUID: ");
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyOffer offer = getAccountApi().lookupOfferToUse(getCountry(), BUNDLE_PREMIUM);
        setAccount(getAccountApi().createAccount(offer, getLocalizationUtils().getLocale(), lang, SUBSCRIPTION_V2));
        setAppToHomeScreen(getAccount());

        handleAlert(IOSUtils.AlertButtonCommand.ACCEPT);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        disneyPlusMoreMenuIOSPageBase.getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.LEGAL_TITLE.getText())).click();
        DisneyLocalizationUtils disneyLocalizationUtils = new DisneyLocalizationUtils("US", lang, MobilePlatform.IOS,
                DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()),
                DISNEY);
        disneyLocalizationUtils.setDictionaries(getConfigApi().getDictionaryVersions());
        disneyLocalizationUtils.setLegalDocuments();
        confirmLegalPageOpens();

        DisneyplusLegalIOSPageBase disneyPlusLegalIOSPageBase = initPage(DisneyplusLegalIOSPageBase.class);
        DisneyPlusOneTrustIOSPageBase oneTrustPage = initPage(DisneyPlusOneTrustIOSPageBase.class);
        getLocalizationUtils().getLegalHeaders().forEach(documentHeader -> {
            disneyPlusLegalIOSPageBase.getTypeButtonByLabel(documentHeader).click();
            LOGGER.info("Comparing '{}'", documentHeader);
            if (documentHeader.equalsIgnoreCase(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.FOOTER_MANAGE_PREFERENCE.getText()))) {
                sa.assertTrue(oneTrustPage.isOpened(), "opt out of Sale/Sharing page is not present");
                oneTrustPage.tapCloseButton();
            } else {
                sa.assertTrue(disneyPlusLegalIOSPageBase.getTypeButtonByLabel(documentHeader).isPresent(),
                        "Legal Page Header " + documentHeader + " is not present");
                disneyPlusLegalIOSPageBase.getTypeButtonByLabel(documentHeader).click();
            }
        });

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68063"})
    @Test(description = "Verify Legal and return to More Menu", groups = {TestGroup.MORE_MENU,
            TestGroup.PRE_CONFIGURATION, TestGroup.SMOKE, US})
    public void testMoreMenuLegalDisplay() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusMoreMenuIOSPageBase moreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyplusLegalIOSPageBase legalIOSPageBase = initPage(DisneyplusLegalIOSPageBase.class);
        DisneyPlusOneTrustIOSPageBase oneTrustPage = initPage(DisneyPlusOneTrustIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        handleAlert(IOSUtils.AlertButtonCommand.ACCEPT);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenuIOSPageBase.getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.LEGAL_TITLE.getText())).click();
        DisneyLocalizationUtils disneyLocalizationUtils = new DisneyLocalizationUtils("US", "en", MobilePlatform.IOS,
                DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()),
                DISNEY);
        disneyLocalizationUtils.setDictionaries(getConfigApi().getDictionaryVersions());
        disneyLocalizationUtils.setLegalDocuments();
        confirmLegalPageOpens();
        legalIOSPageBase.clickAndCollapseLegalScreenSection(sa, DISNEY_TERMS_OF_USE, getLocalizationUtils());
        legalIOSPageBase.clickAndCollapseLegalScreenSection(sa, SUBSCRIBER_AGREEMENT, getLocalizationUtils());
        legalIOSPageBase.clickAndCollapseLegalScreenSection(sa, PRIVACY_POLICY, getLocalizationUtils());
        legalIOSPageBase.clickAndCollapseLegalScreenSection(sa, US_STATE_PRIVACY_RIGHTS_NOTICE, getLocalizationUtils());
        legalIOSPageBase.getTypeButtonByLabel(DO_NOT_SELL_OR_SHARE_MY_PERSONAL_INFORMATION).click();
        sa.assertTrue(oneTrustPage.isOpened(), ONE_TRUST_PAGE_NOT_DISPLAYED);

        oneTrustPage.tapCloseButton();
        Assert.assertTrue(legalIOSPageBase.isLegalHeaderPresent(), LEGAL_PAGE_HEADER_NOT_DISPLAYED);

        legalIOSPageBase.getBackButton().click();
        sa.assertTrue(moreMenuIOSPageBase.isOpened(), MORE_MENU_NOT_DISPLAYED);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62266"})
    @Test(dataProvider = "impressumCountries", groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyImpressumTab(String TUID) {
        initialSetup();
        handleAlert();
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyplusLegalIOSPageBase legalPage = initPage(DisneyplusLegalIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusOneTrustConsentBannerIOSPageBase oneTrustPage = initPage(DisneyPlusOneTrustConsentBannerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        DisneyOffer offer = getAccountApi().lookupOfferToUse(getCountry(), BUNDLE_PREMIUM);
        String country = StringUtils.substringAfter(TUID, "TUID: ");
        setAccount(getAccountApi().createAccount(offer, country, getLocalizationUtils().getUserLanguage(), SUBSCRIPTION_V2));
        getAccountApi().overrideLocations(getAccount(), country);

        Assert.assertTrue(welcomePage.isOpened(), WELCOME_SCREEN_NOT_DISPLAYED);
        welcomePage.clickLogInButton();
        login(getAccount());
        if (oneTrustPage.isAllowAllButtonPresent()) {
            oneTrustPage.tapAcceptAllButton();
        }
        //Dismiss ATT Popup
        handleGenericPopup(5,1);
        if (homePage.isTravelAlertTitlePresent()) {
            homePage.getTravelAlertOk().click();
        }
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenu.getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.LEGAL_TITLE.getText())).click();
        sa.assertTrue(legalPage.isOpened(),"Legal Page did not open on navigation");

        DisneyLocalizationUtils disneyLocalizationUtils = new DisneyLocalizationUtils(country, "en",
                MobilePlatform.IOS,
                DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()),
                DISNEY);
        disneyLocalizationUtils.setDictionaries(getConfigApi().getDictionaryVersions());
        disneyLocalizationUtils.setLegalDocuments();
        disneyLocalizationUtils.getLegalHeaders().forEach(header -> {
            LOGGER.info("Verifying header is present: {}", header);
            Assert.assertTrue(legalPage.isLegalHeadersPresent(header),
                    String.format("Header '%s' was not displayed", header));
        });

        legalPage.getTypeButtonByLabel("Imprint").click();
        String apiResponse = cleanDocument(disneyLocalizationUtils.getLegalDocumentBody("Imprint"));
        String appDisplay = cleanDocument(legalPage.getLegalText());
        sa.assertEquals(appDisplay, apiResponse,
                String.format("'Impressum' text was not correctly displayed for '%s'", country));

        sa.assertAll();
    }

    private String cleanDocument(String original) {
        return original.replace('\ufeff', ' ')
                .replaceAll("\\n|\\r\\n", System.lineSeparator())
                .replaceAll("[\n\r]$", "")
                .strip();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73773"})
    @Test(description = "More Menu - Legal - OneTrust Page UI", groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyOneTrustPageUI() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyplusLegalIOSPageBase disneyPlusLegalIOSPageBase = initPage(DisneyplusLegalIOSPageBase.class);
        DisneyPlusOneTrustIOSPageBase oneTrustPage = initPage(DisneyPlusOneTrustIOSPageBase.class);
        DisneyplusSellingLegalIOSPageBase sellinglegalTextPage = initPage(DisneyplusSellingLegalIOSPageBase.class);

        setAppToHomeScreen(getAccount());
        handleAlert(IOSUtils.AlertButtonCommand.ACCEPT);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        disneyPlusMoreMenuIOSPageBase.getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.LEGAL_TITLE.getText())).click();

        disneyPlusLegalIOSPageBase.getTypeButtonByLabel(DO_NOT_SELL_OR_SHARE_MY_PERSONAL_INFORMATION).click();
        sa.assertTrue(oneTrustPage.isOpened(), ONE_TRUST_PAGE_NOT_DISPLAYED);

        sa.assertTrue(oneTrustPage.isCloseIconPresent(), "Close button was not found");
        sa.assertTrue(oneTrustPage.isWaltDisneyLogoPresent(), "Walt disney logo was not found");
        sa.assertTrue(oneTrustPage.isNoticeOfRightToOptOutOfSaleTitlePresent(), "'Notice of Right to Opt-Out of Sale/Sharing' title was not found");
        sa.assertTrue(oneTrustPage.isLegalTextPresent(), "Legal text was not found");
        sa.assertTrue(oneTrustPage.isUSStatePrivacyRightsLinkPresent(), "'US State Privacy Rights Link' was not found");
        sa.assertTrue(oneTrustPage.isYourCaliforniaPrivacyRightsLinkPresent(), "'Your California Privacy Rights link' was not found");
        sa.assertTrue(oneTrustPage.isSellingSharingTargatedAdvertisingConsentTitlePresent(), "'Selling, Sharing, Targeted Advertising consent Title' was not found");
        sa.assertTrue(oneTrustPage.getValueOfConsentSwitch().equalsIgnoreCase("1"), "toggle was not Turned ON by default");
        sa.assertTrue(oneTrustPage.isArrowIconToRightOfTooglePresent(), "Arrow to the Right of the Toggle was not found");
        sa.assertTrue(oneTrustPage.isConfirmMyChoceButtonPresent(), "Confirm My Choices button was not found");

        oneTrustPage.clickSellingSharingTargatedAdvertisingArrow();

        sa.assertTrue(sellinglegalTextPage.isOpened(), "Legal page/text for Selling, Sharing, Targeted Advertising not opened");
        sa.assertTrue(sellinglegalTextPage.isBackArrowPresent(), "Back button arrow was not found");
        sa.assertTrue(sellinglegalTextPage.isSellingSharingLegalHeaderPresent(), "'Selling, Sharing, Targeted Advertising' Header was not found");
        sa.assertTrue(sellinglegalTextPage.getValueOfConsentSwitch().equalsIgnoreCase("1"), "Blue toggle was not Turned ON by default");
        sa.assertTrue(sellinglegalTextPage.isLegaltextPresent(), "Legal text was not found for selling, sharing");
        sa.assertTrue(sellinglegalTextPage.isOptOutFormLinkPresent(), "Opt-out-form link in the body text was not found");
        sa.assertTrue(sellinglegalTextPage.isIABOptOutListLinkPresent(), "IAB opt-out list in the body text was not found");
        sa.assertTrue(sellinglegalTextPage.isTargatedAdvertisingOptOutRightsLinkPresent(), "Do Not Sell or Share my Personal Information\" and \"Targeted Advertising\" Opt-out Rights link in the body text was not found");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73779"})
    @Test(groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyOneTrustPageToggleBehaviour() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyplusLegalIOSPageBase disneyPlusLegalIOSPageBase = initPage(DisneyplusLegalIOSPageBase.class);
        DisneyPlusOneTrustIOSPageBase oneTrustPage = initPage(DisneyPlusOneTrustIOSPageBase.class);
        DisneyplusSellingLegalIOSPageBase sellingLegalTextPage = initPage(DisneyplusSellingLegalIOSPageBase.class);

        setAppToHomeScreen(getAccount());
        handleAlert(IOSUtils.AlertButtonCommand.ACCEPT);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        disneyPlusMoreMenuIOSPageBase.getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.LEGAL_TITLE.getText())).click();
        disneyPlusLegalIOSPageBase.getTypeButtonByLabel(DO_NOT_SELL_OR_SHARE_MY_PERSONAL_INFORMATION).click();
        sa.assertTrue(oneTrustPage.isOpened(), ONE_TRUST_PAGE_NOT_DISPLAYED);

        //Toggle switch off but do not tap confirm your choice button
        oneTrustPage.waitForPresenceOfAnElement(oneTrustPage.getOneTrustContentSwitch());
        sa.assertTrue(oneTrustPage.getValueOfConsentSwitch().equalsIgnoreCase("1"),
                "Toggle is not Turned ON by default");
        oneTrustPage.tapConsentSwitch();
        sa.assertTrue(oneTrustPage.getValueOfConsentSwitch().equalsIgnoreCase("0"), TOGGLE_NOT_TURNED_OFF);
        oneTrustPage.tapCloseButton();
        disneyPlusLegalIOSPageBase.getTypeButtonByLabel(DO_NOT_SELL_OR_SHARE_MY_PERSONAL_INFORMATION).click();
        sa.assertTrue(oneTrustPage.getValueOfConsentSwitch().equalsIgnoreCase("1"),
                TOGGLE_SHOULD_NOT_SAVE_VALUE);

        //Toggle switch to OFF on Selling sharing page reflect on "Notice of Right to Opt-Out of Sale/Sharing" Page
        oneTrustPage.clickSellingSharingTargatedAdvertisingArrow();
        sa.assertTrue(sellingLegalTextPage.isOpened(),
                "Legal page/text for Selling, Sharing, Targeted Advertising not opened");
        oneTrustPage.waitForPresenceOfAnElement(sellingLegalTextPage.getSellingLegalContentSwitch());
        sa.assertTrue(sellingLegalTextPage.getValueOfConsentSwitch().equalsIgnoreCase("1"),
                "Toggle should reflect the value of Notice of Right to Opt-Out of Sale/Sharing page");
        sellingLegalTextPage.clickSellingSharingTargetedAdvertisingConsentSwitch();
        sa.assertTrue(sellingLegalTextPage.getValueOfConsentSwitch().equalsIgnoreCase("0"),
                TOGGLE_NOT_TURNED_OFF);
        sellingLegalTextPage.clickBackbutton();
        sa.assertTrue(oneTrustPage.getValueOfConsentSwitch().equalsIgnoreCase("0"),
                "Toggle on Notice of Right to Opt-Out of Sale/Sharing should reflect the value of " +
                        "Selling, Sharing, Targeted Advertising page");
        oneTrustPage.tapCloseButton();
        disneyPlusLegalIOSPageBase.getTypeButtonByLabel(DO_NOT_SELL_OR_SHARE_MY_PERSONAL_INFORMATION).click();
        sa.assertTrue(oneTrustPage.getValueOfConsentSwitch().equalsIgnoreCase("1"),
                TOGGLE_SHOULD_NOT_SAVE_VALUE);

        //Toggle switch to OFF on  "Notice of Right to Opt-Out of Sale/Sharing" Page reflect on Selling Sharing" Page
        oneTrustPage.tapConsentSwitch();
        sa.assertTrue(oneTrustPage.getValueOfConsentSwitch().equalsIgnoreCase("0"),
                TOGGLE_DID_NOT_TURN_OFF);
        oneTrustPage.clickSellingSharingTargatedAdvertisingArrow();
        sa.assertTrue(sellingLegalTextPage.getValueOfConsentSwitch().equalsIgnoreCase("0"),
                "Toggle on Selling, Sharing, Targeted Advertising should reflect the value of " +
                        "Notice of Right to Opt-Out of Sale/Sharing Page");

        // Toggle switch to OFF, and tap 'confirm your choice button
        sellingLegalTextPage.clickBackbutton();
        oneTrustPage.tapConfirmMyChoiceButton();
        sa.assertTrue(disneyPlusLegalIOSPageBase.isOpened(),
                "After selecting the choice switch user should land on legal page");
        //Verify that the choice is saved
        disneyPlusLegalIOSPageBase.getTypeButtonByLabel(DO_NOT_SELL_OR_SHARE_MY_PERSONAL_INFORMATION).click();
        sa.assertTrue(oneTrustPage.getValueOfConsentSwitch().equalsIgnoreCase("0"),
                TOGGLE_DID_NOT_TURN_OFF);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73778"})
    @Test(groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyOneTrustPageLinkBehaviour() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyplusLegalIOSPageBase disneyPlusLegalIOSPageBase = initPage(DisneyplusLegalIOSPageBase.class);
        DisneyPlusOneTrustIOSPageBase oneTrustPage = initPage(DisneyPlusOneTrustIOSPageBase.class);
        DisneyplusSellingLegalIOSPageBase sellinglegalTextPage = initPage(DisneyplusSellingLegalIOSPageBase.class);

        setAppToHomeScreen(getAccount());
        handleAlert(IOSUtils.AlertButtonCommand.ACCEPT);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        disneyPlusMoreMenuIOSPageBase.getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.LEGAL_TITLE.getText())).click();
        disneyPlusLegalIOSPageBase.getTypeButtonByLabel(DO_NOT_SELL_OR_SHARE_MY_PERSONAL_INFORMATION).click();
        sa.assertTrue(oneTrustPage.isOpened(), ONE_TRUST_PAGE_NOT_DISPLAYED);

        //Verify US State Privacy Rights Link
        oneTrustPage.clickYourUSStatePrivacyRightsLink();
        sa.assertTrue(oneTrustPage.isYourUSStatePrivacyRightsPageOpened(15), "US State Privacy Rights Link page not opened");
        oneTrustPage.getTypeButtonByLabel("Done").click();
        sa.assertTrue(oneTrustPage.isOpened(), ONE_TRUST_PAGE_NOT_DISPLAYED);

        //Verify California Privacy Rights Link
        oneTrustPage.clickYourCaliforniaPrivacyRightsLink();
        sa.assertTrue(oneTrustPage.isYourCaliforniaPrivacyRightsPageOpened(15), "California Privacy Rights Link page not opened");
        oneTrustPage.getTypeButtonByLabel("Done").click();
        sa.assertTrue(oneTrustPage.isOpened(), ONE_TRUST_PAGE_NOT_DISPLAYED);

        //Verify Opt out form Link
        oneTrustPage.clickSellingSharingTargatedAdvertisingArrow();
        sellinglegalTextPage.clickOptOutFormLink();
        sa.assertTrue(sellinglegalTextPage.isOptOutFormLinkOpened(25), "Opt Out form Link page not opened");
        oneTrustPage.getTypeButtonByLabel("Done").click();
        sa.assertTrue(sellinglegalTextPage.isOpened(), "Selling, Sharing, Targeted Advertising page was not opened");

        //Verify IAB opt out list Link
        sellinglegalTextPage.clickIABOptOutListLink();
        sa.assertTrue(sellinglegalTextPage.isIABOptOutListLinkPageOpened(), "IAB Opt Out List Link page not opened");
        oneTrustPage.getTypeButtonByLabel("Done").click();
        sa.assertTrue(sellinglegalTextPage.isOpened(), "Selling, Sharing, Targeted Advertising page was not opened");

        //Verify Do Not Sell or Share My Personal Information" and "Targeted Advertising" Opt-Out Rights" link
        sellinglegalTextPage.clickTargetedAdvertisingOptOutRightsLink();
        sa.assertTrue(sellinglegalTextPage.isTargetedAdvertisingOptOutRightsLinkPageOpened(15), "Targeted Advertising Opt Out Rights page not opened");
        oneTrustPage.getTypeButtonByLabel("Done").click();
        sa.assertTrue(sellinglegalTextPage.isOpened(), "Selling, Sharing, Targeted Advertising page was not opened");
        sa.assertAll();
    }
}
