package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.MobileUtilsExtended;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.qaprosoft.carina.core.foundation.utils.mobile.IMobileUtils;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyplusLegalIOSPageBase extends DisneyPlusApplePageBase {

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
            return legalHeader.isElementPresent() && getBackArrow().isElementPresent();
        } else {
            return backupHeader.isElementPresent() && getBackArrow().isElementPresent();
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
            new MobileUtilsExtended().swipeInContainer(cell, IMobileUtils.Direction.UP, 1, 500);
            maxSwipes--;
        }
        hyperlink.click();
    }
}
