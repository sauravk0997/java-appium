package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.apple.pages.common.DisneyPlusLoginIOSPageBase;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.stream.IntStream;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.*;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusLoginIOSPageBase.class)
public class DisneyPlusAppleTVLoginPage extends DisneyPlusLoginIOSPageBase {

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label == \"Enter New…\"`]")
    private ExtendedWebElement enterNewBtn;

    public DisneyPlusAppleTVLoginPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return getTextEntryField().format(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, MY_DISNEY_CONTINUE_BTN.getText())).isPresent() && getEmailHint().isPresent(THREE_SEC_TIMEOUT);
    }

    public void clickEnterNewBtn() {
        if (enterNewBtn.isPresent()) {
            IntStream.range(0, getNumberOfPrevUsedEmails()).forEach(i -> {
                clickDown();
                pause(1);
            });
            clickSelect();
        }
    }

    public void clickLocalizationEnterNewBtn() {
        List<ExtendedWebElement> listOfOtherElements = findExtendedWebElements(typeCell.getBy());
        if (!listOfOtherElements.isEmpty()) {
            IntStream.range(0, getNumberOfPrevUsedEmails()).forEach(i ->
                    moveDown(1,1));
            clickSelect();
        }
    }

    public void clickEmailField() {
        textEntryField.click();
    }

    // To enter a temp string "bcd" into the email field
    // also clicks on continue button, using select here because element.click was not working
    public void enterTempEmailTextAndClickContinue() {
        keyboard.isPresent();
        IntStream.range(0, 3).forEach(i -> {
            clickRight();
            clickSelect();
            pause(3);
        });
        moveToContinueOrDoneBtnKeyboardEntry();
        clickSelect();
    }

    public int getNumberOfPrevUsedEmails() {
        List<ExtendedWebElement> list = findExtendedWebElements(typeCell.getBy());
        return list.size();
    }

    public boolean isEmailFieldFocused() {
        return isFocused(getTextEntryField().format(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_ENTER_EMAIL_HINT.getText())));
    }

    public boolean isKeyboardPresent() {
        boolean isPresent = keyboard.isElementPresent();
        return isPresent;
    }

    public boolean isContinueButtonDisplayed() {
        return getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, MY_DISNEY_CONTINUE_BTN.getText())).isPresent();
    }

    public boolean isContinueButtonFocused() {
        //TODO: TVOS-3471 Continue button is not in focus.
        moveDown(1,1);
        return isFocused(getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, MY_DISNEY_CONTINUE_BTN.getText())));
    }

    public void navigateToContinueButton() {
        clickDown();
    }

    public void enterEmail(String email) {
        typeTextView.type(email);
    }

    public void clickContinueBtn() {
        continueButton.click();
    }

    public void proceedToPasswordScreen(String email) {
        clickEmailField();
        clickEnterNewBtn();
        enterEmail(email);
        keyPressTimes(getClickActionBasedOnLocalizedKeyboardOrientation(), 6, 1);
        clickSelect();
        fluentWait(getDriver(), FIFTEEN_SEC_TIMEOUT, THREE_SEC_TIMEOUT, "Continue button wasn't focused within 15 sec")
                .until(it -> isFocused(
                        getTypeButtonByLabel(
                                getLocalizationUtils()
                                        .getDictionaryItem(
                                                DisneyDictionaryApi.ResourceKeys.IDENTITY,
                                                MY_DISNEY_CONTINUE_BTN.getText()))));
        getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, MY_DISNEY_CONTINUE_BTN.getText())).click();
    }

    public void proceedToLocalizedPasswordScreen(String email) {
        clickEmailField();
        clickLocalizationEnterNewBtn();
        enterEmail(email);
        keyPressTimes(getClickActionBasedOnLocalizedKeyboardOrientation(), 6, 1);
        clickSelect();
        clickContinueBtn();
    }

    public void pressMenuBackIfPreviouslyUsedEmailScreen() {
        //Persistent bug where Previously-Used Emails screen is displayed.
        if (enterNewBtn.isElementPresent()) {
            clickMenu();
        }
    }

    public ExtendedWebElement getEmailHint() {
        return getDynamicAccessibilityId(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_ENTER_EMAIL_HINT.getText()));
    }

    public void clickTryAgainBtn() {
        clickSelect();
    }
}
