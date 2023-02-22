package com.disney.qa.api.nhl.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IntermissionClockGame {
	@JsonProperty
	private String gameState;

	@JsonProperty
	private String gameType;

	@JsonProperty
	private int period;

	@JsonProperty
	private boolean intermission;

	@JsonProperty
	private int intermissionTimeRemaining;

	@JsonProperty
	private int intermissionTimeElapsed;

	@JsonProperty
	private String currentPeriodTimeRemaining;

	public String getGameState() {
		return gameState;
	}

	public void setGameState(String state) {
		this.gameState = state;
	}

	public String getGameType() {
		return gameType;
	}

	public void setGameType(String type) {
		this.gameType = type;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public boolean isIntermission() {
		return intermission;
	}

	public void setIntermission(boolean intermission) {
		this.intermission = intermission;
	}

	public int getIntermissionTimeRemaining() {
		return intermissionTimeRemaining;
	}

	public void setIntermissionTimeRemaining(int intermissionTimeRemaining) {
		this.intermissionTimeRemaining = intermissionTimeRemaining;
	}

	public int getIntermissionTimeElapsed() {
		return intermissionTimeElapsed;
	}

	public void setIntermissionTimeElapsed(int intermissionTimeElapsed) {
		this.intermissionTimeElapsed = intermissionTimeElapsed;
	}

	public String getCurrentPeriodTimeRemaining() {
		return currentPeriodTimeRemaining;
	}

	public void setCurrentPeriodTimeRemaining(String currentPeriodTimeRemaining) {
		this.currentPeriodTimeRemaining = currentPeriodTimeRemaining;
	}

}
