package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusLiveEventModalIOSPageBase extends DisneyPlusApplePageBase {

    public DisneyPlusLiveEventModalIOSPageBase(WebDriver driver) {
        super(driver);
    }

    protected ExtendedWebElement watchLiveButton = dynamicBtnFindByLabel.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_PLAYBACK_MODAL_LIVE.getText()));

    protected ExtendedWebElement watchFromStartButton = dynamicBtnFindByLabel.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_PLAYBACK_MODAL_BEGINNING.getText()));

    protected ExtendedWebElement detailsButton = dynamicBtnFindByLabel.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_PLAYBACK_MODAL_DETAILS.getText()));

    @ExtendedFindBy(accessibilityId = "titleLabel")
    private ExtendedWebElement titleLabel;

    @ExtendedFindBy(accessibilityId = "subtitleLabel")
    private ExtendedWebElement subtitleLabel;

    @ExtendedFindBy(accessibilityId = "channelLogo")
    private ExtendedWebElement channelLogo;

    @ExtendedFindBy(accessibilityId = "thumbnailImageView")
    private ExtendedWebElement thumbnailImageView;

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

    public ExtendedWebElement getDetailsButton() { return detailsButton; }

    public boolean isTitleLabelPresent() { return titleLabel.isPresent(); }

    public boolean isSubtitleLabelPresent() { return subtitleLabel.isPresent(); }

    public boolean isChannelLogoPresent() { return channelLogo.isPresent(); }

    public boolean isThumbnailViewPresent() { return thumbnailImageView.isPresent(); }

}
