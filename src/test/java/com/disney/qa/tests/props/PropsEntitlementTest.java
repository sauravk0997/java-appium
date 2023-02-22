package com.disney.qa.tests.props;

import com.disney.qa.props.PropsBasePage;
import com.disney.qa.props.PropsParameter;
import com.disney.qa.tests.BaseTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class PropsEntitlementTest extends BaseTest {

    public static final String INITIAL_PAGE = PropsParameter.PROPS_INITIAL.getValue();
    public static final String ENTITLEMENTS_PAGE = INITIAL_PAGE + "entitlements";

    @BeforeTest
    public void beforeTest() {
        PropsBasePage page = new PropsBasePage(getDriver());
        page.open(getDriver(), INITIAL_PAGE);
        page.login(1);
        page.pageSetUp(ENTITLEMENTS_PAGE);
    }

    @Test(description = "Entitlement dashboard test", priority = 2)
    public void testEntitlementDashboard() {

        SoftAssert sa = new SoftAssert();
        PropsBasePage propsBasePage = new PropsBasePage(getDriver());

        sa.assertTrue(propsBasePage.getPartnerSelector().isElementPresent(), "Partner selector NOT present");
        sa.assertTrue(propsBasePage.getPropsLogo().isElementPresent(), "Props P logo NOT present");
        sa.assertTrue(propsBasePage.getSectionSelector().isElementPresent(), "Section selector NOT present");
        sa.assertTrue(propsBasePage.getPlusButton().isElementPresent(), "Plus button NOT present");
        sa.assertTrue(propsBasePage.getHeroIcon().isElementPresent(), "Hero icon NOT present");

        sa.assertTrue(propsBasePage.getEntitlementBody(0).isElementPresent(), "Entitlement row NOT present");
        propsBasePage.getEntitlementBody(0).hover();
        sa.assertTrue(propsBasePage.getEditEntitlement(0).isElementPresent(), "Edit button NOT present");
        sa.assertTrue(propsBasePage.getEntitlementHistory().isElementPresent(), "History icon NOT present");

        propsBasePage.getEntitlementBody(0).hover();
        pause(3);
        propsBasePage.getEditEntitlement(0).click();
        pause(3);
        sa.assertTrue(propsBasePage.getSaveEntitlement().isElementPresent(), "Save button NOT present");
        sa.assertAll();
    }

    @Test(description = "Creating new entitlement", priority = 2)
    public void createEntitlement() {

        SoftAssert sa = new SoftAssert();
        PropsBasePage propsBasePage = new PropsBasePage(getDriver());

        propsBasePage.getPlusButton().click();
        propsBasePage.getPlusMenu(0).click();

        propsBasePage.getEntitlementName().getElement().sendKeys("TESTING");
        propsBasePage.getEntitlementDescription().getElement().sendKeys("Just a test");
        propsBasePage.getSaveEntitlement().click();
        pause(3);

        sa.assertTrue(propsBasePage.getEntitlementID(0).isElementPresent(), "ID NOT created");

        propsBasePage.getPartnerSelector().click();
        propsBasePage.getPartnerMenu(2).click();
        sa.assertAll();
    }

}
