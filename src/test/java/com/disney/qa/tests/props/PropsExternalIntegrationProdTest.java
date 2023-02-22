package com.disney.qa.tests.props;

import com.disney.qa.props.PropsBasePage;
import com.disney.qa.props.PropsParameter;
import com.disney.qa.tests.BaseTest;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class PropsExternalIntegrationProdTest extends BaseTest {

    public static final String INITIAL_PAGE = PropsParameter.PROPS_INITIAL.getValue();

    @BeforeTest
    public void beforeTest() {
        PropsBasePage page = new PropsBasePage(getDriver());
        page.open(getDriver(), INITIAL_PAGE);
        page.login(1);
    }

    @Test(description = "View elements present when creating EI product", priority = 2)
    public void eiProductTest() {

        SoftAssert sa = new SoftAssert();
        PropsBasePage propsBasePage = new PropsBasePage(getDriver());

        propsBasePage.getPlusButton().click();
        propsBasePage.getPlusMenu(1).click();

        sa.assertTrue(propsBasePage.getProductName().isElementPresent(), "Product title field NOT present");
        sa.assertTrue(propsBasePage.getProductKey().isElementPresent(), "Product key field NOT present");
        sa.assertTrue(propsBasePage.getProductJiraURL().isElementPresent(), "Product JIRA URL field NOT present");
        sa.assertTrue(propsBasePage.getProductPlatform().isElementPresent(), "Product platform menu NOT present");
        sa.assertTrue(propsBasePage.getProductEntitlement().isElementPresent(), "Product entitlements menu NOT present");
        sa.assertTrue(propsBasePage.getSubscriptionRecurSwitch().isElementPresent(), "Subscription recurs toggle NOT present");
        sa.assertTrue(propsBasePage.getSubscriptionPeriod().isElementPresent(), "Subscription period radio bar NOT present");
        sa.assertTrue(propsBasePage.getProductCurrency().isElementPresent(), "Product currency menu NOT present");
        sa.assertTrue(propsBasePage.getProductPrice().isElementPresent(), "Product price field NOT present");
        sa.assertTrue(propsBasePage.getProductTerritory().isElementPresent(), "Product territory field NOT present");
        sa.assertTrue(propsBasePage.getFreeTrialSwitch().isElementPresent(), "Product free trial toggle NOT present");

        sa.assertAll();

    }

    @Test(description = "Creating EI product", priority = 2)
    public void createProductTest() {

        SoftAssert sa = new SoftAssert();
        PropsBasePage propsBasePage = new PropsBasePage(getDriver());

        propsBasePage.getPlusButton().click();
        propsBasePage.getPlusMenu(1).click();

        pause(5);
        propsBasePage.getProductName().getElement().sendKeys("Testing");
        propsBasePage.getProductKey().getElement().sendKeys("test");
        propsBasePage.getProductPlatform().click();
        for (ExtendedWebElement e : propsBasePage.getMenuOptionList()) {
            if(e.getText().equals("Adobe Pass")) {
                e.click();
            }
        }
        propsBasePage.getProductEntitlement().doubleClick();
        for (ExtendedWebElement e : propsBasePage.getMenuOptionList()) {
            if(e.getText().equals("LAUNCHPAD_TEST")) {
                e.click();
            }
        }
        sa.assertTrue(propsBasePage.getSaveButton().isElementPresent(), "Save button NOT present");
        sa.assertTrue(propsBasePage.getSaveAndButton().isElementPresent(), "Save an send to testing button NOT present");

        PropsBasePage page = new PropsBasePage(getDriver());
        page.open(getDriver(), INITIAL_PAGE);

        sa.assertAll();
    }

    @Test(description = "Looking at live EI products", priority = 2)
    public void liveEIProductTest() {

        SoftAssert sa = new SoftAssert();
        PropsBasePage propsBasePage = new PropsBasePage(getDriver());

        for (ExtendedWebElement e : propsBasePage.getStatusBadge()) {
            if (e.getText().equals("Live")) {
                e.click();
                sa.assertTrue(propsBasePage.getDeactivateButton().isElementPresent(), "Deactivate button NOT present");
                sa.assertTrue(propsBasePage.getCommentIcon().isElementPresent(), "Comment icon NOT present");
                sa.assertTrue(propsBasePage.getProductHistory().isElementPresent(), "Product history NOT present");
                sa.assertTrue(propsBasePage.getCloneMenu().isElementPresent(), "Clone option NOT present");
                sa.assertTrue(propsBasePage.getEditButton().isElementPresent(), "Edit button NOT present");
                sa.assertTrue(propsBasePage.getAddToGroups().isElementPresent(), "Add to groups option NOT present");
                sa.assertAll();
            }
        }

    }


}
