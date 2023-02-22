package com.disney.qa.tests.genie;

import com.disney.qa.common.web.SeleniumUtils;
import com.disney.qa.genie.GenieEntitlementPage;
import com.disney.qa.genie.GenieParameter;
import com.disney.qa.genie.GenieSkuPage;
import com.disney.qa.genie.GenieWebPageKeys;
import com.disney.qa.tests.BaseTest;
import com.zebrunner.agent.core.annotation.Maintainer;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Maintainer("shashem")
public class GenieSkuTest extends BaseTest {
    private static final String SSO_USERNAME = GenieParameter.GENIE_ADMIN_EMAIL.getValue();
    private static final String SSO_PASS = GenieParameter.GENIE_ADMIN_PASSWORD.getDecryptedValue();

    private static final ThreadLocal<GenieSkuPage> genieSkuPage = new ThreadLocal<>();
    private static final ThreadLocal<GenieEntitlementPage> genieEntitlement = new ThreadLocal<>();
    protected static final ThreadLocal<SeleniumUtils> util = new ThreadLocal<>();

    private static final String NAME_SPACE = "Disney";
    private static final String SKU_KEY_NAME = "disney_automation_sku_test";
    private static final String SKU_COUNTRY = "United States of America";
    private static final String PRODUCT_DESCRIPTION = "ProductDes";

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(){
        genieSkuPage.set(new GenieSkuPage(getDriver()));
        genieEntitlement.set(new GenieEntitlementPage(getDriver()));
        util.set(new SeleniumUtils(getDriver()));
        genieSkuPage.get().open(getDriver());
        genieSkuPage.get().clickOnLogin();
        genieSkuPage.get().ssoLogin(SSO_USERNAME, SSO_PASS);
    }

    @AfterMethod (alwaysRun = true)
    public void testExecutionTime(ITestResult executeTime) {
       util.get().getTestExecutionTime(executeTime);
    }

    @Test(description = "Verify SKU required field")
    public void verifyEmptyNameField(){
        SoftAssert softAssert = new SoftAssert();

        genieSkuPage.get().selectNameSpace(NAME_SPACE);
        genieSkuPage.get().navigateToCreateNewSkuButton();

        softAssert.assertTrue(util.get().verifyUrlText("skus/create"), "Expected URL contains 'skus/create'");

        genieSkuPage.get().clickOnSkuSaveButton();

        softAssert.assertTrue(genieSkuPage.get().getRequiredFieldErrorMsg("name").isElementPresent(), "Expected error for name field");
        softAssert.assertTrue(genieSkuPage.get().getRequiredFieldErrorMsg("description").isElementPresent(), "Expected error for description field");
        softAssert.assertTrue(genieSkuPage.get().getRequiredFieldErrorMsg("key").isElementPresent(), "Expected error for key field");
        softAssert.assertTrue(genieSkuPage.get().getRequiredFieldErrorMsg("product").isElementPresent(), "Expected error for product field");
        softAssert.assertTrue(genieSkuPage.get().getNewRequiredFieldErrorMsg("platform").isElementPresent(), "Expected error for platform field");
        softAssert.assertTrue(genieSkuPage.get().getRequiredFieldErrorMsg("associatedCountries").isElementPresent(), "Expected error for country field");

        softAssert.assertAll();
    }

    @Test(description = "Create SKU")
    public void createSku(){
        SoftAssert softAssert = new SoftAssert();

        genieSkuPage.get().selectNameSpace(NAME_SPACE);
        genieSkuPage.get().navigateToCreateNewSkuButton();

        softAssert.assertTrue(util.get().verifyUrlText("skus/create"), "Expected URL contains 'skus/create'");

        String skuName = genieSkuPage.get().createSkuName("Test" + "_" + util.get().createRandomAlphabeticString(5).toUpperCase());
        genieSkuPage.get().createSkuDescription("Testing");
        genieSkuPage.get().createSkuKey(SKU_KEY_NAME + "_" + util.get().createRandomAlphabeticString(5));
        genieSkuPage.get().selectProduct("5");
        genieSkuPage.get().selectPlatformType(GenieWebPageKeys.WEB.getKey());
        genieSkuPage.get().selectCountry(SKU_COUNTRY);
        genieSkuPage.get().clickOnSkuSaveButton();
        pause(5);//ensure data grid loading

        softAssert.assertTrue(util.get().verifyUrlText("disney/skus"), "Expected URL contains 'disney/skus'");
        softAssert.assertTrue(genieSkuPage.get().verifyNewlyAddedSkuName(skuName), "New sku name does not exist in the table");

        softAssert.assertAll();
    }

    @Test(description = "Verify Change History for SKU")
    public void verifySkuChangeHistory(){
        SoftAssert softAssert = new SoftAssert();

        genieSkuPage.get().selectNameSpace(NAME_SPACE);
        genieSkuPage.get().navigateToSkus();
        genieSkuPage.get().selectNewlyCreatedSku();
        genieSkuPage.get().clickOnEditButtonForChangeHistory();
        genieSkuPage.get().enterDescription(PRODUCT_DESCRIPTION + "Edit");
        genieSkuPage.get().clickOnSkuSaveButton();
        pause(5);
        genieSkuPage.get().selectNewlyCreatedSku();
        genieSkuPage.get().clickOnChangeHistoryButton();
        pause(5); //needed for stability on assertions below

        softAssert.assertTrue(genieSkuPage.get().getGenericTextEqualsElement("Change History (").isElementPresent(),"'Change History' table is not showing");
        softAssert.assertTrue(genieSkuPage.get().getSpanContainsText("Sku").isElementPresent(), "'Sku' text is not present on Change History table");
        softAssert.assertTrue(genieSkuPage.get().getChangeHistorySize() >= 2, "More than 2 rows are present after editing change history");

        softAssert.assertAll();
    }
}
