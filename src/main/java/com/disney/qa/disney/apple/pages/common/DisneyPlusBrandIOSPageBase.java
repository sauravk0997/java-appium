package com.disney.qa.disney.apple.pages.common;
import com.disney.qa.common.constant.CollectionConstant;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.testng.asserts.SoftAssert;

public class DisneyPlusBrandIOSPageBase extends DisneyPlusApplePageBase {
    private static final String END_TILE_NOT_FOUND_MESSAGE = "End tile of last collection not found";
    private static final String FIRST_TILE_NOT_FOUND_MESSAGE = "First tile of last collection not found";

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"brandLandingView\"`]")
    private ExtendedWebElement brandLandingView;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"brandLandingView\"`]/XCUIElementTypeImage[1]")
    private ExtendedWebElement brandFeaturedImage;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"brandLandingView\"`]/XCUIElementTypeOther[2]/XCUIElementTypeOther/XCUIElementTypeImage")
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

    public ExtendedWebElement getBrandFeaturedImage() {
        return brandFeaturedImage;
    }

    public ExtendedWebElement getBrandLogoImage() { return brandLogoImage; }

    public boolean isEndCollectionPresent(CollectionConstant.Collection collection, String tileTitle) {
        int count = 10;
        while (!typeCellLabelContains.format(tileTitle).isPresent(SHORT_TIMEOUT) && count >= 0) {
            swipeLeftInCollection(collection);
            count--;
        }
        return typeCellLabelContains.format(tileTitle).isPresent(SHORT_TIMEOUT);
    }

    public boolean isFirstCollectionPresent(CollectionConstant.Collection collection, String tileTitle) {
        int count = 10;
        while (!typeCellLabelContains.format(tileTitle).isPresent(SHORT_TIMEOUT) && count >= 0) {
            swipeRightInCollection(collection);
            count--;
        }
        return typeCellLabelContains.format(tileTitle).isPresent(SHORT_TIMEOUT);
    }

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

    public void validateBrand(Brand brand, SoftAssert sa) {
        switch (brand) {
            case DISNEY:
                swipePageTillElementPresent(getCollection(CollectionConstant.Collection.BRANDS_DISNEY_END_COLLECTION),
                        10, null, Direction.UP, 300);
                sa.assertTrue(isEndCollectionPresent(CollectionConstant.Collection.BRANDS_DISNEY_END_COLLECTION,
                        "Pirates of the Caribbean: Dead Men Tell No Tales"), END_TILE_NOT_FOUND_MESSAGE);
                sa.assertTrue(isFirstCollectionPresent(CollectionConstant.Collection.BRANDS_DISNEY_END_COLLECTION,
                                "Jungle Cruise"), FIRST_TILE_NOT_FOUND_MESSAGE);
                break;
            case PIXAR:
                swipePageTillElementPresent(getCollection(CollectionConstant.Collection.BRANDS_PIXAR_END_COLLECTION),
                        10, null, Direction.UP, 300);
                sa.assertTrue(isEndCollectionPresent(CollectionConstant.Collection.BRANDS_PIXAR_END_COLLECTION,
                        "Toy Story"), END_TILE_NOT_FOUND_MESSAGE);
                sa.assertTrue(isFirstCollectionPresent(CollectionConstant.Collection.BRANDS_PIXAR_END_COLLECTION,
                                "Cars"), FIRST_TILE_NOT_FOUND_MESSAGE);
                break;
            case MARVEL:
                swipePageTillElementPresent(getCollection(CollectionConstant.Collection.BRANDS_MARVEL_END_COLLECTION),
                        10, null, Direction.UP, 300);
                sa.assertTrue(isEndCollectionPresent(CollectionConstant.Collection.BRANDS_MARVEL_END_COLLECTION,
                        "ONE SHOT"), END_TILE_NOT_FOUND_MESSAGE);
                sa.assertTrue(isFirstCollectionPresent(CollectionConstant.Collection.BRANDS_MARVEL_END_COLLECTION,
                        "X-MEN"), FIRST_TILE_NOT_FOUND_MESSAGE);
                break;
            case STAR_WARS:
                swipePageTillElementPresent(getCollection(CollectionConstant.Collection.BRANDS_STAR_WARS_END_COLLECTION),
                        10, null, Direction.UP, 300);
                sa.assertTrue(isEndCollectionPresent(CollectionConstant.Collection.BRANDS_STAR_WARS_END_COLLECTION,
                        "Clone Wars"), END_TILE_NOT_FOUND_MESSAGE);
                sa.assertTrue(isFirstCollectionPresent(CollectionConstant.Collection.BRANDS_STAR_WARS_END_COLLECTION,
                        "Droids"), FIRST_TILE_NOT_FOUND_MESSAGE);
                break;
            case NATIONAL_GEOGRAPHIC:
                swipePageTillElementPresent(getCollection(CollectionConstant.Collection.BRANDS_NATIONAL_GEOGRAPHIC_END_COLLECTION),
                        10, null, Direction.UP, 300);
                sa.assertTrue(isEndCollectionPresent(CollectionConstant.Collection.BRANDS_NATIONAL_GEOGRAPHIC_END_COLLECTION,
                        "National Geographic Sharks"), END_TILE_NOT_FOUND_MESSAGE);
                sa.assertTrue(isFirstCollectionPresent(CollectionConstant.Collection.BRANDS_NATIONAL_GEOGRAPHIC_END_COLLECTION, "Exploring Our World"));
                break;
            default:
                throw new IllegalArgumentException(
                        String.format("'%s' Brand is not a valid option", brand));
        }
    }
}
