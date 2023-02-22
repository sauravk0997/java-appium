package com.disney.qa.tests.disney.android.tv;

import com.disney.qa.api.client.requests.content.CollectionRequest;
import com.disney.qa.api.client.requests.content.SetRequest;
import com.disney.qa.api.client.responses.content.ContentCollection;
import com.disney.qa.api.client.responses.content.ContentSet;
import com.disney.qa.api.dictionary.DisneyDictionaryKeys;
import com.disney.qa.api.search.assets.DisneyStandardCollection;
import com.disney.qa.api.search.sets.DisneyCollectionSet;
import com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVDiscoverPage;
import com.disney.qa.disney.android.pages.tv.globalnav.DisneyPlusAndroidTVWatchlistPageBase;
import com.disney.qa.disney.android.pages.tv.utility.navhelper.NavHelper;
import com.disney.util.disney.ZebrunnerXrayLabels;
import com.qaprosoft.carina.core.foundation.utils.R;
import io.appium.java_client.android.nativekey.AndroidKey;
import org.testng.SkipException;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.disney.qa.api.disney.DisneyContentIds.END_GAME;
import static com.disney.qa.api.disney.DisneyContentIds.INCREDIBLES2;
import static com.disney.qa.api.disney.DisneyContentIds.LUCA;
import static com.disney.qa.api.disney.DisneyContentIds.MANDALORIAN;
import static com.disney.qa.api.disney.DisneyContentIds.SOUL;
import static com.disney.qa.api.disney.DisneyContentIds.WANDA_VISION;
import static com.disney.qa.tests.disney.DisneyPlusBaseTest.*;

public class DisneyPlusAndroidTVWatchlistTest extends DisneyPlusAndroidTVBaseTest {

    private static final String SIMPSONS_SERIES_ID = "3ZoBZ52QHb4x";
    private static final String MANDALORIAN_SERIES_ID = "3jLIGMDYINqD";

    private static final String MANDALORIAN_TITLE = "The Mandalorian";
    private static final String SIMPSONS_TITLE = "The Simpsons";
    private static final String STAR_FEATURED_LIVE_AND_UPCOMING = "Featured Live and Upcoming";
    private static final String WATCHLIST_ICON_CHANGE_ERROR = "Watchlist icon should update";
    private static final String WATCHLIST_EMPTY_ERROR = "Watchlist is Empty. Content was not added successfully";
    private static final String WATCHLIST_NOT_EMPTY_ERROR = "Watchlist is not Empty. Content was not removed successfully";

    @Test
    public void watchlistContentSpecificToEachProfile() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67702", "XCDQA-67704"));
        String mando = String.format("%s/series/the-mandalorian/%s", R.TESTDATA.get("disney_prod_discover_deeplink"),
                MANDALORIAN_SERIES_ID);
        String simp = String.format("%s/series/the-simpsons/%s", R.TESTDATA.get("disney_prod_discover_deeplink"),
                SIMPSONS_SERIES_ID);

        SoftAssert sa = new SoftAssert();
        disneyAccountApi.get().addProfile(entitledUser.get(), "test", language, null, false);

        loginWithoutHomeCheck(entitledUser.get());
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVProfilePageBase.get().selectDefaultProfileAfterFocused();
        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().isOpened(), HOME_PAGE_LOAD_ERROR);

        getDriver().get(mando);
        sa.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().isOpened(), String.format("Details page for content ID:%s did not launch", MANDALORIAN_SERIES_ID));
        disneyPlusAndroidTVDetailsPageBase.get().clickWatchlistBtn();
        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.WATCHLIST,
                disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        sa.assertTrue(disneyPlusAndroidTVWatchlistPageBase.get().isOpened(), WATCHLIST_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVWatchlistPageBase.get().getWatchlistAssetTitles().forEach(item -> sa.assertEquals(item, MANDALORIAN_TITLE));

        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.PROFILE,
                disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVProfilePageBase.get().pressRight(1);
        disneyPlusAndroidTVProfilePageBase.get().selectFocusedElement();

        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().isOpened(), HOME_PAGE_LOAD_ERROR);
        getDriver().get(simp);
        sa.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().isOpened(), String.format("Details page for content ID:%s did not launch", SIMPSONS_SERIES_ID));
        disneyPlusAndroidTVDetailsPageBase.get().clickWatchlistBtn();
        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.WATCHLIST,
                disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        sa.assertTrue(disneyPlusAndroidTVWatchlistPageBase.get().isOpened(), WATCHLIST_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVWatchlistPageBase.get().getWatchlistAssetTitles().forEach(item -> sa.assertEquals(item, SIMPSONS_TITLE));

        sa.assertAll();
    }

    @Test()
    public void emptyWatchlistState() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67706", "XCDQA-67708"));
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(STA, country, "XCDQA-100934"));
        SoftAssert sa = new SoftAssert();
        NavHelper navHelper = new NavHelper(this.getCastedDriver());

        List<String> expectedEmptyWatchlistText = new ArrayList<>();
        DisneyPlusAndroidTVWatchlistPageBase.getEmptyWatchlistDictionaryKeys().forEach(
                item -> expectedEmptyWatchlistText.add(apiProvider.get().getDictionaryItemValue(fullDictionary.get(), item)));

        login(entitledUser.get());

        navHelper.keyUntilElementFocused(() -> disneyPlusAndroidTVDiscoverPage.get().getNavHome(), AndroidKey.BACK);
        navHelper.keyUntilElementDescContains(() -> disneyPlusAndroidTVDiscoverPage.get().getFocusedNavItem(),
                AndroidKey.DPAD_DOWN, String.valueOf(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.WATCHLIST));
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();
        sa.assertTrue(disneyPlusAndroidTVWatchlistPageBase.get().isOpened(), "Watchlist page did not open");


        List<String> actualEmptyWatchlistTexts = disneyPlusAndroidTVWatchlistPageBase.get().getEmptyWatchlistTexts();

        for (int i = 0; i < expectedEmptyWatchlistText.size(); i++) {
            sa.assertEquals(actualEmptyWatchlistTexts.get(i), expectedEmptyWatchlistText.get(i));
        }

        sa.assertTrue(disneyPlusAndroidTVWatchlistPageBase.get().isEmptyWatchlistIconPresent(),
                "Empty Watchlist + icon not present");

        sa.assertAll();
    }

    @Test
    public void contentAppearsVerticalScrollingGrid(){
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67710"));
        SoftAssert sa = new SoftAssert();
        disneySearchApi.get().addSeriesToWatchlist(entitledUser.get(), MANDALORIAN.getContentId());
        disneySearchApi.get().addSeriesToWatchlist(entitledUser.get(), WANDA_VISION.getContentId());
        disneySearchApi.get().addMovieToWatchlist(entitledUser.get(), SOUL.getContentId());
        disneySearchApi.get().addMovieToWatchlist(entitledUser.get(), INCREDIBLES2.getContentId());

        login(entitledUser.get());
        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.WATCHLIST,
                disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        sa.assertTrue(disneyPlusAndroidTVWatchlistPageBase.get().isOpened(), WATCHLIST_PAGE_LOAD_ERROR);

        sa.assertTrue(disneyPlusAndroidTVCommonPage.get().getAndroidTVUtilsInstance()
                .isFocused(disneyPlusAndroidTVWatchlistPageBase.get().getMediaPosters().get(0)),
                "First item in watchlist was not focused");

        sa.assertEquals(disneyPlusAndroidTVWatchlistPageBase.get().getRowsInWatchlist(),1,
                "Expected only one row");

        //Press down and verify the focus doesn't move from the first item in the watchlist
        disneyPlusAndroidTVCommonPage.get().pressDown(1);
        sa.assertTrue(disneyPlusAndroidTVCommonPage.get().getAndroidTVUtilsInstance()
                        .isFocused(disneyPlusAndroidTVWatchlistPageBase.get().getMediaPosters().get(0)),
                "First item in watchlist was not focused after pressing down");

        //Press right to verify horizontal scroll
        disneyPlusAndroidTVCommonPage.get().pressRight(2);
        sa.assertTrue(disneyPlusAndroidTVCommonPage.get().getAndroidTVUtilsInstance()
                        .isFocused(disneyPlusAndroidTVWatchlistPageBase.get().getMediaPosters().get(2)),
                "Third item in watchlist was not focused");

        //Navigate back to home page and add another item to the watchlist
        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.HOME,
                disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().isOpened(), HOME_PAGE_LOAD_ERROR);
        disneySearchApi.get().addMovieToWatchlist(entitledUser.get(), LUCA.getContentId());

        //Navigate back to the watchlist
        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.WATCHLIST,
                disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        sa.assertTrue(disneyPlusAndroidTVWatchlistPageBase.get().isOpened(), WATCHLIST_PAGE_LOAD_ERROR);

        //Now that we have five items we expect two rows
        sa.assertEquals(disneyPlusAndroidTVWatchlistPageBase.get().getRowsInWatchlist(),2,
                "Expected two rows");

        //Now that there are 5 items and 2 rows we check that press down lands us on second row's first item
        disneyPlusAndroidTVCommonPage.get().pressDown(1);
        sa.assertTrue(disneyPlusAndroidTVCommonPage.get().getAndroidTVUtilsInstance()
                        .isFocused(disneyPlusAndroidTVWatchlistPageBase.get().getMediaPosters().get(4)),
                "Expected to be on second row's first item");

        sa.assertAll();
    }

    @Test
    public void mostRecentAddedItemAppearsFirst(){
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67712", "XCDQA-67724"));
        SoftAssert sa = new SoftAssert();
        disneySearchApi.get().addMovieToWatchlist(entitledUser.get(),END_GAME.getContentId());

        login(entitledUser.get());

        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.WATCHLIST,
                disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        sa.assertTrue(disneyPlusAndroidTVWatchlistPageBase.get().isOpened(), WATCHLIST_PAGE_LOAD_ERROR);

        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.HOME,
                disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());

        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().isOpened(), HOME_PAGE_LOAD_ERROR);

        disneySearchApi.get().addSeriesToWatchlist(entitledUser.get(),WANDA_VISION.getContentId());

        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.WATCHLIST,
                disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());

        sa.assertTrue(disneyPlusAndroidTVWatchlistPageBase.get().isOpened(), WATCHLIST_PAGE_LOAD_ERROR);

        List<String> assetTitles = disneyPlusAndroidTVWatchlistPageBase.get().getWatchlistAssetTitles();

        sa.assertEquals(assetTitles.get(0), WANDA_VISION.getTitle());
        sa.assertEquals(assetTitles.get(1), END_GAME.getTitle());

        sa.assertAll();
    }

    @Test
    public void seriesAssetSelectedFromWatchlist() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67714"));
        SoftAssert sa = new SoftAssert();
        disneySearchApi.get().addSeriesToWatchlist(entitledUser.get(),MANDALORIAN.getContentId());

        login(entitledUser.get());
        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.WATCHLIST,
                disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        sa.assertTrue(disneyPlusAndroidTVWatchlistPageBase.get().isOpened(), WATCHLIST_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVWatchlistPageBase.get().selectFocusedElement();

        sa.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().isOpened(), DETAILS_PAGE_LOAD_ERROR);
        sa.assertEquals(disneyPlusAndroidTVDetailsPageBase.get().getTitleImageContentDesc(), MANDALORIAN.getTitle());

        sa.assertAll();
    }

    @Test
    public void moviesAssetSelectedFromWatchlist() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67716"));
        SoftAssert sa = new SoftAssert();
        disneySearchApi.get().addMovieToWatchlist(entitledUser.get(), END_GAME.getContentId());

        login(entitledUser.get());
        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.WATCHLIST,
                disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        sa.assertTrue(disneyPlusAndroidTVWatchlistPageBase.get().isOpened(), WATCHLIST_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVWatchlistPageBase.get().selectFocusedElement();

        sa.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().isOpened(), SERIES_PAGE_LOAD_ERROR);
        sa.assertEquals(disneyPlusAndroidTVDetailsPageBase.get().getTitleImageContentDesc(), END_GAME.getTitle());

        sa.assertAll();
    }

    @Test
    public void starUpcomingEventInWatchlist() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(STA, country, "XCDQA-101275", "XCDQA-101277"));
        SoftAssert sa = new SoftAssert();
        NavHelper navHelper = new NavHelper(this.getCastedDriver());

        // Login and navigate to an upcoming piece of content
        login(entitledUser.get());
        disneyPlusAndroidTVDiscoverPage.get().navigateToShelf(STAR_FEATURED_LIVE_AND_UPCOMING);
        navHelper.keyUntilElementDescContains(() -> disneyPlusAndroidTVDiscoverPage.get().getFocusedHeroTile(), AndroidKey.DPAD_RIGHT, "Today");

        // Enter Details Page for Upcoming Content and Add to Watchlist, we use Start Player Btn (MainDetailsButton)
        // because it uses the same element ID as the Watchlist button for Upcoming Details page, which takes place of the play button
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();
        sa.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().isOpened(), DETAILS_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVDetailsPageBase.get().clickStartPlayerBtn();
        sa.assertEquals(disneyPlusAndroidTVDetailsPageBase.get().getMainDetailsButtonContentDesc(),
                DisneyDictionaryKeys.DETAILS_WATCHLIST_ADD, WATCHLIST_ICON_CHANGE_ERROR);
        disneyPlusAndroidTVCommonPage.get().pressRight(1);

        // Check Watchlist Page to confirm content was added
        navHelper.keyUntilElementFocused(() -> disneyPlusAndroidTVDiscoverPage.get().getNavHome(), AndroidKey.BACK);
        navHelper.keyUntilElementDescContains(() -> disneyPlusAndroidTVDiscoverPage.get().getFocusedNavItem(),
                AndroidKey.DPAD_DOWN, String.valueOf(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.WATCHLIST));
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();
        sa.assertFalse(disneyPlusAndroidTVWatchlistPageBase.get().isEmptyWatchlistIconPresent(), WATCHLIST_EMPTY_ERROR);

        // Enter Details Page for added content, Remove From Watchlist, and confirm Watchlist is empty
        disneyPlusAndroidTVCommonPage.get().pressRight(1);
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();
        sa.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().isOpened(), DETAILS_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVDetailsPageBase.get().clickStartPlayerBtn();
        sa.assertEquals(disneyPlusAndroidTVDetailsPageBase.get().getMainDetailsButtonContentDesc(),
                DisneyDictionaryKeys.DETAILS_WATCHLIST_REMOVE, WATCHLIST_ICON_CHANGE_ERROR);
        disneyPlusAndroidTVCommonPage.get().pressBackTimes(1);
        sa.assertTrue(disneyPlusAndroidTVWatchlistPageBase.get().isEmptyWatchlistIconPresent(), WATCHLIST_NOT_EMPTY_ERROR);

        sa.assertAll();
    }
}
