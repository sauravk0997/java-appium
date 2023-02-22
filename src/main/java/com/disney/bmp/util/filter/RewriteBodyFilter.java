package com.disney.bmp.util.filter;

import com.disney.bmp.util.HttpHelper;
import io.netty.handler.codec.http.HttpResponse;
import net.lightbody.bmp.filters.ResponseFilter;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RewriteBodyFilter implements ResponseFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private String hostMatch;
	private String matchItem;
	private String replacement;

	/**
	 * Filter to rewrite body of the responses
	 * 
	 * @param hostMatch
	 *            - host pattern where rewrite should be applied
	 * @param matchItem
	 *            - part of the body that should replaced
	 * @param replacement
	 *            - data that will be added instead of matchItem
	 */
	public RewriteBodyFilter(final String hostMatch, final String matchItem, final String replacement) {
		this.hostMatch = hostMatch;
		this.matchItem = matchItem;
		this.replacement = replacement;
	}

	@Override
	public void filterResponse(HttpResponse response, HttpMessageContents contents, HttpMessageInfo messageInfo) {

		String requestUrl = messageInfo.getUrl();
		if (!requestUrl.matches(hostMatch)) {
			return;
		}
		
		if (matchItem.equalsIgnoreCase(".*")) {
		    contents.setTextContents(replacement);
		    return;
		}

		String responseContent = HttpHelper.removeCharacters(contents.getTextContents());
		Pattern p = Pattern.compile(matchItem);
		Matcher matcher = p.matcher(responseContent);
		if (matcher.find()) {
			LOGGER.info("Rewrite will be applied. URL: ".concat(requestUrl));
			responseContent = matcher.replaceAll(replacement);
			contents.setTextContents(responseContent);
		}

	}

}