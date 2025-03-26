package com.disney.qa.disney.apple.pages.common;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusOneTrustIOSPageBase extends DisneyPlusApplePageBase {

    public DisneyPlusOneTrustIOSPageBase(WebDriver driver) {
        super(driver);
    }

    private static String usPrivacyRights = "Your US State Privacy Rights";
    private static String californiaPrivacyRights = "Your California Privacy Rights";
    private static String pageTitleText = "Notice of Right to Opt Out of Sale/Sharing";
    private static String consentCellGroupNameTitle = "Selling, Sharing, Targeted Advertising";

    @ExtendedFindBy(accessibilityId = "Close")
    private ExtendedWebElement closeButton;
    @ExtendedFindBy(accessibilityId = "TWDC_Logo_Sheet_No_Mickey_Outlines-02.png")
    private ExtendedWebElement logo;
    @ExtendedFindBy(accessibilityId = "pcTitle")
    private ExtendedWebElement pageTitle;
    @ExtendedFindBy(accessibilityId = "pcDescription")
    private ExtendedWebElement legalText;
    @ExtendedFindBy(accessibilityId = "pcEditableConsentCellConsentSwitch")
    private ExtendedWebElement consentSwitch;
    @ExtendedFindBy(accessibilityId = "pcEditableConsentCellGroupName")
    private ExtendedWebElement consentSwitchTitle;
    @ExtendedFindBy(accessibilityId = "pcConfirmMyChoiceButton")
    private ExtendedWebElement confirmMyChoiceButton;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`name == \"pcEditableConsentCell\"`]/XCUIElementTypeImage")
    protected ExtendedWebElement sellingSharingTargatedAdvertisingArrow;

    @Override
    public boolean isOpened() {
        return pageTitle.isPresent();
    }

    public void tapCloseButton(){
        closeButton.click();
    }

    public void tapConsentSwitch() {
        getOneTrustContentSwitch().click();
    }

    public ExtendedWebElement getOneTrustContentSwitch() {
        return consentSwitch;
    }

    public void tapConfirmMyChoiceButton() {
        confirmMyChoiceButton.click();
    }

    public String getValueOfConsentSwitch(){
        return getOneTrustContentSwitch().getAttribute("value");
    }

    public boolean isCloseIconPresent() {
        return closeButton.isPresent();
    }

    public boolean isWaltDisneyLogoPresent() {
        return logo.isPresent();
    }

    public boolean isNoticeOfRightToOptOutOfSaleTitlePresent() {
        return staticTypeTextViewValue.format(pageTitleText).isPresent();
    }

    public boolean isLegalTextPresent() {
        return legalText.isPresent() && !legalText.getText().isEmpty();
    }

    public boolean isUSStatePrivacyRightsLinkPresent() {
        return customHyperlinkByLabel.format(usPrivacyRights).isPresent();
    }

    public void clickYourUSStatePrivacyRightsLink(){
        //The "usPrivacyRights" hyperlink is breaking into two lines due to that the tap location (bottom left corner) is applied
        ExtendedWebElement element = customHyperlinkByLabel.format(usPrivacyRights);
        var dimension = element.getSize();
        Point location = element.getLocation();
        tap(location.getX() , location.getY() + dimension.getHeight());
    }

    public boolean isYourUSStatePrivacyRightsPageOpened(int timeout){
        return staticTextByLabel.format(usPrivacyRights.toUpperCase()).isPresent(timeout);
    }

    public boolean isYourCaliforniaPrivacyRightsLinkPresent() {
        return customHyperlinkByLabel.format(californiaPrivacyRights).isPresent();
    }

    public void clickYourCaliforniaPrivacyRightsLink(){
        clickElementAtLocation(customHyperlinkByLabel.format(californiaPrivacyRights), 10, 75);
//        ExtendedWebElement element = customHyperlinkByLabel.format(californiaPrivacyRights);
//        int heightValue = 60;
//        Dimension dimension = element.getSize();
//        Point location = element.getLocation();
//        System.out.println(location);
//        System.out.println(dimension);
//        if (element.getSize().getHeight() > heightValue) {
//            tap(location.getX() + 55 , location.getY() + dimension.getHeight()*2/3, 2);
//        } else {
//            element.click();
//        }
    }

    public boolean isYourCaliforniaPrivacyRightsPageOpened(int timeout){
        return dynamicBtnFindByLabelContains.format("CALIFORNIA").isPresent(timeout);
    }

    public boolean isSellingSharingTargatedAdvertisingConsentTitlePresent() {
        return consentSwitchTitle.getText().trim().equals(consentCellGroupNameTitle);
    }

    public boolean isArrowIconToRightOfTooglePresent() {
        return sellingSharingTargatedAdvertisingArrow.isPresent();
    }

    public boolean isConfirmMyChoceButtonPresent() {
        return confirmMyChoiceButton.isPresent();
    }

    public void clickSellingSharingTargatedAdvertisingArrow(){
        sellingSharingTargatedAdvertisingArrow.click();
    }

}
