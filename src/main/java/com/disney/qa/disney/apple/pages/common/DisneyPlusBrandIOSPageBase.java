package com.disney.qa.disney.apple.pages.common;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.constant.CollectionConstant;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.testng.asserts.SoftAssert;
import java.awt.image.BufferedImage;
import java.util.*;

public class DisneyPlusBrandIOSPageBase extends DisneyPlusApplePageBase {
    private static final String IMAGES_ARE_THE_SAME_ERROR_MESSAGE = "Images are the same";

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"highEmphasisView\"`]/XCUIElementTypeOther[3]" +
            "/XCUIElementTypeOther/XCUIElementTypeImage")
    protected ExtendedWebElement collectionBrandImageExpanded;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[$name = 'buttonBack'$]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeImage[3]")
    protected ExtendedWebElement collectionBrandImageCollapsed;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"highEmphasisView\"`]/XCUIElementTypeImage[1]")
    private ExtendedWebElement brandFeaturedImage;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"highEmphasisView\"`]/XCUIElementTypeOther[3]" +
            "/XCUIElementTypeOther/XCUIElementTypeImage")
    private ExtendedWebElement brandLogoImage;

    public DisneyPlusBrandIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return brandLandingView.isPresent();
    }

    public boolean isBrandScreenDisplayed(String brandName) {
        return getDynamicAccessibilityId(
                String.format(getLocalizationUtils().formatPlaceholderString(getLocalizationUtils()
                                .getDictionaryItem(
                                        DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY,
                                        DictionaryKeys.BRAND_LANDING_PAGE_LOAD.getText(),
                                        false),
                        Map.of(BRAND_NAME, brandName))))
                .isPresent();
    }

    public void clickFirstCarouselPoster() {
        clickContent(3, 1);
        pause(5);
    }

    public boolean isCollectionBrandImageCollapsed(String header) {
        return !getExpandedBrandImage(header).isPresent(SHORT_TIMEOUT);
    }

    public void swipeInCollectionTillImageExpand(String header, Direction direction, int swipeAttempt) {
        swipeInContainerTillElementIsPresent(null, getExpandedBrandImage(header), swipeAttempt, direction);
    }

    public boolean isCollectionImageCollapsedFromSwipe(String header, Direction direction, int swipeAttempt) {
        while (getExpandedBrandImage(header).isPresent(FIVE_SEC_TIMEOUT) && swipeAttempt > 0) {
            swipeInContainer(null, direction, 1, 900);
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
        STAR_WARS,
        STAR
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
            case STAR:
                return "Star";
            default:
                throw new IllegalArgumentException(
                        String.format("'%s' Brand is not a valid option", brand));
        }
    }

    public String getBrandDeepLink(Brand brand) {
        switch (brand) {
            case DISNEY:
                return R.TESTDATA.get("disney_prod_disney_brand_page_deeplink");
            case ESPN:
                return R.TESTDATA.get("disney_prod_espn_brand_page_deeplink");
            case HULU:
                return R.TESTDATA.get("disney_prod_hulu_brand_deeplink");
            case MARVEL:
                return R.TESTDATA.get("disney_prod_marvel_brand_page_deeplink");
            case NATIONAL_GEOGRAPHIC:
                return R.TESTDATA.get("disney_prod_national_geographic_brand_page_deeplink");
            case PIXAR:
                return R.TESTDATA.get("disney_prod_pixar_brand_page_deeplink");
            case STAR_WARS:
                return R.TESTDATA.get("disney_prod_star_wars_brand_page_deeplink");
            case STAR:
                return R.TESTDATA.get("disney_prod_star_brand_deeplink");
            default:
                throw new IllegalArgumentException(
                        String.format("'%s' Brand is not a valid option", brand));
        }
    }

    public void validateSwipeNavigation(CollectionConstant.Collection collection, SoftAssert sa) {
        swipeTillCollectionTappable(collection, Direction.UP, 8);
        BufferedImage beginningOfCollection = getElementImage(getCollection(collection));
        swipeLeftInCollectionNumOfTimes(5, collection);
        BufferedImage closeToEndOfCollection = getElementImage(getCollection(collection));
        swipeRightInCollectionNumOfTimes(5, collection);
        sa.assertTrue(areImagesDifferent(beginningOfCollection, closeToEndOfCollection), IMAGES_ARE_THE_SAME_ERROR_MESSAGE);
    }

    public ExtendedWebElement getExpandedBrandImage(String header) {
        return imageLabelContains.format(getLocalizationUtils().formatPlaceholderString(
                getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.BRAND_LANDING_PAGE_LOAD.getText(),
                        false), Map.of(BRAND_NAME, header)));
    }
}
