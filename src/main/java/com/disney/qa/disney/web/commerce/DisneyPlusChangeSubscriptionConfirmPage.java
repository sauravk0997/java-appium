package com.disney.qa.disney.web.commerce;

import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusChangeSubscriptionConfirmPage extends DisneyPlusBasePage {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FindBy(xpath = "//*[@data-testid='change-subscription-confirm-header']")
    private ExtendedWebElement headline;

    @FindBy(xpath = "//*[@data-testid='plan-card-title']")
    private ExtendedWebElement planTitle;

    @FindBy(xpath = "//*[@data-testid='plan-card-description']")
    private ExtendedWebElement planDescription;

    @FindBy(xpath = "//*[@data-testid='plan-card-price-details']")
    private ExtendedWebElement planPrice;

    @FindBy(xpath = "//*[@data-testid='current-card-details']")
    private ExtendedWebElement currentCC;

    @FindBy(xpath = "//*[@data-testid='change-subscription-billing'] //*[@data-testid='link-button']")
    private ExtendedWebElement changeCCButton;

    @FindBy(xpath = "//*[@id='cvv']")
    private ExtendedWebElement cvvInput;

    @FindBy(xpath = "//*[@data-testid='share-payment']")
    private ExtendedWebElement sharePaymentButton;

    @FindBy(xpath = "//*[@data-testid='share-payment-description'] //button")
    private ExtendedWebElement learnMoreButton;

    @FindBy(xpath = "//*[@data-testid='total-due']")
    private List<ExtendedWebElement> totalDueToday;

    @FindBy(xpath = "//*[@data-testid='billing-cycle']")
    private ExtendedWebElement billingCycle;

    @FindBy(xpath = "//*[@data-testid='full-summary']")
    private ExtendedWebElement fullSummary;

    @FindBy(xpath = "//*[@data-testid='cta-button']")
    private ExtendedWebElement submitButton;

    @FindBy(xpath = "//div[@data-state='open']")
    private ExtendedWebElement learnMoreDialog;

    @FindBy(css = "[data-testid='change-subscription-confirm-summary'] [data-testid='link-button']")
    private ExtendedWebElement showOrHideSummaryOfChangesLink;

    @FindBy(css = "[data-testid='change-subscription-confirm-summary']")
    private ExtendedWebElement changeSubscriptionSummaryContent;

    @FindBy(partialLinkText = "disneyplus.com/priceinfo")
    private ExtendedWebElement priceInfoLink;

    @FindBy(css = "[data-testid='subscriber-agreement-link'] a")
    private ExtendedWebElement subscriberAgreementLink;

    public DisneyPlusChangeSubscriptionConfirmPage(WebDriver driver) {
        super(driver);
    }

    public boolean verifyPage() {
        LOGGER.info("Verify Change Subscription confirm page loaded");
        return headline.isVisible(LONG_TIMEOUT);
    }

    public DisneyPlusChangeSubscriptionConfirmPage enterCVV(String cvv) {
        LOGGER.info("Enter CVV {}", cvv);
        cvvInput.type(cvv);
        return this;
    }

    public boolean isCVVFieldVisible() {
        LOGGER.info("Is CVV field is visible");
        return cvvInput.isVisible();
    }

    public DisneyPlusChangeSubscriptionConfirmPage clickChangeCC() {
        LOGGER.info("Click change CC button");
        changeCCButton.click();
        return this;
    }

    public DisneyPlusChangeSubscriptionConfirmPage clickSubmitButton() {
        LOGGER.info("Click submit button");
        submitButton.click();
        return this;
    }

    public boolean isSubmitButtonNotPresent() {
        LOGGER.info("Is submit button not present");
        return submitButton.isElementNotPresent(EXPLICIT_TIMEOUT);
    }

    public String getPlanTitle() {
        LOGGER.info("Get plan title");
        return planTitle.getText();
    }

    public String getPlanDescription() {
        LOGGER.info("Get plan description");
        return planDescription.getText();
    }

    public String getPlanPrice() {
        LOGGER.info("Get plan price");
        return planPrice.getText();
    }

    public String getTotalDueToday() {
        waitForSeconds(SHORT_TIMEOUT);
        String totalDue = totalDueToday.get(1).getText();
        LOGGER.info("Get total due today : {}",  totalDue);
        return totalDue;
    }

    public DisneyPlusChangeSubscriptionConfirmPage handleStoredPayment(String cvv){
        LOGGER.info("Handle stored payment");
        enterCVV(cvv);
        clickSubmitButton();
        return this;
    }

    public DisneyPlusChangeSubscriptionConfirmPage clickLearnMoreLink() {
        LOGGER.info("Click lean more link");
        learnMoreButton.click();
        return this;
    }

    public String collectLearnMoreDialogContent() {
        LOGGER.info("Collect learn more dialog content");
        return learnMoreDialog.getText();
    }

    public DisneyPlusChangeSubscriptionConfirmPage clickShowOrHideSummaryOfChangesLink() {
        LOGGER.info("Click show summary of changes link");
        showOrHideSummaryOfChangesLink.click();
        return this;
    }

    public String collectChangeSubscriptionConfirmSummaryContent() {
        LOGGER.info("Collect change subscription confirmation summary content");
        return changeSubscriptionSummaryContent.getText();
    }

    public DisneyPlusChangeSubscriptionConfirmPage clickPriceInfoLink() {
        LOGGER.info("Click price info link");
        priceInfoLink.click();
        return this;
    }

    public DisneyPlusChangeSubscriptionConfirmPage clickSubscriberAgreementLink() {
        LOGGER.info("Click on subscriber agreement link");
        subscriberAgreementLink.click();
        return this;
    }
}
