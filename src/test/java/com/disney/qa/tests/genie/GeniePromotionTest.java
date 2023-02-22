package com.disney.qa.tests.genie;

import com.disney.qa.common.web.SeleniumUtils;
import com.disney.qa.genie.GenieParameter;
import com.disney.qa.genie.GeniePromotionPage;
import com.disney.qa.tests.BaseTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class GeniePromotionTest extends BaseTest {

    private static final ThreadLocal<GeniePromotionPage> promotionPage = new ThreadLocal<>();
    protected static final ThreadLocal<SeleniumUtils> util = new ThreadLocal<>();

    private static final String SSO_USERNAME = GenieParameter.GENIE_ADMIN_EMAIL.getValue();
    private static final String SSO_PASS = GenieParameter.GENIE_ADMIN_PASSWORD.getDecryptedValue();
    private static final String NAME_SPACE = "Disney";
    private static final String ASSOCIATED_COUNTRY = "United States of America";
    private static final String PROMOTION_DESCRIPTION = "ProductDes";

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(){
        promotionPage.set(new GeniePromotionPage(getDriver()));
        promotionPage.get().open(getDriver());
        util.set(new SeleniumUtils(getDriver()));
        promotionPage.get().clickOnLogin();
        promotionPage.get().ssoLogin(SSO_USERNAME, SSO_PASS);
    }

    @Test(description = "Promotion Page Test")
    public void promotionPageTest() {
        SoftAssert softAssert = new SoftAssert();

        promotionPage.get().selectNameSpace(NAME_SPACE);
        promotionPage.get().navigateToPromotion();
        promotionPage.get().clickOnCreateNewPromotion();
        String promotionCodeName = promotionPage.get().enterPromotionCodeName("Automation" + "_" + util.get().createRandomAlphabeticString(5).toUpperCase());
        promotionPage.get().enterPartnershipIdName();
        promotionPage.get().selectAssociatedOffer();
        promotionPage.get().selectAssociatedCountry(ASSOCIATED_COUNTRY);
        promotionPage.get().selectAssociatedSku();
        promotionPage.get().enterPromotionalPartnerName();
        promotionPage.get().enterStartDate();
        promotionPage.get().enterEndDate();
        promotionPage.get().clickOnPromoSaveButton();
        pause(10);//Needed for data to load for assertions below

        softAssert.assertTrue(util.get().verifyUrlText("disney/promotions"), "Expected URL to contain 'disney/promotions'");
        softAssert.assertTrue(promotionPage.get().verifyNewlyAddedPromotionName(promotionCodeName), "New Promotion name does not exist in the table");

        softAssert.assertAll();
    }

    @Test(description = "Verify Change History for SKU")
    public void verifySkuChangeHistory(){
        SoftAssert softAssert = new SoftAssert();

        promotionPage.get().selectNameSpace(NAME_SPACE);
        promotionPage.get().navigateToPromotion();
        promotionPage.get().selectNewlyCreatedPromotion();
        promotionPage.get().clickOnPromotionEditButtonForChangeHistory();
        promotionPage.get().enterDescription(PROMOTION_DESCRIPTION + "Edit");
        promotionPage.get().clickOnPromotionSaveButton();
        pause(5); //Needed for loading data table
        promotionPage.get().selectNewlyCreatedPromotion();
        promotionPage.get().clickOnChangeHistoryButton();
        pause(5); //needed for stability on assertions below

        softAssert.assertTrue(promotionPage.get().getGenericTextEqualsElement("Change History (").isElementPresent(),"'Change History' table is not showing");
        softAssert.assertTrue(promotionPage.get().getSpanContainsText("Promotion").isElementPresent(), "'Promotion' text is not present on Change History table");
        softAssert.assertTrue(promotionPage.get().getChangeHistorySize() >= 2, "More than 2 rows are present after editing change history");

        softAssert.assertAll();
    }
}
