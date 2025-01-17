package com.disney.qa.disney.apple.pages.common;

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
    private static final String DNSSMI_TITLE = "“DO NOT SELL OR SHARE MY PERSONAL INFORMATION” AND “TARGETED ADVERTISING” OPT-OUT RIGHTS";

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
        return getSellingLegalContentSwitch().getAttribute("value");
    }

    public ExtendedWebElement getSellingLegalContentSwitch() {
        return consentSwitch;
    }

    public boolean isLegaltextPresent(){
        return legalText.isPresent() && !legalText.getText().isEmpty();
    }

    public boolean isOptOutFormLinkPresent() {
        return customHyperlinkByLabel.format(optOutFormLink).isPresent();
    }

    public void clickOptOutFormLink(){
        clickElementAtLocation(customHyperlinkByLabel.format(optOutFormLink), 10, 95);
    }

    public boolean isOptOutFormLinkOpened(int timeout){
        return optOutFormPage.isPresent(timeout);
    }

    public boolean isIABOptOutListLinkPresent() {
        swipeUp(400);
        return customHyperlinkByLabel.format(iabOptOutListLink).isPresent();
    }

    public void clickIABOptOutListLink(){
        swipeUp(400);
        customHyperlinkByLabel.format(iabOptOutListLink).click();
    }

    public boolean isIABOptOutListLinkPageOpened(){
        return iabOptOutListPage.getText().contains(iabOptOutPageURL);
    }

    public boolean isTargatedAdvertisingOptOutRightsLinkPresent() {
        return learnMoreTextLink.format(learnMoreText).isPresent();
    }

    public void clickTargetedAdvertisingOptOutRightsLink(){
        learnMoreTextLink.format(learnMoreText).click();
    }

    public boolean isTargetedAdvertisingOptOutRightsLinkPageOpened(int timeout){
        return getStaticTextByLabelContains(DNSSMI_TITLE).isPresent(timeout);
    }

    public void clickBackbutton(){
        backBtn.click();
    }

    public void clickSellingSharingTargetedAdvertisingConsentSwitch(){
        getSellingLegalContentSwitch().click();
    }
}
