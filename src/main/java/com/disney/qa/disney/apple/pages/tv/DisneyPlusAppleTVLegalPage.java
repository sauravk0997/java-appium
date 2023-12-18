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
    private ExtendedWebElement NoticeOfRightButton;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label == \"Notice of Right to Opt Out of Sale/Sharing\"`][2]")
    private ExtendedWebElement NoticeOfRightTitle;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label == \"Some states provide residents (or, in some cases, their authorized agents) with the right to opt out of “targeted advertising,” “selling,” or “sharing” of personal information. Please visit Your US State Privacy Rights (privacy.twdc.com/usstates), including the Your California Privacy Rights section, for more information about your rights and our privacy practices.  You or your legally authorized agent can uncheck the box to opt out of these activities on this digital property consistent with applicable law.\"`]")
    private ExtendedWebElement NoticeOfRightContent;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label == \"Selling, Sharing, Targeted Advertising  \"`][1]")
    private ExtendedWebElement SellingSharingButton;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label == \"Selling, Sharing, Targeted Advertising  \"`][2]")
    private ExtendedWebElement SellingSharingTitle;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label == \"Selling, Sharing, Targeted Advertising\"`]")
    private ExtendedWebElement SellingSharingCheckboxLabel;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label == \"We may use personal information to support “targeted advertising,” “selling,” or “sharing,” as defined by applicable privacy laws, which may result in third parties receiving your personal information. Please note that, your opt-out choice is specific to this property, on this device. If you access other digital properties of the Walt Disney Family of Companies, you will need to make your election for each property and device. Also, if you clear your device settings, you may need to opt out again on this property. \"`]")
    private ExtendedWebElement SellingSharingContent1;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label == \"In addition to the above checkbox, you may choose to provide the information requested in this opt-out form (usprivacy.disney.com/dnssmpi), which may enable us to take action on your opt-out election more broadly than just on this digital property. \"`]")
    private ExtendedWebElement SellingSharingContent2;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label == \"You may also choose to enable online, where available, a universal tool that automatically communicates your opt-out preferences, such as the Global Privacy Control (“GPC”). We will process the GPC signal as a request to opt out. \"`]")
    private ExtendedWebElement SellingSharingContent3;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label == \"If you opt out, you may continue to see advertising, including ads that may be based on personal information processed before you opted out. \"`]")
    private ExtendedWebElement SellingSharingContent4;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label == \"You also may have rights to opt out from certain third parties selling and sharing your personal information. You will need to separately exercise your opt-out rights with regard to each, which you may do through this IAB opt-out list (iabprivacy.com/optout.html). You may also wish to use other available online tools to limit various types of interest-based advertising or tracking. \"`]")
    private ExtendedWebElement SellingSharingContent5;

    @ExtendedFindBy(accessibilityId = "To learn more, visit \"Do Not Sell or Share My Personal Information\" and \"Targeted Advertising\" Opt-Out Rights (privacy.thewaltdisneycompany.com/en/dnssmpi).")
    private ExtendedWebElement SellingSharingContent6;

    public DisneyPlusAppleTVLegalPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        boolean isPresent = legalTitle.isPresent();
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
            String document = getDictionary().getLegalDocumentBody(apiChecker.queryResponse(allDocuments, getAllLabels).get(i));
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
                sa.assertTrue(NoticeOfRightButton.isPresent(),"Notice of Right to Opt Out of Sale/Sharing Button is not been displayed");
                sa.assertTrue(NoticeOfRightTitle.isPresent(),"Notice of Right to Opt Out of Sale/Sharing Title is not been displayed");
                sa.assertTrue(NoticeOfRightContent.isPresent(),"Notice of Right to Opt Out of Sale/Sharing Content is not been displayed");

                moveDown(1,1);
                moveRight(1,1);
                sa.assertTrue(SellingSharingButton.isPresent(),"Selling, Sharing, Targeted Advertising Button is not been displayed");
                sa.assertTrue(SellingSharingTitle.isPresent(),"Selling, Sharing, Targeted Advertising Title is not been displayed");
                sa.assertTrue(SellingSharingCheckboxLabel.isPresent(),"Selling, Sharing, Targeted Advertising Checkbox label is not been displayed");
                sa.assertTrue(SellingSharingContent1.isPresent(),"Selling, Sharing, Targeted Advertising Content paragraph 1 is not been displayed");
                sa.assertTrue(SellingSharingContent2.isPresent(),"Selling, Sharing, Targeted Advertising Content paragraph 2 is not been displayed");
                sa.assertTrue(SellingSharingContent3.isPresent(),"Selling, Sharing, Targeted Advertising Content paragraph 3 is not been displayed");
                moveDown(6,1);
                sa.assertTrue(SellingSharingContent4.isPresent(),"Selling, Sharing, Targeted Advertising Content paragraph 4 is not been displayed");
                sa.assertTrue(SellingSharingContent5.isPresent(),"Selling, Sharing, Targeted Advertising Content paragraph 5 is not been displayed");
                sa.assertTrue(SellingSharingContent6.isPresent(),"Selling, Sharing, Targeted Advertising Content paragraph 6 is not been displayed");

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
