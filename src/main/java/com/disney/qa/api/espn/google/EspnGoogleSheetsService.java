package com.disney.qa.api.espn.google;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class EspnGoogleSheetsService {
    private static final String APPLICATION_NAME = "QE-EOD-Report";

    private EspnGoogleSheetsService() {

    }

    public static Sheets getSheetsService() throws IOException, GeneralSecurityException {
        Credential credential = EspnGoogleAuth.authorize();

        return new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(), credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}
