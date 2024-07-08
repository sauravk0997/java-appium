package com.disney.qa.tests.disney.apple.ios.regression.moremenu;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.dictionary.DisneyLocalizationUtils;
import com.disney.config.DisneyParameters;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusMoreMenuIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusOneTrustIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyplusLegalIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyplusSellingLegalIOSPageBase;
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

public class DisneyPlusMoreMenuLegalTest extends DisneyBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String DISNEY_TERMS_OF_USE = "Disney Terms of Use";
    private static final String SUBSCRIBER_AGREEMENT = "Subscriber Agreement";
    private static final String PRIVACY_POLICY = "Privacy Policy";
    private static final String US_STATE_PRIVACY_RIGHTS_NOTICE = "US State Privacy Rights Notice";
    private static final String DO_NOT_SELL_OR_SHARE_MY_PERSONAL_INFORMATION = "Do Not Sell or Share My Personal Information";

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
        Assert.assertTrue(disneyPlusLegalIOSPageBase.isOpened(),
                "Legal Page did not open on navigation");
        getLocalizationUtils().getLegalHeaders().forEach(header -> {
            LOGGER.info("Verifying header is present: {}", header);
            Assert.assertTrue(disneyPlusLegalIOSPageBase.isLegalHeadersPresent(header),
                    String.format("Header '%s' was not displayed", header));
        });
    }

    private void confirmLegalPageOpensImpressum() {
        String[] legalHeaders = {"Privacy Policy", "Cookies Policy", "UK & EU Privacy Rights", "Imprint", "Subscriber Agreement"};
        DisneyplusLegalIOSPageBase disneyPlusLegalIOSPageBase = initPage(DisneyplusLegalIOSPageBase.class);
        Assert.assertTrue(disneyPlusLegalIOSPageBase.isOpened(),
                "XMOBQA-62261 - Legal Page did not open on navigation");
        
        for (String header : legalHeaders) {
            LOGGER.info("Verifying header is present: {}", header);
            Assert.assertTrue(disneyPlusLegalIOSPageBase.isLegalHeadersPresent(header),
                    String.format("Header '%s' was not displayed", header));
        }
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68063"})
    @Test(dataProvider = "fallbackLanguages", description = "Verify the displays in Legal only show in the profile language if the account's country supports it", groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION})
    public void verifyLegalUsesFallbackDictionary(String TUID) {
        SoftAssert sa = new SoftAssert();
        String lang = StringUtils.substringAfter(TUID, "TUID: ");
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyOffer offer = getAccountApi().lookupOfferToUse(getCountry(), BUNDLE_PREMIUM);
        setAccount(getAccountApi().createAccount(offer, "US", lang, SUBSCRIPTION_V1));
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
        getLocalizationUtils().getLegalDocuments().forEach((String documentHeader, String apiResponseBody) -> {
            disneyPlusLegalIOSPageBase.getTypeButtonByLabel(documentHeader).click();
            LOGGER.info("Comparing '{}'", documentHeader);
            if (documentHeader.equalsIgnoreCase(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.FOOTER_MANAGE_PREFERENCE.getText()))) {
                sa.assertTrue(oneTrustPage.isOpened(), "opt out of Sale/Sharing page is not present");
                oneTrustPage.tapCloseButton();

            } else {
                sa.assertEquals(cleanDocument(disneyPlusLegalIOSPageBase.getLegalText()), cleanDocument(apiResponseBody),
                        String.format("Document: '%s' did not match api response.", documentHeader));
                disneyPlusLegalIOSPageBase.getTypeButtonByLabel(documentHeader).click();
            }
        });

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68063"})
    @Test(description = "Verify Legal and return to More Menu", groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION})
    public void testCheckLegalDisplay() {
        DisneyPlusMoreMenuIOSPageBase moreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyplusLegalIOSPageBase legalIOSPageBase = initPage(DisneyplusLegalIOSPageBase.class);
        DisneyPlusOneTrustIOSPageBase oneTrustPage = initPage(DisneyPlusOneTrustIOSPageBase.class);
        DisneyOffer offer = getAccountApi().lookupOfferToUse(getCountry(), BUNDLE_PREMIUM);
        setAccount(getAccountApi().createAccount(offer, "US", "en", SUBSCRIPTION_V1));
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());
        
        handleAlert(IOSUtils.AlertButtonCommand.ACCEPT);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        Assert.assertTrue(moreMenuIOSPageBase.isOpened(), "More Menu is not opened");

        moreMenuIOSPageBase.getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.LEGAL_TITLE.getText())).click();
        Assert.assertTrue(legalIOSPageBase.isLegalHeaderPresent(), "Legal Center Header is not displayed");

        sa.assertTrue(moreMenuIOSPageBase.isBackButtonPresent(), "Back button not displayed");
        legalIOSPageBase.clickLegalScreenSection(sa, DISNEY_TERMS_OF_USE);
        legalIOSPageBase.clickLegalScreenSection(sa, SUBSCRIBER_AGREEMENT);
        legalIOSPageBase.clickLegalScreenSection(sa, PRIVACY_POLICY);
        legalIOSPageBase.clickLegalScreenSection(sa, US_STATE_PRIVACY_RIGHTS_NOTICE);
        legalIOSPageBase.getStaticTextByLabel(DO_NOT_SELL_OR_SHARE_MY_PERSONAL_INFORMATION).click();
        sa.assertTrue(oneTrustPage.isOpened(), "One trust page was not opened");

        oneTrustPage.tapCloseButton();
        Assert.assertTrue(legalIOSPageBase.isLegalHeaderPresent(), "Legal Center Header is not displayed");

        legalIOSPageBase.clickBack();
        sa.assertTrue(moreMenuIOSPageBase.isOpened(), "More menu screen not displayed");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62266"})
    @Test(dataProvider = "impressumCountries", description = "Verify 'Impressum' functionality", groups = {"More Menu", TestGroup.PRE_CONFIGURATION})
    public void verifyImpressumTab(String TUID) {
        SoftAssert sa = new SoftAssert();
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyOffer offer = getAccountApi().lookupOfferToUse(getCountry(), BUNDLE_PREMIUM);
        String country = StringUtils.substringAfter(TUID, "TUID: ");
        setAccount(getAccountApi().createAccount(offer, country, "en", SUBSCRIPTION_V1));
        setAppToHomeScreen(getAccount());

        handleAlert(IOSUtils.AlertButtonCommand.ACCEPT);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        disneyPlusMoreMenuIOSPageBase.getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.LEGAL_TITLE.getText())).click();
        DisneyLocalizationUtils disneyLocalizationUtils = new DisneyLocalizationUtils(country, "en", MobilePlatform.IOS,
                DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()),
                DISNEY);
        disneyLocalizationUtils.setDictionaries(getConfigApi().getDictionaryVersions());
        disneyLocalizationUtils.setLegalDocuments();
        confirmLegalPageOpensImpressum();
        DisneyplusLegalIOSPageBase disneyPlusLegalIOSPageBase = initPage(DisneyplusLegalIOSPageBase.class);
        disneyPlusLegalIOSPageBase.getTypeButtonByLabel("Imprint").click();
        String apiResponse = cleanDocument(disneyLocalizationUtils.getLegalDocumentBody("Imprint"));
        String appDisplay = cleanDocument(disneyPlusLegalIOSPageBase.getLegalText());
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66561"})
    @Test(description = "One trust - 'opt-out module'", groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION})
    public void verifyOneTrustModal() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyplusLegalIOSPageBase disneyPlusLegalIOSPageBase = initPage(DisneyplusLegalIOSPageBase.class);
        DisneyPlusOneTrustIOSPageBase oneTrustPage = initPage(DisneyPlusOneTrustIOSPageBase.class);

        setAppToHomeScreen(getAccount());
        handleAlert(IOSUtils.AlertButtonCommand.ACCEPT);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        disneyPlusMoreMenuIOSPageBase.getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.LEGAL_TITLE.getText())).click();

        confirmLegalPageOpens();
        String doNotSellString = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.FOOTER_MANAGE_PREFERENCE.getText());
        disneyPlusLegalIOSPageBase.getStaticTextLabelName(doNotSellString).click();
        sa.assertTrue(oneTrustPage.isOpened(), "");
        //Toggle switch but do not tap confirm your choice button
        sa.assertTrue(oneTrustPage.getValueOfConsentSwitch().equalsIgnoreCase("1"), "toggle is not Turned ON by default");
        oneTrustPage.tapConsentSwitch();
        oneTrustPage.tapCloseButton();
        disneyPlusLegalIOSPageBase.getStaticTextLabelName(doNotSellString).click();
        sa.assertTrue(oneTrustPage.getValueOfConsentSwitch().equalsIgnoreCase("1"), "toggle should not save value unless confirm button is tapped");
        //Toggle switch to OFF, tap 'confirm your choice' button
        oneTrustPage.tapConsentSwitch();
        sa.assertTrue(oneTrustPage.getValueOfConsentSwitch().equalsIgnoreCase("0"), "toggle didn't not turn OFF after selecting");
        oneTrustPage.tapConfirmMyChoiceButton();
        sa.assertTrue(disneyPlusLegalIOSPageBase.isOpened(), "after selecting the choice switch user should land on legal page");
        //Verify that the choice is saved
        disneyPlusLegalIOSPageBase.getStaticTextLabelName(doNotSellString).click();
        sa.assertTrue(oneTrustPage.getValueOfConsentSwitch().equalsIgnoreCase("0"), "toggle didn't not turn OFF after selecting");
        // Toggle switch to ON, and tap 'confirm your choice button
        oneTrustPage.tapConsentSwitch();
        sa.assertTrue(oneTrustPage.getValueOfConsentSwitch().equalsIgnoreCase("1"), "toggle didn't ON turn OFF after selecting");
        oneTrustPage.tapConfirmMyChoiceButton();
        sa.assertTrue(disneyPlusLegalIOSPageBase.isOpened(), "after selecting the choice switch user should land on legal page");
        //Verify that the choice is saved
        disneyPlusLegalIOSPageBase.getStaticTextLabelName(doNotSellString).click();
        sa.assertTrue(oneTrustPage.getValueOfConsentSwitch().equalsIgnoreCase("1"),"toggle didn't not turn ON after selecting");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73773"})
    @Test(description = "More Menu - Legal - OneTrust Page UI", groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION}, enabled = false)
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
        sa.assertTrue(oneTrustPage.isOpened(), "One trust page was not opened");

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
    @Test(description = "More Menu - Legal - OneTrust Page - Verify Toggle Behavior", groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION})
    public void verifyOneTrustPageToggleBehaviour() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyplusLegalIOSPageBase disneyPlusLegalIOSPageBase = initPage(DisneyplusLegalIOSPageBase.class);
        DisneyPlusOneTrustIOSPageBase oneTrustPage = initPage(DisneyPlusOneTrustIOSPageBase.class);
        DisneyplusSellingLegalIOSPageBase sellinglegalTextPage = initPage(DisneyplusSellingLegalIOSPageBase.class);

        setAppToHomeScreen(getAccount());
        handleAlert(IOSUtils.AlertButtonCommand.ACCEPT);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        disneyPlusMoreMenuIOSPageBase.getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.LEGAL_TITLE.getText())).click();
        disneyPlusLegalIOSPageBase.getStaticTextByLabel(DO_NOT_SELL_OR_SHARE_MY_PERSONAL_INFORMATION).click();
        sa.assertTrue(oneTrustPage.isOpened(), "One trust page was not opened");

        //Toggle switch off but do not tap confirm your choice button
        sa.assertTrue(oneTrustPage.getValueOfConsentSwitch().equalsIgnoreCase("1"), "toggle is not Turned ON by default");
        oneTrustPage.tapConsentSwitch();
        sa.assertTrue(oneTrustPage.getValueOfConsentSwitch().equalsIgnoreCase("0"), "toggle was not Turned Off");
        oneTrustPage.tapCloseButton();
        disneyPlusLegalIOSPageBase.getStaticTextByLabel(DO_NOT_SELL_OR_SHARE_MY_PERSONAL_INFORMATION).click();
        sa.assertTrue(oneTrustPage.getValueOfConsentSwitch().equalsIgnoreCase("1"), "toggle should not save value unless confirm button is tapped");

        //Toggle switch to OFF on Selling sharing page reflect on "Notice of Right to Opt-Out of Sale/Sharing" Page
        oneTrustPage.clickSellingSharingTargatedAdvertisingArrow();
        sa.assertTrue(sellinglegalTextPage.isOpened(), "Legal page/text for Selling, Sharing, Targeted Advertising not opened");
        sa.assertTrue(sellinglegalTextPage.getValueOfConsentSwitch().equalsIgnoreCase("1"), "toggle should reflect the value of Notice of Right to Opt-Out of Sale/Sharing page");
        sellinglegalTextPage.clickSellingSharingTargatedAdvertisingConsentSwitch();
        sa.assertTrue(sellinglegalTextPage.getValueOfConsentSwitch().equalsIgnoreCase("0"), "toggle was not Turned Off");
        sellinglegalTextPage.clickBackbutton();
        sa.assertTrue(oneTrustPage.getValueOfConsentSwitch().equalsIgnoreCase("0"), "toggle on Notice of Right to Opt-Out of Sale/Sharing should reflect the value of Selling, Sharing, Targeted Advertising page");
        oneTrustPage.tapCloseButton();
        disneyPlusLegalIOSPageBase.getStaticTextByLabel(DO_NOT_SELL_OR_SHARE_MY_PERSONAL_INFORMATION).click();
        sa.assertTrue(oneTrustPage.getValueOfConsentSwitch().equalsIgnoreCase("1"), "toggle should not save value unless confirm button is tapped");

        //Toggle switch to OFF on  "Notice of Right to Opt-Out of Sale/Sharing" Page reflect on Selling Sharing" Page
        oneTrustPage.tapConsentSwitch();
        sa.assertTrue(oneTrustPage.getValueOfConsentSwitch().equalsIgnoreCase("0"), "toggle didn't not turn OFF after selecting");
        oneTrustPage.clickSellingSharingTargatedAdvertisingArrow();
        sa.assertTrue(sellinglegalTextPage.getValueOfConsentSwitch().equalsIgnoreCase("0"), "toggle on Selling, Sharing, Targeted Advertising should reflect the value of Notice of Right to Opt-Out of Sale/Sharing Page");

        // Toggle switch to OFF, and tap 'confirm your choice button
        sellinglegalTextPage.clickBackbutton();
        oneTrustPage.tapConfirmMyChoiceButton();
        sa.assertTrue(disneyPlusLegalIOSPageBase.isOpened(), "after selecting the choice switch user should land on legal page");
        //Verify that the choice is saved
        disneyPlusLegalIOSPageBase.getStaticTextByLabel(DO_NOT_SELL_OR_SHARE_MY_PERSONAL_INFORMATION).click();
        sa.assertTrue(oneTrustPage.getValueOfConsentSwitch().equalsIgnoreCase("0"), "toggle didn't not turn OFF after selecting");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73778"})
    @Test(description = "More Menu - Legal - OneTrust Page - Verify Links", groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION})
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
        disneyPlusLegalIOSPageBase.getStaticTextByLabel(DO_NOT_SELL_OR_SHARE_MY_PERSONAL_INFORMATION).click();
        sa.assertTrue(oneTrustPage.isOpened(), "One trust page was not opened");

        //Verify US State Privacy Rights Link
        oneTrustPage.clickYourUSStatePrivacyRightsLink();
        sa.assertTrue(oneTrustPage.isYourUSStatePrivacyRightsPageOpened(15), "US State Privacy Rights Link page not opened");
        oneTrustPage.getTypeButtonByLabel("Done").click();
        sa.assertTrue(oneTrustPage.isOpened(), "One trust page was not opened");

        //Verify California Privacy Rights Link
        oneTrustPage.clickYourCaliforniaPrivacyRightsLink();
        sa.assertTrue(oneTrustPage.isYourCaliforniaPrivacyRightsPageOpened(15), "California Privacy Rights Link page not opened");
        oneTrustPage.getTypeButtonByLabel("Done").click();
        sa.assertTrue(oneTrustPage.isOpened(), "One trust page was not opened");

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
        sellinglegalTextPage.clickTargatedAdvertisingOptOutRightsLink();
        sa.assertTrue(sellinglegalTextPage.isTargatedAdvertisingOptOutRightsLinkPageOpened(15), "Targated Advertising Opt Out Rights page not opened");
        oneTrustPage.getTypeButtonByLabel("Done").click();
        sa.assertTrue(sellinglegalTextPage.isOpened(), "Selling, Sharing, Targeted Advertising page was not opened");
        sa.assertAll();
    }

}
