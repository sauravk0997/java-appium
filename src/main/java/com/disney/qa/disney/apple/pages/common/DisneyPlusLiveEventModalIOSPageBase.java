package com.disney.qa.disney.apple.pages.common;

import org.openqa.selenium.WebDriver;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusLiveEventModalIOSPageBase extends DisneyPlusApplePageBase {

    public DisneyPlusLiveEventModalIOSPageBase(WebDriver driver) {
        super(driver);
    }

    protected ExtendedWebElement watchLiveButton = dynamicBtnFindByLabel.format(
            getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY,
                    DictionaryKeys.BTN_PLAYBACK_MODAL_LIVE.getText()));

    protected ExtendedWebElement watchFromStartButton = dynamicBtnFindByLabel.format(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_PLAYBACK_MODAL_BEGINNING.getText()));

    @ExtendedFindBy(accessibilityId = "titleLabel")
    private ExtendedWebElement titleLabel;

    @ExtendedFindBy(accessibilityId = "subtitleLabel")
    private ExtendedWebElement subtitleLabel;

    @ExtendedFindBy(accessibilityId = "thumbnailImageView")
    private ExtendedWebElement thumbnailImageView;

    @Override
    public boolean isOpened() {
        return watchLiveButton.isElementPresent();
    }

    public ExtendedWebElement getDetailsButton() {
        return dynamicBtnFindByLabel.format(
                getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.BTN_DETAILS.getText()));
    }

    public ExtendedWebElement getWatchLiveButton() {
        return watchLiveButton;
    }

    public ExtendedWebElement getSubtitleLabel() {
        return subtitleLabel;
    }
}
