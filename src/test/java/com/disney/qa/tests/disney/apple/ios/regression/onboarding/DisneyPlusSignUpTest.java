package com.disney.qa.tests.disney.apple.ios.regression.onboarding;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.AGREE_AND_CONTINUE_BTN;

public class DisneyPlusSignUpTest extends DisneyBaseTest {

    public static final String NO_ERROR_DISPLAYED = "error message was not displayed";
    static final String EXPANDED = "Expanded";
    static final String COLLAPSED = "Collapsed";
    static final String YOUR_CALIFORNIA_PRIVACY_RIGHTS = "Your US State Privacy Rights";
    static final String SUBSCRIBER_AGREEMENT = "Subscriber Agreement";
    static final String PRIVACY_POLICY = "Privacy Policy";
    static final String DO_NOT_SELL_MY_PERSONAL_INFORMATION = "Do Not Sell or Share My Personal Information";


    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62219", "XMOBQA-62221"})
    @Test(description = "Verify 'Sign Up' page elements are all present", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void verifySignUpPageUI() {
        DisneyPlusSignUpIOSPageBase disneyPlusSignUpIOSPageBase = initPage(DisneyPlusSignUpIOSPageBase.class);
        DisneyPlusApplePageBase disneyPlusApplePageBase = initPage(DisneyPlusApplePageBase.class);
        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickSignUpButton();
        Assert.assertTrue(disneyPlusSignUpIOSPageBase.isOpened(),
                "'Sign Up' did not open the email submission screen as expected");
        SoftAssert sa = new SoftAssert();
        sa.assertTrue(disneyPlusSignUpIOSPageBase.getBackArrow().isElementPresent(),
                "Back Button (arrow) was not displayed as expected");

        sa.assertTrue(disneyPlusSignUpIOSPageBase.isEmailFieldDisplayed(),
                "Email field was not displayed as expected");

        sa.assertTrue(disneyPlusSignUpIOSPageBase.isConsentFormPresent(),
                "Opt-In Consent Form (Checkbox) was not displayed as expected");

        sa.assertTrue(disneyPlusSignUpIOSPageBase.isTermsOfUserDisclaimerDisplayed(),
                "Acknowledgement text was not displayed as expected");

        sa.assertTrue(disneyPlusSignUpIOSPageBase.arePrivacyPolicyLinksDisplayed(),
                "'Privacy Policy' hyperlinks were not displayed as expected");

        sa.assertTrue(disneyPlusSignUpIOSPageBase.isSubscriberAgreementLinkDisplayed(),
                "'Subscriber Agreement' hyperlink was not displayed as expected");

        sa.assertEquals(disneyPlusApplePageBase.getPrimaryButtonText().toLowerCase(),
                languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, AGREE_AND_CONTINUE_BTN.getText()).toLowerCase());

        sa.assertAll();
    }



    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62225", "XMOBQA-62227", "XMOBQA-62229"})
    @Test(description = "Verify 'Sign Up' page elements are all present", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void verifyInvalidEmailSubmissions() {
        DisneyPlusSignUpIOSPageBase disneyPlusSignUpIOSPageBase = initPage(DisneyPlusSignUpIOSPageBase.class);
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusCreatePasswordIOSPageBase disneyPlusCreatePasswordIOSPageBase = initPage(DisneyPlusCreatePasswordIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        welcomePage.clickSignUpButton();

        disneyPlusSignUpIOSPageBase.clickAgreeAndContinue();
        String invalidEmailError = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SDK_ERRORS, DictionaryKeys.ATTRIBUTE_VALIDATION.getText());

        sa.assertEquals(disneyPlusSignUpIOSPageBase.getErrorMessageLabelText(), invalidEmailError,
                "XMOBQA-62229 - Submitting no email did not produce an invalid email error");

        if (!disneyPlusSignUpIOSPageBase.isOpened()) {
            disneyPlusCreatePasswordIOSPageBase.getBackArrow().click();
        }
        disneyPlusSignUpIOSPageBase.submitEmailAddress("EmailWithoutSymbol.com");

        sa.assertEquals(disneyPlusSignUpIOSPageBase.getErrorMessageLabelText(), invalidEmailError,
                "XMOBQA-62225 - Missing '@' did not produce an invalid email error");

        if (!disneyPlusSignUpIOSPageBase.isOpened()) {
            disneyPlusCreatePasswordIOSPageBase.getBackArrow().click();
        }
        disneyPlusSignUpIOSPageBase.submitEmailAddress("EmailWithoutDomain");

        sa.assertEquals(disneyPlusSignUpIOSPageBase.getErrorMessageLabelText(), invalidEmailError,
                "XMOBQA-62227 - Missing email domain (.com) did not produce an invalid email error");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62231"})
    @Test(description = "Verify signup with new account", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void verifyNewEmailSubmission() {
        DisneyPlusSignUpIOSPageBase disneyPlusSignUpIOSPageBase = initPage(DisneyPlusSignUpIOSPageBase.class);
        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickSignUpButton();
        Assert.assertTrue(disneyPlusSignUpIOSPageBase.isOpened(),
                "'Sign Up' did not open the email submission screen as expected");

        disneyPlusSignUpIOSPageBase.submitEmailAddress(generateGmailAccount());
        disneyPlusSignUpIOSPageBase.clickPrimaryButtonByCoordinates();
        Assert.assertTrue(initPage(DisneyPlusCreatePasswordIOSPageBase.class).isOpened(),
                "User was not directed to Create Password as expected");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62223"})
    @Test(description = "Verify signup with pre-existing account", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void verifyExistingEmailSubmission() {
        DisneyPlusSignUpIOSPageBase disneyPlusSignUpIOSPageBase = initPage(DisneyPlusSignUpIOSPageBase.class);
        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickSignUpButton();
        Assert.assertTrue(disneyPlusSignUpIOSPageBase.isOpened(),
                "'Sign Up' did not open the email submission screen as expected");

        disneyPlusSignUpIOSPageBase.submitEmailAddress(disneyAccount.get().getEmail());

        Assert.assertTrue(initPage(DisneyPlusPasswordIOSPageBase.class).isOpened(),
                "User was not directed to Password Entry as expected");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62223"})
    @Test(description = "Verify 'Create Password' page elements are all present", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void verifySubmitPasswordPageUI() {
        DisneyPlusSignUpIOSPageBase disneyPlusSignUpIOSPageBase = initPage(DisneyPlusSignUpIOSPageBase.class);
        DisneyPlusCreatePasswordIOSPageBase disneyPlusCreatePasswordIOSPageBase = initPage(DisneyPlusCreatePasswordIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickSignUpButton();
        Assert.assertTrue(disneyPlusSignUpIOSPageBase.isOpened(),
                "'Sign Up' did not open the email submission screen as expected");

        String email = generateGmailAccount();
        disneyPlusSignUpIOSPageBase.submitEmailAddress(email);

        Assert.assertTrue(disneyPlusCreatePasswordIOSPageBase.isOpened(),
                "User was not directed to Password Creation as expected");

        sa.assertTrue(disneyPlusCreatePasswordIOSPageBase.getBackArrow().isElementPresent(),
                "Back Arrow was not displayed as expected");

        sa.assertTrue(disneyPlusCreatePasswordIOSPageBase.isPasswordEntryFieldPresent(),
                "Password entry text field was not displayed as expected");

        sa.assertTrue(disneyPlusCreatePasswordIOSPageBase.isHidePasswordIconPresent(),
                "Show/Hide Password icon was not displayed as expected");

        sa.assertTrue(disneyPlusCreatePasswordIOSPageBase.isPasswordStrengthHeaderPresent(),
                "Password Strength header text was not displayed as expected");

        sa.assertTrue(disneyPlusCreatePasswordIOSPageBase.isEmailInUseDisplayed(email),
                "'You'll be using...' text and/or email submitted was not displayed as expected");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62235"})
    @Test(description = "Verify invalid password submissions", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void verifyInvalidPasswordSubmissions() {
        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickSignUpButton();
        DisneyPlusSignUpIOSPageBase disneyPlusSignUpIOSPageBase = initPage(DisneyPlusSignUpIOSPageBase.class);
        DisneyPlusCreatePasswordIOSPageBase disneyPlusCreatePasswordIOSPageBase = initPage(DisneyPlusCreatePasswordIOSPageBase.class);

        disneyPlusSignUpIOSPageBase.submitEmailAddress(generateGmailAccount());
        disneyPlusCreatePasswordIOSPageBase.submitPasswordValue("123456");

        Assert.assertTrue(disneyPlusCreatePasswordIOSPageBase.isInvalidPasswordErrorDisplayed(),
                "Invalid Password Error was not displayed as expected");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62247"})
    @Test(description = "Verify onboarding stepper for EU based users", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION }, enabled = false)
    public void verifyOnboardingStepperEU() {
        initiateProxy("Germany");
        DisneyPlusSignUpIOSPageBase disneyPlusSignUpIOSPageBase = initPage(DisneyPlusSignUpIOSPageBase.class);
        DisneyPlusCreatePasswordIOSPageBase disneyPlusCreatePasswordIOSPageBase = initPage(DisneyPlusCreatePasswordIOSPageBase.class);

        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickSignUpButton();

        Assert.assertTrue(disneyPlusSignUpIOSPageBase.isOpened(),
                "'Sign Up' did not open the email submission screen");

        disneyPlusSignUpIOSPageBase.submitEmailAddress(generateGmailAccount());

        Assert.assertTrue(disneyPlusSignUpIOSPageBase.getStaticTextByLabel("Subscriber Agreement").isElementPresent(),
                "Subscriber Agreement sub-page was not displayed");

        disneyPlusSignUpIOSPageBase.getTypeButtonByLabel("AGREE & CONTINUE").click();

        Assert.assertTrue(disneyPlusCreatePasswordIOSPageBase.isOpened(),
                "User was not directed to 'Create Password'");

        disneyPlusCreatePasswordIOSPageBase.submitPasswordValue("abcd123!@");

        Assert.assertTrue(initPage(DisneyPlusPaywallIOSPageBase.class).isOpened(),
                "User was not directed to the paywall");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62375"})
    @Test(description = "Verify valid Subscriber Agreement link, expand/collapse/scroll content", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void verifyUSLegalHyperlinkSubscriberAgreement() {
        setOneTrustConfig();
        DisneyPlusSignUpIOSPageBase signUp = initPage(DisneyPlusSignUpIOSPageBase.class);
        DisneyplusLegalIOSPageBase legal = initPage(DisneyplusLegalIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickSignUpButton();

        signUp.openSubscriberAgreement();
        Assert.assertTrue(legal.isOpened(),
                "Legal page was not opened after " + SUBSCRIBER_AGREEMENT + " link clicked");
        validateUSLegalPageUI(sa, SUBSCRIBER_AGREEMENT);

        pressByElement(legal.getBackArrow(), 1); //click() is flaky on legal
        Assert.assertTrue(signUp.isOpened(),
                "'Back Button' navigation did not return the user to the Sign Up page");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62377"})
    @Test(description = "Verify valid Privacy Policy link, expand/collapse/scroll content", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void verifyUSLegalHyperlinkPrivacyPolicy() {
        setOneTrustConfig();
        DisneyPlusSignUpIOSPageBase signUp = initPage(DisneyPlusSignUpIOSPageBase.class);
        DisneyplusLegalIOSPageBase legal = initPage(DisneyplusLegalIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickSignUpButton();

        signUp.openPrivacyPolicyLink();
        Assert.assertTrue(legal.isOpened(),
                "Legal page was not opened after " + PRIVACY_POLICY + " link clicked");
        validateUSLegalPageUI(sa, PRIVACY_POLICY);

        pressByElement(legal.getBackArrow(), 1); //click() is flaky on legal
        Assert.assertTrue(signUp.isOpened(),
                "'Back Button' navigation did not return the user to the Sign Up page");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62385"})
    @Test(description = "Verify Legal Center UI is present", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void verifyUSLegalCenterUI() {
        setOneTrustConfig();
        DisneyPlusSignUpIOSPageBase signUp = initPage(DisneyPlusSignUpIOSPageBase.class);
        DisneyplusLegalIOSPageBase legal = initPage(DisneyplusLegalIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickSignUpButton();

        signUp.openSubscriberAgreement();
        Assert.assertTrue(legal.isOpened(),
                "Legal page was not opened after " + SUBSCRIBER_AGREEMENT + " link clicked");
        sa.assertTrue(legal.getTypeButtonByLabel(PRIVACY_POLICY).isPresent(),
                PRIVACY_POLICY + " is not visible");
        sa.assertTrue(legal.getTypeButtonByLabel(SUBSCRIBER_AGREEMENT).isPresent(),
                SUBSCRIBER_AGREEMENT + " is not visible");
        sa.assertTrue(legal.getTypeButtonByLabel(YOUR_CALIFORNIA_PRIVACY_RIGHTS).isPresent(),
                YOUR_CALIFORNIA_PRIVACY_RIGHTS + " is not visible");
        sa.assertTrue(legal.getTypeButtonByLabel(DO_NOT_SELL_MY_PERSONAL_INFORMATION).isPresent(),
                DO_NOT_SELL_MY_PERSONAL_INFORMATION + " is not visible");

        pressByElement(legal.getBackArrow(), 1); //click() is flaky on legal
        Assert.assertTrue(signUp.isOpened(),
                "'Back Button' navigation did not return the user to the Sign Up page");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62387"})
    @Test(description = "Verify Your California Privacy Rights and Do Not Sell My Personal Information expand/collapse/scroll content", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION }, enabled = false)
    public void verifyUSLegalCenterCaliforniaPrivacyAndDoNotSell() {
        setOneTrustConfig();
        DisneyPlusSignUpIOSPageBase signUp = initPage(DisneyPlusSignUpIOSPageBase.class);
        DisneyplusLegalIOSPageBase legal = initPage(DisneyplusLegalIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickSignUpButton();

        signUp.openSubscriberAgreement();
        Assert.assertTrue(legal.isOpened(),
                "Legal page was not opened after " + SUBSCRIBER_AGREEMENT + " link clicked");

        //Do Not Sell My Personal Information - no scrolling needed
        pressByElement(legal.getTypeButtonByLabel(DO_NOT_SELL_MY_PERSONAL_INFORMATION), 1); //expand
        sa.assertTrue(legal.getTypeButtonByLabel(DO_NOT_SELL_MY_PERSONAL_INFORMATION).getAttribute(IOSUtils.Attributes.VALUE.getAttribute()).equals(EXPANDED),
                DO_NOT_SELL_MY_PERSONAL_INFORMATION + " was not expanded.");

        pressByElement(legal.getTypeButtonByLabel(DO_NOT_SELL_MY_PERSONAL_INFORMATION), 1); //collapse
        sa.assertTrue(legal.getTypeButtonByLabel(DO_NOT_SELL_MY_PERSONAL_INFORMATION).getAttribute(IOSUtils.Attributes.VALUE.getAttribute()).equals(COLLAPSED),
                DO_NOT_SELL_MY_PERSONAL_INFORMATION + " was not collapsed");

        //Your California Privacy Rights
        validateUSLegalPageUI(sa, YOUR_CALIFORNIA_PRIVACY_RIGHTS);

        pressByElement(legal.getBackArrow(), 1); //click() is flaky on legal
        Assert.assertTrue(signUp.isOpened(),
                "'Back Button' navigation did not return the user to the Sign Up page");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62375", "XMOBQA-62377", "XMOBQA-62379", "XMOBQA-62381", "XMOBQA-62383", "XMOBQA-62385", "XMOBQA-62387", "XMOBQA-62389"})
    @Test(description = "Verify valid Privacy Policy, UK & EU Privacy Rights, and Cookies Policy links and Legal UI", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION }, enabled = false)
    public void verifyEULegalHyperlinks() {
        initiateProxy("Netherlands");
        Assert.fail("Disabled initial setup and reused default PRE_CONFIGURATION. Please check, why we need NL here.");
//        initialSetup("NL", "en");
        SoftAssert sa = new SoftAssert();
        DisneyPlusSignUpIOSPageBase disneyPlusSignUpIOSPageBase = initPage(DisneyPlusSignUpIOSPageBase.class);
        DisneyplusLegalIOSPageBase disneyPlusLegalIOSPageBase = initPage(DisneyplusLegalIOSPageBase.class);

        restart();
        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickSignUpButton();

        disneyPlusSignUpIOSPageBase.openSubscriberAgreement();

        Assert.assertTrue(disneyPlusLegalIOSPageBase.isOpened(),
                "XMOBQA-62375 - Legal page was not opened after 'Subscriber Agreement' hyperlink navigation");

        sa.assertTrue(disneyPlusLegalIOSPageBase.getTypeButtonByLabel("Subscriber Agreement").getAttribute(IOSUtils.Attributes.VALUE.getAttribute()).equals(EXPANDED),
                "XMOBQA-62375 - 'Subscriber Agreement' was not expanded on navigation");

        verifyLegalPageUI(sa, "NL");

        disneyPlusLegalIOSPageBase.getBackArrow().click();

        Assert.assertTrue(disneyPlusSignUpIOSPageBase.isOpened(),
                "XMOBQA-62389 - 'Back Button' navigation did not return the user to the Sign Up page");

        disneyPlusSignUpIOSPageBase.openPrivacyPolicyLink();

        Assert.assertTrue(disneyPlusLegalIOSPageBase.isOpened(),
                "XMOBQA-62377 - Legal page was not opened after 'Privacy Policy' hyperlink navigation");

        sa.assertTrue(disneyPlusLegalIOSPageBase.getTypeButtonByLabel("Privacy Policy").getAttribute(IOSUtils.Attributes.VALUE.getAttribute()).equals(EXPANDED),
                "XMOBQA-62377 - 'Privacy Policy' was not expanded on navigation");

        verifyLegalPageUI(sa, "NL");

        disneyPlusLegalIOSPageBase.getBackArrow().click();

        Assert.assertTrue(disneyPlusSignUpIOSPageBase.isOpened(),
                "XMOBQA-62389 - 'Back Button' navigation did not return the user to the Sign Up page");

        disneyPlusSignUpIOSPageBase.openCookiesPolicyLink();

        Assert.assertTrue(disneyPlusLegalIOSPageBase.isOpened(),
                "XMOBQA-62381 - Legal page was not opened after 'Cookies Policy' hyperlink navigation");

        sa.assertTrue(disneyPlusLegalIOSPageBase.getTypeButtonByLabel("Cookies Policy").getAttribute(IOSUtils.Attributes.VALUE.getAttribute()).equals(EXPANDED),
                "XMOBQA-62381 - 'Cookies Policy' was not expanded on navigation");

        verifyLegalPageUI(sa, "NL");

        disneyPlusLegalIOSPageBase.getBackArrow().click();

        Assert.assertTrue(disneyPlusSignUpIOSPageBase.isOpened(),
                "XMOBQA-62389 - 'Back Button' navigation did not return the user to the Sign Up page");

        disneyPlusSignUpIOSPageBase.openEuPrivacyLink();

        Assert.assertTrue(disneyPlusLegalIOSPageBase.isOpened(),
                "XMOBQA-62379 - Legal page was not opened after 'EU & UK Privacy Rights' hyperlink navigation");

        sa.assertTrue(disneyPlusLegalIOSPageBase.getTypeButtonByLabel("UK & EU Privacy Rights").getAttribute(IOSUtils.Attributes.VALUE.getAttribute()).equals(EXPANDED),
                "XMOBQA-62379 - 'UK & EU Privacy Rights' was not expanded on navigation");

        verifyLegalPageUI(sa, "NL");

        disneyPlusLegalIOSPageBase.getBackArrow().click();

        Assert.assertTrue(disneyPlusSignUpIOSPageBase.isOpened(),
                "XMOBQA-62389 - 'Back Button' navigation did not return the user to the Sign Up page");

        sa.assertAll();
    }

    @Maintainer("mboulogne1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62054"})
    @Test(description = "Email Validation Rules - Verify Error code string", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void verifyInvalidEmailError() {
        String invalidEmailError = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SDK_ERRORS, DictionaryKeys.ATTRIBUTE_VALIDATION.getText());
        SoftAssert sa = new SoftAssert();
        DisneyPlusSignUpIOSPageBase disneyPlusSignUpIOSPageBase = initPage(DisneyPlusSignUpIOSPageBase.class);
        DisneyPlusLoginIOSPageBase disneyPlusLoginIOSPageBase = initPage(DisneyPlusLoginIOSPageBase.class);

        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickSignUpButton();
        disneyPlusSignUpIOSPageBase.submitEmailAddress("abc");

        sa.assertEquals(disneyPlusLoginIOSPageBase.getErrorMessageString(), invalidEmailError, NO_ERROR_DISPLAYED);
        sa.assertAll();
    }

    private void verifyLegalPageUI(SoftAssert sa, String locale) {
        //TODO: IOS-6072 & IOS-6073: alternative validation above
        DisneyplusLegalIOSPageBase disneyPlusLegalIOSPageBase = initPage(DisneyplusLegalIOSPageBase.class);

        languageUtils.get().getLegalHeaders().forEach(header ->
                sa.assertTrue(disneyPlusLegalIOSPageBase.getTypeButtonByLabel(header).isElementPresent(),
                        String.format("XMOBQA-62385 - Header '%s' was not displayed for the given locale '%s'", header, locale)));

        String expandedHeader = "";
        for (String headerToExpand : languageUtils.get().getLegalHeaders()) {
            for (String header : languageUtils.get().getLegalHeaders()) {
                if (disneyPlusLegalIOSPageBase.getTypeButtonByLabel(header).getAttribute(IOSUtils.Attributes.VALUE.getAttribute()).equals(EXPANDED)) {
                    expandedHeader = header;
                }
            }

            if (!headerToExpand.equals(expandedHeader)) {
                disneyPlusLegalIOSPageBase.getTypeButtonByLabel(headerToExpand).click();

                sa.assertTrue(disneyPlusLegalIOSPageBase.getTypeButtonByLabel(expandedHeader).getAttribute(IOSUtils.Attributes.VALUE.getAttribute()).equals(COLLAPSED),
                        String.format("Previous expanded section '%s' was not collapsed as expected", expandedHeader));

                sa.assertTrue(disneyPlusLegalIOSPageBase.getTypeButtonByLabel(headerToExpand).getAttribute(IOSUtils.Attributes.VALUE.getAttribute()).equals(EXPANDED),
                        String.format("New section '%s' was not expanded as expected", headerToExpand));
            }
        }

        Assert.assertTrue(disneyPlusLegalIOSPageBase.getBackArrow().isElementPresent(),
                "XMOBQA-62385 - Back Arrow was not displayed on Legal display as expected");
    }

    private void validateUSLegalPageUI(SoftAssert sa, String legalHeader) {
        DisneyplusLegalIOSPageBase legal = initPage(DisneyplusLegalIOSPageBase.class);

        pressByElement(legal.getTypeButtonByLabel(legalHeader), 1); //expand
        sa.assertTrue(legal.getTypeButtonByLabel(legalHeader).getAttribute(IOSUtils.Attributes.VALUE.getAttribute()).equals(EXPANDED),
                legalHeader + " was not expanded");

        swipePageTillElementPresent(legal.getTypeButtonByLabel(DO_NOT_SELL_MY_PERSONAL_INFORMATION), 8, null, Direction.UP, 25);
        sa.assertTrue(legal.getTypeButtonByLabel(DO_NOT_SELL_MY_PERSONAL_INFORMATION).isPresent(),
                DO_NOT_SELL_MY_PERSONAL_INFORMATION + " is not visible");

        swipePageTillElementPresent(legal.getTypeButtonByLabel(PRIVACY_POLICY), 8, null, Direction.DOWN, 25);

        pressByElement(legal.getTypeButtonByLabel(legalHeader), 1); //collapse
        sa.assertTrue(legal.getTypeButtonByLabel(legalHeader).getAttribute(IOSUtils.Attributes.VALUE.getAttribute()).equals(COLLAPSED),
                legalHeader + " was not collapsed");
    }
}
