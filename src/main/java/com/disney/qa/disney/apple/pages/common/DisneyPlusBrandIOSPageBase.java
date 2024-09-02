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
        PIXAR,
        MARVEL,
        STAR_WARS,
        NATIONAL_GEOGRAPHIC
    }

    public String getBrand(Brand brand) {
        switch (brand) {
            case DISNEY:
                return "Disney";
            case PIXAR:
                return "Pixar";
            case MARVEL:
                return "Marvel";
            case STAR_WARS:
                return "Star Wars";
            case NATIONAL_GEOGRAPHIC:
                return "National Geographic";
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

    public void validateBrand(Brand brand, SoftAssert sa) {
        switch (brand) {
            case DISNEY:
                validateSwipeNavigation(CollectionConstant.Collection.BRANDS_DISNEY_ORIGINALS, sa);
                break;
            case PIXAR:
                validateSwipeNavigation(CollectionConstant.Collection.BRANDS_PIXAR_FEATURED, sa);
                break;
            case MARVEL:
                validateSwipeNavigation(CollectionConstant.Collection.BRANDS_MARVEL_FEATURED, sa);
                break;
            case STAR_WARS:
                validateSwipeNavigation(CollectionConstant.Collection.BRANDS_STAR_WARS_ORIGINALS, sa);
                break;
            case NATIONAL_GEOGRAPHIC:
                validateSwipeNavigation(CollectionConstant.Collection.BRANDS_NATIONAL_GEOGRAPHIC_FEATURED, sa);
                break;
            default:
                throw new IllegalArgumentException(
                        String.format("'%s' Brand is not a valid option", brand));
        }
    }

    public boolean isCollectionTitleDisplayed() {
        return getTypeCellLabelContains(
                getDictionary().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY,
                        DictionaryKeys.CONTENT_TILE_INTERACT.getText())).isDisplayed();
    }
}
