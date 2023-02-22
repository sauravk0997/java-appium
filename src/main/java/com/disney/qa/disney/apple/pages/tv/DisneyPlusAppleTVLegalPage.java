package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.disney.DisneyContentApiChecker;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.fasterxml.jackson.databind.JsonNode;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.LEGAL_TITLE;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusApplePageBase.class)
public class DisneyPlusAppleTVLegalPage extends DisneyPlusApplePageBase {

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
        UniversalUtils.captureAndUpload(getCastedDriver());
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
                        UniversalUtils.captureAndUpload(getCastedDriver());
                        moveDown(30, 1);
                        UniversalUtils.captureAndUpload(getCastedDriver());
                        break;
                    case "Subscriber Agreement":
                        sa.assertFalse(isFocused(subscriberAgreement), String.format(assertionMessage, labels.get(i)));
                        UniversalUtils.captureAndUpload(getCastedDriver());
                        moveDown(45, 1);
                        UniversalUtils.captureAndUpload(getCastedDriver());
                        break;
                    case "Your California Privacy Rights":
                        sa.assertFalse(isFocused(yourCAPrivacyRights), String.format(assertionMessage, labels.get(i)));
                        UniversalUtils.captureAndUpload(getCastedDriver());
                        moveDown(10, 1);
                        UniversalUtils.captureAndUpload(getCastedDriver());
                        break;
                    default:
                        LOGGER.info(String.format("'%s' is not a valid Legal page selection", labels.get(i)));
                }

                sa.assertTrue(isDynamicAccessibilityIDElementPresent(documentArray[documentArray.length - 1]), String.format(errorMessage, documentArray[documentArray.length - 1]));
                moveLeft(1, 1);
                UniversalUtils.captureAndUpload(getCastedDriver());
            } else {
                clickSelect();
                sa.assertFalse(isFocused(doNotSellMyPersonalInfo), String.format(assertionMessage, labels.get(i)));
                UniversalUtils.captureAndUpload(getCastedDriver());
                documentArray = documents.get(i).split(lineFeed);
                IntStream.range(0, documentArray.length).forEach(j -> sa.assertTrue(isDynamicAccessibilityIDElementPresent(documentArray[j]), String.format(errorMessage, documentArray[j])));
                UniversalUtils.captureAndUpload(getCastedDriver());
            }
            moveDown(1, 1);
        });

    }

    public void getAllLegalSectionsScreenshot(String filename, ThreadLocal<String> directory) {
        List<ExtendedWebElement> legalTitles = findExtendedWebElements(typeButtons.getBy());
        legalTitles.forEach(legalTitle -> {
            String sectionName = legalTitle.getAttribute("name");
            UniversalUtils.storeScreenshot(getCastedDriver(), filename + "_" + sectionName + "_tvOS", directory.get());
            moveDown(1, 1);
        });
    }
}
