package com.disney.qa.tests.disney.apple.ios.regression.moremenu;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusMoreMenuIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyplusLegalIOSPageBase;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
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
        setGlobalVariables(locale, language);
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);

        setAppToHomeScreen(disneyAccount.get());
        handleAlert(IOSUtils.AlertButtonCommand.ACCEPT);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        disneyPlusMoreMenuIOSPageBase.getDynamicXpathContainsName(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.LEGAL_TITLE.getText())).click();
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
        setGlobalVariables();
        SoftAssert sa = new SoftAssert();
        String lang = StringUtils.substringAfter(TUID, "TUID: ");

        onboard("US", lang);
        confirmLegalPageOpens();
        DisneyplusLegalIOSPageBase disneyPlusLegalIOSPageBase = initPage(DisneyplusLegalIOSPageBase.class);
        languageUtils.get().getLegalDocuments().forEach((String documentHeader, String apiResponseBody) -> {
            disneyPlusLegalIOSPageBase.getTypeButtonByLabel(documentHeader).click();
            LOGGER.info("Comparing '{}'", documentHeader);

            sa.assertEquals(cleanDocument(disneyPlusLegalIOSPageBase.getLegalText()), cleanDocument(apiResponseBody),
                    String.format("Document: '%s' did not match api response.", documentHeader));

            disneyPlusLegalIOSPageBase.getTypeButtonByLabel(documentHeader).click();
        });

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62261", "XMOBQA-62265"})
    @Test(description = "Verify hyperlink functionality opens into browser", groups = {"More Menu"})
    public void verifyUserTapsOnLink() {
        setGlobalVariables();
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

        if(!hyperlinkFound) {
            skipExecution("No legal options have a hyperlink at present. Skipping test.");
        }

        Assert.assertTrue(disneyPlusLegalIOSPageBase.isWebviewOpen(),
                "Browser did not open");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62261", "XMOBQA-62266"})
    @Test(dataProvider = "impressumCountries", description = "Verify 'Impressum' functionality", groups = {"More Menu"})
    public void verifyImpressumTab(String TUID) {
        setGlobalVariables();
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
}
