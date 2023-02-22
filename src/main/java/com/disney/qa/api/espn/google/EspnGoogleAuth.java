package com.disney.qa.api.espn.google;

import com.disney.qait.auth.ServiceAccountCredentials;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.sheets.v4.SheetsScopes;

import java.util.Collections;
import java.util.List;

public class EspnGoogleAuth {

    private static final String CLIENT_SECRET = "/google/google-sheets-client-secret.json";

    private EspnGoogleAuth() {

    }

    public static Credential authorize(){
        List<String> scopes = Collections.singletonList(SheetsScopes.SPREADSHEETS);
        return new ServiceAccountCredentials(CLIENT_SECRET,scopes).getCredentials();
    }
}
