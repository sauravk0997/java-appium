package com.disney.qa.tests.props;

import com.disney.qa.props.PropsBasePage;
import com.disney.qa.props.PropsParameter;
import com.disney.qa.tests.BaseTest;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class PropsDashboardTest extends BaseTest {

    public static final String INITIAL_PAGE = PropsParameter.PROPS_INITIAL.getValue();

    @BeforeTest
    public void beforeTest() {
        PropsBasePage page = new PropsBasePage(getDriver());
        page.open(getDriver(), INITIAL_PAGE);
        page.login(1);
    }

    @Test(description = "Dashboard options test", priority = 1)
    public void dashboardSearchOptionsTest() {

        SoftAssert sa = new SoftAssert();
        PropsBasePage propsBasePage = new PropsBasePage(getDriver());

        PropsBasePage page = new PropsBasePage(getDriver());
        page.open(getDriver(), INITIAL_PAGE);

        sa.assertTrue(propsBasePage.getPropsLogo().isElementPresent(), "Props logo NOT present");
        sa.assertTrue(propsBasePage.getPartnerSelector().isElementPresent(), "Partner selector NOT present");
        sa.assertTrue(propsBasePage.getSectionSelector().isElementPresent(), "Section selector NOT present");
        sa.assertTrue(propsBasePage.getSearchBar().isElementPresent(), "Search bar NOT present");
        sa.assertTrue(propsBasePage.getPlusButton().isElementPresent() , "Plus button NOT present");
        sa.assertTrue(propsBasePage.getHeroIcon().isElementPresent(), "Hero icon NOT present");

        for (ExtendedWebElement e : propsBasePage.getFilterDropdown()) {
            sa.assertTrue(e.isClickable(), "Filter dropdown '" + e.getText() + "' not present");
        }

        // check if number of products appears on dashboard
        boolean isNumbered = false;
        char[] checkNumber = propsBasePage.getSelectAllToggle().getText().toCharArray();
        for (char c : checkNumber) {
            if (Character.isDigit(c)) {
                isNumbered = true;
                return;
            }
        }

        sa.assertTrue(isNumbered, "User unable to see how many products there are");
        sa.assertTrue(propsBasePage.getProductReport().isClickable(), "Product report button NOT clickable");

        sa.assertAll();

    }

    @Test(description = "Ability to clone products", priority = 2)
    public void cloneFromDashboardTest() {

        SoftAssert sa = new SoftAssert();
        PropsBasePage propsBasePage = new PropsBasePage(getDriver());

        propsBasePage.getProductRow().hover();
        propsBasePage.getCloneMenu().click();
        propsBasePage.getCloneButton().click();

        sa.assertTrue(propsBasePage.getProductName().getText().contains("CLONE"), "Name of clone does NOT start with CLONE");
        sa.assertTrue(propsBasePage.getSaveAndButton().isElementPresent(), "Save button NOT present");

        propsBasePage.navigateBack();
        propsBasePage.getProductRow().click();
        propsBasePage.getCloneMenu().click();
        propsBasePage.getCloneButton().click();

        sa.assertTrue(propsBasePage.getProductName().getText().contains("CLONE"), "Name of clone does NOT start with CLONE");
        sa.assertTrue(propsBasePage.getSaveAndButton().isElementPresent(), "Save button NOT present");

        sa.assertAll();

    }

    @Test(description = "Generate csv file", priority = 3)
    public void generateCsvTest() {

        SoftAssert sa = new SoftAssert();
        PropsBasePage propsBasePage = new PropsBasePage(getDriver());

        sa.assertTrue(propsBasePage.getProductReport().isElementPresent(), "Product report button (...) NOT present");
        propsBasePage.getProductReport().click();
        sa.assertTrue(propsBasePage.getGenerateCsv().isElementPresent(), "Generate CSV option NOT present");
        propsBasePage.getGenerateCsv().click();

        sa.assertAll();

    }

}
