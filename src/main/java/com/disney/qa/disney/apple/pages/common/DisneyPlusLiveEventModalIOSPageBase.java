package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusLiveEventModalIOSPageBase extends DisneyPlusApplePageBase {

    public DisneyPlusLiveEventModalIOSPageBase(WebDriver driver) {
        super(driver);
    }

    protected ExtendedWebElement watchLiveButton = dynamicBtnFindByLabel.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.BTN_PLAYBACK_MODAL_LIVE.getText()));

    protected ExtendedWebElement watchFromStartButton = dynamicBtnFindByLabel.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.BTN_PLAYBACK_MODAL_BEGINNING.getText()));

    @Override
    public boolean isOpened() {
        return watchLiveButton.isElementPresent();
    }

    public ExtendedWebElement getWatchLiveButton() {
        return watchLiveButton;
    }

    public ExtendedWebElement getWatchFromStartButton() {
        return watchFromStartButton;
    }
}
