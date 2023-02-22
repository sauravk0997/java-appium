package com.disney.qa.disney.web.commerce;

import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusCancelRecommendationPage extends DisneyPlusBasePage {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FindBy(xpath = "//*[@data-testid='cancel-recommendation-heading']")
    private ExtendedWebElement recommendationHeader;

    @FindBy(xpath = "//*[@data-testid='recommendation-link']")
    private ExtendedWebElement recommendationLink;

    @FindBy(xpath = "//*[@data-testid='recommendation-link'] //img")
    private ExtendedWebElement recommendationLinkImage;

    @FindBy(xpath = "//*[@data-testid='default-button']")
    private ExtendedWebElement keepSubscriptionButton;

    @FindBy(xpath = "//span[contains(text(),'CANCEL SUBSCRIPTION')]")
    private ExtendedWebElement cancelSubscriptionButton;

    public DisneyPlusCancelRecommendationPage(WebDriver driver) {
        super(driver);
    }

    public boolean verifyPage() {
        LOGGER.info("Verify Cancel Recommendation page loaded");
        return recommendationHeader.isVisible();
    }

    public boolean isRecommendationLinkVisible() {
        LOGGER.info("Is recommendation link visible");
        return recommendationLink.isVisible(5);
    }

    public String getRecommendationLinkHref() {
        LOGGER.info("Get recommendation link href");
        String href = recommendationLink.getAttribute("href");
        LOGGER.info("Href is {}", href);
        return href;
    }

    public String getRecommendationLinkAltText() {
        LOGGER.info("Get recommendation link alt text");
        String altText = recommendationLinkImage.getAttribute("alt");
        LOGGER.info("Alt text is {}", altText);
        return altText;
    }

    public DisneyPlusCancelRecommendationPage clickRecommendationLink() {
        LOGGER.info("Click recommendation link");
        recommendationLink.click();
        return this;
    }
    public DisneyPlusCancelRecommendationPage clickKeepSubscriptionButton() {
        LOGGER.info("Click keep subscription button");
        keepSubscriptionButton.click();
        return this;
    }

    public DisneyPlusCancelRecommendationPage clickCancelSubscriptionButton() {
        LOGGER.info("Click cancel subscription button");
        cancelSubscriptionButton.click();
        return this;
    }
}
