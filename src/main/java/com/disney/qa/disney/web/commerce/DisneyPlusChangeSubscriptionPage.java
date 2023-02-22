package com.disney.qa.disney.web.commerce;

import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.entities.PlanCardTypes;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusChangeSubscriptionPage extends DisneyPlusBasePage {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FindBy(xpath = "//*[@data-testid='headline']")
    private ExtendedWebElement headline;

    @FindBy(xpath = "//*[@data-testid='banner-warning']")
    private ExtendedWebElement priceChangeMessage;

    @FindBy(xpath = "//*[@data-testid='current-plan-details']")
    private ExtendedWebElement currentPlanDetails;

    @FindBy(xpath = "//*[@data-testid='link-button']")
    private ExtendedWebElement expandAnnualPlans;

    @FindBy(xpath = "//*[@data-testid='%s'] //*[@data-testid='plan-card-title']")
    private ExtendedWebElement planCardTitle;

    @FindBy(xpath = "//*[@data-testid='%s'] //*[@data-testid='plan-card-description']")
    private ExtendedWebElement planCardDescription;

    @FindBy(xpath = "//*[@data-testid='%s'] //*[@data-testid='plan-card-price-details']")
    private ExtendedWebElement planCardPrice;

    @FindBy(xpath = "//*[@data-testid='%s']")
    private ExtendedWebElement planCardButton;

    @FindBy(css="[data-testid='banner-warning'] a")
    private ExtendedWebElement priceInfoLink;

    @FindBy(css="[data-testid='plan-section-disclaimer']")
    private ExtendedWebElement bundleTermsLink;

    @FindBy(css="[aria-controls='radix-:r3:']")
    private ExtendedWebElement aFewShowsThatPlayAdsLink;

    @FindBy(id="radix-:r0:")
    private ExtendedWebElement bundleTermsDialog;

    @FindBy(id="radix-:r3:")
    private ExtendedWebElement aFewShowsThatPlayAdsDialog;

    @FindBy(linkText="Help Center")
    private ExtendedWebElement helpCenterLink;

    @FindBy(linkText="Hulu Help Center")
    private ExtendedWebElement huluHelpCenterLink;

    public DisneyPlusChangeSubscriptionPage(WebDriver driver) {
        super(driver);
    }

    public boolean verifyPage() {
        LOGGER.info("Verify Change Subscription page loaded");
        return headline.isVisible(LONG_TIMEOUT);
    }

    public String getCurrentPlanDetails() {
        LOGGER.info("Get current plan details");
        return currentPlanDetails.getText();
    }

    public DisneyPlusChangeSubscriptionPage clickExpandAnnualPlans() {
        LOGGER.info("Click to expand annual plans");
        expandAnnualPlans.click();
        return this;
    }

    public String getPlanCardTitle(PlanCardTypes.PlanSwitchCard card) {
        LOGGER.info("Get plan card title");
        return  planCardTitle.format(card.getPlanDataTestId()).getText();
    }

    public String getPlanCardDescription(PlanCardTypes.PlanSwitchCard card) {
        LOGGER.info("Get plan card description");
        return  planCardDescription.format(card.getPlanDataTestId()).getText();
    }

    public String getPlanCardPrice(PlanCardTypes.PlanSwitchCard card) {
        LOGGER.info("Get plan card price");
        return  planCardPrice.format(card.getPlanDataTestId()).getText();
    }

    public DisneyPlusChangeSubscriptionPage clickPlanCardButton(PlanCardTypes.PlanSwitchCard card) {
        LOGGER.info("Click plan card button");
        planCardButton.format(card.getPlanDataTestId()).click();
        return this;
    }

    public boolean isPlanCardButtonVisible(PlanCardTypes.PlanSwitchCard card) {
        LOGGER.info("Is Plan card button visible");
        return planCardButton.format(card.getPlanDataTestId()).isVisible(SHORT_TIMEOUT);
    }

    public DisneyPlusChangeSubscriptionPage clickPriceInfoLink() {
        LOGGER.info("Click the link of disneyplus.com/priceinfo");
        priceInfoLink.click();
        return this;
    }

    public DisneyPlusChangeSubscriptionPage clickBundleTermsLink() {
        LOGGER.info("Click Bundle Terms link to open the popup");
        bundleTermsLink.click();
        return this;
    }

    public String collectBundleTermsPopupContent() {
        LOGGER.info("Collect the plan section disclaimers content: <Bundle Terms>");
        return bundleTermsDialog.getText();
    }

    public DisneyPlusChangeSubscriptionPage clickHelpCenterLinkInBundleTermsPopup() {
        LOGGER.info("Click the link of <Help Center> in <Bundle Terms> popup");
        helpCenterLink.click();
        return this;
    }

    public DisneyPlusChangeSubscriptionPage clickAFewShowsThatPlayAdsLink() {
        LOGGER.info("Click <a few shows that play ads> link to open the popup");
        aFewShowsThatPlayAdsLink.click();
        return this;
    }

    public String collectAFewShowsThatPlayAdsPopupContent() {
        LOGGER.info("Collect the plan section disclaimers content: <a few shows that play ads>");
        return aFewShowsThatPlayAdsDialog.getText();
    }

    public DisneyPlusChangeSubscriptionPage clickHuluHelpCenterLink() {
        LOGGER.info("Click <Hulu Help Center> link in <a few shows that play ads> content");
        huluHelpCenterLink.click();
        return this;
    }
}
