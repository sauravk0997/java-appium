package com.disney.qa.disney.apple.pages.common;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.constant.CollectionConstant;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.testng.asserts.SoftAssert;
import java.awt.image.BufferedImage;

public class DisneyPlusBrandIOSPageBase extends DisneyPlusApplePageBase {
    private static final String IMAGES_ARE_THE_SAME_ERROR_MESSAGE = "Images are the same";

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"highEmphasisView\"`]/XCUIElementTypeOther[2]/XCUIElementTypeOther/XCUIElementTypeImage")
    protected ExtendedWebElement collectionBrandImageExpanded;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[$name = 'buttonBack'$]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeImage[3]")
    protected ExtendedWebElement collectionBrandImageCollapsed;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"highEmphasisView\"`]/XCUIElementTypeImage[1]")
    private ExtendedWebElement brandFeaturedImage;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"highEmphasisView\"`]/XCUIElementTypeOther[2]/XCUIElementTypeOther/XCUIElementTypeImage")
    private ExtendedWebElement brandLogoImage;

    public DisneyPlusBrandIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return brandLandingView.isPresent();
    }

    public void clickFirstCarouselPoster() {
        clickContent(3, 1);
        pause(5);
    }

    public void clickFirstNoLiveEvent() {
        clickContent(4, 1);
    }

    public boolean isCollectionBrandImageExpanded() {
        return collectionBrandImageExpanded.isPresent();
    }

    public boolean isCollectionBrandImageCollapsed() {
        return collectionBrandImageCollapsed.isPresent() && !collectionBrandImageExpanded.isPresent(SHORT_TIMEOUT);
    }

    public void swipeInCollectionTillImageExpand(Direction direction, int swipeAttempt) {
        swipeInContainerTillElementIsPresent(brandLandingView, collectionBrandImageExpanded, swipeAttempt, direction);
    }

    public boolean isCollectionImageCollapsedFromSwipe(Direction direction, int swipeAttempt) {
        while (collectionBrandImageExpanded.isPresent(FIVE_SEC_TIMEOUT) && swipeAttempt > 0) {
            swipeInContainer(brandLandingView, direction, 1, 900);
            swipeAttempt--;
        }
        return collectionBrandImageCollapsed.isPresent(FIVE_SEC_TIMEOUT);
    }

    public ExtendedWebElement getBrandFeaturedImage() {
        return brandFeaturedImage;
    }

    public ExtendedWebElement getBrandLogoImage() { return brandLogoImage; }

    public enum Brand {
        DISNEY,
        ESPN,
        HULU,
        MARVEL,
        NATIONAL_GEOGRAPHIC,
        PIXAR,
        STAR_WARS

    }

    public String getBrand(Brand brand) {
        switch (brand) {
            case DISNEY:
                return "Disney";
            case ESPN:
                return "ESPN";
            case HULU:
                return "Hulu";
            case MARVEL:
                return "Marvel";
            case NATIONAL_GEOGRAPHIC:
                return "National Geographic";
            case PIXAR:
                return "Pixar";
            case STAR_WARS:
                return "Star Wars";
            default:
                throw new IllegalArgumentException(
                        String.format("'%s' Brand is not a valid option", brand));
        }
    }

    public void validateSwipeNavigation(CollectionConstant.Collection collection, SoftAssert sa) {
        BufferedImage beginningOfCollection = getElementImage(getCollection(collection));
        swipeLeftInCollectionNumOfTimes(5, collection);
        BufferedImage closeToEndOfCollection = getElementImage(getCollection(collection));
        swipeRightInCollectionNumOfTimes(5, collection);
        sa.assertTrue(areImagesDifferent(beginningOfCollection, closeToEndOfCollection), IMAGES_ARE_THE_SAME_ERROR_MESSAGE);
    }
    public boolean isCollectionTitleDisplayed() {
        return getTypeCellLabelContains(
                getLocalizationUtils().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY,
                        DictionaryKeys.CONTENT_TILE_INTERACT.getText())).isDisplayed();
    }
}
