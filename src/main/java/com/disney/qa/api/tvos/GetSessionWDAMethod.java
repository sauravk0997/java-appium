package com.disney.qa.api.tvos;

import com.qaprosoft.carina.core.foundation.api.AbstractApiMethodV2;

import java.util.Properties;

import static com.disney.qa.disney.apple.pages.tv.AppleTVConstants.ENDPOINT;

public class GetSessionWDAMethod extends AbstractApiMethodV2 {

	public GetSessionWDAMethod(String sessionWDA) {
		super(null, null, new Properties());
		replaceUrlPlaceholder("url", ENDPOINT);
		replaceUrlPlaceholder("session_id", sessionWDA);

	}
}
