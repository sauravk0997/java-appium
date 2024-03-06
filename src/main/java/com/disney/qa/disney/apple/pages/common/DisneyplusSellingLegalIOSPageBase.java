package com.disney.qa.disney.apple.pages.common;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyplusSellingLegalIOSPageBase extends DisneyPlusApplePageBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static String seelingSharingLegalPageHeader = "Selling, Sharing, Targeted Advertising";
    private static String optOutFormLink = "opt-out form";
    private static String iabOptOutListLink = "IAB opt-out list";
    private static String learnMoreText = "Do Not Sell or Share My Personal Information\\\" and \\\"Targeted Advertising\\\" Opt-Out Rights";

    @ExtendedFindBy(accessibilityId = "purposeDetailsConsentLabel")
    private ExtendedWebElement pageHeader;

    @FindBy(id = "Back")
    protected ExtendedWebElement backBtn;

    @ExtendedFindBy(accessibilityId = "purposeDetailsConsentSwitch")
    private ExtendedWebElement consentSwitch;

    @ExtendedFindBy(accessibilityId = "purposeChildDetailsDescriptionTextView")
    private ExtendedWebElement legalText;


    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeLink[`label CONTAINS \"%s\"`]")
    protected ExtendedWebElement learnMoreTextLink;

    public DisneyplusSellingLegalIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return pageHeader.isPresent();
    }

    public boolean isSellingSharingLegalHeaderPresent(){
        return pageHeader.getAttribute("label").trim().equals(seelingSharingLegalPageHeader);
    }

    public boolean isBackArrowPresent(){ return backBtn.isPresent(); }

    public String getValueOfConsentSwitch(){
        return consentSwitch.getAttribute("value");
    }

    public boolean isLegaltextPresent(){
        return legalText.isPresent() && !legalText.getText().isEmpty();
    }

    public boolean isOptOutFormLinkPresent() {
        return customHyperlinkByLabel.format(optOutFormLink).isPresent();
    }

    public boolean isIABOptOutListLinkLinkPresent() {
        scrollDown();
        return customHyperlinkByLabel.format(iabOptOutListLink).isPresent();
    }

    public boolean isTargatedAdvertisingOptOutRightsLinkPresent() {
        return learnMoreTextLink.format(learnMoreText).isPresent();
    }

    public void clickBackbutton(){
        backBtn.click();
    }

    public void clickSellingSharingTargatedAdvertisingConsentSwitch(){
        consentSwitch.click();
    }
}
