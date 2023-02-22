package com.disney.qa.disney.android.pages.tv.globalnav;

import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.common.utils.androidtv.AndroidTVUtils;
import com.disney.qa.disney.android.pages.common.DisneyPlusMoreMenuPageBase;
import com.disney.qa.disney.android.pages.tv.utility.navhelper.NavHelper;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import io.appium.java_client.android.nativekey.AndroidKey;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusAndroidTVSettingsPageBase extends DisneyPlusMoreMenuPageBase {

    @FindBy(id = "optionsListRowRoot")
    List<ExtendedWebElement> settingsOptions;

    @FindBy(id = "app_settings_title")
    private ExtendedWebElement appSettingsTitle;

    @FindBy(id = "accountTitle")
    private ExtendedWebElement accountTitle;

    @FindBy(id = "helpTitle")
    private ExtendedWebElement helpTitle;

    @FindBy(xpath = "//*[@resource-id = 'com.disney.disneyplus:id/recyclerView']/*")
    private ExtendedWebElement accountSettingsOptions;

    @FindBy(id = "changeEmailRoot")
    private ExtendedWebElement changeEmailTitle;

    @FindBy(id = "logoutAllCheckbox")
    private ExtendedWebElement logoutAllCheckBox;

    @FindBy(id = "saveLoadingButton")
    private ExtendedWebElement saveBtn;

    @FindBy(id = "logOutAllTitle")
    private ExtendedWebElement logOutAllTitle;

    @FindBy(id = "logOutCtaButton")
    private ExtendedWebElement logOutAllBtn;

    @FindBy(id = "subscription_title")
    private ExtendedWebElement subscriptionTitle;

    @FindBy(id = "subscription_copy")
    private ExtendedWebElement subscriptionCopy;

    /**
     * Looks for the button title element matching the passed in string
     * then fetches its parent (which is the actual button element)
     */
    @FindBy(xpath = "//*[contains(@resource-id, ':id/title') and @text='%s']/..")
    private ExtendedWebElement settingsOption;

    public ExtendedWebElement getSubscriptionTitle() {
        return subscriptionTitle;
    }

    public ExtendedWebElement getSubscriptionCopy() {
        return subscriptionCopy;
    }

    public ExtendedWebElement getSettingsOption(String settingsButton) {
        return settingsOption.format(settingsButton);
    }

    public void selectSettingsOption(String button, AndroidKey direction){
        AndroidTVUtils utils = new AndroidTVUtils(getDriver());
        if (utils.isFocused(settingsOption.format(button))) {
            UniversalUtils.captureAndUpload(getCastedDriver());
            utils.pressSelect();
            return;
        }
        NavHelper navHelper = new NavHelper(getCastedDriver());
        navHelper.keyUntilElementFocused(() -> settingsOption.format(button),  direction);
        UniversalUtils.captureAndUpload(getCastedDriver());
        utils.pressSelect();
    }

    public DisneyPlusAndroidTVSettingsPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        boolean isPresent = title.isElementPresent(SHORT_TIMEOUT);
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }

    public boolean isAppSettingsOpened() {
        boolean isPresent = appSettingsTitle.isElementPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }

    public boolean isSettingsAccountOpened() {
        boolean isPresent = accountTitle.isElementPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }

    public boolean isHelpOpen() {
        boolean isPresent = helpTitle.isElementPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }

    public enum Settings {
        PROFILES("Profiles"),
        APP_SETTINGS("App Settings"),
        ACCOUNT("Account"),
        HELP("Help"),
        LEGAL("Legal"),
        LOGOUT("Log Out");

        private String settingsMenu;

        Settings(String settings) {
            this.settingsMenu = settings;
        }

        public String getText() {
            return this.settingsMenu;
        }
    }

    public List<ExtendedWebElement> getAllSettings() {
        return settingsOptions;
    }

    public boolean isSettingsOptionFocused(int optionIndex, List<ExtendedWebElement> settings) {
        return new AndroidTVUtils(getDriver()).isElementFocused(settings.get(optionIndex));
    }

    public void selectSettingsBasedOnIndex(int optionIndex, boolean isAccount) {
        AndroidTVUtils androidTVUtils = new AndroidTVUtils(getDriver());
        while (!isSettingsOptionFocused(optionIndex, isAccount ? findExtendedWebElements(accountSettingsOptions.getBy()) : settingsOptions)) {
            androidTVUtils.pressDown();
        }
        androidTVUtils.pressSelect();
    }

    public void selectGenericContentElement(String text) {
        clickOnContentDescContains(text);
        UniversalUtils.captureAndUpload(getCastedDriver());
    }

    public boolean isChangeEmailScreenOpened() {
        return changeEmailTitle.isElementPresent();
    }

    public void clickLogoutAllCheckBox() {
        logoutAllCheckBox.click();
    }

    public void clickSaveBtn() {
        saveBtn.click();
    }

    public boolean isLogoutAllPageOpened() {
        return logOutAllTitle.isElementPresent();
    }

    public void clickLogOutAll() {
        logOutAllBtn.click();
    }
}
