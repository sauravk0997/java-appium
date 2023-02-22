package com.disney.qa.tests.props;

import com.disney.qa.props.PropsBasePage;
import com.disney.qa.props.PropsParameter;
import com.disney.qa.tests.BaseTest;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class PropsFinanceReviewTest extends BaseTest {

    public static final String INITIAL_PAGE = PropsParameter.PROPS_INITIAL.getValue();
    public static final String FINANCE = "Finance review";

    @BeforeTest
    public void beforeTest() {
        PropsBasePage page = new PropsBasePage(getDriver());
        page.open(getDriver(), INITIAL_PAGE);
        page.login(5);
    }

    @Test(description = "View Only Product Fields test for FI Approvers", priority = 2)
    public void testViewOnly() {

        SoftAssert sa = new SoftAssert();
        PropsBasePage propsBasePage = new PropsBasePage(getDriver());

        boolean isPresent = false;

        for (ExtendedWebElement e : propsBasePage.getStatusBadge()) {
            if (e.getText().equals(FINANCE)) {
                isPresent = true;
                e.click();
                sa.assertTrue(propsBasePage.getFinanceMetadata().isElementPresent(), "Metadata NOT present");
            }
        }

        sa.assertTrue(!isPresent, "No finance review products present");

        sa.assertAll();

    }

    @Test(description = "Required Finance Fields test for FI Approvers", priority = 2)
    public void testRequiredFinanceFields() {

        SoftAssert sa = new SoftAssert();
        PropsBasePage propsBasePage = new PropsBasePage(getDriver());

        boolean isPresent = false;

        for (ExtendedWebElement e : propsBasePage.getStatusBadge()) {
            if (e.getText().equals(FINANCE)) {
                isPresent = true;
                e.click();

                sa.assertTrue(propsBasePage.getIssueID().isElementPresent(), "SAP MPM Issue ID field NOT present");
                sa.assertTrue(propsBasePage.getAccountingRecurringType().isElementPresent(), "Accounting Recurring Type field NOT present");
                propsBasePage.getAccountingRecurringType().select(1);
                sa.assertTrue(propsBasePage.getTaxCategory().isElementPresent(), "Tax Category field NOT present");
                propsBasePage.getTaxCategory().select(1);
                sa.assertTrue(propsBasePage.getIsTaxableSwitch().isElementPresent(), "Is Taxable toggle NOT present");

                sa.assertTrue(propsBasePage.getSaveAndButton().isElementPresent(), "Save and Approve button NOT present");
            }
        }

        sa.assertTrue(!isPresent, "No finance review products present");

        sa.assertAll();

    }

    // test case says to verify that products made live are sent to appropriate databases -- unsure how to check if that happens
    // currently, test only clicks into product requiring financial approval and checks if it is able to be approved
    @Test(description = "Write to DB Environments", priority = 2)
    public void writeToDBTest() {

        SoftAssert sa = new SoftAssert();
        PropsBasePage propsBasePage = new PropsBasePage(getDriver());

        for (ExtendedWebElement e : propsBasePage.getStatusBadge()) {
            if (e.getText().equals(FINANCE)) {
                e.click();
            }
        }

        sa.assertTrue(propsBasePage.getApproveLiveButton().isElementPresent(), "Approve live button NOT present");

        sa.assertAll();

    }

}
