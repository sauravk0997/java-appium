package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.stream.IntStream;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.BTN_DETAILS_RESTART;
import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.DETAILS_WATCHLIST;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusDetailsIOSPageBase.class)
public class DisneyPlusAppleTVDetailsPage extends DisneyPlusDetailsIOSPageBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String DETAILS = "DETAILS";

    @ExtendedFindBy(accessibilityId = "contentSummaryView")
    private ExtendedWebElement contentSummaryView;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"contentDetailsPage\"`]/XCUIElementTypeOther[1]/XCUIElementTypeImage")
    private ExtendedWebElement heroImage;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`focused==1`]" +
            "/**/XCUIElementTypeStaticText[`name=='titleLabel'`]")
    protected ExtendedWebElement extraEpisodeTitle;
    @ExtendedFindBy(iosClassChain =
            "**/XCUIElementTypeScrollView[`name == \"tabBar\"`]/**/XCUIElementTypeButton[`accessible=true`]")
    private ExtendedWebElement tabBarTitles;
    @ExtendedFindBy(accessibilityId = "title")
    private ExtendedWebElement title;
    @ExtendedFindBy(accessibilityId = "seasonView")
    private ExtendedWebElement seasonViewSection;
    @ExtendedFindBy(accessibilityId = "seasonTitle")
    private ExtendedWebElement episodeTitleSection;
    @ExtendedFindBy(accessibilityId = "seasonTotalEpisodes")
    private ExtendedWebElement seasonEpisodeNumber;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCollectionView[$name = 'contentImageView'$]")
    private ExtendedWebElement episodeViewSection;

    public DisneyPlusAppleTVDetailsPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        boolean isPresent = DisneyPlusAppleTVCommonPage.isProd() ? logoImage.isElementPresent() : playButton.isElementPresent();
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

    @Override
    public  ExtendedWebElement getPlayButton() {
        return getTypeButtonByName(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DictionaryKeys.BTN_PLAY.getText()));
    }

    @Override
    public DisneyPlusAppleTVVideoPlayerPage clickPlayButton() {
        getPlayButton().click();
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

    public String getExtraEpisodeTitle() {
        return extraEpisodeTitle.getText();
    }

    @Override
    public DisneyPlusAppleTVVideoPlayerPage clickWatchButton() {
        watchButton.click();
        return new DisneyPlusAppleTVVideoPlayerPage(getDriver());
    }

    @Override
    public boolean isHeroImagePresent() {
        return heroImage.isPresent();
    }

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

    @Override
    public void clickExtrasTab() {
        extrasTab.click();
    }

    @Override
    public boolean isExtrasTabPresent() {
        return extrasTab.isPresent();
    }

    public ExtendedWebElement getDetailsTitleLabel() {
        return titleLabel;
    }

    public ExtendedWebElement getAddToWatchlistText() {
        return getTypeButtonContainsLabel(getAppleTVLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DETAILS_WATCHLIST.getText()));
    }

    public ExtendedWebElement getRestartButton() {
        return getTypeButtonByLabel(getAppleTVLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION, BTN_DETAILS_RESTART.getText()));
    }

    public List<ExtendedWebElement> getTabBarTitleInfo() {
        return findExtendedWebElements(tabBarTitles.getBy());
    }

    public ExtendedWebElement getSeasonViewSection() {
        return seasonViewSection;
    }

    public ExtendedWebElement getEpisodeViewSection() {
        return episodeViewSection;
    }

    public ExtendedWebElement getEpisodeTitleSection() {
        return episodeTitleSection;
    }

    public boolean isListOrdered(String element) {
        List<ExtendedWebElement> titlesFromScreen = findExtendedWebElements(getStaticTextByName(element).getBy());
        return IntStream.range(0, titlesFromScreen.size() - 1)
                .allMatch(i -> titlesFromScreen.get(i).getText().compareTo(titlesFromScreen.get(i + 1).getText()) <= 0);
    }

    public ExtendedWebElement getSeasonEpisodeNumber() {
        return seasonEpisodeNumber;
    }
}
