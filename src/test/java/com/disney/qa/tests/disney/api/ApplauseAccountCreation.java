package com.disney.qa.tests.disney.api;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.client.requests.oneId.CreateEspnAccountRequest;
import com.disney.qa.api.pojos.DisneyEntitlement;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.api.pojos.ESPNEntitlement;
import com.disney.qa.tests.BaseAPITest;
import com.disney.qait.auth.ServiceAccountCredentials;
import com.disney.qait.options.ValueInputOption;
import com.disney.qait.services.GSheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.disney.qa.api.utils.DisneySkuParameters.ESPN_EXTERNAL_STANDALONE;

public class ApplauseAccountCreation extends BaseAPITest {

    @Test
    public void applauseAccountCreation() throws IOException {
        String sheetId = "1Up7iZMiloUST1HDUleHGbA_dPdt_tMeblGukEUGRqsM";
        GSheets gSheets = new GSheets(new ServiceAccountCredentials("/espn/api/google/credentials.json",
                Stream.of(SheetsScopes.SPREADSHEETS).collect(Collectors.toList())).getCredentials(),
                "GSheets",
                sheetId);
        DisneyAccountApi disneyAccountApi = new DisneyAccountApi("browser", "PROD", "disney");
        ZonedDateTime zonedDateTime = new Date().toInstant().atZone(ZoneId.of("America/New_York"));
        String sheetDate = zonedDateTime.format(DateTimeFormatter.ofPattern("MM/dd"));
        String sheetName = sheetDate + " - LRQA Test Accounts";

        UpdateSheetPropertiesRequest updateSpreadsheetPropertiesRequest = new UpdateSheetPropertiesRequest();
        SheetProperties sheetProperties = new SheetProperties();
        //https://docs.google.com/spreadsheets/d/1Up7iZMiloUST1HDUleHGbA_dPdt_tMeblGukEUGRqsM/edit#gid=632655641
        sheetProperties.setSheetId(1590531697);
        sheetProperties.setTitle(sheetName);
        updateSpreadsheetPropertiesRequest.setProperties(sheetProperties).setFields("title");
        List<Request> requests = new LinkedList<>();
        requests.add(new Request().setUpdateSheetProperties(updateSpreadsheetPropertiesRequest));
        BatchUpdateSpreadsheetRequest body = new BatchUpdateSpreadsheetRequest().setRequests(requests);
        gSheets.getSheetService().spreadsheets().batchUpdate(gSheets.getSpreadSheet(), body).execute();

        ValueRange countries = gSheets.getValues(sheetName + "!A2:A");
        ValueRange accountType = gSheets.getValues(sheetName + "!B2:B");
        int i = 2;
        List<List<List<Object>>> batchUpdateEmailValues = new ArrayList<>();
        List<List<List<Object>>> batchUpdateExpirationValues = new ArrayList<>();
        List<String> batchEmailRange = new ArrayList<>();
        List<String> batchExpirationRange = new ArrayList<>();
        List<List<Object>> countryValues = countries.getValues();
        List<List<Object>> accountValues = accountType.getValues();
        String countryForCounter = "US";
        int countryCounter = 0;
        for (int j = 0; j < countryValues.size(); j++) {
            List<Object> cellObject = countryValues.get(j);
            if (!cellObject.isEmpty()) {
                String country = cellObject.get(0).toString();
                country = "" + country.charAt(0) + country.charAt(1);
                if (country.equalsIgnoreCase(countryForCounter)) {
                    countryCounter++;
                } else {
                    countryCounter = 1;
                    countryForCounter = country;
                }
                boolean isDisney = accountValues.get(j).get(0).toString().contains("D");
                String prepend = "";
                if (isDisney) {
                    disneyAccountApi.partner = "disney";
                    prepend = "d";
                } else {
                    disneyAccountApi.partner = "star";
                    prepend = "s";
                }

                boolean isAds = isAd(country, countryCounter);
                String email = prepend + "apl" +
                        country.toLowerCase() + zonedDateTime.format(DateTimeFormatter.ofPattern("MMddyy")) + "-" + countryCounter + (isAds ? "-ads" : "") + "@dispostable.com";
                DisneyEntitlement disneyEntitlement = new DisneyEntitlement();
                DisneyOffer offer;
                if (!isAds && (country.equalsIgnoreCase("us") || country.equalsIgnoreCase("gu"))) {
                    offer = new DisneyOffer().setSku("1999199999999910151999000_disney");
                } else {
                    offer = disneyAccountApi.lookupOfferToUse(country, "monthly");
                }
                disneyEntitlement.setOffer(offer);
                disneyEntitlement.setSubVersion("V2");
                CreateDisneyAccountRequest disneyAccountRequest = CreateDisneyAccountRequest.builder()
                        .country(country).language("en").email(email).password("Test123!").isStarOnboarded(false).entitlements(List.of(disneyEntitlement)).entitlementExpiry(1).build();
                disneyAccountApi.createAccount(disneyAccountRequest);
                batchEmailRange.add(sheetName + "!C" + i);
                batchExpirationRange.add(sheetName + "!E" + i);
                batchUpdateEmailValues.add(Collections.singletonList(Collections.singletonList(email)));
                batchUpdateExpirationValues.add(Collections.singletonList(Collections.singletonList(zonedDateTime.plusWeeks(5).format(DateTimeFormatter.ofPattern("MM/dd/yy")))));
            }
            i++;
        }
        gSheets.batchWrite(batchUpdateEmailValues, batchEmailRange, ValueInputOption.USER_ENTERED);
        gSheets.batchWrite(batchUpdateExpirationValues, batchExpirationRange, ValueInputOption.USER_ENTERED);
    }


    @Test
    public void espnAccountCreation() throws IOException {
        String sheetId = "1gFYga_Ho3yK7HmrPRlQAOiRptg1cKbWJBKrQrHosB60";
        GSheets gSheets = new GSheets(new ServiceAccountCredentials("/espn/api/google/credentials.json",
                Stream.of(SheetsScopes.SPREADSHEETS).collect(Collectors.toList())).getCredentials(),
                "GSheets",
                sheetId);
        DisneyAccountApi disneyAccountApi = new DisneyAccountApi("browser", "PROD", "espn");
        ZonedDateTime zonedDateTime = new Date().toInstant().atZone(ZoneId.of("America/New_York"));
        String sheetDate = zonedDateTime.format(DateTimeFormatter.ofPattern("MM/dd"));
        String sheetName = sheetDate + " - ESPN LRQA Test Accounts";

        UpdateSheetPropertiesRequest updateSpreadsheetPropertiesRequest = new UpdateSheetPropertiesRequest();
        SheetProperties sheetProperties = new SheetProperties();
        sheetProperties.setSheetId(76840138);
        sheetProperties.setTitle(sheetName);
        updateSpreadsheetPropertiesRequest.setProperties(sheetProperties).setFields("title");
        List<Request> requests = new LinkedList<>();
        requests.add(new Request().setUpdateSheetProperties(updateSpreadsheetPropertiesRequest));
        BatchUpdateSpreadsheetRequest body = new BatchUpdateSpreadsheetRequest().setRequests(requests);
        gSheets.getSheetService().spreadsheets().batchUpdate(gSheets.getSpreadSheet(), body).execute();
        ValueRange emails = gSheets.getValues(sheetName + "!C4:C");
        List<List<List<Object>>> batchUpdateEmailValues = new ArrayList<>();
        List<String> batchEmailRange = new ArrayList<>();
        List<List<Object>> emailValues = emails.getValues();

        int i = 4;
        int k = 1;
        for (List<Object> cellObject : emailValues) {
            if (!cellObject.isEmpty()) {
                String email = "eapl" + zonedDateTime.format(DateTimeFormatter.ofPattern("MMddyy")) + "-" + (k) + "@dispostable.com";
                CreateEspnAccountRequest createEspnAccountRequest = CreateEspnAccountRequest.builder()
                        .email(email)
                        .password("Test123!")
                        .entitlements(List.of(ESPNEntitlement.builder().sku(ESPN_EXTERNAL_STANDALONE).subVersion("CST").build()))
                        .build();
                disneyAccountApi.createESPNAccount(createEspnAccountRequest);
                batchEmailRange.add(sheetName + "!C" + i);
                batchUpdateEmailValues.add(Collections.singletonList(Collections.singletonList(email)));
                k++;
            }
            i++;
        }
        gSheets.batchWrite(batchUpdateEmailValues, batchEmailRange, ValueInputOption.USER_ENTERED);
    }

    //TODO: Only US & GU offer ads sub, add capability in disney api utils to return ads & non ads offer using appropriate params
    public boolean isAd(String country, int countryCounter) {
        return (country.equalsIgnoreCase("us") && (countryCounter == 3 || countryCounter == 4)) ||
                (country.equalsIgnoreCase("gu") && (countryCounter == 4 || countryCounter == 5));
    }
}
