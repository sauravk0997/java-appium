package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.common.utils.IOSUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyplusLegalIOSPageBase extends DisneyPlusApplePageBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String EXPANDED = "Expanded";
    private static final String COLLAPSED = "Collapsed";

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

    public boolean isLegalHeaderPresent() {
        return legalHeader.isElementPresent(SHORT_TIMEOUT);
    }

    public boolean isLegalHeadersPresent(String header) {
        return staticTextByName.format(header).isElementPresent();
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

    public void clickAndCollapseLegalScreenSection(SoftAssert sa, String legalSection) {
        LOGGER.info("Validating functions for: {}", legalSection);
        getStaticTextByName(legalSection).click();
        waitForPresenceOfAnElement(getStaticTextByName(legalSection));
        sa.assertTrue(getStaticTextByName(legalSection).getAttribute(IOSUtils.Attributes.VALUE.getAttribute()).equals(EXPANDED),
                legalSection + " was not expanded");

        getStaticTextByName(legalSection).click();
        waitForPresenceOfAnElement(getStaticTextByName(legalSection));
        sa.assertTrue(getStaticTextByName(legalSection).getAttribute(IOSUtils.Attributes.VALUE.getAttribute()).equals(COLLAPSED),
                legalSection + " was not collapsed");
    }
}
