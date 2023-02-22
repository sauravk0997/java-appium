package com.disney.qa.tests.genie;

import com.disney.qa.common.web.SeleniumUtils;
import com.disney.qa.genie.GenieEntitlementPage;
import com.disney.qa.genie.GenieParameter;
import com.disney.qa.genie.GenieProductPage;
import com.disney.qa.genie.GenieWebPageKeys;
import com.disney.qa.tests.BaseTest;
import com.zebrunner.agent.core.annotation.Maintainer;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Maintainer("shashem")
public class GenieProductTest extends BaseTest {

    private static final String SSO_USERNAME = GenieParameter.GENIE_ADMIN_EMAIL.getValue();
    private static final String SSO_PASS = GenieParameter.GENIE_ADMIN_PASSWORD.getDecryptedValue();

    private static final ThreadLocal<GenieProductPage> genieProduct = new ThreadLocal<>();
    private static final ThreadLocal<GenieEntitlementPage> genieEntitlement = new ThreadLocal<>();
    protected static final ThreadLocal<SeleniumUtils> util = new ThreadLocal<>();

    private static final String NAME_SPACE = "Disney";
    private static final String PRODUCT_NAME = "ProductTest";
    private static final String PRODUCT_DESCRIPTION = "ProductDes";
    private static final int NAME_MAX_LENGTH = 60;
    private static final int DESCRIPTION_MAX_LENGTH = 60;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(){
        genieProduct.set(new GenieProductPage(getDriver()));
        genieEntitlement.set(new GenieEntitlementPage(getDriver()));
        genieProduct.get().open(getDriver());
        util.set(new SeleniumUtils(getDriver()));
        genieProduct.get().clickOnLogin();
        genieProduct.get().ssoLogin(SSO_USERNAME, SSO_PASS);
    }

    @Test(description = "Verify empty name field")
    public void verifyEmptyNameField(){
        SoftAssert softAssert = new SoftAssert();

        genieProduct.get().selectNameSpace(NAME_SPACE);
        genieProduct.get().navigateToProducts();
        genieProduct.get().clickOnCreateNewProduct();
        genieProduct.get().clickProductSaveButton();

        softAssert.assertTrue(genieProduct.get().getRequiredFieldError("name").isElementPresent(), "Expected error for name field");

        softAssert.assertAll();
    }

    @Test(description = "Verify name field character limit")
    public void verifyNameFieldCharacterLimit(){
        SoftAssert softAssert = new SoftAssert();

        genieProduct.get().selectNameSpace(NAME_SPACE);
        genieProduct.get().navigateToProducts();
        genieProduct.get().clickOnCreateNewProduct();
        genieProduct.get().createNewProductName(PRODUCT_NAME + util.get().createRandomAlphabeticString(60).toUpperCase());
        int nameField = genieProduct.get().getNameField().getAttribute("value").length();

        softAssert.assertEquals(nameField, NAME_MAX_LENGTH, "Name field has more than max length value ");

        softAssert.assertAll();
    }

    @Test(description = "Verify description field character limit")
    public void verifyDescriptionFieldCharacterLimit(){
        SoftAssert softAssert = new SoftAssert();

        genieProduct.get().selectNameSpace(NAME_SPACE);
        genieProduct.get().navigateToProducts();
        genieProduct.get().clickOnCreateNewProduct();
        genieProduct.get().enterDescription(PRODUCT_DESCRIPTION + util.get().createRandomAlphabeticString(60).toUpperCase());
        int descriptionField = genieProduct.get().getDescriptionField().getAttribute("value").length();

        softAssert.assertEquals(descriptionField, DESCRIPTION_MAX_LENGTH, "Description field has more than max length value ");

        softAssert.assertAll();
    }

    @Test(description = "Verify Empty Entitlement field")
    public void verifyEmptyEntitlementField(){
        SoftAssert softAssert = new SoftAssert();

        genieProduct.get().selectNameSpace(NAME_SPACE);
        genieProduct.get().navigateToProducts();
        genieProduct.get().clickOnCreateNewProduct();
        genieProduct.get().createNewProductName(PRODUCT_NAME);
        genieProduct.get().enterDescription(PRODUCT_DESCRIPTION);
        genieProduct.get().selectProductType(GenieWebPageKeys.BUNDLE.getKey().toUpperCase());
        genieProduct.get().clickProductSaveButton();

        softAssert.assertTrue(genieProduct.get().getEntitlementReqField().isElementPresent(), "Expected error for empty entitlement field");

        softAssert.assertAll();
    }

    @Test(description = "Create New Product")
    public void createNewProduct(){
        SoftAssert softAssert = new SoftAssert();

        genieEntitlement.get().selectNameSpace(NAME_SPACE);
        genieEntitlement.get().navigateToEntitlementsPage();
        genieEntitlement.get().clickOnCreateNewEntitlement();

        String newEntitlement = genieEntitlement.get().createNewEntitlementName("TEST" + util.get().createRandomAlphabeticString(5).toUpperCase());
        genieEntitlement.get().enterDescription("Testing");
        genieEntitlement.get().selectInternalType(1);
        genieEntitlement.get().selectEntitlementCategoryType(GenieWebPageKeys.DISNEY_PLUS_STANDALONE.getKey().toUpperCase());
        genieEntitlement.get().clickOnSaveButton();
        pause(5); //need to pause here for entitlement to be saved before proceeding to create product

        genieProduct.get().selectNameSpace(NAME_SPACE);
        genieProduct.get().navigateToProducts();
        genieProduct.get().clickOnCreateNewProduct();
        String newProduct = genieProduct.get().createNewProductName(PRODUCT_NAME);
        genieProduct.get().enterDescription(PRODUCT_DESCRIPTION);
        genieProduct.get().selectProductType(GenieWebPageKeys.STANDALONE.getKey().toUpperCase());

        genieProduct.get().selectEntitlement(newEntitlement);
        genieProduct.get().clickProductSaveButton();

        softAssert.assertTrue(genieProduct.get().verifyNewlyCreatedItem(newProduct), "New product name does not exist in the table");

        softAssert.assertAll();
    }

    @Test(description = "Select More Than One Entitlement")
    public void verifyMoreThanOneEntitlementSelection(){
        SoftAssert softAssert = new SoftAssert();

        genieEntitlement.get().selectNameSpace(NAME_SPACE);
        genieEntitlement.get().navigateToEntitlementsPage();
        genieEntitlement.get().clickOnCreateNewEntitlement();

        String newEntitlement = genieEntitlement.get().createNewEntitlementName("TEST" + util.get().createRandomAlphabeticString(5).toUpperCase());
        genieEntitlement.get().enterDescription("Testing");
        genieEntitlement.get().selectInternalType(1);
        genieEntitlement.get().selectEntitlementCategoryType(GenieWebPageKeys.DISNEY_PLUS_STANDALONE.getKey().toUpperCase());
        genieEntitlement.get().clickOnSaveButton();
        pause(5); //need to pause here for entitlement to be saved before proceeding to create product

        genieProduct.get().selectNameSpace(NAME_SPACE);
        genieProduct.get().navigateToProducts();
        genieProduct.get().clickOnCreateNewProduct();
        String newProduct = genieProduct.get().createNewProductName(PRODUCT_NAME);
        genieProduct.get().enterDescription(PRODUCT_DESCRIPTION);
        genieProduct.get().selectProductType(GenieWebPageKeys.STANDALONE.getKey().toUpperCase());

        genieProduct.get().selectEntitlement(newEntitlement);
        genieProduct.get().clickProductSaveButton();

        softAssert.assertTrue(genieProduct.get().verifyNewlyCreatedProduct(newProduct), "New product name does not exist in the table");

        genieProduct.get().clickOnNewlyCreatedProduct();
        pause(5); //to ensure stability of assertion below

        softAssert.assertTrue(genieProduct.get().getNumberOfEntitlements(2), "Expected more than 1 entitlement");

        softAssert.assertAll();
    }

    @Test(description = "Verify Event date field for Premier Access")
    public void verifyEventDateFieldForPremierAccess(){
        SoftAssert softAssert = new SoftAssert();

        genieProduct.get().selectNameSpace(NAME_SPACE);
        genieProduct.get().navigateToProducts();
        genieProduct.get().clickOnCreateNewProduct();
        genieProduct.get().createNewProductName(PRODUCT_NAME);
        genieProduct.get().enterDescription(PRODUCT_DESCRIPTION);
        genieProduct.get().selectProductType(GenieWebPageKeys.PREMIER_ACCESS.getKey().toUpperCase());
        genieProduct.get().selectEntitlement();
        genieProduct.get().clickProductSaveButton();

        softAssert.assertTrue(genieProduct.get().getRequiredFieldError("eventDate").isElementPresent(), "Expected error for event date field");

        softAssert.assertAll();
    }

    @Test(description = "Verify Catalog Date field for Premier Access")
    public void verifyCatalogDateForPremierAccess(){
        SoftAssert softAssert = new SoftAssert();

        genieProduct.get().selectNameSpace(NAME_SPACE);
        genieProduct.get().navigateToProducts();
        genieProduct.get().clickOnCreateNewProduct();
        genieProduct.get().createNewProductName(PRODUCT_NAME);
        genieProduct.get().enterDescription(PRODUCT_DESCRIPTION);
        genieProduct.get().selectProductType(GenieWebPageKeys.PREMIER_ACCESS.getKey().toUpperCase());
        genieProduct.get().selectEntitlement();
        genieProduct.get().clickCatalogField();
        genieProduct.get().clickProductSaveButton();

        softAssert.assertTrue(genieProduct.get().getRequiredFieldError("catalogDate").isElementPresent(), "Expected error for Catalog Date field");

        softAssert.assertAll();
    }

    @Test(description = "Verify Change History for Products")
    public void verifyProductChangeHistory(){
        SoftAssert softAssert = new SoftAssert();

        genieProduct.get().selectNameSpace(NAME_SPACE);
        genieProduct.get().navigateToProducts();
        genieProduct.get().selectFirstItemOnFirstRow();
        genieProduct.get().clickOnEditButtonForChangeHistory();
        genieProduct.get().enterDescription(PRODUCT_DESCRIPTION + "Edit");
        genieProduct.get().clickProductSaveButton();
        pause(5); //needed for stability
        genieProduct.get().selectFirstItemOnFirstRow();
        genieProduct.get().clickOnChangeHistoryButton();
        pause(5); //needed for stability on assertions below

        softAssert.assertTrue(genieProduct.get().getGenericTextEqualsElement("Change History (").isElementPresent(),"'Change History' table is not showing");
        softAssert.assertTrue(genieProduct.get().getSpanContainsText("Product").isElementPresent(), "'Product' text is not present on change history table");
        softAssert.assertTrue(genieProduct.get().getChangeHistorySize() >= 2, "More than two rows are present for change history after edit");

        softAssert.assertAll();
    }
}
