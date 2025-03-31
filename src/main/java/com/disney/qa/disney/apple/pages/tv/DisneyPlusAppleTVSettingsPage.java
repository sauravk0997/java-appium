package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusMoreMenuIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.APP_SETTINGS_SUBSCRIPTIONS_LABEL;
import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.EM_SUBSCRIPTION_DETAIL_SUBCOPY;
import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.EM_SUBSCRIPTION_DETAIL_TITLE;

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

    public void clickSubscriptionsCell() {
        String cellName = getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION,
                APP_SETTINGS_SUBSCRIPTIONS_LABEL.getText());
        moveDownUntilElementIsFocused(settingsCellItem.format(cellName), 6);
        clickSelect();
    }

    public boolean isExtraMemberSubscriptionDetailTitlePresent() {
        return getStaticCellByLabel(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                EM_SUBSCRIPTION_DETAIL_TITLE.getText())).isPresent();
    }

    public boolean isExtraMemberSubscriptionDetailSubCopyPresent() {
        return getStaticCellByLabel(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                EM_SUBSCRIPTION_DETAIL_SUBCOPY.getText())).isPresent();
    }
}
