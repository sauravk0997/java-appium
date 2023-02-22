package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.common.utils.IOSUtils;
import com.qaprosoft.carina.core.foundation.utils.mobile.IMobileUtils;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusAudioSubtitleIOSPageBase extends DisneyPlusApplePageBase {

    //LOCATORS

    @ExtendedFindBy(accessibilityId = "languageSettingsView")
    private ExtendedWebElement languageSettingsView;

    @ExtendedFindBy(accessibilityId = "Audio")
    private ExtendedWebElement audioHeading;

    @ExtendedFindBy(accessibilityId = "Subtitles")
    private ExtendedWebElement subtitlesHeading;

    @ExtendedFindBy(accessibilityId = "audioCollectionView")
    private ExtendedWebElement audioCollectionView;

    @ExtendedFindBy(accessibilityId = "subtitleCollectionView")
    private ExtendedWebElement subtitleCollectionView;

    @ExtendedFindBy(accessibilityId = "closeInactive")
    private ExtendedWebElement closeButton;

    @FindBy(xpath = "//XCUIElementTypeCell[@name=\"%s\"]")
    private ExtendedWebElement languageCell;

    //This will return the third generation element in view hierarchy
    @FindBy(xpath = "//XCUIElementTypeCell[@name=\"%s\"]/*/*/*")
    private ExtendedWebElement languageCellCheckmark;

    //This will return the immediate preceding sibling in view hierarchy
    @FindBy(xpath = "//XCUIElementTypeStaticText[@name=\"%s\"]/preceding-sibling::*")
    private ExtendedWebElement audioSubtitleCheckBox;

    //FUNCTIONS
    public DisneyPlusAudioSubtitleIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return languageSettingsView.isPresent();
    }

    public void tapCloseButton() {
        closeButton.click();
    }

    //Audio related functions
    public boolean isAudioHeadingPresent() {
        return audioHeading.isElementPresent() && audioCollectionView.isElementPresent();
    }

    public boolean verifySelectedAudioIs(String language) {
        LOGGER.info("verifying if selected audio language is: {}", language);
        ExtendedWebElement element = audioSubtitleCheckBox.format(language);
        new IOSUtils().swipeInContainerTillElementIsPresent(audioCollectionView, element, 5, IMobileUtils.Direction.UP);
        return element.getAttribute("label").equalsIgnoreCase("checkmark");
    }

    public void chooseAudioLanguage(String language) {
        LOGGER.info("selecting audio language: {}", language);
        ExtendedWebElement element = languageCell.format(language);
        new IOSUtils().swipeInContainerTillElementIsPresent(audioCollectionView, element, 5, IMobileUtils.Direction.UP);
        element.click();
    }

    //Subtitle related functions
    public boolean isSubtitleHeadingPresent() {
        return subtitlesHeading.isElementPresent() && subtitleCollectionView.isElementPresent();
    }

    public void chooseSubtitlesLanguage(String language) {
        ExtendedWebElement element = languageCell.format(language);
        new IOSUtils().swipeInContainerTillElementIsPresent(subtitleCollectionView, element, 5, IMobileUtils.Direction.UP);
        element.click();
    }

    public boolean verifySelectedSubtitleLangIs(String language) {
        LOGGER.info("verifying if selected subtitles language is: {}", language);
        ExtendedWebElement element = languageCellCheckmark.format(language);
        return element.getAttribute("label").equalsIgnoreCase("checkmark");
    }
}
