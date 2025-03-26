package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusDetailsIOSPageBase.class)
public class DisneyPlusAppleTVDetailsPage extends DisneyPlusDetailsIOSPageBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String DETAILS = "DETAILS";

    @ExtendedFindBy(accessibilityId = "contentSummaryView")
    private ExtendedWebElement contentSummaryView;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"contentDetailsPage\"`]/XCUIElementTypeOther[1]/XCUIElementTypeImage")
    private ExtendedWebElement heroImage;

    @ExtendedFindBy(accessibilityId = "title")
    private ExtendedWebElement title;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeImage[`name CONTAINS \"backgroundGradient\"`]")
    private ExtendedWebElement backgroundImage;

    public DisneyPlusAppleTVDetailsPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        boolean isPresent = DisneyPlusAppleTVCommonPage.isProd() ? logoImage.isElementPresent() : playButton.isElementPresent();
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isPresent;
    }

    public boolean isLogoPresent() {
        return logoImage.isElementPresent();
    }

    public boolean isContentSummaryView() { return contentSummaryView.isElementPresent(); }
    public boolean isBriefDescriptionPresent(String text) {
        ExtendedWebElement briefDesc = getDynamicAccessibilityId(text);
        return briefDesc.isElementPresent() && briefDesc.getText().chars().count() <= 120;
    }

    public boolean isAnthologyTitlePresent() {
        return getStaticTextByLabel("Dancing with the Stars").isElementPresent();
    }

    public DisneyPlusAppleTVVideoPlayerPage clickPlayButton() {
        getTypeButtonByName(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DictionaryKeys.BTN_PLAY.getText())).click();
        return new DisneyPlusAppleTVVideoPlayerPage(getDriver());
    }

    @Override
    public DisneyPlusAppleTVVideoPlayerPage clickContinueButton() {
        getContinueButton().click();
        return new DisneyPlusAppleTVVideoPlayerPage(getDriver());
    }

    @Override
    public ExtendedWebElement getContinueButton() {
        return getTypeButtonByName(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_CONTINUE_DETAILS.getText()));
    }

    @Override
    public DisneyPlusAppleTVVideoPlayerPage clickWatchButton() {
        watchButton.click();
        return new DisneyPlusAppleTVVideoPlayerPage(getDriver());
    }

    @Override
    public boolean compareEpisodeNum() {
        DisneyPlusAppleTVVideoPlayerPage videoPlayerPage = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        isOpened();
        moveDown(2,1);
        String currentEpisodeNum = getParsedString(getDynamicXpathContainsName(titleLabel.toString()), "0", ". ");
        moveUp(2,2);
        clickWatchButton();
        new DisneyPlusAppleTVLiveEventModalPage(getDriver()).clickWatchLiveButton();
        videoPlayerPage.waitForVideoToStart();
        videoPlayerPage.waitForTvosContentToEnd(350, 20); //Enters playback ~5 min till end
        clickMenuTimes(1,1);
        isOpened();
        moveDown(2,1);
        String recentlyPlayedEpisode = getParsedString(getDynamicXpathContainsName(titleLabel.toString()), "0", ". ");
        return currentEpisodeNum.contains(recentlyPlayedEpisode);
    }

    @Override
    public boolean isHeroImagePresent() {return heroImage.isPresent(); }

    public ExtendedWebElement getMetadataLabel() {
        return getStaticTextLabelName("metaDataLabel");
    }

    public boolean doesMetadataYearContainDetailsTabYear() {
        LOGGER.info("verifying season year range");
        Map<String, String> params = new HashMap<>();
        String[] metadataLabelParts = getMetadataLabel().getText().split(",");
        params.put("metaDataYear(s)", metadataLabelParts[0]);
        moveDown(1,1);
        moveRight(3, 1);
        isFocused(getTypeButtonByLabel(DETAILS));
        String[] detailsTabYear = releaseDate.getText().split(", ");
        return params.get("metaDataYear(s)").contains(detailsTabYear[1]);
    }

    @Override
    public String getDetailsTabTitle() {
        return title.getText();
    }

    public void compareSuggestedTitleToDetailsTabTitle(SoftAssert sa) {
        Map<String, String> params = new HashMap<>();
        moveDown(1,1);
        moveRight(1,1);
        isFocused(getSuggestedTab());
        params.put("suggestedCellTitle", getTabCells().get(0));
        clickFirstTabCell(getSuggestedTab());
        isOpened();
        moveDown(1,1);
        moveRight(2,1);
        isFocused(detailsTab);
        sa.assertTrue(params.get("suggestedCellTitle").contains(getDetailsTabTitle()), "Suggested title is not the same as details title.");
        params.clear();
    }

    public void clickFirstTabCell(ExtendedWebElement tab) {
        String firstTabContentCell = getTabCells().get(0);
        if (isFocused(tab)) {
            moveDown(1,1);
        }
        LOGGER.info("Getting first tab content cell title");
        if (isFocused(getDynamicCellByLabel(firstTabContentCell))) {
            getDynamicCellByLabel(firstTabContentCell).click();
        }
    }

    @Override
    public void compareExtrasTabToPlayerTitle(SoftAssert sa) {
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        moveDown(1,1);
        moveRight(2,1);
        isFocused(extrasTab);
        String extrasCellTitle = titleLabel.getText();
        clickFirstTabCell(getExtrasTab());
        videoPlayer.waitForVideoToStart(10,3);
        sa.assertTrue(extrasCellTitle.equalsIgnoreCase(getTypeOtherByLabel(videoPlayer.getTitleLabel()).getText()), "Extras title is not the same as video player title");
    }

    /**
     * This returns first tab cells in view. This can be used for Suggested or Extras tab.
     * @return - Tab cells
     */
    @Override
    public List<String> getTabCells() {
        return getContentItems(0);
    }

    /**
     * Below are QA env specific methods for DWTS Anthology.
     * To be deprecated when DWTS Test Streams no longer available on QA env (QAA-12244).
     */
    private static final String WATCH = "WATCH";
    private static final String BOOKMARKED = "BOOKMARKED";
    private static final String PLAY = "PLAY";

    @Override
    public DisneyPlusAppleTVVideoPlayerPage clickQAWatchButton() {
        if (getTypeButtonByName(WATCH).isPresent()) {
            getTypeButtonByName(WATCH).click();
        } else {
            getTypeButtonByName("watch").click();
        }
        return new DisneyPlusAppleTVVideoPlayerPage(getDriver());
    }

    @Override
    public void clickExtrasTab() {
        extrasTab.click();
    }

    @Override
    public boolean isExtrasTabPresent() {
        return extrasTab.isPresent();
    }

    public ExtendedWebElement getBackgroundImage() {
        return backgroundImage;

    }

    public ExtendedWebElement getDetailsTitleLabel() {
        return titleLabel;
    }
}
