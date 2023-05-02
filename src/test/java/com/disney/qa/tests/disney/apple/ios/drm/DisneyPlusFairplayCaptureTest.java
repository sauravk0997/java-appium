package com.disney.qa.tests.disney.apple.ios.drm;

import com.browserup.bup.proxy.CaptureType;
import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.client.requests.content.SetRequest;
import com.disney.qa.api.config.DisneyMobileConfigApi;
import com.disney.qa.api.dictionary.DisneyLocalizationUtils;
import com.disney.qa.api.disney.DisneyContentApiChecker;
import com.disney.qa.api.disney.DisneyHttpHeaders;
import com.disney.qa.api.disney.DisneyParameters;
import com.disney.qa.api.disney.DisneyPlusOverrideKeys;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.search.DisneySearchApi;
import com.disney.qa.carina.GeoedgeProxyServer;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.common.utils.MobileUtilsExtended;
import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusLoginIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusPasswordIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusWelcomeScreenIOSPageBase;
import com.disney.qa.tests.BaseMobileTest;
import com.disney.util.HARUtils;
import com.disney.util.ZipUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.zebrunner.carina.proxy.browserup.ProxyPool;
import com.zebrunner.carina.utils.R;
import com.zebrunner.agent.core.registrar.Artifact;
import io.appium.java_client.ios.IOSDriver;
import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DisneyPlusFairplayCaptureTest extends BaseMobileTest {

    private static final String COUNTRY = R.CONFIG.get("locale");
    private static final String LANGUAGE = R.CONFIG.get("language");

    private static final String CONTENT_TITLES = "$..title.full..content";

    @Test
    public void fairplayPayloadCapture() throws IOException {
        initiateProxy(new Locale("", COUNTRY).getDisplayCountry(), R.CONFIG.get("proxy_port"),
                CaptureType.REQUEST_BINARY_CONTENT, CaptureType.REQUEST_CONTENT,
                CaptureType.RESPONSE_BINARY_CONTENT, CaptureType.RESPONSE_CONTENT);
        String slug = R.TESTDATA.get("disney_home_content_class");
        DisneyContentApiChecker apiProvider = new DisneyContentApiChecker();
        DisneyAccountApi accountApi = new DisneyAccountApi("apple", R.CONFIG.get("environment"), "disney");
        DisneySearchApi searchApi = new DisneySearchApi("apple", R.CONFIG.get("environment"), "disney");

        DisneyAccount disneyAccount = accountApi.createAccount("Yearly", COUNTRY, LANGUAGE, "V1");
        DisneyLocalizationUtils disneyLanguageUtils = new DisneyLocalizationUtils(COUNTRY,LANGUAGE, "ios", DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()), "disney");
        DisneyMobileConfigApi configApi = new DisneyMobileConfigApi("ios", DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()), "disney", new MobileUtilsExtended().getInstalledAppVersion());
        disneyLanguageUtils.setDictionaries(configApi.getDictionaryVersions());
        DisneyPlusApplePageBase.setDictionary(disneyLanguageUtils);
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPage = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());
        DisneyPlusLoginIOSPageBase disneyPlusLoginIOSPage = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase disneyPlusPasswordIOSPage = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusDetailsIOSPageBase disneyPlusDetailsIOSPage = new DisneyPlusDetailsIOSPageBase(getDriver());


        JsonNode homePageContent = searchApi.getPersonalizedCollection(disneyAccount,
                disneyAccount.getProfileLang(), disneyAccount.getCountryCode(), slug, slug).getJsonNode();
        var getAllSetsAndTypes = apiProvider.getSetIdAndType(homePageContent, "ShelfContainer");
        Map.Entry<String, String> entry = getAllSetsAndTypes.entrySet().iterator().next();

        SetRequest setRequest = SetRequest.builder().account(disneyAccount).refType(entry.getValue()).setId(entry.getKey()).language(disneyAccount.getProfileLang()).region(disneyAccount.getCountryCode()).build();
        JsonNode set = searchApi.getSet(setRequest).getJsonNode().get(0);
        var getSetAssets = apiProvider.queryResponse(set, CONTENT_TITLES);

        proxy.get().newHar();
        ((IOSDriver) getCastedDriver()).resetApp();
        new IOSUtils().handleSystemAlert(IOSUtils.AlertButtonCommand.DISMISS, 10);
        disneyPlusWelcomeScreenIOSPage.clickLogInButton();
        disneyPlusLoginIOSPage.submitEmail(disneyAccount.getEmail());
        disneyPlusPasswordIOSPage.submitPasswordForLogin(disneyAccount.getUserPass());
        disneyPlusWelcomeScreenIOSPage.isOpened();
        disneyPlusWelcomeScreenIOSPage.getDynamicCellByLabel(getSetAssets.get(0)).click();
        disneyPlusDetailsIOSPage.isOpened();
        disneyPlusDetailsIOSPage.clickPlayButton();
        pause(30);

        new HARUtils(proxy.get()).printSpecificHarDetails(Stream.of(HARUtils.RequestDataType.URL, HARUtils.RequestDataType.POST_DATA,
                HARUtils.RequestDataType.RESPONSE_DATA).collect(Collectors.toList()),
                Collections.singletonList("fairplay/v1/obtain-license"));
        String file = "FairplayLogs_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss")) + "_" + UUID.randomUUID();
        String harPath = file + ".har";
        HARUtils.generateValidHarForCharles(proxy.get(), file);
        String baseFile = "FairPlayLogs/";
        FileUtils.moveFile(new File(harPath), new File(baseFile + harPath));
        String pathToZip = file + ".zip";

        ZipUtils.zipDirectory(baseFile, pathToZip);
        Artifact.attachToTest(pathToZip, Path.of(pathToZip));
    }

    public void initiateProxy(String country, String devicePort, CaptureType... captureTypes) {
        GeoedgeProxyServer geoedgeProxyFreshInstance = new GeoedgeProxyServer();
        geoedgeProxyFreshInstance.setProxyHostForSelenoid();
        R.CONFIG.put("browserup_proxy", "true");
        getDriver();

        Map<String, String> headers = new HashMap<>();
        headers.put(DisneyHttpHeaders.BAMTECH_VPN_OVERRIDE, DisneyPlusOverrideKeys.OVERRIDE_KEY);
        headers.put(DisneyHttpHeaders.BAMTECH_OVERRIDE_SUPPORTED_LOCATION, DisneyPlusOverrideKeys.SUPPORTED_LOCATION_OVERRIDE_KEY);
        headers.put(DisneyHttpHeaders.BAMTECH_GEO_ALLOW, DisneyPlusOverrideKeys.GEO_ALLOW_KEY);

        Set<CaptureType> captureTypeSet = new HashSet<>();
        IntStream.range(0, captureTypes.length - 1).forEach(type -> captureTypeSet.add(captureTypes[type]));

        try {
            ProxyPool.registerProxy(geoedgeProxyFreshInstance.getGeoedgeProxy(country));
            proxy.set(ProxyPool.getProxy());
            proxy.get().addHeaders(headers);
            proxy.get().enableHarCaptureTypes(captureTypes);
            proxy.get().start(Integer.parseInt(devicePort));
        } catch (NullPointerException e) {
            e.printStackTrace();
            Assert.fail(String.format("Proxy Cannot be started for country '%s'. Manual validation is required.", country));
        }
    }
}
