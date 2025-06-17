package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.utils.DisneyContentApiChecker;
import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.disney.qa.disney.apple.pages.common.DisneyplusLegalIOSPageBase;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.fasterxml.jackson.databind.JsonNode;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.LEGAL_TITLE;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusApplePageBase.class)
public class DisneyPlusAppleTVLegalPage extends DisneyplusLegalIOSPageBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label == \"Legal\"`]")
    private ExtendedWebElement legalTitle;

    public DisneyPlusAppleTVLegalPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        boolean isPresent = legalTitle.isPresent();
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isPresent;
    }

    public ExtendedWebElement getLegalOption() {
        return getDynamicCellByLabel(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.LEGAL_TITLE.getText()));
    }

    public void verifyLegalHeaders() {
        getLocalizationUtils().getLegalHeaders().forEach(header -> {
            LOGGER.info("Verifying header is present: {}", header);
            Assert.assertTrue(isLegalHeadersPresent(header),
                    String.format("Header '%s' was not displayed", header));
        });
    }

    public void verifyLegalOptionExpanded(String option) {
        String expandedHeader = getLocalizationUtils().getLegalDocumentBody(option).split("\\n")[0];
        expandedHeader = expandedHeader.trim();
        Assert.assertTrue(waitUntil(ExpectedConditions.visibilityOfElementLocated(
                        getDynamicAccessibilityId(expandedHeader).getBy()), DEFAULT_EXPLICIT_TIMEOUT),
                expandedHeader + " Expanded Header is not visible");
    }

    public void getAllLegalSectionsScreenshot(String filename, String directory) {
        getLegalTabs().forEach(legalTitle -> {
            String sectionName = legalTitle.getAttribute("name");
            Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE, filename + "_" + sectionName + "_tvOS");
            moveDown(1, 1);
        });
    }

    public List<ExtendedWebElement> getLegalTabs() {
        return findExtendedWebElements(typeButtons.getBy());
    }
}
