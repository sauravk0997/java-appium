package com.disney.qa.tests.genie;

import com.disney.qa.common.web.SeleniumUtils;
import com.disney.qa.genie.GenieParameter;
import com.disney.qa.genie.GeniePartnerPage;
import com.disney.qa.tests.BaseTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class GeniePartnerTest extends BaseTest {
    private static final String SSO_USERNAME = GenieParameter.GENIE_ADMIN_EMAIL.getValue();
    private static final String SSO_PASS = GenieParameter.GENIE_ADMIN_PASSWORD.getDecryptedValue();

    private static final ThreadLocal<GeniePartnerPage> geniePartnerPage = new ThreadLocal<>();
    protected static final ThreadLocal<SeleniumUtils> util = new ThreadLocal<>();

    private static final String NAME_SPACE = "Disney";
    private static final String SKU_COUNTRY = "United States of America";

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(){
        geniePartnerPage.set(new GeniePartnerPage(getDriver()));
        geniePartnerPage.get().open(getDriver());
        util.set(new SeleniumUtils(getDriver()));
        geniePartnerPage.get().clickOnLogin();
        geniePartnerPage.get().ssoLogin(SSO_USERNAME, SSO_PASS);
    }

    @Test(description = "Create new partner")
    public void newPartnerCreation(){
        SoftAssert softAssert = new SoftAssert();

        geniePartnerPage.get().selectNameSpace(NAME_SPACE);
        geniePartnerPage.get().navigateToPartner();

        softAssert.assertTrue(util.get().verifyUrlText("partners"), "Expected URL text not present");

        geniePartnerPage.get().clickOnCreateNewPartner();
        String newPartner = geniePartnerPage.get().createNewPartnerName("Test+" + util.get().createRandomAlphabeticString(5));
        geniePartnerPage.get().enterProviderNameForPartner("Provider+" + util.get().createRandomAlphabeticString(5));
        geniePartnerPage.get().enterProviderDisplayName("Provider+" + util.get().createRandomAlphabeticString(5));
        geniePartnerPage.get().selectCountryForPartner(SKU_COUNTRY);
        geniePartnerPage.get().clickPartnerSaveButton();
        pause(5);//ensure data grid loading

        softAssert.assertTrue(geniePartnerPage.get().verifyNewlyCreatedItem(newPartner), "New partner name does not exist in the table");

        softAssert.assertAll();
    }

    @Test(description = "Verify Change History for Partner")
    public void verifyPartnerChangeHistory(){
        SoftAssert softAssert = new SoftAssert();

        geniePartnerPage.get().selectNameSpace(NAME_SPACE);
        geniePartnerPage.get().navigateToPartner();
        geniePartnerPage.get().selectFirstItemOnFirstRow();
        geniePartnerPage.get().clickOnEditButtonForChangeHistory();
        geniePartnerPage.get().enterProviderDisplayName("+Edit");
        geniePartnerPage.get().clickPartnerSaveButton();
        pause(5); //needed for stability
        geniePartnerPage.get().selectFirstItemOnFirstRow();
        geniePartnerPage.get().clickOnChangeHistoryButton();
        pause(5); //needed for stability on assertions below

        softAssert.assertTrue(geniePartnerPage.get().getGenericTextEqualsElement("Change History (").isElementPresent(),"'Change History' table is not showing");
        softAssert.assertTrue(geniePartnerPage.get().getSpanContainsText("Partner").isElementPresent(), "'Partner' text is not present on change history table");
        softAssert.assertTrue(geniePartnerPage.get().getChangeHistorySize() >= 2, "More than two rows are present for change history after edit");

        softAssert.assertAll();
    }
}
