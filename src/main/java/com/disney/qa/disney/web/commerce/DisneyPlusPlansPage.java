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

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusPlansPage extends DisneyPlusBasePage {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FindBy(xpath = "//*[@data-testid='sub-selector']")
    private ExtendedWebElement planSection;

    @FindBy(xpath = "//*[@data-testid='buttonOne']")
    private ExtendedWebElement bundleToggleBtn;

    @FindBy(xpath = "//*[@data-testid='buttonTwo']")
    private ExtendedWebElement standaloneToggleBtn;

    @FindBy(xpath = "(//*[@id='plan'])")
    private List<ExtendedWebElement> planCardList;

    @FindBy(xpath = "//*[@data-testid='plan-standalone-with-ads']")
    private ExtendedWebElement standalonePlanWithAds;

    @FindBy(xpath = "//*[@data-testid='%s'] //*[@data-testid='plan-title']")
    private ExtendedWebElement planTitle;

    @FindBy(xpath = "//*[@data-testid='%s'] //*[@data-testid='plan-subcopy']")
    private ExtendedWebElement planSubCopy;

    @FindBy(xpath = "//*[@data-testid='%s'] //*[contains(@class, 'body')]")
    private ExtendedWebElement planBody;

    @FindBy(xpath = "//*[@data-testid='%s'] //*[@data-testid='plan-price']")
    private ExtendedWebElement planPrice;

    @FindBy(xpath = "//*[@data-testid='%s'] //*[@data-testid='plan-cta']")
    private ExtendedWebElement planCta;

    @FindBy(xpath = "//*[@data-testid='%s'] //*[@data-testid='plan-logo']")
    private ExtendedWebElement planLogo;

    @FindBy(xpath = "//*[@id='bundle_terms_apply']")
    private ExtendedWebElement bundleTermsLink;

    @FindBy(xpath = "//*[@id='bundle_learn_more']")
    private ExtendedWebElement bundleLearnMoreLink;

    public DisneyPlusPlansPage(WebDriver driver) {
        super(driver);
    }

    public boolean verifyPage() {
        LOGGER.info("Verify Plans page loaded");
        return planSection.isVisible();
    }

    public boolean isStandalonePlanCardVisible() {
        LOGGER.info("Is standalone plan card visible");
        return standalonePlanWithAds.isVisible(SHORT_TIMEOUT);
    }

    public DisneyPlusPlansPage clickStandaloneToggleButton() {
        LOGGER.info("Click standalone toggle button");
        standaloneToggleBtn.click();
        return this;
    }

    public String getBundleToggleBtnState() {
        LOGGER.info("Get bundle toggle state");
        return bundleToggleBtn.getAttribute("class").split(" ")[2];
    }

    public String getStandaloneToggleBtnState() {
        LOGGER.info("Get standalone toggle state");
        return standaloneToggleBtn.getAttribute("class").split(" ")[2];
    }

    public boolean isStandaloneToggleVisible() {
        LOGGER.info("Is Standalone Toggle visible");
        return standaloneToggleBtn.isVisible();
    }

    public boolean isBundleToggleVisible() {
        LOGGER.info("Is Bundle Toggle visible");
        return bundleToggleBtn.isVisible();
    }

    public int getPlanCardsCount() {
        LOGGER.info("Get plan cards count");
        return planCardList.size();
    }

    public String getPlanTitle(PlanCardTypes.PlanSelectCard planCardType) {
        LOGGER.info("Get plan title");
        return planTitle.format(planCardType.getPlanDataTestId()).getText();
    }

    public String getPlanSubCopy(PlanCardTypes.PlanSelectCard planCardType) {
        LOGGER.info("Get plan sub copy");
        return planSubCopy.format(planCardType.getPlanDataTestId()).getText();
    }

    public String getPlanPrice(PlanCardTypes.PlanSelectCard planCardType) {
        LOGGER.info("Get plan price");
        return planPrice.format(planCardType.getPlanDataTestId()).getText();
    }

    public String getPlanLogoAltTxt(PlanCardTypes.PlanSelectCard planCardType) {
        LOGGER.info("Get plan logo alt text");
        return planLogo.format(planCardType.getPlanDataTestId()).getAttribute("alt");
    }

    public int getPlanBodyCopyLength(PlanCardTypes.PlanSelectCard planCardType) {
        LOGGER.info("Get plan body copy length");
        return planBody.format(planCardType.getPlanDataTestId()).getText().length();
    }

    public DisneyPlusPlansPage clickPlanCTA(PlanCardTypes.PlanSelectCard planCardType) {
        LOGGER.info("Click plan CTA");
        planCta.format(planCardType.getPlanDataTestId()).click();
        return this;
    }

    public DisneyPlusPlansPage clickBundleTermsLink() {
        LOGGER.info("Click bundle terms link");
        bundleTermsLink.click();
        return this;
    }

    public DisneyPlusPlansPage clickBundleLearnMoreLink() {
        LOGGER.info("Click bundle learn more link");
        bundleLearnMoreLink.click();
        return this;
    }

    public boolean isBundleTermsLinkVisible() {
        LOGGER.info("Is bundle terms link visible");
        return bundleTermsLink.isVisible();
    }

    public boolean isBundleLearnMoreLinkVisible() {
        LOGGER.info("Is bundle learn more  link visible");
        return bundleLearnMoreLink.isVisible();
    }

}
