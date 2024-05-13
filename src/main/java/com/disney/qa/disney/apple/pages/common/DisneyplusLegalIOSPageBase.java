package com.disney.qa.disney.apple.pages.common;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyplusLegalIOSPageBase extends DisneyPlusApplePageBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private ExtendedWebElement legalHeader = findByAccessibilityId(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.LEGAL_TITLE);

    private ExtendedWebElement backupHeader = findByFallbackAccessibilityId(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.LEGAL_TITLE);

    @FindBy(xpath = "//XCUIElementTypeLink")
    private ExtendedWebElement hyperlink;

    public DisneyplusLegalIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        if(getDictionary().isSelectedLanguageSupported()) {
            return legalHeader.isElementPresent() && getNavBackArrow().isElementPresent();
        } else {
            return backupHeader.isElementPresent() && getNavBackArrow().isElementPresent();
        }
    }

    public boolean isLegalHeadersPresent(String header) {
        return dynamicBtnFindByLabel.format(header).isElementPresent();
    }

    public String getLegalText() {
        return cell.getText();
    }

    public boolean isHyperlinkPresent() {
        return hyperlink.isPresent();
    }

    public void clickHyperlink() {
        int containerDepth = cell.getLocation().getY() + cell.getSize().getHeight();
        var maxSwipes = 20;
        while(hyperlink.getLocation().getY() > containerDepth && maxSwipes > 0) {
            LOGGER.info("Hyperlink is not within visible range. Swiping container up. Attempts remaining {}/20", maxSwipes);
            swipeInContainer(cell, Direction.UP, 1, 500);
            maxSwipes--;
        }
        hyperlink.click();
    }
}
