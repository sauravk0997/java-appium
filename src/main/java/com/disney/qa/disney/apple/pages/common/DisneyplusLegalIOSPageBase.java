package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.api.dictionary.DisneyLocalizationUtils;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
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

    @ExtendedFindBy(accessibilityId = "legalConsole")
    private ExtendedWebElement legalPage;

    private ExtendedWebElement legalHeader = findByAccessibilityId(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.LEGAL_TITLE);

    @FindBy(xpath = "//XCUIElementTypeLink")
    private ExtendedWebElement hyperlink;

    public DisneyplusLegalIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
            return legalPage.isPresent() && getBackButton().isPresent();
    }

    public boolean isLegalHeaderPresent() {
        return legalHeader.isElementPresent(SHORT_TIMEOUT);
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

    public void clickAndCollapseLegalScreenSection(SoftAssert sa, String legalSection, DisneyLocalizationUtils localizationObj) {
        LOGGER.info("Validating functions for: {}", legalSection);
        String expandedHeader = localizationObj.getLegalDocumentBody(legalSection).split("\\n")[0];
        expandedHeader = expandedHeader.trim();
        getTypeButtonByLabel(legalSection).click();
        sa.assertTrue(waitUntil(ExpectedConditions.visibilityOfElementLocated(getDynamicAccessibilityId(expandedHeader).getBy()), DEFAULT_EXPLICIT_TIMEOUT), expandedHeader + " Expanded Header is not visible");
        sa.assertTrue(getTypeButtonByLabel(legalSection).getAttribute(IOSUtils.Attributes.VALUE.getAttribute()).equals(EXPANDED), legalSection + " was not expanded");

        getTypeButtonByLabel(legalSection).click();
        sa.assertTrue(waitUntil(ExpectedConditions.invisibilityOfElementLocated(getDynamicAccessibilityId(expandedHeader).getBy()), DEFAULT_EXPLICIT_TIMEOUT), expandedHeader + " Expanded Header is visible");
        sa.assertTrue(getTypeButtonByLabel(legalSection).getAttribute(IOSUtils.Attributes.VALUE.getAttribute()).equals(COLLAPSED), legalSection + " was not collapsed");
    }
}
