package com.disney.qa.common.timeout;

public enum Timeouts {
	DEFAULT_CONTENT_LOADING_TIMEOUT(60l),		// 1 min
	;
	
	private Long timeoutInSeconds;
	private Timeouts(Long timeoutInSeconds){
		this.timeoutInSeconds = timeoutInSeconds;
	}
	
	public Long getTimeoutInSeconds(){
		return this.timeoutInSeconds;
	}
}
