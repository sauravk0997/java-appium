package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.apple.pages.common.DisneyPlusMoreMenuIOSPageBase;
import com.disney.qa.disney.apple.pages.phone.DisneyPlusMoreMenuIOSPage;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusMoreMenuIOSPageBase.class)
public class DisneyPlusTabletMoreMenuIOSPage extends DisneyPlusMoreMenuIOSPage {

	public DisneyPlusTabletMoreMenuIOSPage(WebDriver driver){
		super(driver);
	}

	@Override
	public boolean isDeviceStorageCorrectlyDisplayed() {
		ExtendedWebElement storageText = getDynamicXpathContainsName(String.format("iPad %s", getDictionary().getValuesBetweenPlaceholders(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DEVICE_STORAGE.getText())).get(0)));
		if(storageText.isElementPresent()) {
			return storageText.getText().contains(getDictionary().getValuesBetweenPlaceholders(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DEVICE_STORAGE_APP.getText())).get(0))
					&& storageText.getText().contains(getDictionary().getValuesBetweenPlaceholders(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DEVICE_STORAGE_FREE.getText())).get(0))
					&& storageText.getText().contains(getDictionary().getValuesBetweenPlaceholders(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DEVICE_STORAGE_USED.getText())).get(0));
		} else {
			return false;
		}
	}
}
