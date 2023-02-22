package com.disney.qa.tests.disney.windows10;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.dictionary.DisneyLocalizationUtils;
import com.disney.qa.api.disney.DisneyParameters;
import com.disney.qa.api.disney.windows10.DisneyWindowsAppLoader;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.windows10.DisneyWindowsHomePage;
import com.disney.qa.disney.windows10.DisneyWindowsLoginPage;
import com.disney.qa.disney.windows10.DisneyWindowsPasswordPage;
import com.disney.qa.disney.windows10.DisneyWindowsWelcomePage;
import com.disney.qa.tests.BaseTest;
import com.qaprosoft.carina.core.foundation.utils.R;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.concurrent.TimeoutException;

@SuppressWarnings("squid:S2187")
public class DisneyWindowsBaseTest extends BaseTest {

    protected static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final DisneyWindowsAppLoader disneyWindowsAppLoader = new DisneyWindowsAppLoader();
    protected ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<>();
    protected ThreadLocal<DisneyAccountApi> accountApi = new ThreadLocal<>();
    protected ThreadLocal<DisneyLocalizationUtils> languageUtils = new ThreadLocal<>();
    protected String language = R.CONFIG.get("language");
    protected String country = R.CONFIG.get("locale");

    @BeforeMethod(alwaysRun = true)
    public void setUp() throws URISyntaxException, IOException, JSONException, TimeoutException {
        languageUtils.set(new DisneyLocalizationUtils(country, language, "tvos", "prod", "disney"));
        accountApi.set(new DisneyAccountApi("microsoft", DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()), "disney"));
        disneyAccount.set(accountApi.get().createAccount("Yearly", languageUtils.get().getLocale(), languageUtils.get().getUserLanguage(), "V1"));
        Path pathToDisneyBuilds = disneyWindowsAppLoader.downloadBuildFromGitHub();
        disneyWindowsAppLoader.installAppCert(pathToDisneyBuilds);
        disneyWindowsAppLoader.installApp(pathToDisneyBuilds);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        disneyWindowsAppLoader.uninstallApp();
    }

    public void login() {
        DisneyWindowsWelcomePage disneyWindowsWelcomePage = new DisneyWindowsWelcomePage(getDriver());
        DisneyWindowsLoginPage disneyWindowsLoginPage = new DisneyWindowsLoginPage(getDriver());
        DisneyWindowsPasswordPage disneyWindowsPasswordPage = new DisneyWindowsPasswordPage(getDriver());
        disneyWindowsWelcomePage.clickLogin();
        disneyWindowsLoginPage.isOpened();
        disneyWindowsLoginPage.enterEmail(disneyAccount.get().getEmail());
        disneyWindowsLoginPage.clickContinueBtn();
        disneyWindowsPasswordPage.isOpened();
        disneyWindowsPasswordPage.enterPassword(disneyAccount.get().getUserPass());
        disneyWindowsPasswordPage.clickLoginBtn();
        new DisneyWindowsHomePage(getDriver()).isPixarPresent();
    }
}
