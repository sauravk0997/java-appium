package com.disney.qa.api.tvos;

import com.zebrunner.carina.api.AbstractApiMethodV2;

import java.util.Properties;

import static com.disney.qa.disney.apple.pages.tv.AppleTVConstants.ENDPOINT;

public class GetScreenshotMethod extends AbstractApiMethodV2 {

	public GetScreenshotMethod(String sessionWDA) {
		super(null, null, new Properties());
		replaceUrlPlaceholder("url", ENDPOINT);
		replaceUrlPlaceholder("session_id", sessionWDA);
	}
}
