package com.disney.qa.tests.props;

import com.disney.qa.props.PropsBasePage;
import com.disney.qa.props.PropsParameter;
import com.disney.qa.tests.BaseTest;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class PropsUsersTest extends BaseTest {

    public static final String INITIAL_PAGE = PropsParameter.PROPS_INITIAL.getValue();

    @BeforeTest
    public void beforeTest() {
        PropsBasePage page = new PropsBasePage(getDriver());
        page.open(getDriver(), INITIAL_PAGE);
        page.login(3);
    }

    @Test(description = "User permission assignment test", priority = 2)
    public void permissionAssignmentTest() {

        SoftAssert sa = new SoftAssert();
        PropsBasePage propsBasePage = new PropsBasePage(getDriver());

        propsBasePage.getHeroIcon().click();
        propsBasePage.getHeroIconMenu(2).click();

        propsBasePage.getUserEmail().getElement().sendKeys("test@disneystreaming.com");
        propsBasePage.getUserRoleDropdown().click();

        for (ExtendedWebElement e : propsBasePage.getMenuOptionList()) {
            if(e.getText().equals("Basic")) {
                e.click();
            }
        }

        propsBasePage.getUserPartnerDropdown().click();

        for (ExtendedWebElement e : propsBasePage.getMenuOptionList()) {
            if(e.getText().equals("Disney")) {
                e.click();
            }
        }

        sa.assertTrue(propsBasePage.getSaveAndButton().isClickable(), "Submit button NOT clickable");
        sa.assertAll();

    }

}
