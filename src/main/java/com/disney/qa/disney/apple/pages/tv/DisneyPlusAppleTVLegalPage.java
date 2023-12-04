package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.disney.DisneyContentApiChecker;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.fasterxml.jackson.databind.JsonNode;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class DisneyPlusAppleTVLegalPage extends DisneyPlusApplePageBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static String errorMessage = "'%s' is not shown";
    private static String assertionMessage = "%s is focused after selecting the menu";

    private ExtendedWebElement legalCenterTitle = getDynamicAccessibilityId(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, LEGAL_TITLE.getText()));

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == \"Privacy Policy\"`]")
    private ExtendedWebElement privacyPolicy;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == \"Subscriber Agreement\"`]")
    private ExtendedWebElement subscriberAgreement;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == \"Your California Privacy Rights\"`]")
    private ExtendedWebElement yourCAPrivacyRights;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == \"Do Not Sell My Personal Information\"`]")
    private ExtendedWebElement doNotSellMyPersonalInfo;

    public DisneyPlusAppleTVLegalPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        boolean isPresent = legalCenterTitle.isPresent();
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isPresent;
    }

    public void areAllLegalDocumentsPresentAndScrollable(String siteConfig, SoftAssert sa, DisneyContentApiChecker apiChecker) {
        String allLegalDocuments = "links[*]";
        String getAllLabels = "$..label";
        String getAllDocumentCodes = "$..documentCode";
        String lineFeed = "[\\r\\n]+";
        Set<String> legalButtons = new LinkedHashSet<>();
        Set<String> legalDocuments = new LinkedHashSet<>();
        String legalTitle = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, LEGAL_TITLE.getText());
        JsonNode allDocuments = getDictionary().getLegalItems(siteConfig, allLegalDocuments);

        IntStream.range(0, allDocuments.size()).forEach(i -> {
            legalButtons.add(apiChecker.queryResponse(allDocuments, getAllLabels).get(i));
            String document = getDictionary().getLegalDocumentBody(apiChecker.queryResponse(allDocuments, getAllDocumentCodes).get(i));
            legalDocuments.add(getDictionary().getLegalDocumentBody(document));
        });
        List<String> labels = new ArrayList<>(legalButtons);
        List<String> documents = new ArrayList<>(legalDocuments);
        sa.assertTrue(isFocused(getTypeButtonByLabel(labels.get(0))), String.format("'%s' is not focused by default", labels.get(0)));
        sa.assertTrue(isStaticTextPresentWithScreenShot(legalTitle), String.format(errorMessage, legalTitle));

        IntStream.range(0, labels.size()).forEach(i -> {
            sa.assertTrue(isStaticTextPresentWithScreenShot(labels.get(i)), String.format(errorMessage, labels.get(i)));
            sa.assertTrue(isFocused(getTypeButtonByLabel(labels.get(i))));
            String[] documentArray;
            if (i != 3) {
                documentArray = documents.get(i).split(lineFeed);
                IntStream.range(0, 3).forEach(j -> sa.assertTrue(isDynamicAccessibilityIDElementPresent(documentArray[j]), String.format(errorMessage, documentArray[j])));
                clickSelect();
                switch (labels.get(i)) {
                    case "Privacy Policy":
                        sa.assertFalse(isFocused(privacyPolicy), String.format(assertionMessage, labels.get(i)));
                        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
                        moveDown(30, 1);
                        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
                        break;
                    case "Subscriber Agreement":
                        sa.assertFalse(isFocused(subscriberAgreement), String.format(assertionMessage, labels.get(i)));
                        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
                        moveDown(45, 1);
                        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
                        break;
                    case "Your California Privacy Rights":
                        sa.assertFalse(isFocused(yourCAPrivacyRights), String.format(assertionMessage, labels.get(i)));
                        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
                        moveDown(10, 1);
                        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
                        break;
                    default:
                        LOGGER.info(String.format("'%s' is not a valid Legal page selection", labels.get(i)));
                }

                sa.assertTrue(isDynamicAccessibilityIDElementPresent(documentArray[documentArray.length - 1]), String.format(errorMessage, documentArray[documentArray.length - 1]));
                moveLeft(1, 1);
                Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
            } else {
                clickSelect();
                sa.assertFalse(isFocused(doNotSellMyPersonalInfo), String.format(assertionMessage, labels.get(i)));
                Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
                documentArray = documents.get(i).split(lineFeed);
                IntStream.range(0, documentArray.length).forEach(j -> sa.assertTrue(isDynamicAccessibilityIDElementPresent(documentArray[j]), String.format(errorMessage, documentArray[j])));
                Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
            }
            moveDown(1, 1);
        });

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
