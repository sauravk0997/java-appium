package com.disney.qa.tests.disney.apple.ios.regression.moremenu;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusMoreMenuIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusOneTrustIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyplusLegalIOSPageBase;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusMoreMenuLegalTest extends DisneyBaseTest {

    @DataProvider
    private Object[] fallbackLanguages() {
        return new String[]{"TUID: en", "TUID: es", "TUID: fr"};
    }

    @DataProvider
    private Object[] impressumCountries() {
        return new String[]{"TUID: DE", "TUID: AT", "TUID: CH"};
    }

    public void onboard(String locale, String language) {
        LOGGER.info("Language in test: " + language);
        LOGGER.info("Country in test: " + locale);
        initialSetup(locale, language);
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);

        setAppToHomeScreen(disneyAccount.get());
        handleAlert(IOSUtils.AlertButtonCommand.ACCEPT);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        disneyPlusMoreMenuIOSPageBase.getStaticTextByLabel(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.LEGAL_TITLE.getText())).click();
    }

    /**
     * Navigation is a required test case, so hard asserting it in each test provides coverage
     * and a readable error log in case navigation while opening the page in any test fails.
     */
    private void confirmLegalPageOpens() {
        DisneyplusLegalIOSPageBase disneyPlusLegalIOSPageBase = initPage(DisneyplusLegalIOSPageBase.class);
        Assert.assertTrue(disneyPlusLegalIOSPageBase.isOpened(),
                "XMOBQA-62261 - Legal Page did not open on navigation");

        languageUtils.get().getLegalHeaders().forEach(header -> {
            LOGGER.info("Verifying header is present: {}", header);
            Assert.assertTrue(disneyPlusLegalIOSPageBase.isLegalHeadersPresent(header),
                    String.format("Header '%s' was not displayed", header));
        });
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62261", "XMOBQA-62263"})
    @Test(dataProvider = "fallbackLanguages", description = "Verify the displays in Legal only show in the profile language if the account's country supports it", groups = {"More Menu"})
    public void verifyLegalUsesFallbackDictionary(String TUID) {
        initialSetup();
        SoftAssert sa = new SoftAssert();
        String lang = StringUtils.substringAfter(TUID, "TUID: ");

        onboard("US", lang);
        confirmLegalPageOpens();
        DisneyplusLegalIOSPageBase disneyPlusLegalIOSPageBase = initPage(DisneyplusLegalIOSPageBase.class);
        DisneyPlusOneTrustIOSPageBase oneTrustPage = initPage(DisneyPlusOneTrustIOSPageBase.class);
        languageUtils.get().getLegalDocuments().forEach((String documentHeader, String apiResponseBody) -> {
            disneyPlusLegalIOSPageBase.getTypeButtonByLabel(documentHeader).click();
            LOGGER.info("Comparing '{}'", documentHeader);
            if (documentHeader.equalsIgnoreCase(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.FOOTER_MANAGE_PREFERENCE.getText()))) {
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62261", "XMOBQA-62265"})
    @Test(description = "Verify hyperlink functionality opens into browser", groups = {"More Menu"})
    public void verifyUserTapsOnLink() {
        initialSetup();
        DisneyplusLegalIOSPageBase disneyPlusLegalIOSPageBase = new DisneyplusLegalIOSPageBase(getDriver());

        onboard("US", "en");
        confirmLegalPageOpens();
        boolean hyperlinkFound = false;

        for (String header : languageUtils.get().getLegalHeaders()) {
            LOGGER.info("Looking for hyperlink in '{}'", header);
            disneyPlusLegalIOSPageBase.getTypeButtonByLabel(header).click();
            pause(5);
            hyperlinkFound = disneyPlusLegalIOSPageBase.isHyperlinkPresent();
            if (hyperlinkFound) {
                LOGGER.info("Hyperlink found!");
                disneyPlusLegalIOSPageBase.clickHyperlink();
                break;
            }
        }

        if (!hyperlinkFound) {
            skipExecution("No legal options have a hyperlink at present. Skipping test.");
        }

        Assert.assertTrue(disneyPlusLegalIOSPageBase.isWebviewOpen(),
                "Browser did not open");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62261", "XMOBQA-62266"})
    @Test(dataProvider = "impressumCountries", description = "Verify 'Impressum' functionality", groups = {"More Menu"})
    public void verifyImpressumTab(String TUID) {
        initialSetup();
        SoftAssert sa = new SoftAssert();
        String country = StringUtils.substringAfter(TUID, "TUID: ");
        onboard(country, "en");
        confirmLegalPageOpens();
        DisneyplusLegalIOSPageBase disneyPlusLegalIOSPageBase = initPage(DisneyplusLegalIOSPageBase.class);
        disneyPlusLegalIOSPageBase.getTypeButtonByLabel("Imprint").click();
        String apiResponse = cleanDocument(languageUtils.get().getLegalDocumentBody("Imprint"));
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73783"})
    @Maintainer("gkrishna")
    @Test(description = "One trust - 'opt-out module'", groups = {"More Menu"})
    public void verifyOneTrustModal() {
        initialSetup();
        SoftAssert sa = new SoftAssert();
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyplusLegalIOSPageBase disneyPlusLegalIOSPageBase = initPage(DisneyplusLegalIOSPageBase.class);
        DisneyPlusOneTrustIOSPageBase oneTrustPage = initPage(DisneyPlusOneTrustIOSPageBase.class);

        setAppToHomeScreen(disneyAccount.get());
        handleAlert(IOSUtils.AlertButtonCommand.ACCEPT);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        disneyPlusMoreMenuIOSPageBase.getStaticTextByLabel(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.LEGAL_TITLE.getText())).click();

        confirmLegalPageOpens();
        String doNotSellString = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.FOOTER_MANAGE_PREFERENCE.getText());
        disneyPlusLegalIOSPageBase.getTypeButtonByLabel(doNotSellString).click();
        sa.assertTrue(oneTrustPage.isOpened(), "");
        //Toggle switch but do not tap confirm your choice button
        sa.assertTrue(oneTrustPage.getValueOfConsentSwitch().equalsIgnoreCase("1"), "toggle is not Turned ON by default");
        oneTrustPage.tapConsentSwitch();
        oneTrustPage.tapCloseButton();
        disneyPlusLegalIOSPageBase.getTypeButtonByLabel(doNotSellString).click();
        sa.assertTrue(oneTrustPage.getValueOfConsentSwitch().equalsIgnoreCase("1"), "toggle should not save value unless confirm button is tapped");
        //Toggle switch to OFF, tap 'confirm your choice' button
        oneTrustPage.tapConsentSwitch();
        sa.assertTrue(oneTrustPage.getValueOfConsentSwitch().equalsIgnoreCase("0"), "toggle didn't not turn OFF after selecting");
        oneTrustPage.tapConfirmMyChoiceButton();
        sa.assertTrue(disneyPlusLegalIOSPageBase.isOpened(), "after selecting the choice switch user should land on legal page");
        //Verify that the choice is saved
        disneyPlusLegalIOSPageBase.getTypeButtonByLabel(doNotSellString).click();
        sa.assertTrue(oneTrustPage.getValueOfConsentSwitch().equalsIgnoreCase("0"), "toggle didn't not turn OFF after selecting");
        // Toggle switch to ON, and tap 'confirm your choice button
        oneTrustPage.tapConsentSwitch();
        sa.assertTrue(oneTrustPage.getValueOfConsentSwitch().equalsIgnoreCase("0"), "toggle didn't not turn OFF after selecting");
        oneTrustPage.tapConfirmMyChoiceButton();
        sa.assertTrue(disneyPlusLegalIOSPageBase.isOpened(), "after selecting the choice switch user should land on legal page");
        //Verify that the choice is saved
        disneyPlusLegalIOSPageBase.getTypeButtonByLabel(doNotSellString).click();
        sa.assertTrue(oneTrustPage.getValueOfConsentSwitch().equalsIgnoreCase("1"),"toggle didn't not turn ON after selecting");
        sa.assertAll();
    }
}
