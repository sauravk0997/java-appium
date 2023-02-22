package com.disney.qa.disney.web.commerce;

import com.disney.qa.api.disney.DisneyHttpHeaders;
import com.disney.qa.disney.DisneyCountryData;
import com.disney.qa.disney.web.DisneyWebKeys;
import com.disney.qa.disney.web.DisneyWebParameters;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.fasterxml.jackson.databind.JsonNode;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import net.lightbody.bmp.BrowserMobProxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusPortabilityPage extends DisneyPlusCommercePage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    protected DisneyCountryData countryData = new DisneyCountryData();

    @FindBy(xpath = "//*[@data-testid='mlp_link_header']")
    private ExtendedWebElement euLoginId;

    @FindBy(xpath = "//*[contains(@class,'input-error')]")
    private ExtendedWebElement dPlusEuInputError;

    @FindBy(xpath = "//*[@data-testid='login-page-signup-cta']")
    private ExtendedWebElement loginPageSignUpCta;

    @FindBy(xpath = "//*[@data-testid='portability-title-text']")
    private ExtendedWebElement portabilityTitleText;

    @FindBy(xpath = "//*[@data-testid='portability-body-text']")
    private ExtendedWebElement portabilityBodyText;

    public static final String PORTABILITY_PASS = DisneyWebParameters.DISNEY_WEB_PORTABILITY_PASS.getValue();

    /**
     * Override to set default browser language on launch
     *
     * @param driver
     */
    public DisneyPlusPortabilityPage(WebDriver driver) {
        super(driver);
    }

    //Booleans

    public boolean isPortabilityTitleTextPresent() {
        waitFor(portabilityTitleText);
        return portabilityTitleText.isElementPresent();
    }

    public boolean isPortabilityBodyPresent() {
        waitFor(portabilityBodyText);
        return portabilityBodyText.isElementPresent();
    }

    public boolean isPortabilityEupLoginNotePresent(JsonNode dictionary) {
        return getDictionaryContainsIgnoreCaseElement(dictionary, DisneyWebKeys.EUP_LOGIN_NOTE.getText()).isElementPresent();
    }

    public boolean isdPlusEuInputErrorPresent() {
        return dPlusEuInputError.isElementPresent();
    }

    public boolean isEuLoginIdPresent() {
        return euLoginId.isElementPresent();
    }

    public boolean isLoginPageSignUpCtaPresent() {
        return loginPageSignUpCta.isElementPresent();
    }

    //Assertions

    public void assertUrlLanguage(SoftAssert sa, String language) {
        LOGGER.info("Url grabbed: {}", getCurrentUrl());
        sa.assertTrue(getCurrentUrl().contains("/eu/" + language.toLowerCase()),
                String.format("Url does not contain language: %s", getCurrentUrl()));
    }

    public void assertUrlLanguageLogin(SoftAssert sa, String language) {
        LOGGER.info("Url grabbed: {}", getCurrentUrl());
        sa.assertTrue(getCurrentUrl().contains(language.toLowerCase()),
                String.format("Url does not contain language: %s", getCurrentUrl()));
    }

    public void assertEuLoginId(SoftAssert sa) {
        if (BRANCH.equalsIgnoreCase("LIVE") && ENVIRONMENT.equals("PROD")) {
            sa.assertTrue(isEuLoginIdPresent(),
                    String.format("Main Id not present on : %s", getCurrentUrl()));
        } else {
            LOGGER.info("Skipping assertEuLoginId for environments other than PROD");
        }
    }

    public void assertFalseLogoRedirect(SoftAssert sa) {
        if (BRANCH.equalsIgnoreCase("LIVE") && ENVIRONMENT.equals("PROD")) {
            sa.assertFalse(isEuLoginIdPresent(),
                    String.format("Eu-Login Id is present on : %s", getCurrentUrl()));
        } else {
            LOGGER.info("Skipping assertFalseLogoRedirect for environments other than PROD");
        }
    }

    public void assertPreviewUrl(SoftAssert sa) {
        if (BRANCH.equalsIgnoreCase("LIVE") && ENVIRONMENT.equals("PROD")) {
            assertUrlContains(sa, "preview.disneyplus.com");
        } else {
            LOGGER.info("Skipping assertPreviewUrl for environments other than PROD");
        }
    }

    public BrowserMobProxy handlePortabilityStartup(String locale, String code, String country, String overrideCountry) {
        BrowserMobProxy proxy = null;

        if (BRANCH.equalsIgnoreCase("LIVE") && ENVIRONMENT.equals("PROD")) {
            R.CONFIG.put("locale", overrideCountry, true);
            proxy = environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(overrideCountry, code, country));
        } else {
            proxy = environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, code, country));

            if (!locale.equalsIgnoreCase(overrideCountry)) {
                proxy.addHeader(DisneyHttpHeaders.BAMTECH_DSS_PHYSICAL_COUNTRY_OVERRIDE, overrideCountry);
            }
        }

        return proxy;
    }

    public BrowserMobProxy handlePortabilityStartup(String overrideCountry, String code, String country) {
        BrowserMobProxy proxy = null;
        R.CONFIG.put("locale", overrideCountry, true);
        proxy = environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(overrideCountry, code, country));

        return proxy;
    }
}
