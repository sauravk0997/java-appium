package com.disney.qa.disney.web.commerce;

import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusCancelSuccessPage extends DisneyPlusBasePage {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FindBy(xpath = "//*[@data-testid='cancel-success-header']")
    private ExtendedWebElement cancelSuccessHeader;

    @FindBy(xpath = "//*[@data-testid='cancel-success-subheader']")
    private ExtendedWebElement cancelSuccessSubheader;

    @FindBy(xpath = "//*[@data-testid='link-button']")
    private ExtendedWebElement restartSubscriptionButton;

    @FindBy(xpath = "//*[@data-testid='survey-header']")
    private ExtendedWebElement surveyHeader;

    @FindBy(xpath = "(//*[@name='cancel-survey'])[%d]")
    private ExtendedWebElement surveyOption;

    @FindBy(xpath = "//*[@data-testid='cta-button']")
    private ExtendedWebElement submitButton;

    @FindBy(xpath = "//*[@data-testid='banner-success']")
    private ExtendedWebElement thanksForFeedbackLabel;

    //German cancel 
    @FindBy(id ="cancel_contract_success")
    private ExtendedWebElement cancelContractSuccessTitle;

    @FindBy(xpath = "//*[@data-testid='cancellationConfirmationBlurb']/p")
    private ExtendedWebElement cancelContractConfirmation;

    public DisneyPlusCancelSuccessPage(WebDriver driver) throws NoSuchAlgorithmException {
        super(driver);
    }

    public boolean verifyPage() {
        LOGGER.info("Verify Cancel Success page loaded");
        return cancelSuccessHeader.isVisible();
    }

    public boolean isCancelSuccessSubheaderVisible() {
        LOGGER.info("Is cancel success subheader visible");
        return cancelSuccessSubheader.isVisible();
    }

    public boolean isRestartSubscriptionButtonVisible() {
        LOGGER.info("Is restart subscription button visible");
        return restartSubscriptionButton.isVisible();
    }

    public boolean isSurveyHeaderVisible() {
        LOGGER.info("Is survey header visible");
        return surveyHeader.isVisible();
    }

    public boolean isSubmitVisible() {
        LOGGER.info("Is submit button visible");
        return submitButton.isVisible();
    }

    public boolean isThanksForFeedbackLabelVisible() {
        LOGGER.info("Is thanks for feedback label visible");
        return thanksForFeedbackLabel.isVisible();
    }

    public DisneyPlusCancelSuccessPage clickSurveyOption(int randomIndex) {
        LOGGER.info("Click survey option: {}", randomIndex);
        surveyOption.format(randomIndex).click();
        return this;
    }

    public DisneyPlusCancelSuccessPage clickRestartSubscriptionButton() {
        LOGGER.info("Click switch plan button");
        restartSubscriptionButton.click();
        return this;
    }

    public DisneyPlusCancelSuccessPage clickSubmitButton() {
        LOGGER.info("Click submit button");
        submitButton.click();
        return this;
    }

    public DisneyPlusCancelSuccessPage selectRandomSurveyOption() {
        LOGGER.info("Select random survey option");
        int randomInt = new SecureRandom().nextInt(10) + 1;

        LOGGER.info("Random int: {}", randomInt);
        clickSurveyOption(randomInt);
        return this;
    }

    //German Cancel
    public boolean isCancelSuccessTitleVisible() {
        LOGGER.info("Is Cancel Success title visible");
        return cancelContractSuccessTitle.isVisible();
    }

    public String cancelConfirmationBlurbText() {
        LOGGER.info("Checking cancel confirmation copy");
        return cancelContractConfirmation.getText();
    }
}
