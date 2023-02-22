package com.disney.qa.api.tvos;

import com.qaprosoft.carina.core.foundation.api.AbstractApiMethodV2;

import java.util.Properties;

import static com.disney.qa.disney.apple.pages.tv.AppleTVConstants.ENDPOINT;

public class GetStatusWDAMethod extends AbstractApiMethodV2 {

	public GetStatusWDAMethod() {
		super(null, null, new Properties());
		replaceUrlPlaceholder("url", ENDPOINT);
	}
}
