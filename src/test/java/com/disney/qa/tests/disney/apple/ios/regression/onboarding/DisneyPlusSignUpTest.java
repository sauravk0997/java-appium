package com.disney.qa.tests.disney.apple.ios.regression.onboarding;

import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusSignUpTest extends DisneyBaseTest {

    public static final String NO_ERROR_DISPLAYED = "error message was not displayed";
    static final String EXPANDED = "Expanded";
    static final String COLLAPSED = "Collapsed";
    static final String US_STATE_PRIVACY_RIGHTS = "US State Privacy Rights Notice";
    static final String SUBSCRIBER_AGREEMENT = "Subscriber Agreement";
    static final String PRIVACY_POLICY = "Privacy Policy";
    static final String DO_NOT_SELL_MY_PERSONAL_INFORMATION = "Do Not Sell or Share My Personal Information";
    static final String DISNEY_TERMS_OF_USE = "Disney Terms of Use";


    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66555"})
    @Test(description = "Verify 'Sign Up' page elements are all present", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION})
    public void verifySignUpPageUI() {
        DisneyPlusSignUpIOSPageBase disneyPlusSignUpIOSPageBase = initPage(DisneyPlusSignUpIOSPageBase.class);
        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickSignUpButton();
        SoftAssert sa = new SoftAssert();
        Assert.assertTrue(disneyPlusSignUpIOSPageBase.isOpened(), "'Sign Up' did not open the email submission screen as expected");
        sa.assertTrue(disneyPlusSignUpIOSPageBase.isBackButtonPresent(), "Back Button (arrow) was not displayed as expected");
        sa.assertTrue(disneyPlusSignUpIOSPageBase.isStep1LabelDisplayed(), "STEP 1 label was not displayed");
        sa.assertTrue(disneyPlusSignUpIOSPageBase.isEnterEmailHeaderDisplayed(), "Enter Email Header was not displayed");
        sa.assertTrue(disneyPlusSignUpIOSPageBase.isEnterEmailBodyDisplayed(), "Enter Email body was not displayed");
        sa.assertTrue(disneyPlusSignUpIOSPageBase.isEmailFieldDisplayed(), "Email field was not displayed as expected");
        sa.assertTrue(disneyPlusSignUpIOSPageBase.continueButtonPresent(), "Continue button was not found");
        sa.assertTrue(disneyPlusSignUpIOSPageBase.isLearnMoreHeaderDisplayed(), "Learn more header was not displayed");
        sa.assertTrue(disneyPlusSignUpIOSPageBase.isLearnMoreBodyDisplayed(), "Learn more body was not displayed");

        //Need to remove below assertion and method once developer add specific accessibility Id to each logo
        //Dev ticket to add  accessibility to each logo - IOS-11385
        sa.assertTrue(disneyPlusSignUpIOSPageBase.isMultipleBrandLogosDisplayed(), "Brand Logos are not displayed");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66567"})
    @Test(description = "Verify 'Sign Up' page elements are all present", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void verifyInvalidEmailSubmissions() {
        DisneyPlusSignUpIOSPageBase disneyPlusSignUpIOSPageBase = initPage(DisneyPlusSignUpIOSPageBase.class);
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusCreatePasswordIOSPageBase disneyPlusCreatePasswordIOSPageBase = initPage(DisneyPlusCreatePasswordIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        welcomePage.clickSignUpButton();

        disneyPlusSignUpIOSPageBase.clickContinueBtn();
        sa.assertTrue(disneyPlusCreatePasswordIOSPageBase.isAttributeValidationErrorMessagePresent(), "Submitting no email did not produce an invalid email error");

        disneyPlusSignUpIOSPageBase.submitEmailAddress("EmailWithoutSymbol.com");

        sa.assertTrue(disneyPlusCreatePasswordIOSPageBase.isAttributeValidationErrorMessagePresent(), "Missing '@' did not produce an invalid email error");
        disneyPlusSignUpIOSPageBase.clearEmailAddress();

        disneyPlusSignUpIOSPageBase.submitEmailAddress("EmailWithoutDomain");

        sa.assertTrue(disneyPlusCreatePasswordIOSPageBase.isAttributeValidationErrorMessagePresent(), "Missing email domain (.com) did not produce an invalid email error");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66565"})
    @Test(description = "Verify signup with new account", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void verifyNewEmailSubmission() {
        DisneyPlusSignUpIOSPageBase disneyPlusSignUpIOSPageBase = initPage(DisneyPlusSignUpIOSPageBase.class);
        DisneyPlusCreatePasswordIOSPageBase disneyPlusCreatePasswordIOSPageBase = initPage(DisneyPlusCreatePasswordIOSPageBase.class);
        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickSignUpButton();
        Assert.assertTrue(disneyPlusSignUpIOSPageBase.isOpened(),
                "'Sign Up' did not open the email submission screen as expected");

        disneyPlusSignUpIOSPageBase.submitEmailAddress(generateGmailAccount());
        Assert.assertTrue(disneyPlusCreatePasswordIOSPageBase.isCreateNewPasswordPageOpened(),
                "User was not directed to Create Password as expected");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66563"})
    @Test(description = "Verify signup with pre-existing account", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void verifyExistingEmailSubmission() {
        DisneyPlusSignUpIOSPageBase disneyPlusSignUpIOSPageBase = initPage(DisneyPlusSignUpIOSPageBase.class);
        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickSignUpButton();
        Assert.assertTrue(disneyPlusSignUpIOSPageBase.isOpened(),
                "'Sign Up' did not open the email submission screen as expected");

        disneyPlusSignUpIOSPageBase.enterEmailAddress(getAccount().getEmail());
        disneyPlusSignUpIOSPageBase.clickContinueBtn();

        Assert.assertTrue(initPage(DisneyPlusPasswordIOSPageBase.class).isPasswordPagePresent(),
                "User was not directed to Password Entry as expected");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66581"})
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66583"})
    @Test(description = "Verify invalid password submissions", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION})
    public void verifyInvalidPasswordSubmissions() {
        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickSignUpButton();
        DisneyPlusSignUpIOSPageBase signUp = initPage(DisneyPlusSignUpIOSPageBase.class);
        DisneyPlusCreatePasswordIOSPageBase createPasswordPage = initPage(DisneyPlusCreatePasswordIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        signUp.enterEmailAddress(generateGmailAccount() + "\n");
        Assert.assertTrue(createPasswordPage.isCreateNewPasswordPageOpened(), "Create password page not opened");
        createPasswordPage.submitPasswordValue("12345");
        sa.assertTrue(createPasswordPage.isInvalidPasswordErrorDisplayed(), "Invalid Password Error was not displayed as expected");

        createPasswordPage.clickShowHidePassword();
        createPasswordPage.getKeyboardDelete().click();
        sa.assertFalse(createPasswordPage.isInvalidPasswordErrorDisplayed(), "'Invalid Password' Error was still displayed after user delete one char");

        createPasswordPage.submitPasswordValue("");
        sa.assertTrue(createPasswordPage.isEmptyPasswordErrorDisplayed(), "Empty Password Error was not displayed");

        createPasswordPage.submitPasswordValue("abcghtjk");
        sa.assertTrue(createPasswordPage.isInvalidPasswordErrorDisplayed(), "'Invalid Password' error was not displayed for 6 digit password that did not meet password requirements.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62247"})
    @Test(description = "Verify onboarding stepper for EU based users", groups = {"Onboarding", "DE", TestGroup.PRE_CONFIGURATION, TestGroup.PROXY }, enabled = false)
    public void verifyOnboardingStepperEU() {
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66561"})
    @Test(description = "Verify valid Subscriber Agreement link, expand/collapse/scroll content", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void verifyUSLegalHyperlinkSubscriberAgreement() {
        DisneyPlusSignUpIOSPageBase signUp = initPage(DisneyPlusSignUpIOSPageBase.class);
        DisneyplusLegalIOSPageBase legal = initPage(DisneyplusLegalIOSPageBase.class);
        DisneyPlusCreatePasswordIOSPageBase createPasswordPage = initPage(DisneyPlusCreatePasswordIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickSignUpButton();
        Assert.assertTrue(signUp.isOpened(),
                "'Sign Up' did not open the email submission screen as expected");
        signUp.enterEmailAddress(generateGmailAccount() + "\n");

        Assert.assertTrue(createPasswordPage.isCreateNewPasswordPageOpened(), "Create password page not opened");

        createPasswordPage.openSubscriberAgreement();
        Assert.assertTrue(legal.isOpened(), "Legal page was not opened after " + SUBSCRIBER_AGREEMENT + " link clicked");
        validateUSLegalPageUI(sa, SUBSCRIBER_AGREEMENT);

        pressByElement(legal.getBackArrow(), 1); //click() is flaky on legal
        Assert.assertTrue(createPasswordPage.isCreateNewPasswordPageOpened(), "Legal model not closed");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66561"})
    @Test(description = "Verify valid Privacy Policy link, expand/collapse/scroll content", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION})
    public void verifyUSLegalHyperlinkPrivacyPolicy() {
        DisneyPlusSignUpIOSPageBase signUp = initPage(DisneyPlusSignUpIOSPageBase.class);
        DisneyplusLegalIOSPageBase legal = initPage(DisneyplusLegalIOSPageBase.class);
        DisneyPlusCreatePasswordIOSPageBase createPasswordPage = initPage(DisneyPlusCreatePasswordIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickSignUpButton();
        Assert.assertTrue(signUp.isOpened(),
                "'Sign Up' did not open the email submission screen as expected");
        signUp.enterEmailAddress(generateGmailAccount() + "\n");

        Assert.assertTrue(createPasswordPage.isCreateNewPasswordPageOpened(), "Create password page not opened");

        createPasswordPage.openPrivacyPolicyLink();
        Assert.assertTrue(legal.isOpened(), "Legal page was not opened after " + PRIVACY_POLICY + " link clicked");
        validateUSLegalPageUI(sa, PRIVACY_POLICY);

        pressByElement(legal.getBackArrow(), 1); //click() is flaky on legal
        Assert.assertTrue(createPasswordPage.isCreateNewPasswordPageOpened(), "Legal model not closed");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66561"})
    @Test(description = "Verify Legal Center UI is present", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION})
    public void verifyUSLegalCenterUI() {
        DisneyPlusSignUpIOSPageBase signUp = initPage(DisneyPlusSignUpIOSPageBase.class);
        DisneyplusLegalIOSPageBase legal = initPage(DisneyplusLegalIOSPageBase.class);
        DisneyPlusCreatePasswordIOSPageBase createPasswordPage = initPage(DisneyPlusCreatePasswordIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickSignUpButton();
        Assert.assertTrue(signUp.isOpened(),
                "'Sign Up' did not open the email submission screen as expected");
        signUp.enterEmailAddress(generateGmailAccount() + "\n");

        Assert.assertTrue(createPasswordPage.isCreateNewPasswordPageOpened(), "Create password page not opened");

        createPasswordPage.openSubscriberAgreement();
        Assert.assertTrue(legal.isOpened(), "Legal page was not opened after " + SUBSCRIBER_AGREEMENT + " link clicked");
        sa.assertTrue(legal.getTypeButtonByLabel(DISNEY_TERMS_OF_USE).isPresent(),
                DISNEY_TERMS_OF_USE + " is not visible");
        sa.assertTrue(legal.getTypeButtonByLabel(PRIVACY_POLICY).isPresent(),
                PRIVACY_POLICY + " is not visible");
        sa.assertTrue(legal.getTypeButtonByLabel(SUBSCRIBER_AGREEMENT).isPresent(),
                SUBSCRIBER_AGREEMENT + " is not visible");
        sa.assertTrue(legal.getTypeButtonByLabel(US_STATE_PRIVACY_RIGHTS).isPresent(),
                US_STATE_PRIVACY_RIGHTS + " is not visible");
        sa.assertTrue(legal.getTypeButtonByLabel(DO_NOT_SELL_MY_PERSONAL_INFORMATION).isPresent(),
                DO_NOT_SELL_MY_PERSONAL_INFORMATION + " is not visible");

        pressByElement(legal.getBackArrow(), 1); //click() is flaky on legal
        Assert.assertTrue(createPasswordPage.isCreateNewPasswordPageOpened(), "Legal model not closed");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66561"})
    @Test(description = "Verify Your California Privacy Rights and Do Not Sell My Personal Information expand/collapse/scroll content", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void verifyUSLegalCenterUSStatePrivacy() {
        DisneyPlusSignUpIOSPageBase signUp = initPage(DisneyPlusSignUpIOSPageBase.class);
        DisneyplusLegalIOSPageBase legal = initPage(DisneyplusLegalIOSPageBase.class);
        DisneyPlusCreatePasswordIOSPageBase createPasswordPage = initPage(DisneyPlusCreatePasswordIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickSignUpButton();

        createPasswordPage.openSubscriberAgreement();
        Assert.assertTrue(legal.isOpened(),
                "Legal page was not opened after " + SUBSCRIBER_AGREEMENT + " link clicked");

        //Your California Privacy Rights
        validateUSLegalPageUI(sa, US_STATE_PRIVACY_RIGHTS);

        pressByElement(legal.getBackArrow(), 1); //click() is flaky on legal
        Assert.assertTrue(signUp.isOpened(),
                "'Back Button' navigation did not return the user to the Sign Up page");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62375", "XMOBQA-62377", "XMOBQA-62379", "XMOBQA-62381", "XMOBQA-62383", "XMOBQA-62385", "XMOBQA-62387", "XMOBQA-62389"})
    @Test(description = "Verify valid Privacy Policy, UK & EU Privacy Rights, and Cookies Policy links and Legal UI", groups = {"Onboarding", "NL", TestGroup.PRE_CONFIGURATION }, enabled = false)
    public void verifyEULegalHyperlinks() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusSignUpIOSPageBase disneyPlusSignUpIOSPageBase = initPage(DisneyPlusSignUpIOSPageBase.class);
        DisneyplusLegalIOSPageBase disneyPlusLegalIOSPageBase = initPage(DisneyplusLegalIOSPageBase.class);
        DisneyPlusCreatePasswordIOSPageBase createPasswordPage = initPage(DisneyPlusCreatePasswordIOSPageBase.class);

        restart();
        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickSignUpButton();

        createPasswordPage.openSubscriberAgreement();

        Assert.assertTrue(disneyPlusLegalIOSPageBase.isOpened(),
                "XMOBQA-62375 - Legal page was not opened after 'Subscriber Agreement' hyperlink navigation");

        sa.assertTrue(disneyPlusLegalIOSPageBase.getTypeButtonByLabel("Subscriber Agreement").getAttribute(IOSUtils.Attributes.VALUE.getAttribute()).equals(EXPANDED),
                "XMOBQA-62375 - 'Subscriber Agreement' was not expanded on navigation");

        verifyLegalPageUI(sa, "NL");

        disneyPlusLegalIOSPageBase.getBackArrow().click();

        Assert.assertTrue(disneyPlusSignUpIOSPageBase.isOpened(),
                "XMOBQA-62389 - 'Back Button' navigation did not return the user to the Sign Up page");

        createPasswordPage.openPrivacyPolicyLink();

        Assert.assertTrue(disneyPlusLegalIOSPageBase.isOpened(),
                "XMOBQA-62377 - Legal page was not opened after 'Privacy Policy' hyperlink navigation");

        sa.assertTrue(disneyPlusLegalIOSPageBase.getTypeButtonByLabel("Privacy Policy").getAttribute(IOSUtils.Attributes.VALUE.getAttribute()).equals(EXPANDED),
                "XMOBQA-62377 - 'Privacy Policy' was not expanded on navigation");

        verifyLegalPageUI(sa, "NL");

        disneyPlusLegalIOSPageBase.getBackArrow().click();

        Assert.assertTrue(disneyPlusSignUpIOSPageBase.isOpened(),
                "XMOBQA-62389 - 'Back Button' navigation did not return the user to the Sign Up page");

        createPasswordPage.openCookiesPolicyLink();

        Assert.assertTrue(disneyPlusLegalIOSPageBase.isOpened(),
                "XMOBQA-62381 - Legal page was not opened after 'Cookies Policy' hyperlink navigation");

        sa.assertTrue(disneyPlusLegalIOSPageBase.getTypeButtonByLabel("Cookies Policy").getAttribute(IOSUtils.Attributes.VALUE.getAttribute()).equals(EXPANDED),
                "XMOBQA-62381 - 'Cookies Policy' was not expanded on navigation");

        verifyLegalPageUI(sa, "NL");

        disneyPlusLegalIOSPageBase.getBackArrow().click();

        Assert.assertTrue(disneyPlusSignUpIOSPageBase.isOpened(),
                "XMOBQA-62389 - 'Back Button' navigation did not return the user to the Sign Up page");

        createPasswordPage.openEuPrivacyLink();

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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67218"})
    @Test(description = "Email Validation Rules - Verify Error code string", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION})
    public void verifyInvalidEmailError() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusSignUpIOSPageBase disneyPlusSignUpIOSPageBase = initPage(DisneyPlusSignUpIOSPageBase.class);
        DisneyPlusLoginIOSPageBase disneyPlusLoginIOSPageBase = initPage(DisneyPlusLoginIOSPageBase.class);

        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickSignUpButton();
        disneyPlusSignUpIOSPageBase.enterEmailAddress("abc");
        disneyPlusSignUpIOSPageBase.clickContinueBtn();

        sa.assertTrue(disneyPlusLoginIOSPageBase.isAttributeValidationErrorMessagePresent(), NO_ERROR_DISPLAYED);
        sa.assertAll();
    }

    private void verifyLegalPageUI(SoftAssert sa, String locale) {
        //TODO: IOS-6072 & IOS-6073: alternative validation above
        DisneyplusLegalIOSPageBase disneyPlusLegalIOSPageBase = initPage(DisneyplusLegalIOSPageBase.class);

        getLocalizationUtils().getLegalHeaders().forEach(header ->
                sa.assertTrue(disneyPlusLegalIOSPageBase.getTypeButtonByLabel(header).isElementPresent(),
                        String.format("XMOBQA-62385 - Header '%s' was not displayed for the given locale '%s'", header, locale)));

        String expandedHeader = "";
        for (String headerToExpand : getLocalizationUtils().getLegalHeaders()) {
            for (String header : getLocalizationUtils().getLegalHeaders()) {
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

        swipePageTillElementPresent(legal.getTypeButtonByLabel(DISNEY_TERMS_OF_USE), 8, null, Direction.DOWN, 25);
        pressByElement(legal.getTypeButtonByLabel(legalHeader), 1); //collapse

        sa.assertTrue(legal.getTypeButtonByLabel(legalHeader).getAttribute(IOSUtils.Attributes.VALUE.getAttribute()).equals(COLLAPSED),
                legalHeader + " was not collapsed");
    }
}
