package com.disney.qa.disney.android.pages.common;

import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.Map;
import java.util.TreeMap;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusDOBCollectionPageBase extends DisneyPlusCommonPageBase {

    @FindBy(id = "dateOfBirthTitle")
    private ExtendedWebElement dobTitle;

    @FindBy(id = "actionButton")
    private ExtendedWebElement dobCancelBtn;

    @FindBy(id = "dateOfBirthSubCopy")
    private ExtendedWebElement dobSubCopy;

    @FindBy(id = "dateOfBirthInputTitle")
    private ExtendedWebElement dobInputLabel;

    @FindBy(id = "editFieldEditText")
    private ExtendedWebElement dobEditText;

    @FindBy(id = "TextView")
    private ExtendedWebElement dobTextView;

    @FindBy(id = "standardButtonContainer")
    private ExtendedWebElement dobContinueBtn;

    @FindBy(id = "dobPageOverlay")
    private ExtendedWebElement dobPageOverlay;

    @FindBy(id = "titleTextSwitcher")
    private ExtendedWebElement titleTextSwitcher;

    @FindBy(xpath = "//*[@resource-id='com.disney.disneyplus:id/titleTextSwitcher']/*[@class='android.widget.TextView']")
    private ExtendedWebElement confirmCTA;

    @FindBy(id = "accountHolderEmail")
    private ExtendedWebElement accountHolderEmail;

    @FindBy(id = "dobDisclaimerText")
    private ExtendedWebElement dobDisclaimerText;

    @FindBy(xpath = "//*[contains(@text, 'Help')]")
    private ExtendedWebElement helpCenterLink;

    public boolean isDOBTitleDisplayed() {
        return dobTitle.isElementPresent();
    }

    public boolean isDOBCancelBtnDisplayed() {
        return dobCancelBtn.isElementPresent();
    }

    public boolean isDOBSubCopyDisplayed() {
        return dobSubCopy.isElementPresent();
    }

    public boolean isDOBInputLabelDisplayed() {
        return dobInputLabel.isElementPresent();
    }

    public boolean isDOBEditTextDisplayed() {
        return dobEditText.isElementPresent();
    }

    public boolean isDOBTextViewDisplayed() {
        return dobTextView.isElementPresent();
    }

    public boolean isDOBContinueBtnDisplayed() {
        return dobContinueBtn.isElementPresent();
    }

    public boolean isAccountHolderEmailDisplayed() {
        return accountHolderEmail.isElementPresent();
    }

    public String getAccountHolderEmailText() {
        return getElementText(accountHolderEmail);
    }

    public boolean isDOBDisclaimerTextDisplayed() {
        return dobDisclaimerText.isElementPresent();
    }

    public String getDateOfBirthDisclaimerText() {
        return getElementText(dobDisclaimerText);
    }

    public void clickDOBCancelBtn() { dobCancelBtn.click(); }

    public void clickDOBContinueBtn() { dobContinueBtn.click(); }

    public void clickDOBEditText() { dobEditText.click(); }

    public String getDOBTitleText() { return dobTitle.getText(); }

    public String getDOBCancelBtnText() { return dobCancelBtn.getText(); }

    public String getDOBInputLabelText() { return dobInputLabel.getText(); }

    public String getDOBSubCopyText() { return dobSubCopy.getText(); }

    public String getDOBContinueBtnText() { return confirmCTA.getText(); }

    public void clickDisneyPlusHelpCenterLink() { helpCenterLink.click(); }

    public void submitDOBValue(String dob) {
        editTextByClass.click();
        dobEditText.type(dob);
        new AndroidUtilsExtended().hideKeyboard();
        clickDOBContinueBtn();
    }

    public Map<Integer, Boolean> isNonEntitledDOBCollectionDisplayed() {
        DisneyPlusDOBCollectionPageBase dobCollectionPageBase = initPage(DisneyPlusDOBCollectionPageBase.class);
        Map<Integer, Boolean> dobCollectionStatus = new TreeMap<>();

        dobCollectionStatus.put(0, dobCollectionPageBase.isDOBTitleDisplayed());
        LOGGER.info("Step 1 Result: '{}'", dobCollectionPageBase.isDOBTitleDisplayed());

        dobCollectionStatus.put(1, dobCollectionPageBase.isDOBSubCopyDisplayed());
        LOGGER.info("Step 2 Result: '{}'", dobCollectionPageBase.isDOBSubCopyDisplayed());

        dobCollectionStatus.put(2, dobCollectionPageBase.isDOBInputLabelDisplayed());
        LOGGER.info("Step 3 Result: '{}'", dobCollectionPageBase.isDOBInputLabelDisplayed());

        dobCollectionStatus.put(3, dobCollectionPageBase.isDOBEditTextDisplayed());
        LOGGER.info("Step 4 Result: '{}'", dobCollectionPageBase.isDOBEditTextDisplayed());

        dobCollectionStatus.put(4, dobCollectionPageBase.isDOBContinueBtnDisplayed());
        LOGGER.info("Step 5 Result: '{}'", dobCollectionPageBase.isDOBContinueBtnDisplayed());

        dobCollectionPageBase.clickDOBEditText();
        dobCollectionStatus.put(5, new AndroidUtilsExtended().isKeyboardShown());
        LOGGER.info("Step 6 Result: '{}'", new AndroidUtilsExtended().isKeyboardShown());

        return dobCollectionStatus;
    }

    public DisneyPlusDOBCollectionPageBase(WebDriver driver) {
        super(driver);
    }
}
