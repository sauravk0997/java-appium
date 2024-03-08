package com.disney.qa.disney.apple.pages.common;

import com.zebrunner.carina.utils.mobile.IMobileUtils;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyplusSellingLegalIOSPageBase extends DisneyPlusApplePageBase {

    private static String seelingSharingLegalPageHeader = "Selling, Sharing, Targeted Advertising";
    private static String optOutFormLink = "opt-out form";
    private static String iabOptOutListLink = "IAB opt-out list";
    private static String learnMoreText = "Do Not Sell or Share My Personal Information\\\" and \\\"Targeted Advertising\\\" Opt-Out Rights";
    private static String iabOptOutPageURL = "iabprivacy.com";

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

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`label == \"Privacy Web Form\"`]")
    protected ExtendedWebElement optOutFormPage;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`label == \"Address\"`]")
    protected ExtendedWebElement iabOptOutListPage;

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

    public void clickOptOutFormLink(){
        customHyperlinkByLabel.format(optOutFormLink).click();
    }

    public boolean isOptOutFormLinkOpened(int timeout){
        return optOutFormPage.isPresent(timeout);
    }

    public boolean isIABOptOutListLinkLinkPresent() {
        scrollInSellingSharingLegalPage(1);
        return customHyperlinkByLabel.format(iabOptOutListLink).isPresent();
    }

    public void scrollInSellingSharingLegalPage(int times){
        swipeInContainer(legalText, IMobileUtils.Direction.UP,times,500);
    }

    public void clickIABOptOutListLink(){
        customHyperlinkByLabel.format(iabOptOutListLink).click();
    }

    public boolean isIABOptOutListLinkPageOpened(){
        return iabOptOutListPage.getText().contains(iabOptOutPageURL);
    }

    public boolean isTargatedAdvertisingOptOutRightsLinkPresent() {
        return learnMoreTextLink.format(learnMoreText).isPresent();
    }

    public void clickTargatedAdvertisingOptOutRightsLink(){
        learnMoreTextLink.format(learnMoreText).click();
    }

    public boolean isTargatedAdvertisingOptOutRightsLinkPageOpened(int timeout){
        return dynamicBtnFindByLabelContains.format(learnMoreText.toUpperCase()).isPresent(timeout);
    }

    public void clickBackbutton(){
        backBtn.click();
    }

    public void clickSellingSharingTargatedAdvertisingConsentSwitch(){
        consentSwitch.click();
    }
}
