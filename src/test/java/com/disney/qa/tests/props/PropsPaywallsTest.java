package com.disney.qa.tests.props;

import com.disney.qa.props.PropsBasePage;
import com.disney.qa.props.PropsParameter;
import com.disney.qa.tests.BaseTest;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class PropsPaywallsTest extends BaseTest {

    public static final String INITIAL_PAGE = PropsParameter.PROPS_INITIAL.getValue();
    public static final String PAYWALLS_PAGE = INITIAL_PAGE + "paywalls";

    //Adjust following values accordingly to suit live product(s) being tested with
    public static final String TEST_PRODUCT_PLATFORM1 = "Sony";
    public static final String TEST_PRODUCT_TERRITORY1 = "Chile";
    public static final String TEST_PRODUCT_PLATFORM2 = "Vizio - Direct Billing";
    public static final String TEST_PRODUCT_TERRITORY2 = "Estonia";

    @BeforeTest
    public void beforeTest() {
        PropsBasePage page = new PropsBasePage(getDriver());
        page.open(getDriver(), INITIAL_PAGE);
        page.login(1);
        page.pageSetUp(PAYWALLS_PAGE);
    }

    @Test(description = "Paywalls dashboard test", priority = 2)
    public void testPaywallsDashboard() {

        SoftAssert sa = new SoftAssert();
        PropsBasePage propsBasePage = new PropsBasePage(getDriver());

        sa.assertTrue(propsBasePage.getPartnerSelector().isElementPresent(), "Partner selector NOT present");
        sa.assertTrue(propsBasePage.getPropsLogo().isElementPresent(), "Props P logo NOT present");
        sa.assertTrue(propsBasePage.getSectionSelector().isElementPresent(), "Section selector NOT present");
        sa.assertTrue(propsBasePage.getSearchBar().isElementPresent(), "Search bar NOT present");
        propsBasePage.getSearchBar().click();
        sa.assertTrue(propsBasePage.getPlusButton().isElementPresent(), "Plus button NOT present");
        sa.assertTrue(propsBasePage.getHeroIcon().isElementPresent(), "Hero icon NOT present");
        sa.assertTrue(propsBasePage.getPaywallPlatformFilter().isElementPresent(), "Platform filter NOT present");
        propsBasePage.getPaywallPlatformFilter().click();
        sa.assertTrue(propsBasePage.getPaywallTerritoryFilter().isElementPresent(), "Territories filter NOT present");
        propsBasePage.getPaywallTerritoryFilter().click();
        sa.assertAll();

    }

    @Test(description = "Ability to create new paywall test", priority = 2)
    public void newPaywallCreationTest() {

        SoftAssert sa = new SoftAssert();
        PropsBasePage propsBasePage = new PropsBasePage(getDriver());

        sa.assertTrue(propsBasePage.getPlusButton().isElementPresent(), "Plus menu button NOT present");
        propsBasePage.getPlusButton().click();
        sa.assertTrue(propsBasePage.getPlusMenu(0).isElementPresent(), "Menu NOT showing up");
        propsBasePage.getPlusMenu(0).click();

        propsBasePage.navigateBack();

        propsBasePage.getPlusButton().click();
        propsBasePage.getPlusMenu(1).click();

        sa.assertAll();

    }

    @Test(description = "Create third party paywall test", priority = 2)
    public void thirdPartyPaywallCreationTest() {

        SoftAssert sa = new SoftAssert();
        PropsBasePage propsBasePage = new PropsBasePage(getDriver());

        //possible issue with partner selector if options in menu have changed
        propsBasePage.getPartnerSelector().click();
        propsBasePage.getPartnerMenu(2).click();

        propsBasePage.getPlusButton().click();
        propsBasePage.getPlusMenu(0).click();

        propsBasePage.getPaywallName().getElement().sendKeys("Test Paywall");
        propsBasePage.getPaywallPlatform().click();

        for (ExtendedWebElement e : propsBasePage.getMenuOptionList()) {
            if (e.getText().equals(TEST_PRODUCT_PLATFORM1)) {
                e.click();
                break;
            }
        }

        propsBasePage.getPaywallTerritory().click();
        for (ExtendedWebElement e : propsBasePage.getMenuOptionList()) {
            if (e.getText().equals(TEST_PRODUCT_TERRITORY1)) {
                e.click();
                break;
            }
        }

        propsBasePage.getUserContext().click();
        propsBasePage.getUserSelection().click();

        propsBasePage.scrollDown();

        propsBasePage.getConsumerPurchased().click();
        propsBasePage.getConsumerProductSelection().click();

        propsBasePage.getPaywallProduct().click();
        propsBasePage.getPaywallProductSelection().click();

        sa.assertTrue(propsBasePage.getPaywallSave().isElementPresent(), "Save to preview button NOT present");
        sa.assertTrue(propsBasePage.getPaywallGoLive().isElementPresent(), "Go live button NOT present");

        sa.assertAll();

    }

    @Test(description = "Create direct billing paywall test", priority = 2)
    public void directBillingPaywallTest() {

        SoftAssert sa = new SoftAssert();
        PropsBasePage propsBasePage = new PropsBasePage(getDriver());

        propsBasePage.getPlusButton().click();
        propsBasePage.getPlusMenu(1).click();

        propsBasePage.getPaywallName().getElement().sendKeys("Test Paywall");
        propsBasePage.getPaywallPlatform().click();

        for (ExtendedWebElement e : propsBasePage.getMenuOptionList()) {
            if (e.getText().equals(TEST_PRODUCT_PLATFORM2)) {
                e.click();
                break;
            }
        }

        propsBasePage.getPaywallTerritory().click();
        for (ExtendedWebElement e : propsBasePage.getMenuOptionList()) {
            if (e.getText().equals(TEST_PRODUCT_TERRITORY2)) {
                e.click();
                break;
            }
        }

        propsBasePage.getUserContext().click();
        propsBasePage.getUserSelection().click();

        propsBasePage.getPaywallProduct().click();
        propsBasePage.getPaywallProductSelection().click();

        propsBasePage.getCampaignCode().click();
        propsBasePage.getCampaignSelection().click();

        propsBasePage.getVoucherCode().click();
        propsBasePage.getVoucherSelection().click();

        sa.assertTrue(propsBasePage.getPaywallSave().isElementPresent(), "Save to preview button NOT present");
        sa.assertTrue(propsBasePage.getPaywallGoLive().isElementPresent(), "Go live button NOT present");
        sa.assertAll();

    }
}