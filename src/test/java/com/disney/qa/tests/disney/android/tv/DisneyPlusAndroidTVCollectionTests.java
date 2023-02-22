package com.disney.qa.tests.disney.android.tv;
import com.disney.qa.api.client.requests.content.CollectionRequest;
import com.disney.qa.api.client.requests.content.SetRequest;
import com.disney.qa.api.client.responses.content.ContentCollection;
import com.disney.qa.api.search.assets.DisneyStandardCollection;
import com.disney.qa.api.search.sets.DisneyCollection;
import com.disney.qa.api.search.sets.DisneyCollectionSet;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.android.pages.common.DisneyPlusEditorialPageBase;
import com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVDiscoverPage;
import com.disney.util.disney.ZebrunnerXrayLabels;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import java.util.List;

import static com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.PROFILE;
import static com.disney.qa.tests.disney.DisneyPlusBaseTest.*;

public class DisneyPlusAndroidTVCollectionTests extends DisneyPlusAndroidTVBaseTest {

    @Test(description = "Verify content refreshes when language changed via edit profile UI")
    public void contentRefreshOnLanguageChange() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67350"));
        SoftAssert sa = new SoftAssert();
        login(entitledUser.get());

        // Go to MOVIES
        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.MOVIES,
                disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());

        // Fetch current movies collection titles.
        List <String> tileTitlesBefore = disneyPlusAndroidTVDiscoverPage.get().getVisibleCollectionTitles();

        // Navigate to profile and change profile language
        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(PROFILE,
                disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());

        disneyPlusAndroidTVProfilePageBase.get().clickEditProfiles();
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVProfilePageBase.get().selectFocusedElement();

        disneyPlusAndroidTVProfilePageBase.get().
                selectOptionFromEditProfile(disneyPlusAndroidTVProfilePageBase.get().getEditProfileLanguageOption());
        disneyPlusAndroidTVProfilePageBase.get().pressDown(2);
        disneyPlusAndroidTVProfilePageBase.get().selectFocusedElement();
        disneyPlusAndroidTVProfilePageBase.get().selectEditProfileDoneBtn();

        // Return to movies. Verify content is refreshed
        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.MOVIES,
                disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());

        List <String> tileTitlesAfter = disneyPlusAndroidTVDiscoverPage.get().getVisibleCollectionTitles();

        sa.assertNotEquals(tileTitlesAfter, tileTitlesBefore, "Collection titles before and after language change should not match.");
        checkAssertions(sa);
    }

    @Test(description = "Verify details page opens from general (not EXPLORE) collection.")
    public void selectItemFromGeneralCollection() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67344"));
        SoftAssert sa = new SoftAssert();
        login(entitledUser.get());

        // Go to MOVIES and select item.
        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.MOVIES,
                disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());

        disneyPlusAndroidTVCommonPage.get().focusFirstTile();
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();

        sa.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().isOpened(), DETAILS_PAGE_LOAD_ERROR);
        UniversalUtils.captureAndUpload(getCastedDriver());

        checkAssertions(sa);
    }

    @Test(description = "Verify items open from SEARCH/EXPLORE and validate appearance")
    public void selectItemsFromSearchExplore() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67340", "XCDQA-67342", "XCDQA-67332", "XCDQA-67366"));
        SoftAssert sa = new SoftAssert();
        login(entitledUser.get());

        // Goto SEARCH and select item from Explore view.
        // Get the EXPLORE collection
        String searchRow = DisneyStandardCollection.EXPLORE.getName();
        CollectionRequest collectionRequest = CollectionRequest.builder()
                .region(country).language(language)
                .slug(DisneyStandardCollection.EXPLORE.getSlug()).contentClass(DisneyStandardCollection.EXPLORE.getContentClass()).build();

        ContentCollection collection = getSearchApi().getCollection(collectionRequest);
        DisneyCollectionSet set = collection.getCollectionSetByName(searchRow);
        SetRequest setRequest = SetRequest.builder().region(country).language(language).setId(set.getRefId()).refType(set.getRefType()).build();

        // Query the first collection within EXPLORE...
        DisneyCollection targetCollection = getSearchApi().getSet(setRequest).getCollectionsInSet().get(0);

        collectionRequest = CollectionRequest.builder()
                .region(country).language(language)
                .slug(targetCollection.getSlug()).contentClass(targetCollection.getContentType()).build();

        collection = getSearchApi().getCollection(collectionRequest);
        List<String> setTitles = collection.getCollectionSetTitles();
        set = collection.getCollectionSetByName(setTitles.get(0));
        setRequest = SetRequest.builder().region(country).language(language).setId(set.getRefId()).refType(set.getRefType()).build();
        List<String> responseTitles = getSearchApi().getSet(setRequest).getTitles();

        // Returns the set type: ie. Grid, Shelf.
        String setType = collection.getCollectionSetsInfo().get(0).getLayoutType();

        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.SEARCH,
                disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());

        // XCDQA-67332 - each tile in explore has an image.
        // Verify feature images are seen for each tile in EXPLORE
        disneyPlusAndroidTVSearchPage.get().focusFirstSearchedItem();
        UniversalUtils.captureAndUpload(getCastedDriver());
        Integer tileCount = disneyPlusAndroidTVDiscoverPage.get().getVisibleCollectionTitles().size();
        Integer posterCount = disneyPlusAndroidTVDiscoverPage.get().getShelfContentPosters().size();
        sa.assertEquals(posterCount, tileCount, "Poster element count should match visible tile count.");

        disneyPlusAndroidTVSearchPage.get().focusFirstSearchedItem();
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();
        List<String> visibleCollectionTitles = disneyPlusAndroidTVDiscoverPage.get().getVisibleCollectionTitles();

        // Determine how many tiles to compare. If the set is a grid, compare all visible tiles.
        // If it is a shelf or something else, compare the 4 on screen or the total set response. Picks the shorter option.
        int loopExpression;
        if (setType.equals("GridContainer")) {
            loopExpression = visibleCollectionTitles.size();
        } else {
            loopExpression = Math.min(responseTitles.size(), 4);
        }

        // XCDQA-67340 - User is able to select a Collection tile from EXPLORE.
        // XCDQA-67366 - 1/2 Tile goes to a collection. Some ambiguity with this case.
       for(int index = 0; index < loopExpression; index++) {
           sa.assertEquals(visibleCollectionTitles.get(index), responseTitles.get(index),
                   "Titles at index " + index + " should match." );
       }

        // XCDQA-67342 - appearance
        // Verify editorial background art and title are shown.
        DisneyPlusEditorialPageBase editorialPageBase = initPage(DisneyPlusEditorialPageBase.class);
        sa.assertTrue(editorialPageBase.isOpened(), "Title image should be present.");
        sa.assertTrue(editorialPageBase.isBackgroundPresent(), "Background image should be present.");
        UniversalUtils.captureAndUpload(getCastedDriver());

        // XCDQA-67366 2/2 single item navigable from explore, goes to details
        String searchTarget = "star wars";
        // Return to SEARCH from collection view
        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.SEARCH,
                disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());

        disneyPlusAndroidTVSearchPage.get().typeInSearchBox(searchTarget);
        disneyPlusAndroidTVSearchPage.get().focusFirstSearchedItem();
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();
        sa.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().isOpened(), DETAILS_PAGE_LOAD_ERROR);
        UniversalUtils.captureAndUpload(getCastedDriver());

        checkAssertions(sa);
    }
}
