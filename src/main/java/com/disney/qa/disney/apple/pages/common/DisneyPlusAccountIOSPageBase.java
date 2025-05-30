package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.pojos.*;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.DISNEY_PLUS_PREMIUM;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusApplePageBase.class)
public class DisneyPlusAccountIOSPageBase extends DisneyPlusApplePageBase{

    private static final String SUBSCRIPTION_MESSAGE = "Some account management features are only available " +
            "via the website. Create a Disney+ account and more at disneyplus.com/next";

    private ExtendedWebElement accountDetailsSection = getDynamicAccessibilityId(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.NAV_ACCOUNT.getText()));

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton/XCUIElementTypeImage[1]")
    private ExtendedWebElement editEmailButton;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton/XCUIElementTypeImage[2]")
    private ExtendedWebElement editPasswordButton;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`name == \"subscriptionChange\"`]/" +
            "**/XCUIElementTypeButton[2]")
    private ExtendedWebElement subscriptionMessage;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeToggle[2]")
    private ExtendedWebElement restrictedProfileToggle;
    @ExtendedFindBy(accessibilityId = "manageMyAccountCell")
    private ExtendedWebElement manageWithMyDisney;
    @ExtendedFindBy(accessibilityId = "restrictProfileCreation")
    private ExtendedWebElement restrictProfileCreation;
    @ExtendedFindBy(accessibilityId = "subscriptionChange")
    private ExtendedWebElement subscriptionChange;
    @ExtendedFindBy(accessibilityId = "manageParentalControls")
    private ExtendedWebElement manageParentalControls;
    @ExtendedFindBy(accessibilityId = "manageDevices")
    private ExtendedWebElement manageDevices;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == 'Continue'`][2]")
    private ExtendedWebElement popupContinueButton;

    private final ExtendedWebElement accessAndSecurityText =
            getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                    DictionaryKeys.ACCESS_SECURITY_HEADER.getText()));
    private final ExtendedWebElement manageDevicesText =
            getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                    DictionaryKeys.DEVICE_MANAGEMENT_BUTTON_LABEL.getText()));

    public ExtendedWebElement getSubscriptionMessage() {
        return subscriptionMessage;
    }

    public ExtendedWebElement getManageMyAccountCell() {
        return manageWithMyDisney;
    }

    public String getManageDevicesText() {
        return manageDevices.getText();
    }

    public boolean isSubscriptionMessageDisplayed() {
        return getSubscriptionMessage().getText().equals(SUBSCRIPTION_MESSAGE);
    }

    public boolean isPremiumSubscriptionTitlePresent() {
        return getTypeButtonByLabel(DISNEY_PLUS_PREMIUM.getValue()).isPresent();
    }

    public ExtendedWebElement getRestrictProfileCreationContainer() {
        return restrictProfileCreation;
    }


    public DisneyPlusAccountIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return accountDetailsSection.isElementPresent();
    }

    public void waitForAccountPageToOpen() {
        fluentWait(getDriver(), SIXTY_SEC_TIMEOUT, THREE_SEC_TIMEOUT, "Account page did not open")
                .until(it -> accountDetailsSection.isPresent(THREE_SEC_TIMEOUT));
    }

    public boolean isChangeLinkPresent(String text) {
        return getStaticTextByLabelContains(text).isPresent();
    }

    public boolean isRestrictProfilesContainerPresent() {
        return getRestrictProfileCreationContainer().isElementPresent();
    }

    public ExtendedWebElement getEditProfileLink() {
        return manageParentalControls;
    }

    public boolean isEditProfilesLinkPresent() {
        return getEditProfileLink().isElementPresent();
    }

    public void tapEditProfilesLink() {
        int heightValue;
        int tapOffset;

        Dimension screenSize = getDriver().manage().window().getSize();
        int screenWidth = screenSize.getWidth();

        if (screenWidth < 768) {
            heightValue = 40;
            tapOffset = 30;
        } else {
            heightValue = 60;
            tapOffset = 55;
        }

        ExtendedWebElement element = getEditProfileLink();
        Dimension dimension = element.getSize();
        Point location = element.getLocation();

        if (dimension.getHeight() > heightValue) {
            tap(location.getX() + tapOffset, location.getY() + dimension.getHeight() * 2 / 3, 2);
        } else {
            element.click();
        }
    }

    public boolean isRestrictProfileCreationValueExpected(String expectedValue) {
        return getRestrictProfileCreationContainer()
                .getAttribute(Attributes.VALUE.getAttribute()).equals(expectedValue);
    }

    public void toggleRestrictProfileCreation() {
            restrictedProfileToggle.click();
    }

    public void clickSubscriptionCell() {
        subscriptionChange.click();
    }

    public void tapEditEmailButton() {
        editEmailButton.click();
    }

    public boolean waitForManageMyDisneyAccountOverlayToOpen(UnifiedAccount account) {
        try {
            return fluentWait(getDriver(), FIFTEEN_SEC_TIMEOUT, THREE_SEC_TIMEOUT, "Time out exception occurred")
                    .until(it -> getStaticTextByLabelContains(account.getEmail()).isPresent(THREE_SEC_TIMEOUT));
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isSubscriptionCellPresent() {
        return subscriptionChange.isElementPresent();
    }

    public boolean isAccessAndSecurityTextPresent() {
        return accessAndSecurityText.isElementPresent();
    }

    public boolean isAccountManagementLinkPresent() {
        return getAccountManagementTextElement().isElementPresent();
    }

    public boolean isAccountManagementFAQWebViewDisplayed() {
        String acctMgmtFaqText = "Account Management FAQ";
        return staticTextByLabel.format(acctMgmtFaqText).isPresent();
    }

    public ExtendedWebElement getAccountManagementTextElement() {
        String dictValOfAccountManagement = getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.COMMUNICATION_SETTINGS.getText());
        String editProfileText = dictValOfAccountManagement.
                replaceAll("\\([^()]*\\)", "").replaceAll("[\\[\\]]","");
        return staticTextByLabel.format(editProfileText);
    }

    public boolean isAccountManagementTextPresent() {
        return getAccountManagementTextElement().isElementPresent();
    }

    public ExtendedWebElement getEditPasswordButton() {
        return editPasswordButton;
    }

    public void tapAccountManagementLink() {
        ExtendedWebElement element = getAccountManagementTextElement();
        int maxHeight = getDriver().manage().window().getSize().getHeight();
        int yCoordinate = element.getLocation().getY();
        if (maxHeight- yCoordinate < 150) {
            swipeUp(2, 1000);
        }
        element.click();
    }

    public void clickManageWithMyDisneyButton() {
        getManageMyAccountCell().click();
    }

    public ExtendedWebElement getPopupContinueButton() {
        return popupContinueButton;
    }
}
