package com.disney.qa.tests.genie;

import com.disney.qa.common.web.SeleniumUtils;
import com.disney.qa.genie.GenieEntitlementPage;
import com.disney.qa.genie.GenieParameter;
import com.disney.qa.genie.GenieWebPageKeys;
import com.disney.qa.tests.BaseTest;
import com.zebrunner.agent.core.annotation.Maintainer;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Maintainer("shashem")
public class GenieEntitlementTest extends BaseTest {

    private static final String SSO_USERNAME = GenieParameter.GENIE_ADMIN_EMAIL.getValue();
    private static final String SSO_PASS = GenieParameter.GENIE_ADMIN_PASSWORD.getDecryptedValue();
    private static final String NAME_SPACE = "Disney";
    private static final String DESCRIPTION_TEXT = "Testing";
    private static final String DUPLICATE_NAME_ERROR_MSG = "Name already exists, please use a different name";
    private static final int DESCRIPTION_MAX_LENGTH = 40;
    private static final int NAME_MAX_LENGTH = 27;
    private static final String NAME_SPACE_TO_UNDERSCORE = "new_test_entitlement".toUpperCase();

    private static final ThreadLocal<GenieEntitlementPage> geniePage = new ThreadLocal<>();
    protected static final ThreadLocal<SeleniumUtils> util = new ThreadLocal<>();

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(){
        geniePage.set(new GenieEntitlementPage(getDriver()));
        geniePage.get().open(getDriver());
        util.set(new SeleniumUtils(getDriver()));
        geniePage.get().clickOnLogin();
        geniePage.get().ssoLogin(SSO_USERNAME, SSO_PASS);
    }

    @Test(description = "Entitlement Page Test")
    public void entitlementPageTest() {
        SoftAssert softAssert = new SoftAssert();

        geniePage.get().selectNameSpace(NAME_SPACE);
        geniePage.get().navigateToEntitlementsPage();

        softAssert.assertTrue(geniePage.get().getGenieLogo().isElementPresent(),
                "Genie G logo not present");
        softAssert.assertTrue(geniePage.get().getCreateNewEntitlementButton().isElementPresent(),
                "Create Entitlement button not present");
        softAssert.assertTrue(util.get().verifyUrlText("entitlements"), "Expected URL text not present");

        geniePage.get().clickOnCreateNewEntitlement();

        softAssert.assertTrue(geniePage.get().getNameField().isElementPresent(), "Name field in entitlement form is not present");
        softAssert.assertTrue(geniePage.get().getDescriptionField().isElementPresent(), "Description field in entitlement form is not present");
        softAssert.assertTrue(geniePage.get().getCancelButton().isElementPresent(), "Cancel button is not present");
        softAssert.assertTrue(geniePage.get().getSaveButton().isElementPresent(), "Save button is not present");

        geniePage.get().getCancelButton();
        geniePage.get().clickOnCancelButton();

        softAssert.assertAll();
    }

    @Test(description = "Create new entitlement")
    public void createNewEntitlement(){
        SoftAssert softAssert = new SoftAssert();

        geniePage.get().selectNameSpace(NAME_SPACE);
        geniePage.get().navigateToEntitlementsPage();
        geniePage.get().clickOnCreateNewEntitlement();

        String newlyAddedEntitlement = geniePage.get().createNewEntitlementName("TEST" + util.get().createRandomAlphabeticString(5).toUpperCase());
        geniePage.get().enterDescription("Testing");

        geniePage.get().selectInternalType(1);
        geniePage.get().selectEntitlementCategoryType(GenieWebPageKeys.DISNEY_PLUS_STANDALONE.getKey().toUpperCase());
        geniePage.get().clickOnSaveButton();
        pause(5);//ensure data grid loading

        softAssert.assertTrue(geniePage.get().verifyNewlyCreatedItem(newlyAddedEntitlement), "New entitlement name does not exist in the table");

        softAssert.assertAll();
    }

    @Test(description = "Duplicate Name Error")
    public void verifyDuplicateNameError(){
        SoftAssert softAssert = new SoftAssert();

        geniePage.get().selectNameSpace(NAME_SPACE);
        geniePage.get().navigateToEntitlementsPage();
        geniePage.get().clickOnCreateNewEntitlement();

        String newlyAddedEntitlement = geniePage.get().createNewEntitlementName("TEST" + util.get().createRandomAlphabeticString(5).toUpperCase());
        geniePage.get().enterDescription(DESCRIPTION_TEXT);
        geniePage.get().selectInternalType(1);
        geniePage.get().selectEntitlementCategoryType(GenieWebPageKeys.DISNEY_PLUS_STANDALONE.getKey().toUpperCase());
        geniePage.get().clickOnSaveButton();

        geniePage.get().clickOnCreateNewEntitlement();
        geniePage.get().createNewEntitlementName(newlyAddedEntitlement);
        geniePage.get().enterDescription(DESCRIPTION_TEXT);
        geniePage.get().clickOnSaveButton();

        softAssert.assertTrue(geniePage.get().getRequiredFieldError("name").getText().equals(DUPLICATE_NAME_ERROR_MSG),
                String.format("Duplicate name error message expected: '%s' actual: '%s'",DUPLICATE_NAME_ERROR_MSG,
                        geniePage.get().getRequiredFieldError("name").getText()));

        geniePage.get().clickOnCancelButton();

        softAssert.assertAll();
    }

    @Test(description = "Required Name Field Error")
    public void verifyRequiredNameFieldError(){
        SoftAssert softAssert = new SoftAssert();

        geniePage.get().selectNameSpace(NAME_SPACE);
        geniePage.get().navigateToEntitlementsPage();
        geniePage.get().clickOnCreateNewEntitlement();
        geniePage.get().clickOnSaveButton();

        softAssert.assertTrue(geniePage.get().getRequiredFieldError("name").isElementPresent(), "Required name field error not present");

        geniePage.get().clickOnCancelButton();

        softAssert.assertAll();
    }

    @Test(description = "Required Description Field Error")
    public void verifyRequiredDescriptionFieldError(){
        SoftAssert softAssert = new SoftAssert();

        geniePage.get().selectNameSpace(NAME_SPACE);
        geniePage.get().navigateToEntitlementsPage();
        geniePage.get().clickOnCreateNewEntitlement();
        geniePage.get().createNewEntitlementName("TEST" + util.get().createRandomAlphabeticString(5).toUpperCase());
        geniePage.get().clickOnSaveButton();

        softAssert.assertTrue(geniePage.get().getRequiredFieldError("description").isElementPresent(), "Required description field error not present");

        geniePage.get().clickOnCancelButton();

        softAssert.assertAll();
    }

    @Test(description = "Entitlement Description Character Limit")
    public void verifyEntitlementDescriptionCharacterLimit(){
        SoftAssert softAssert = new SoftAssert();

        geniePage.get().selectNameSpace(NAME_SPACE);
        geniePage.get().navigateToEntitlementsPage();
        geniePage.get().clickOnCreateNewEntitlement();
        geniePage.get().enterDescription("TEST" + util.get().createRandomAlphabeticString(41).toUpperCase());
        int descriptionField = geniePage.get().getDescriptionField().getAttribute("value").length();

        softAssert.assertEquals(descriptionField, DESCRIPTION_MAX_LENGTH, "Description field has more than max length value ");

        geniePage.get().clickOnCancelButton();

        softAssert.assertAll();
    }

    @Test(description = "Entitlement Name Character Limit")
    public void verifyEntitlementNameCharacterLimit(){
        SoftAssert softAssert = new SoftAssert();

        geniePage.get().selectNameSpace(NAME_SPACE);
        geniePage.get().navigateToEntitlementsPage();
        geniePage.get().clickOnCreateNewEntitlement();
        geniePage.get().createNewEntitlementName("TEST" + util.get().createRandomAlphabeticString(30).toUpperCase());
        int nameField = geniePage.get().getNameField().getAttribute("value").length();

        softAssert.assertEquals(nameField, NAME_MAX_LENGTH, "Name field has more than max length value ");

        geniePage.get().clickOnCancelButton();

        softAssert.assertAll();
    }

    @Test(description = "Entitlement Name No Space")
    public void verifyEntitlementNameNoSpace(){
        SoftAssert softAssert = new SoftAssert();

        geniePage.get().selectNameSpace(NAME_SPACE);
        geniePage.get().navigateToEntitlementsPage();
        geniePage.get().clickOnCreateNewEntitlement();
        geniePage.get().createNewEntitlementName("new test entitlement");
        String name = geniePage.get().getNameField().getAttribute("value");

        softAssert.assertEquals(name, NAME_SPACE_TO_UNDERSCORE, "Spaces in name field did not change to underscore ");

        geniePage.get().clickOnCancelButton();

        softAssert.assertAll();
    }
}
