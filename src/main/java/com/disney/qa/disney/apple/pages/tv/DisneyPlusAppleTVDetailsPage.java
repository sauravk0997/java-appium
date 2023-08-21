package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusDetailsIOSPageBase.class)
public class DisneyPlusAppleTVDetailsPage extends DisneyPlusDetailsIOSPageBase {

    private static final String DETAILS = "DETAILS";

    @ExtendedFindBy(accessibilityId = "trailerButton")
    private ExtendedWebElement trailerButton;

    @ExtendedFindBy(accessibilityId = "contentSummaryView")
    private ExtendedWebElement contentSummaryView;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"contentDetailsPage\"`]/XCUIElementTypeOther[2]/XCUIElementTypeImage")
    private ExtendedWebElement heroImage;

    @ExtendedFindBy(accessibilityId = "title")
    private ExtendedWebElement title;

    public DisneyPlusAppleTVDetailsPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        boolean isPresent = DisneyPlusAppleTVCommonPage.isProd() ? logoImage.isElementPresent() : playButton.isElementPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }

    public boolean isLogoPresent() {
        return logoImage.isElementPresent();
    }

    public boolean isTrailerButtonPresent() {
        return trailerButton.isElementPresent();
    }

    public boolean isContentSummaryView() { return contentSummaryView.isElementPresent(); }
    public boolean isBriefDescriptionPresent(String text) {
        ExtendedWebElement briefDesc = getDynamicAccessibilityId(text);
        return briefDesc.isElementPresent() && briefDesc.getText().chars().count() <= 120;
    }

    public void clickTrailerButton() {
        trailerButton.click();
    }

    public boolean isAnthologyTitlePresent() {
        return getStaticTextByLabel("Dancing with the Stars").isElementPresent();
    }

    @Override
    public DisneyPlusAppleTVVideoPlayerPage clickPlayButton() {
        getTypeButtonByName("play").click();
        return new DisneyPlusAppleTVVideoPlayerPage(getDriver());
    }

    @Override
    public DisneyPlusAppleTVVideoPlayerPage clickContinueButton() {
        getTypeButtonByName("bookmarked").click();
        return new DisneyPlusAppleTVVideoPlayerPage(getDriver());
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
        videoPlayerPage.waitForContentToEnd(350, 20); //Enters playback ~5 min till end
        clickMenuTimes(1,1);
        isOpened();
        moveDown(2,1);
        String recentlyPlayedEpisode = getParsedString(getDynamicXpathContainsName(titleLabel.toString()), "0", ". ");
        return currentEpisodeNum.contains(recentlyPlayedEpisode);
    }

    @Override
    public boolean isWatchButtonPresent() {
        return watchButton.isElementPresent();
    }

    @Override
    public boolean isHeroImagePresent() {return heroImage.isPresent(); }

    public ExtendedWebElement getMetadataLabel() {
        return getStaticTextLabelName("metaDataLabel");
    }

    @Override
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
        sa.assertTrue(params.get("suggestedCellTitle").equalsIgnoreCase(getDetailsTabTitle()), "Suggested title is not the same media title.");
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
        Map<String, String> params = new HashMap<>();
        moveDown(1,1);
        moveRight(2,1);
        isFocused(extrasTab);
        String[] extrasCellTitle = getTabCells().get(0).split(",");
        params.put("extrasCellTitle", extrasCellTitle[0].trim());
        clickFirstTabCell(getExtrasTab());
        videoPlayer.waitForVideoToStart();
        sa.assertTrue(videoPlayer.isOpened(), "Video player did not open.");
        String[] videoPlayerTitle = videoPlayer.getTitleLabel().split("32s");
        sa.assertTrue(params.get("extrasCellTitle").equalsIgnoreCase(videoPlayerTitle[0].trim()),
                "Extras title is not the same as video player title");
    }

    /**
     * This returns first tab cells in view. This can be used for Suggested or Extras tab.
     * @return - Tab cells
     */
    @Override
    public List<String> getTabCells() {
        return getContentItems(0);
    }
}
