package com.disney.qa.api.espn.google;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.Border;
import com.google.api.services.sheets.v4.model.Borders;
import com.google.api.services.sheets.v4.model.CellData;
import com.google.api.services.sheets.v4.model.CellFormat;
import com.google.api.services.sheets.v4.model.DeleteSheetRequest;
import com.google.api.services.sheets.v4.model.DuplicateSheetRequest;
import com.google.api.services.sheets.v4.model.GridRange;
import com.google.api.services.sheets.v4.model.RepeatCellRequest;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.TextFormat;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The sheetId of this class is the unique Google Sheet identifier and can be found in the URL when the sheet is open.
 * 17nQ3X6SqfTG1dYUW6zKjXOAK3Mf4KQuokCnSekBZ-kk - QE | ESPN+ EOD Report 2.0
 * The sheetTabId is the Template 2.0 tab on the Google sheet, which can be found by looking at the gid in the url.
 */
public class EspnGoogleSheets {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static String sheetID = "17nQ3X6SqfTG1dYUW6zKjXOAK3Mf4KQuokCnSekBZ-kk";

    private static Integer sheetTabIdToCopy = 248602327;

    private static String initialEventRange = "A13:E26";

    private Integer throttler = 0;

    private Sheets serviceSheets;

    public EspnGoogleSheets(Sheets service) {
        this.serviceSheets = service;
    }

    private Spreadsheet fetchSheet() throws IOException {
        checkIfThrottlingNeeded();
        return serviceSheets.spreadsheets().get(sheetID).execute();
    }

    private boolean doesSheetExist(String sheetName) throws IOException {
        for (Sheet sheet : fetchSheet().getSheets()) {
            if (sheet.getProperties().getTitle().equalsIgnoreCase(sheetName)) {
                return true;
            }
        }
        return false;
    }

    public String fetchRequiredSheetId(String sheetName) throws IOException {
        List<Sheet> sheetsList = fetchSheet().getSheets();
        String sheetId = "";

        for (Sheet sheet : sheetsList) {
            if (sheet.getProperties().getTitle().equalsIgnoreCase(sheetName)) {
                LOGGER.info(
                        String.format("Retrieving Sheet Id for (%s): %s", sheet.getProperties().getTitle(), sheet.getProperties().getSheetId()));
                sheetId = sheet.getProperties().getSheetId().toString();
            }
        }
        return sheetId;
    }

    public EspnGoogleSheets copyTemplateToNewSheet(String newSheetName, Map<String, String> events) throws IOException, URISyntaxException {

        List<Request> requests = new ArrayList<>();

        if (doesSheetExist(newSheetName)) {
            LOGGER.info(String.format("Sheet (%s) already exists, attempting to delete to recreate...", newSheetName));
            requests.add(new Request()
                    .setDeleteSheet(new DeleteSheetRequest()
                            .setSheetId(
                                    Integer.parseInt(
                                            fetchRequiredSheetId(newSheetName)))));

        }

        requests.add(new Request()
                .setDuplicateSheet(new DuplicateSheetRequest()
                        .setNewSheetName(newSheetName)
                        .setSourceSheetId(sheetTabIdToCopy)));

        BatchUpdateSpreadsheetRequest batchUpdate = new BatchUpdateSpreadsheetRequest().setRequests(requests);
        checkIfThrottlingNeeded();
        serviceSheets.spreadsheets().batchUpdate(sheetID, batchUpdate).execute();

        copyEventsToSpreadSheet(newSheetName, events);

        return new EspnGoogleSheets(serviceSheets);
    }

    private EspnGoogleSheets copyEventsToSpreadSheet(String sheetName, Map<String, String> events) throws IOException, URISyntaxException {

        checkIfThrottlingNeeded();
        ValueRange range = serviceSheets.spreadsheets()
                .values()
                .get(sheetID, sheetName)
                .setRange(initialEventRange)
                .execute();

        for (int i = 1; i < events.size(); i++) {
            checkIfThrottlingNeeded();
            serviceSheets.spreadsheets()
                    .values()
                    .append(sheetID, range.getRange(), range)
                    .setValueInputOption("USER_ENTERED")
                    .setInsertDataOption("INSERT_ROWS")
                    .execute();
        }

        updateEvent(sheetName, events);

        return new EspnGoogleSheets(serviceSheets);
    }

    private void updateEvent(String sheetName, Map<String, String> events) throws IOException, URISyntaxException {
        checkIfThrottlingNeeded();
        ValueRange range = serviceSheets.spreadsheets()
                .values()
                .get(sheetID, sheetName)
                .execute();

        checkIfThrottlingNeeded();
        ValueRange entireSheet = serviceSheets
                .spreadsheets()
                .values()
                .get(sheetID, range.getRange())
                .execute();

        LOGGER.info("Checking Events: " + events.size());

        int i = 0;
        int eventIterator = 0;

        List<List<Object>> valueSheet = entireSheet.getValues();

        for (List row : entireSheet.getValues()) {
            i += 1;
            if (!row.isEmpty() && "xx:xx Event Name".equals(row.get(0))) {
                LOGGER.info("Checking Events: " + events.size() + " Current Iterator: " + eventIterator);
                String eventName = events.keySet().toArray()[eventIterator].toString();
                String contentId = events.values().toArray()[eventIterator].toString();
                LOGGER.info("[Event]: " + eventName + " [Content ID]: " + contentId);

                row.remove(0);
                row.add(0, eventName);

                String rangeToUpdate = "A" + i + ":E" + i;
                ValueRange rangeThatWasUpdated = new ValueRange();
                rangeThatWasUpdated.setValues(Arrays.asList(row));

                checkIfThrottlingNeeded();
                serviceSheets
                        .spreadsheets()
                        .values()
                        .update(sheetID, rangeToUpdate, rangeThatWasUpdated)
                        .setValueInputOption("USER_ENTERED")
                        .execute();

                eventIterator = eventIterator + 1;
            }
        }

        updateFormatting(sheetName);
    }

    private void updateFormatting(String sheetName) throws IOException {
        List<Request> requests = new ArrayList<>();

        Integer currentSheetId = Integer.parseInt(fetchRequiredSheetId(sheetName));

        GridRange platformRange = new GridRange();
        platformRange.setSheetId(currentSheetId);
        platformRange.setStartColumnIndex(0);
        platformRange.setEndColumnIndex(1);
        platformRange.setStartRowIndex(26);

        GridRange valueRange = new GridRange();
        valueRange.setSheetId(currentSheetId);
        valueRange.setStartColumnIndex(1);
        valueRange.setStartRowIndex(26);

        requests.add(new Request().setRepeatCell(new RepeatCellRequest()
                .setCell(new CellData()
                        .setUserEnteredFormat(platformFormatting().setBorders(getSolidBorders())))
                .setRange(platformRange)
                .setFields("userEnteredFormat.textFormat,userEnteredFormat.borders")));

        requests.add(new Request().setRepeatCell(new RepeatCellRequest()
                .setCell(new CellData()
                        .setUserEnteredFormat(new CellFormat()
                                .setBorders(getSolidBorders()).setHorizontalAlignment("CENTER")
                                ))
                .setRange(valueRange)
                .setFields("userEnteredFormat.borders,userEnteredFormat.horizontalAlignment,userEnteredFormat.backgroundColor")));

        BatchUpdateSpreadsheetRequest batchUpdate = new BatchUpdateSpreadsheetRequest().setRequests(requests);

        checkIfThrottlingNeeded();
        serviceSheets.spreadsheets().batchUpdate(sheetID, batchUpdate).execute();
    }

    private CellFormat platformFormatting() {

        return new CellFormat()
                .setTextFormat(new TextFormat().setBold(true));
    }

    private Borders getSolidBorders() {
        Border border = new Border();
        border.setStyle("SOLID");

        Borders borders = new Borders();
        borders.setBottom(border);
        borders.setLeft(border);
        borders.setRight(border);
        borders.setTop(border);

        return borders;
    }

    private void checkIfThrottlingNeeded() {
        throttler = throttler + 1;

        LOGGER.debug("Throttler Ping: " + throttler);
        if (throttler == 80) {
            try {
                LOGGER.info("Throttling For a Minute...");
                Thread.sleep(120000);
                throttler = 0;
            } catch (Exception ex) {
                LOGGER.error("Error: " + ex.getMessage(), ex);
            }
        }
    }
}

