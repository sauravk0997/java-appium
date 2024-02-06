package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.dictionary.DisneyLocalizationUtils;
import com.disney.qa.disney.apple.pages.common.DisneyPlusPasswordIOSPageBase;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.*;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusPasswordIOSPageBase.class)
public class DisneyPlusAppleTVPasswordPage extends DisneyPlusPasswordIOSPageBase {

    @ExtendedFindBy(accessibilityId = "loginButton")
    protected ExtendedWebElement logInButtonPasswordScreen;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label == \"Create a password\"`]")
    private ExtendedWebElement createPasswordTitle;
    @ExtendedFindBy(accessibilityId = "progressBar")
    private ExtendedWebElement passwordStrengthMeter;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"progressBar\"`]/XCUIElementTypeOther")
    private ExtendedWebElement passwordStrengthMeterColor;
    @ExtendedFindBy(accessibilityId = "secureTextFieldPassword")
    private ExtendedWebElement createPasswordTextField;
    @ExtendedFindBy(accessibilityId = "buttonSignUp")
    private ExtendedWebElement signUpBtn;
    @ExtendedFindBy(accessibilityId = "buttonForgotPassword")
    private ExtendedWebElement forgotPasswordBtn;
    @ExtendedFindBy(accessibilityId = "buttonShowHidePassword")
    private ExtendedWebElement hideShowPasswordBtn;

    public DisneyPlusAppleTVPasswordPage(WebDriver driver) {
        super(driver);
    }

    public static List<String> getLogInPasswordScreenTexts(DisneyLocalizationUtils disneyLanguageUtils) {
        var list = new ArrayList<String>();
        getEnumValues(ENTER_YOUR_PASSWORD, PASSWORD, FORGOT_PASSWORD, LOGIN_BTN)
                .forEach(item -> list.add(disneyLanguageUtils.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, item)));
        return list;
    }

    @Override
    public boolean isOpened() {
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return staticTextLabelContains.format("Enter your password").isPresent();
    }

    public int getPasswordStrengthMeterWidth() {
        return passwordStrengthMeterColor.getSize().getWidth();
    }

    public void clickPassword() {
        passwordEntryField.click();
    }

    public String getPasswordFieldText() {
        return passwordEntryField.getText();
    }

    public boolean isPasswordFieldFocused() {
        return isFocused(passwordEntryField);
    }

    public boolean isForgotPasswordBtnFocused() {
        boolean isFocused = isFocused(forgotPasswordBtn);
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isFocused;
    }

    public boolean isLogInBtnFocused() {
        boolean isFocused = isFocused(primaryButton);
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isFocused;
    }

    public boolean isSignUpBtnFocused() {
        boolean isFocused = isFocused(primaryButton);
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isFocused;
    }

    public void createNewPasswordEntry(String password) {
        clickPassword();
        enterPasswordCreatePassword(password);
        moveToContinueBtnKeyboardEntry();
        clickSelect();
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        primaryButton.click();
    }

    public void enterPassword(String password) {
        passwordEntryField.type(password);
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
    }

    public void enterPasswordCreatePassword(String password) {
        createPasswordTextField.type(password);
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
    }

    public void clickSignUp() {
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        primaryButton.click();
    }

    public boolean isCreatePasswordScreenOpen() {
        boolean isPresent = createPasswordTitle.isElementPresent();
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isPresent;
    }

    public List<String> getLogInPasswordScreenActualTexts() {
        return Stream.of(headlineHeader.getText(), passwordEntryField.getText(),
                        getDynamicAccessibilityId(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, FORGOT_PASSWORD.getText())).getText(), primaryButton.getText())
                .collect(Collectors.toList());
    }

    public String getShowHidePasswordBtnState() {
        String text = hideShowPasswordBtn.getText();
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return text;
    }

    public void passwordEntry(String password) {
        clickPassword();
        enterPassword(password);
        moveToContinueBtnKeyboardEntry();
        clickSelect();
    }

    public void logInWithPassword(String password) {
        passwordEntry(password);
        Assert.assertTrue(isOpened(), "Password entry page did not open");
        clickSelect();
    }

    public void logInWithPasswordLocalized(String password) {
        clickPassword();
        enterPassword(password);
        keyPressTimes(getClickActionBasedOnLocalizedKeyboardOrientation(), 6, 1);
        clickSelect();
        isOpened();
        //TODO: TVOS-3472 focus not found on login button after user enters password
        moveDown(2,1);
        clickSelect();
    }

    public void submitSandboxPassword(String password) {
        clickPassword();
        enterPassword(password);
    }

    public boolean isContinueBtnOnCreatePasswordPresent() {
        boolean isPresent = primaryButton.isElementPresent();
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isPresent;
    }

    public boolean isContinueButtonFocused() {
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isFocused(primaryButton);
    }

    public void clickForgotPasswordBtn() {
        forgotPasswordBtn.click();
    }

    public boolean isCreateNewPasswordScreenOpen() {
        return staticTextByLabel.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, CREATE_NEW_PASSWORD.getText())).isPresent();
    }

    public boolean isStrengthBarPresent() {
        return passwordStrengthMeter.isElementPresent();
    }

    public List<List<String>> getStrengthMeterVerificationLists() {
        var dictionaryList = new LinkedList<String>();
        String widthListName = getDevice().getName().contains("4K") ? "disney_password_meter_width_4K" : "disney_password_meter_width";
        Stream.of(PASSWORD_RATING_FAIR, PASSWORD_RATING_FAIR, PASSWORD_RATING_FAIR, PASSWORD_RATING_GOOD, PASSWORD_RATING_GREAT, PASSWORD_RATING_GREAT)
                .collect(Collectors.toList()).forEach(item -> dictionaryList.add(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, item.getText())));
        return Stream.of(dictionaryList,
                        Stream.of(R.TESTDATA.get("disney_password_list").split(",")).collect(Collectors.toList()),
                        Stream.of(R.TESTDATA.get(widthListName).split(",")).collect(Collectors.toList()),
                        Stream.of(R.TESTDATA.get("disney_password_meter_colors").split(",")).collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    public void clickHideShowPasswordBtn() {
        hideShowPasswordBtn.click();
    }

    public String getPasswordHintText() {
        return passwordHintText.getText();
    }

    public void clickContinueBtn() {
        primaryButton.click();
    }

    public void enterNewPassword(String text) {
        getDynamicAccessibilityId(getDictionary()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, CREATE_NEW_PASSWORD.getText()))
                .type(text);
    }

    /**
     * This method is used when user has already entered a password and wants to replace the existing password
     *
     * @param currentPassword - currently entered password
     * @param newPassword     - password to be entered
     */
    public void clearAndEnterNewPassword(String currentPassword, String newPassword) {
        try {
            staticTypeTextViewValueDoubleQuotes.format(currentPassword).getElement().clear();
        } catch (StaleElementReferenceException e) {
            enterNewPassword(newPassword);
        }
    }
}
