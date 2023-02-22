package com.disney.qa.disney.web.commerce;

import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.entities.PlanCardTypes;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusCancelSwitchPage extends DisneyPlusBasePage {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FindBy(xpath = "//*[@data-testid='switch-header']")
    private ExtendedWebElement cancelSwitchHeader;

    @FindBy(xpath = "//*[@data-testid='save-on-plan']")
    private ExtendedWebElement saveOnPlanSubheader;

    @FindBy(xpath = "//*[@data-testid='%s'] //span[contains(text(),'%s')]")
    private ExtendedWebElement planTitle;

    @FindBy(xpath = "//*[@data-testid='plan-description']")
    private List<ExtendedWebElement> planDescriptionList;

    @FindBy(xpath = "//*[@data-testid='plan-price']")
    private List<ExtendedWebElement> planPriceList;

    @FindBy(xpath = "//*[@data-testid='cta-button']")
    private ExtendedWebElement switchPlanButton;

    @FindBy(xpath = "//*[@data-testid='default-button']")
    private ExtendedWebElement keepSubscriptionButton;

    @FindBy(xpath = "//span[contains(text(),'CANCEL SUBSCRIPTION')]")
    private ExtendedWebElement cancelSubscriptionButton;

    @FindBy(xpath = "//span[contains(text(),'CONTINUE TO CANCEL')]")
    private ExtendedWebElement continueToCancelButton;

    public DisneyPlusCancelSwitchPage(WebDriver driver) {
        super(driver);
    }

    public boolean verifyPage() {
        LOGGER.info("Verify Cancel Switch page loaded");
        return cancelSwitchHeader.isVisible();
    }

    public boolean isSaveOnPlanSubheaderVisible() {
        LOGGER.info("Is save on plan subheader visible");
        return saveOnPlanSubheader.isVisible();
    }

    public String getPlanTitle(PlanCardTypes.PlanCancelSwitchCard card) {
        LOGGER.info("Get plan title");
        return planTitle.format(card.getPlanDataTestId(), card.getPlanTitle()).getText();
    }

    public List<String> getPlanDescription() {
        LOGGER.info("Get plan description");
        return planDescriptionList.stream().map(ExtendedWebElement::getText).collect(Collectors.toList());
    }

    public String getPlanPrice() {
        LOGGER.info("Get plan price");
        String price = planPriceList.stream().map(ExtendedWebElement::getText).collect(Collectors.joining());
        if (price.contains("/mo")) {
            return price.replace("/mo", "/month");
        } else {
            return price.replace("/yr", "/year");
        }
    }

    public boolean isSwitchPlanButtonVisible() {
        LOGGER.info("Is switch plan button visible");
        return switchPlanButton.isVisible();
    }

    public boolean isKeepSubscriptionVisible() {
        LOGGER.info("Is keep subscription button visible");
        return keepSubscriptionButton.isVisible();
    }

    public boolean isCancelSubscriptionVisible() {
        LOGGER.info("Is cancel subscription button visible");
        return cancelSubscriptionButton.isVisible();
    }

    public boolean isContinueToCancelVisible() {
        LOGGER.info("Is continue to cancel button visible");
        return continueToCancelButton.isVisible();
    }

    public DisneyPlusCancelSwitchPage clickSwitchPlanButton() {
        LOGGER.info("Click switch plan button");
        switchPlanButton.click();
        return this;
    }

    public DisneyPlusCancelSwitchPage clickKeepSubscriptionButton() {
        LOGGER.info("Click keep subscription button");
        keepSubscriptionButton.click();
        return this;
    }

    public DisneyPlusCancelSwitchPage clickCancelSubscriptionButton() {
        LOGGER.info("Click cancel subscription button");
        cancelSubscriptionButton.click();
        return this;
    }

    public DisneyPlusCancelSwitchPage clickContinueToCancelButton() {
        LOGGER.info("Click continue to cancel button");
        continueToCancelButton.click();
        return this;
    }
}
