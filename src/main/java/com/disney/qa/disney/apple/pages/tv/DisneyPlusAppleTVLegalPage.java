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
public class DisneyPlusAppleTVLegalPage extends DisneyPlusApplePageBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static String errorMessage = "'%s' is not shown";
    private static String assertionMessage = "%s is focused after selecting the menu";

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label == \"Legal\"`]")
    private ExtendedWebElement legalTitle;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == \"Privacy Policy\"`]")
    private ExtendedWebElement privacyPolicy;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == \"Subscriber Agreement\"`]")
    private ExtendedWebElement subscriberAgreement;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == \"Your US State Privacy Rights\"`]")
    private ExtendedWebElement yourUSPrivacyRights;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == \"Do Not Sell or Share My Personal Information\"`]")
    private ExtendedWebElement doNotSellMyPersonalInfo;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label == \"Notice of Right to Opt Out of Sale/Sharing\"`][1]")
    private ExtendedWebElement noticeOfRightButton;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label == \"Notice of Right to Opt Out of Sale/Sharing\"`][2]")
    private ExtendedWebElement noticeOfRightTitle;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label == \"Selling, Sharing, Targeted Advertising  \"`][1]")
    private ExtendedWebElement sellingSharingButton;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label == \"Selling, Sharing, Targeted Advertising  \"`][2]")
    private ExtendedWebElement sellingSharingTitle;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label == \"Selling, Sharing, Targeted Advertising\"`]")
    private ExtendedWebElement sellingSharingCheckboxLabel;

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
        DisneyplusLegalIOSPageBase legalPage = new DisneyplusLegalIOSPageBase(getDriver());

        getLocalizationUtils().getLegalHeaders().forEach(header -> {
            LOGGER.info("Verifying header is present: {}", header);
            Assert.assertTrue(legalPage.isLegalHeadersPresent(header),
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

    public void areAllLegalDocumentsPresentAndScrollable(String siteConfig, SoftAssert sa, DisneyContentApiChecker apiChecker) {
        String allLegalDocuments = "links[*]";
        String getAllLabels = "$..label";
        String getAllDocumentCodes = "$..documentCode";
        String lineFeed = "[\\r\\n]+";
        Set<String> legalButtons = new LinkedHashSet<>();
        Set<String> legalDocuments = new LinkedHashSet<>();
        String legalTitle = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, LEGAL_TITLE.getText());
        JsonNode allDocuments = getLocalizationUtils().getLegalItems(siteConfig, allLegalDocuments);

        IntStream.range(0, allDocuments.size()).forEach(i -> {
            legalButtons.add(apiChecker.queryResponse(allDocuments, getAllLabels).get(i));
            String document = getLocalizationUtils().getLegalDocumentBody(apiChecker.queryResponse(allDocuments, getAllLabels).get(i));
            legalDocuments.add(document);
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
                    case "Your US State Privacy Rights":
                        sa.assertFalse(isFocused(yourUSPrivacyRights), String.format(assertionMessage, labels.get(i)));
                        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
                        moveDown(25, 1);
                        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
                        break;
                    default:
                        LOGGER.info(String.format("'%s' is not a valid Legal page selection", labels.get(i)));
                }

                sa.assertTrue(isDynamicAccessibilityIDElementPresent(documentArray[documentArray.length - 1]), String.format(errorMessage, documentArray[documentArray.length - 1]));
                moveLeft(1, 1);
                Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
            } else {
                sa.assertTrue(isFocused(doNotSellMyPersonalInfo), String.format(assertionMessage, labels.get(i)));
                clickSelect();
                Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
                sa.assertTrue(noticeOfRightButton.isPresent(),"Notice of Right to Opt Out of Sale/Sharing Button is not been displayed");
                sa.assertTrue(noticeOfRightTitle.isPresent(),"Notice of Right to Opt Out of Sale/Sharing Title is not been displayed");

                moveDown(1,1);
                moveRight(1,1);
                sa.assertTrue(sellingSharingButton.isPresent(),"Selling, Sharing, Targeted Advertising Button is not been displayed");
                sa.assertTrue(sellingSharingTitle.isPresent(),"Selling, Sharing, Targeted Advertising Title is not been displayed");
                sa.assertTrue(sellingSharingCheckboxLabel.isPresent(),"Selling, Sharing, Targeted Advertising Checkbox label is not been displayed");

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
