package com.disney.qa.disney.windows10;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyWindowsLegalPage extends DisneyWindowsCommonPage {
    @FindBy(name = "Privacy Policy")
    private ExtendedWebElement privacyPolicy;
    @FindBy(name = "Subscriber Agreement")
    private ExtendedWebElement subscriberAgreement;
    @FindBy(name = "Your California Privacy Rights")
    private ExtendedWebElement yourCAPrivacyRights;
    @FindBy(name = "Do Not Sell My Personal Information")
    private ExtendedWebElement doNotSellMyPersonalInformation;
    @FindBy(name = "Legal")
    private ExtendedWebElement legalTitle;
    @ExtendedFindBy(accessibilityId = "DocumentTextTextBlock")
    private ExtendedWebElement documentTextBlock;

    public DisneyWindowsLegalPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return false;
    }

    public ExtendedWebElement getPrivacyPolicy() {
        return privacyPolicy;
    }

    public ExtendedWebElement getSubscriberAgreement() {
        return subscriberAgreement;
    }

    public ExtendedWebElement getYourCAPrivacyRights() {
        return yourCAPrivacyRights;
    }

    public ExtendedWebElement getDoNotSellMyPersonalInformation() {
        return doNotSellMyPersonalInformation;
    }

    public List<ExtendedWebElement> legalMenu() {
        return Stream.of(privacyPolicy, subscriberAgreement, yourCAPrivacyRights, doNotSellMyPersonalInformation).collect(Collectors.toList());
    }
}
