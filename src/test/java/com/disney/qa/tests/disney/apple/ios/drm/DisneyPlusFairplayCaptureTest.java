package com.disney.qa.tests.disney.apple.ios.drm;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

import com.disney.qa.api.client.requests.content.SetRequest;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusLoginIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusPasswordIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusWelcomeScreenIOSPageBase;
import com.disney.util.ZipUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.zebrunner.agent.core.registrar.Artifact;
import com.zebrunner.carina.utils.R;

public class DisneyPlusFairplayCaptureTest extends DisneyBaseTest {

    private static final String CONTENT_TITLES = "$..title.full..content";

    @Test(groups = TestGroup.PROXY)
    public void fairplayPayloadCapture() throws IOException {
        String slug = R.TESTDATA.get("disney_home_content_class");

        DisneyAccount disneyAccount = getAccountApi().createAccount("Yearly", getCountry(), getLanguage(), "V1");
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPage = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());
        DisneyPlusLoginIOSPageBase disneyPlusLoginIOSPage = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase disneyPlusPasswordIOSPage = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusDetailsIOSPageBase disneyPlusDetailsIOSPage = new DisneyPlusDetailsIOSPageBase(getDriver());

        JsonNode homePageContent = getSearchApi().getPersonalizedCollection(disneyAccount,
                disneyAccount.getProfileLang(), disneyAccount.getCountryCode(), slug, slug).getJsonNode();
        var getAllSetsAndTypes = getContentApiChecker().getSetIdAndType(homePageContent, "ShelfContainer");
        Map.Entry<String, String> entry = getAllSetsAndTypes.entrySet().iterator().next();

        SetRequest setRequest = SetRequest.builder().account(disneyAccount).refType(entry.getValue()).setId(entry.getKey())
                .language(disneyAccount.getProfileLang()).region(disneyAccount.getCountryCode()).build();
        JsonNode set = getSearchApi().getSet(setRequest).getJsonNode().get(0);
        var getSetAssets = getContentApiChecker().queryResponse(set, CONTENT_TITLES);

        clearAppCache();
        handleSystemAlert(IOSUtils.AlertButtonCommand.DISMISS, 10);
        disneyPlusWelcomeScreenIOSPage.clickLogInButton();
        disneyPlusLoginIOSPage.submitEmail(disneyAccount.getEmail());
        disneyPlusPasswordIOSPage.submitPasswordForLogin(disneyAccount.getUserPass());
        disneyPlusWelcomeScreenIOSPage.isOpened();
        disneyPlusWelcomeScreenIOSPage.getDynamicCellByLabel(getSetAssets.get(0)).click();
        disneyPlusDetailsIOSPage.isOpened();
        disneyPlusDetailsIOSPage.clickPlayButton();
        pause(30);

        //        new HARUtils(proxy.get()).printSpecificHarDetails(Stream.of(HARUtils.RequestDataType.URL, HARUtils.RequestDataType.POST_DATA,
        //                        HARUtils.RequestDataType.RESPONSE_DATA).collect(Collectors.toList()),
        //                Collections.singletonList("fairplay/v1/obtain-license"));
        String file = "FairplayLogs_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss")) + "_" + UUID.randomUUID();
        String harPath = file + ".har";
        //        HARUtils.generateValidHarForCharles(proxy.get(), file);
        String baseFile = "FairPlayLogs/";
        FileUtils.moveFile(new File(harPath), new File(baseFile + harPath));
        String pathToZip = file + ".zip";

        ZipUtils.zipDirectory(baseFile, pathToZip);
        Artifact.attachToTest(pathToZip, Path.of(pathToZip));
    }
}
