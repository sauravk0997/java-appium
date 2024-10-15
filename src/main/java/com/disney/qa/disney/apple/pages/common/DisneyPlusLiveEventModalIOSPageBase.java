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

    protected ExtendedWebElement watchLiveButton = dynamicBtnFindByLabel.format(iapiHelper.getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_PLAYBACK_MODAL_LIVE.getText()));

    protected ExtendedWebElement watchFromStartButton = dynamicBtnFindByLabel.format(iapiHelper.getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_PLAYBACK_MODAL_BEGINNING.getText()));

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

    public ExtendedWebElement getWatchLiveButton() {
        return watchLiveButton;
    }

    public ExtendedWebElement getWatchFromStartButton() {
        return watchFromStartButton;
    }

    public boolean isTitleLabelPresent() { return titleLabel.isPresent(); }

    public boolean isSubtitleLabelPresent() { return subtitleLabel.isPresent(); }

    @Override
    public boolean isThumbnailViewPresent() { return thumbnailImageView.isPresent(); }

    /**
     * Below are QA env specific methods for DWTS Anthology.
     * To be deprecated when DWTS Test Streams no longer available on QA env (QAA-12244).
     */
    public ExtendedWebElement getQAWatchLiveButton() { return getTypeButtonContainsLabel("WATCH LIVE"); }

    public ExtendedWebElement getQAWatchFromStartButton() { return getTypeButtonContainsLabel("WATCH FROM START"); }

    public boolean isQASubtitleLabelPresent() { return getStaticTextByNameContains("subtitleLabel").isPresent(); }

    public boolean isQAThumbnailViewPresent() { return getTypeOtherContainsName("thumbnailView").isPresent(); }
}
