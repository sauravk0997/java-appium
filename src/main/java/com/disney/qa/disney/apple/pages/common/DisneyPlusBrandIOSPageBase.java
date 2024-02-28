package com.disney.qa.disney.apple.pages.common;
import com.disney.qa.common.constant.CollectionConstant;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.testng.asserts.SoftAssert;

import java.awt.image.BufferedImage;

public class DisneyPlusBrandIOSPageBase extends DisneyPlusApplePageBase {

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
                BufferedImage disneyOriginalsBeginning = getElementImage(getCollection(CollectionConstant.Collection.BRANDS_DISNEY_ORIGINALS));
                swipeLeftInCollectionNumOfTimes(5, CollectionConstant.Collection.BRANDS_DISNEY_ORIGINALS);
                BufferedImage disneyOriginalsCloseToEnd = getElementImage(getCollection(CollectionConstant.Collection.BRANDS_DISNEY_ORIGINALS));
                swipeRightInCollectionNumOfTimes(5, CollectionConstant.Collection.BRANDS_DISNEY_ORIGINALS);
                BufferedImage disneyOriginalsBackToBeginning = getElementImage(getCollection(CollectionConstant.Collection.BRANDS_DISNEY_ORIGINALS));
                sa.assertTrue(areImagesDifferent(disneyOriginalsBeginning, disneyOriginalsCloseToEnd), "Images are the same.");
                sa.assertTrue(areImagesTheSame(disneyOriginalsBeginning, disneyOriginalsBackToBeginning, 10), "Images are not the same");
                break;
            case PIXAR:
                BufferedImage pixarFeaturedBeginning = getElementImage(getCollection(CollectionConstant.Collection.BRANDS_PIXAR_FEATURED));
                swipeLeftInCollectionNumOfTimes(5, CollectionConstant.Collection.BRANDS_PIXAR_FEATURED);
                BufferedImage pixarFeaturedCloseToEnd = getElementImage(getCollection(CollectionConstant.Collection.BRANDS_PIXAR_FEATURED));
                swipeRightInCollectionNumOfTimes(5, CollectionConstant.Collection.BRANDS_PIXAR_FEATURED);
                BufferedImage pixarFeaturedBackToBeginning = getElementImage(getCollection(CollectionConstant.Collection.BRANDS_PIXAR_FEATURED));
                sa.assertTrue(areImagesDifferent(pixarFeaturedBeginning, pixarFeaturedCloseToEnd), "Images are the same.");
                sa.assertTrue(areImagesTheSame(pixarFeaturedBeginning, pixarFeaturedBackToBeginning, 10), "Images are not the same");
                break;
            case MARVEL:
                BufferedImage marvelFeaturedBeginning = getElementImage(getCollection(CollectionConstant.Collection.BRANDS_MARVEL_FEATURED));
                swipeLeftInCollectionNumOfTimes(3, CollectionConstant.Collection.BRANDS_MARVEL_FEATURED);
                BufferedImage marvelFeaturedCloseToEnd = getElementImage(getCollection(CollectionConstant.Collection.BRANDS_MARVEL_FEATURED));
                swipeRightInCollectionNumOfTimes(3, CollectionConstant.Collection.BRANDS_MARVEL_FEATURED);
                BufferedImage marvelFeaturedBackToBeginning = getElementImage(getCollection(CollectionConstant.Collection.BRANDS_MARVEL_FEATURED));
                sa.assertTrue(areImagesDifferent(marvelFeaturedBeginning, marvelFeaturedCloseToEnd), "Images are the same.");
                sa.assertTrue(areImagesTheSame(marvelFeaturedBeginning, marvelFeaturedBackToBeginning, 10), "Images are not the same");
                break;
            case STAR_WARS:
                BufferedImage starWarsOriginalsBeginning = getElementImage(getCollection(CollectionConstant.Collection.BRANDS_STAR_WARS_ORIGINALS));
                swipeLeftInCollectionNumOfTimes(5, CollectionConstant.Collection.BRANDS_STAR_WARS_ORIGINALS);
                BufferedImage starWarsOriginalsCloseToEnd = getElementImage(getCollection(CollectionConstant.Collection.BRANDS_STAR_WARS_ORIGINALS));
                swipeRightInCollectionNumOfTimes(5, CollectionConstant.Collection.BRANDS_STAR_WARS_ORIGINALS);
                BufferedImage starWarsOriginalsBackToBeginning = getElementImage(getCollection(CollectionConstant.Collection.BRANDS_STAR_WARS_ORIGINALS));
                sa.assertTrue(areImagesDifferent(starWarsOriginalsBeginning, starWarsOriginalsCloseToEnd), "Images are the same.");
                sa.assertTrue(areImagesTheSame(starWarsOriginalsBeginning, starWarsOriginalsBackToBeginning, 10), "Images are not the same");
                break;
            case NATIONAL_GEOGRAPHIC:
                BufferedImage nationalGeographicFeaturedBeginning = getElementImage(getCollection(CollectionConstant.Collection.BRANDS_NATIONAL_GEOGRAPHIC_FEATURED));
                swipeLeftInCollectionNumOfTimes(5, CollectionConstant.Collection.BRANDS_NATIONAL_GEOGRAPHIC_FEATURED);
                BufferedImage nationalGeographicFeaturedCloseToEnd = getElementImage(getCollection(CollectionConstant.Collection.BRANDS_NATIONAL_GEOGRAPHIC_FEATURED));
                swipeRightInCollectionNumOfTimes(5, CollectionConstant.Collection.BRANDS_NATIONAL_GEOGRAPHIC_FEATURED);
                BufferedImage nationalGeographicFeaturedBackToBeginning = getElementImage(getCollection(CollectionConstant.Collection.BRANDS_NATIONAL_GEOGRAPHIC_FEATURED));
                sa.assertTrue(areImagesDifferent(nationalGeographicFeaturedBeginning, nationalGeographicFeaturedCloseToEnd), "Images are the same.");
                sa.assertTrue(areImagesTheSame(nationalGeographicFeaturedBeginning, nationalGeographicFeaturedBackToBeginning, 10), "Images are not the same");
                break;
            default:
                throw new IllegalArgumentException(
                        String.format("'%s' Brand is not a valid option", brand));
        }
    }
}
