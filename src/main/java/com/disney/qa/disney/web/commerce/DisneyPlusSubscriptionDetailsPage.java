package com.disney.qa.disney.web.commerce;

import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusSubscriptionDetailsPage extends DisneyPlusBasePage {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FindBy(xpath = "//h3[contains(text(),'Subscription details')]")
    private ExtendedWebElement subscriptionDetailsHeader;

    @FindBy(xpath = "//*[contains(text(),'Back to Account')]")
    private ExtendedWebElement backToAccountButton;

    @FindBy(xpath = "//*[@data-testid='subscription-plan']")
    private ExtendedWebElement subscriptionPlanSection;

    @FindBy(xpath = "//*[@data-testid='card-details-section']")
    private ExtendedWebElement cardDetailsSection;

    @FindBy(xpath = "//div[contains(text(), 'Payment Method')]")
    private ExtendedWebElement subscriptionDetailsPaymentMethod;

    @FindBy(xpath = "//img[@data-testid='subscription-details-paymentcard']")
    private ExtendedWebElement subscriptionDetailsPaymentCard;

    @FindBy(xpath = "//div[contains(text(), 'Next Billing Date')]")
    private ExtendedWebElement subscriptionDetailsNextBillingDate;

    @FindBy(xpath = "//div[contains(text(), 'Last Payment')]")
    private ExtendedWebElement subscriptionDetailsLastPayment;

    @FindBy(xpath = "//*[@data-testid='last-payment-price']")
    private ExtendedWebElement subscriptionDetailsLastPaymentPrice;

    @FindBy(xpath = "//button[@data-testid='subscription-details-change-payment']")
    private ExtendedWebElement subscriptionDetailsChangePaymentButton;

    @FindBy(xpath = "//*[@data-testid='subscription-details-cancel-subscription']")
    private ExtendedWebElement cancelSubscriptionButton;

    @FindBy(xpath = "//*[@data-testid='restart-subscription-button']")
    private ExtendedWebElement restartSubscriptionButton;

    @FindBy(xpath = "//*[@data-testid='subscription-plan']/div[2]/span/p")
    private ExtendedWebElement subscriptionID;

    @FindBy(xpath = "//*[@aria-label='info2']")
    private ExtendedWebElement subscriptionIDInfo;

    @FindBy(xpath = "//*[@data-gv2containerkey='modalContainer']")
    private ExtendedWebElement subscriptionInfoModal;

    @FindBy(xpath = "//button[@data-testid='close-memo-modal']")
    private ExtendedWebElement subscriptionInfoModalCloseButton;

    public DisneyPlusSubscriptionDetailsPage(WebDriver driver) {
        super(driver);
    }

    public boolean verifyPage() {
        LOGGER.info("Verify Subscription Details page loaded");
        return subscriptionDetailsHeader.isVisible();
    }

    public boolean isSubscriptionPlanSectionVisible() {
        LOGGER.info("Is subscription plan section visible");
        return subscriptionPlanSection.isVisible();
    }

    public boolean isCardDetailsSectionVisible() {
        LOGGER.info("Is card details section visible");
        return cardDetailsSection.isVisible();
    }

    public boolean isPaymentMethodVisible() {
        LOGGER.info("Is Payment method visible");
        return subscriptionDetailsPaymentMethod.isVisible();
    }

    public boolean isPaymentCardVisible() {
        LOGGER.info("Is Payment card visible");
        return subscriptionDetailsPaymentCard.isVisible();
    }

    public boolean isNextBillingDateVisible() {
        LOGGER.info("Is Next billing method visible");
        return subscriptionDetailsNextBillingDate.isVisible();
    }

    public boolean isLastPaymentVisible() {
        LOGGER.info("Is Last Payment visible");
        return subscriptionDetailsLastPayment.isVisible();
    }

    public boolean isLastPaymentPriceVisible() {
        LOGGER.info("Is Last payment price visible");
        return subscriptionDetailsLastPaymentPrice.isVisible();
    }

    public boolean isSubscriptionDetailsChangePaymentButtonVisible() {
        LOGGER.info("Is subscription details change payment button visible");
        return subscriptionDetailsChangePaymentButton.isVisible();
    }

    public boolean isSubscriptionDetailsCancelSubscriptionButtonVisible() {
        LOGGER.info("Is subscription details cancel subscription button visible");
        return cancelSubscriptionButton.isVisible();
    }

    public boolean isRestartSubscriptionButtonVisible() {
        LOGGER.info("Is restart subscription button visible");
        return restartSubscriptionButton.isVisible();
    }

    public boolean isSubscriptionInfoModalVisible() {
        LOGGER.info("Is subscription ID info modal visible");
        return subscriptionInfoModal.isVisible();
    }

    public DisneyPlusSubscriptionDetailsPage clickBackToAccountLink() {
        LOGGER.info("Back to Account is clicked");
         backToAccountButton.click();
         return this;
    }

    public DisneyPlusSubscriptionDetailsPage clickSubscriptionDetailsChangePaymentButton() {
        LOGGER.info("Click subscription details change payment button");
        subscriptionDetailsChangePaymentButton.click();
        return this;
    }

    public DisneyPlusSubscriptionDetailsPage clickSubscriptionDetailsCancelSubscriptionButton() {
        LOGGER.info("Click subscription details cancel subscription button");
        cancelSubscriptionButton.click();
        return this;
    }

    public DisneyPlusSubscriptionDetailsPage clickSubscriptionIDInfoButton() {
        LOGGER.info("Click subscription details subscription ID info button");
        subscriptionIDInfo.click();
        return this;
    }

    public DisneyPlusSubscriptionDetailsPage clickSubscriptionInfoModalCloseButton() {
        LOGGER.info("Click subscription ID info modal close button");
        subscriptionInfoModalCloseButton.click();
        return this;
    }

    public String getSubscriptionDetailsText() {
        LOGGER.info("Getting subscription plan details");
        waitFor(subscriptionPlanSection,SHORT_TIMEOUT);
        return subscriptionPlanSection.getText();
    }

    public String getLastPaymentPrice() {
        LOGGER.info("Getting last payment price");
        return subscriptionDetailsLastPaymentPrice.getText();
    }

    public String getSubscriptionID() {
        LOGGER.info("Getting subscription ID from subscription details");
        return subscriptionID.getText();
    }

}
