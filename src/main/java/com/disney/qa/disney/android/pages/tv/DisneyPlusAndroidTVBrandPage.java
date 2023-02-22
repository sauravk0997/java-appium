package com.disney.qa.disney.android.pages.tv;

import com.disney.qa.api.client.requests.content.CollectionRequest;
import com.disney.qa.api.client.requests.content.SetRequest;
import com.disney.qa.api.client.responses.content.ContentCollection;
import com.disney.qa.api.client.responses.content.ContentSet;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.search.DisneySearchApi;
import com.disney.qa.api.search.sets.DisneyCollectionSet;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.common.utils.androidtv.AndroidTVUtils;
import com.disney.qa.disney.android.pages.common.DisneyPlusBrandPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.asserts.SoftAssert;

import java.awt.image.BufferedImage;
import java.time.Duration;
import java.util.List;

@DeviceType(pageType = DeviceType.Type.ANDROID_TV, parentClass = DisneyPlusBrandPageBase.class)
public class DisneyPlusAndroidTVBrandPage extends DisneyPlusBrandPageBase {

    @FindBy(xpath = "//*[@resource-id='com.disney.disneyplus:id/collectionRecyclerView']/*[%d]/*[2]/*/*")
    private ExtendedWebElement assetPoster;

    @FindBy(id = "brandBackgroundPlayerView")
    private ExtendedWebElement playerView;

    public DisneyPlusAndroidTVBrandPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        boolean isPresent = playerView.isElementPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }

    public BufferedImage getBrandPageImage() {
        return new UniversalUtils().getElementImage(brandBackground);
    }

    public void selectBrandTileFromHeroCarousel(int index) {
        AndroidTVUtils androidTVUtils = new AndroidTVUtils(getDriver());
        androidTVUtils.keyPressTimes(AndroidTVUtils::pressDown, 1, 1);
        androidTVUtils.keyPressTimes(AndroidTVUtils::pressRight, index, 1);
        UniversalUtils.captureAndUpload(getCastedDriver());
        androidTVUtils.pressSelect();
    }

    public void traverseAndVerifyContent(String slug, DisneySearchApi disneySearchApi, DisneyAccount account, SoftAssert sa) {
        AndroidTVUtils androidTVUtils = new AndroidTVUtils(getDriver());
        CollectionRequest collectionRequest = CollectionRequest.builder().collectionType("PersonalizedCollection")
                .account(account).region(account.getCountryCode()).language(account.getProfileLang()).contentClass(slug).slug(slug).build();
        ContentCollection collection = disneySearchApi.getCollection(collectionRequest);
        List<DisneyCollectionSet> sets = collection.getCollectionSetsInfo();

        // Check the first tile in the first 8 rows
        for (int setIndex = 0; setIndex < 9; setIndex++) {
            UniversalUtils.captureAndUpload(getCastedDriver());

            SetRequest setRequest = SetRequest.builder().account(account).language(account.getProfileLang())
                    .region(account.getCountryCode()).setId(sets.get(setIndex).getRefId()).refType(sets.get(setIndex).getRefType()).build();
            ContentSet set = disneySearchApi.getSet(setRequest);
            int shelfIndex = setIndex == 0 ? 1 : 2;

            List<String> rowContentExpected = set.getTitles();
            List<ExtendedWebElement> rowContentActual = findExtendedWebElements(shelfItem.format(shelfIndex).getBy());

            sa.assertTrue(androidTVUtils.getContentDescription(rowContentActual.get(0)).contains(rowContentExpected.get(0)),
                    androidTVUtils.getContentDescription(rowContentActual.get(0)) + " title of focused tile should be " + rowContentExpected.get(0) + " for row: " + shelfIndex);

            androidTVUtils.keyPressTimes(AndroidTVUtils::pressDown, 1, 1);
        }
    }

    public void stopBrandLandingVideo() {
        new AndroidTVUtils(getDriver()).pressLeft();
    }

    public void waitUntilFirstTileIsFocused() {
        FluentWait<WebDriver> wait = new FluentWait<>(getDriver())
                .withTimeout(Duration.ofSeconds(LONG_TIMEOUT * 2))
                .pollingEvery(Duration.ofSeconds(ONE_SEC_TIMEOUT))
                .withMessage("First Tile should be focused")
                .ignoring(NoSuchElementException.class)
                .ignoring(TimeoutException.class)
                .ignoring(WebDriverException.class);
        wait.until(it -> new AndroidTVUtils(getDriver()).isElementFocused(new DisneyPlusAndroidTVCommonPage(getDriver()).mediaPoster));
        UniversalUtils.captureAndUpload(getCastedDriver());
    }

    public int getNumberOfRowsDisplayed() {
        UniversalUtils.captureAndUpload(getCastedDriver());
        return findExtendedWebElements(shelfTitle.getBy()).size();
    }

    public enum BrandItems {
        GET_NUMBER_OF_ROWS("$..containers.length()"),
        GET_ASSET_TITLES_FROM_SET("$..items..texts..[?(@.field == 'title' && @.type == 'full')].content"),
        GET_ITEM_NUMBER_IN_SET("$..items.length()"),
        GET_SET_TITLE("$..SetBySetId.texts..[?(@.field == 'title')].content"),
        GET_CONTAINER_SET_ID("$..containers[%d]..setId");

        private String dictionaryKey;

        BrandItems(String dictionaryKey) {
            this.dictionaryKey = dictionaryKey;
        }

        public String getText() {
            return dictionaryKey;
        }
    }
}
