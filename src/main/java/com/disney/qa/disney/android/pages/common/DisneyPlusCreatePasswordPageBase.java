package com.disney.qa.disney.android.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.disney.qa.common.utils.MobileUtilsExtended;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusCreatePasswordPageBase extends DisneyPlusCommonPageBase{

    @FindBy(id = "onboardingDisneyLogo")
    private ExtendedWebElement disneyLogo;

    @FindBy(id = "registerTitle")
    private ExtendedWebElement createPasswordTitle;

    @FindBy(xpath = "//*[@resource-id='com.disney.disneyplus:id/resetLayout']/*[@resource-id='com.disney.disneyplus:id/title']")
    private ExtendedWebElement createNewPasswordTitle;

    @FindBy(id = "welcomeMessage")
    private ExtendedWebElement welcomeMessage;

    @FindBy(id = "meterProgressBar")
    private ExtendedWebElement passwordStrengthMeter;

    @FindBy(id ="inputShowPwdImageView")
    private ExtendedWebElement showHideIcon;

    @FindBy(id = "backButton")
    private ExtendedWebElement backButton;

    @FindBy(id = "onboardingStepperTextView")
    private ExtendedWebElement onboardingStepperTextView;

    @FindBy(id = "editFieldEditText")
    private ExtendedWebElement editFieldEditText;

    @FindBy(id = "inputDescriptionTextView")
    private ExtendedWebElement inputDescriptionTextView;

    @FindBy(id = "fatFingerHeaderTextView")
    private ExtendedWebElement fatFingerHeaderTextView;

    @FindBy(id = "standardButtonContainer")
    private ExtendedWebElement standardButtonContainer;

    @FindBy(id = "meterTextView")
    private ExtendedWebElement meterTextView;

    public DisneyPlusCreatePasswordPageBase(WebDriver driver) {
        super(driver);
    }

    /**
     * Create Password Layout ViewGroup: registerAccountLayout
     * Create New Password Layout ViewGroup: passwordResetRoot
     */
    public boolean isCreatePasswordOpened() {
        return createPasswordTitle.isElementPresent();
    }

    public boolean isCreateNewPasswordOpened() {
        return createNewPasswordTitle.isElementPresent();
    }

    public boolean isDisneyLogoDisplayed() {
        return disneyLogo.isElementPresent();
    }

    public boolean isTitleDisplayed() {
        return createPasswordTitle.isElementPresent();
    }

    public String getTitleText() {
        return getElementText(createPasswordTitle);
    }

    public String getMeterTextViewText() {
        return getElementText(meterTextView);
    }

    public String getCreateNewPasswordTitleText() {
        return getElementText(createNewPasswordTitle);
    }

    public boolean isMessageDisplayed() {
        return welcomeMessage.isElementPresent();
    }

    public String getMessageText() {
        return getElementText(welcomeMessage);
    }

    public boolean isBackButtonDisplayed() {
        return backButton.isElementPresent();
    }

    public boolean isOnboardingStepperTextViewDisplayed() {
        return onboardingStepperTextView.isElementPresent();
    }

    public boolean isInputDescriptionTextViewDisplayed() {
        return inputDescriptionTextView.isElementPresent();
    }

    public boolean isFatFingerHeaderTextViewDisplayed() {
        return fatFingerHeaderTextView.isElementPresent();
    }

    public boolean isCreateAPasswordTitleDisplayed() {
        return createPasswordTitle.isElementPresent();
    }

    public boolean isPasswordEntryFieldDisplayed() {
        return editTextField.isElementPresent();
    }

    public boolean isShowHidePasswordIconDisplayed() {
        return getShowPwdToggle().isElementPresent();
    }

    public boolean isShowHideExpectedStatus(String status) { return showHideIcon.getAttribute(CONTENT_DESC).equals(status);}

    public void enterNewPassword(String value) {
        if(editTextField.isElementPresent()) {
            editTextField.click();
            editTextField.type(value);
        }
    }

    public boolean isStrengthMeterDisplayed() {
        return passwordStrengthMeter.isElementPresent();
    }

    public void submitNewPassword(String value) {
        enterNewPassword(value);
        new AndroidUtilsExtended().hideKeyboard();
        clickStandardButton();
    }

    public boolean isPasswordInstructionsDisplayed() {
        return getCaseSensitiveSubcopy().isElementPresent();
    }

    public String getPasswordInstructionsText() {
        return getElementText(getCaseSensitiveSubcopy());
    }

    public boolean isContinueButtonPresent() {
        return isStandardButtonPresent();
    }

    public boolean isInputErrorDisplayed() {
        return errorTextView.isElementPresent();
    }

    public String getInputErrorText() {
        return getElementText(errorTextView);
    }

    public boolean isStatusBarRed() {
        return isStatusBarCorrectHexValue("FF554C");
    }

    public boolean isStatusBarOrange() {
        return isStatusBarCorrectHexValue("FFB63F");
    }

    public boolean isStatusBarGreen() {
        return isStatusBarCorrectHexValue("63DC9F");
    }
    
    //Returns is RGB Hex value of the status bar at 10% width / 50% height is correct
    private boolean isStatusBarCorrectHexValue(String rgbHex) {
        MobileUtilsExtended util = new MobileUtilsExtended();
        return util.isImagePointDesiredColor(util.getElementImage(passwordStrengthMeter), rgbHex, .1, .5);
    }

    public boolean isFairTextCorrect() {
        DisneyPlusCreatePasswordPageBase createPasswordPage = initPage(DisneyPlusCreatePasswordPageBase.class);
        return (Objects.equals(createPasswordPage.getMeterTextViewText(), getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DictionaryKeys.PASSWORD_RATING_FAIR.getText())));
    }

    public boolean isGoodTextCorrect() {
        DisneyPlusCreatePasswordPageBase createPasswordPage = initPage(DisneyPlusCreatePasswordPageBase.class);
        return (Objects.equals(createPasswordPage.getMeterTextViewText(), getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DictionaryKeys.PASSWORD_RATING_GOOD.getText())));
    }

    public boolean isGreatTextCorrect() {
        DisneyPlusCreatePasswordPageBase createPasswordPage = initPage(DisneyPlusCreatePasswordPageBase.class);
        return (Objects.equals(createPasswordPage.getMeterTextViewText(), getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DictionaryKeys.PASSWORD_RATING_GREAT.getText())));
    }

    public Map <Integer, Boolean> isCreatePasswordStrengthMeterFunctioning(){
        DisneyPlusCreatePasswordPageBase createPasswordPage = initPage(DisneyPlusCreatePasswordPageBase.class);

        String passwordLevel1 = "1";
        String passwordLevel2 = "1234!@";
        String passwordLevel3 = "Test3r!@";
        String passwordLevel4 = "Test3r!@D";
        String passwordLevel5 = "G0D1sn3yQ@";
        String passwordLevel6 = "G0D1sn3yQ@W00";

        Map<Integer, Boolean> strengthMeter = new TreeMap<>();

        createPasswordPage.enterNewPassword(passwordLevel1);
        strengthMeter.put(1, createPasswordPage.isStatusBarRed());
        LOGGER.info("Step 1 Result: '{}'", createPasswordPage.isStatusBarRed());

        createPasswordPage.enterNewPassword(passwordLevel2);
        strengthMeter.put(2, createPasswordPage.isStatusBarRed());
        LOGGER.info("Step 2 Result: '{}'", createPasswordPage.isStatusBarRed());

        createPasswordPage.enterNewPassword(passwordLevel3);
        strengthMeter.put(3, createPasswordPage.isStatusBarOrange());
        LOGGER.info("Step 3 Result: '{}'", createPasswordPage.isStatusBarOrange());

        strengthMeter.put(4, createPasswordPage.isFairTextCorrect());
        LOGGER.info("Step 4 Result: '{}'", createPasswordPage.isFairTextCorrect());

        createPasswordPage.enterNewPassword(passwordLevel4);
        strengthMeter.put(5, createPasswordPage.isStatusBarOrange());
        LOGGER.info("Step 5 Result: '{}'", createPasswordPage.isStatusBarOrange());

        strengthMeter.put(6, createPasswordPage.isFairTextCorrect());
        LOGGER.info("Step 6 Result: '{}'", createPasswordPage.isFairTextCorrect());

        createPasswordPage.enterNewPassword(passwordLevel5);
        strengthMeter.put(7, createPasswordPage.isStatusBarGreen());
        LOGGER.info("Step 7 Result: '{}'", createPasswordPage.isStatusBarGreen());

        strengthMeter.put(8, createPasswordPage.isGoodTextCorrect());
        LOGGER.info("Step 8 Result: '{}'", createPasswordPage.isGoodTextCorrect());

        createPasswordPage.enterNewPassword(passwordLevel6);
        strengthMeter.put(9, createPasswordPage.isStatusBarGreen());
        LOGGER.info("Step 9 Result: '{}'", createPasswordPage.isStatusBarGreen());

        strengthMeter.put(10, createPasswordPage.isGreatTextCorrect());
        LOGGER.info("Step 10 Result: '{}'", createPasswordPage.isGreatTextCorrect());

        return strengthMeter;
    }
}
