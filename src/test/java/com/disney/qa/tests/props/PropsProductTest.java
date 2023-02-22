package com.disney.qa.tests.props;

import com.disney.qa.props.PropsBasePage;
import com.disney.qa.props.PropsParameter;
import com.disney.qa.tests.BaseTest;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class PropsProductTest extends BaseTest {

    public static final String INITIAL_PAGE = PropsParameter.PROPS_INITIAL.getValue();

    @BeforeTest
    public void beforeTest() {
        PropsBasePage page = new PropsBasePage(getDriver());
        page.open(getDriver(), INITIAL_PAGE);
        page.login(1);
    }

    @Test(description = "Product dashboard clone and scrolling test", priority = 2)
    public void dashboardMainViewTest() {

        SoftAssert sa = new SoftAssert();
        PropsBasePage propsBasePage = new PropsBasePage(getDriver());

        propsBasePage.getProductRow().hover();
        sa.assertTrue(propsBasePage.getCloneMenu().isElementPresent(), "Clone button NOT present");

        propsBasePage.getProductRow().click();
        sa.assertTrue(propsBasePage.getEditButton().isElementPresent(), "Edit button NOT present");
        sa.assertTrue(propsBasePage.getDeactivateButton().isElementPresent(), "Deactivate button NOT present");

        propsBasePage.scrollDown();
        sa.assertAll();
    }

    @Test(description = "New product forms test", priority = 2)
    public void productFormsTest() {

        SoftAssert sa = new SoftAssert();
        PropsBasePage propsBasePage = new PropsBasePage(getDriver());

        propsBasePage.getPlusButton().click();
        propsBasePage.getPlusMenu(0).click();
        sa.assertTrue(propsBasePage.getPageURL().contains("new-product-d2c"));

        propsBasePage.navigateBack();

        propsBasePage.getPlusButton().click();
        propsBasePage.getPlusMenu(1).click();
        sa.assertTrue(propsBasePage.getPageURL().contains("new-product-iap"));
        sa.assertAll();
    }

    @Test(description = "Live product warning modals", priority = 2)
    public void warningModalsTest() {

        SoftAssert sa = new SoftAssert();
        PropsBasePage propsBasePage = new PropsBasePage(getDriver());

        propsBasePage.getLiveProduct().click();

        sa.assertTrue(propsBasePage.getEditButton().isElementPresent(), "Edit button NOT present");
        propsBasePage.getEditButton().click();
        sa.assertTrue(propsBasePage.getEditWarningModal().isElementPresent(), "Warning modal for editing NOT present");

        propsBasePage.navigateBack();
        propsBasePage.getLiveProduct().click();

        sa.assertTrue(propsBasePage.getDeactivateButton().isElementPresent(), "Deactivate button NOT present");
        propsBasePage.getDeactivateButton().click();
        sa.assertTrue(propsBasePage.getDeactivateWarningModal().isElementPresent(), "Warning modal for deactivation NOT present");

        sa.assertAll();

    }

    @Test(description = "Third party products access", priority = 2)
    public void thirdPartyProductAccessTest() {

        SoftAssert sa = new SoftAssert();
        PropsBasePage propsBasePage = new PropsBasePage(getDriver());

        propsBasePage.getPlusButton().click();
        propsBasePage.getPlusMenu(1).click();
        propsBasePage.scrollDown();
        sa.assertTrue(propsBasePage.isUrlAsExpected("https://props-qa.us-east-1.bamgrid.net/#/new-product-iap"), "Unable to access third party product creation page");

        sa.assertAll();

    }

    @Test(description = "D2C products access", priority = 2)
    public void d2cProductAccessTest() {

        SoftAssert sa = new SoftAssert();
        PropsBasePage propsBasePage = new PropsBasePage(getDriver());

        propsBasePage.getPlusButton().click();
        propsBasePage.getPlusMenu(0).click();
        propsBasePage.scrollDown();
        sa.assertTrue(propsBasePage.isUrlAsExpected("https://props-qa.us-east-1.bamgrid.net/#/new-product-d2c"), "Unable to access D2C product creation page");

        sa.assertAll();

    }

    //unable to discern if products are properly segmented IF certain segments are empty
    @Test(description = "Products segmented by status", priority = 2)
    public void segmentedStatusTest() {

        SoftAssert sa = new SoftAssert();
        PropsBasePage propsBasePage = new PropsBasePage(getDriver());

        String currentStatus = "";
        int countSegments = -1;
        boolean isSegmented = false;

        for (ExtendedWebElement e : propsBasePage.getStatusBadge()) {

            if (!currentStatus.equals(e.getText())) {
                currentStatus = e.getText();
                countSegments++;
            }

        }

        if (countSegments <= 5) {
            isSegmented = true;
        }

        sa.assertTrue(!isSegmented, "Products NOT segmented properly");
        sa.assertAll();

    }

    //test for IAP products only
    //currently no D2C products in queue to be tested, but code should be about the same for them
    @Test(description = "Edit and make products live", priority = 2)
    public void editMakeLiveTest() {

        SoftAssert sa = new SoftAssert();
        PropsBasePage propsBasePage = new PropsBasePage(getDriver());

        for (ExtendedWebElement e : propsBasePage.getStatusBadge()) {
            if (e.getText().equals("Third party queue")) {
                e.click();
                sa.assertTrue(propsBasePage.getApproveLiveButton().isElementPresent(), "Approve live button NOT present");
                propsBasePage.getApproveLiveButton().click();
                //below assert only will be true if dev approval is required
                sa.assertTrue(propsBasePage.getDeactivateWarningModal().isElementPresent(), "Warning modal NOT present");
            }
        }

        sa.assertAll();

    }

}