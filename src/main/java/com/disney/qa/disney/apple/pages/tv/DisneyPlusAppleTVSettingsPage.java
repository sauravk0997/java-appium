package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusMoreMenuIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.APP_SETTINGS_SUBSCRIPTIONS_LABEL;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusHomeIOSPageBase.class)
public class DisneyPlusAppleTVSettingsPage extends DisneyPlusMoreMenuIOSPageBase {
    public DisneyPlusAppleTVSettingsPage(WebDriver driver) {super(driver);}

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`name == 'accountTab'`]")
    private ExtendedWebElement accountBtn;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`name == 'logOutAllDevicesCell'`]")
    private ExtendedWebElement  logOutAllDevicesBtn;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[$type='XCUIElementTypeStaticText' AND label='%s'$]")
    private ExtendedWebElement  settingsCellItem;

    @ExtendedFindBy(accessibilityId = "accountView")
    ExtendedWebElement accountView;

    @Override
    public boolean isOpened() { return accountView.isElementPresent(); }

    public void clickAccountBtn() { accountBtn.click(); }

    public void clickLogOutAllDevicesBtn() { logOutAllDevicesBtn.click(); }

    public ExtendedWebElement getSubscriptionsCell() {
        String cellName = getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION,
                APP_SETTINGS_SUBSCRIPTIONS_LABEL.getText());
        return settingsCellItem.format(cellName);
    }
}
