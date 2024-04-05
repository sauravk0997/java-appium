package com.disney.qa.disney.apple.pages.common;

import org.openqa.selenium.WebDriver;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusAudioSubtitleIOSPageBase extends DisneyPlusApplePageBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`label == \"%s\"`]")
    private ExtendedWebElement languageCell;

    //This will return the third generation element in view hierarchy
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCollectionView[$name='subtitleCollectionView'$]/XCUIElementTypeCell/XCUIElementTypeOther[$name CONTAINS '%s'$]/XCUIElementTypeOther/XCUIElementTypeButton[$name='audioSubtitleCellButton'$]")
    private ExtendedWebElement languageCellCheckmark;

    //This will return the immediate preceding sibling in view hierarchy
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[$name='%s'$]/XCUIElementTypeOther/XCUIElementTypeButton[$name='audioSubtitleCellButton'$]")
    private ExtendedWebElement audioSubtitleCheckBox;

    //FUNCTIONS
    public DisneyPlusAudioSubtitleIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return languageSettingsView.isPresent(SHORT_TIMEOUT);
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
        swipeInContainerTillElementIsPresent(audioCollectionView, element, 5, Direction.UP);
        return element.getAttribute("label").equalsIgnoreCase("checkmark");
    }

    public void chooseAudioLanguage(String language) {
        LOGGER.info("selecting audio language: {}", language);
        ExtendedWebElement element = languageCell.format(language);
        swipeInContainerTillElementIsPresent(audioCollectionView, element, 5, Direction.UP);
        element.click();
    }

    public boolean isLanguagePresent(String language) {
        ExtendedWebElement audioLanguage = languageCell.format(language);
        return audioLanguage.isPresent();
    }

    //Subtitle related functions
    public boolean isSubtitleHeadingPresent() {
        return subtitlesHeading.isElementPresent() && subtitleCollectionView.isElementPresent();
    }

    public void chooseSubtitlesLanguage(String language) {
        ExtendedWebElement element = languageCell.format(language);
        swipeInContainerTillElementIsPresent(subtitleCollectionView, element, 5, Direction.UP);
        subtitleCollectionView.findExtendedWebElement(element.getBy()).click();
    }

    public boolean verifySelectedSubtitleLangIs(String language) {
        LOGGER.info("verifying if selected subtitles language is: {}", language);
        ExtendedWebElement element = languageCellCheckmark.format(language);
        swipeInContainerTillElementIsPresent(subtitleCollectionView, element, 5, Direction.UP);
        return element.getAttribute("label").equalsIgnoreCase("checkmark");
    }
}
