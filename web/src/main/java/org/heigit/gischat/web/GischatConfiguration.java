package org.heigit.gischat.web;

import org.springframework.boot.context.properties.*;
import org.springframework.stereotype.*;

@Component
@ConfigurationProperties(prefix = "gischat")
public class GischatConfiguration {

	private String chatTitle;
	private int pollingInterval;
	private String pychatterUrl;

	public String getChatTitle() {
		return chatTitle;
	}

	public void setChatTitle(String chatTitle) {
		this.chatTitle = chatTitle;
	}

	public int getPollingInterval() {
		return pollingInterval;
	}

	public void setPollingInterval(int pollingInterval) {
		this.pollingInterval = pollingInterval;
	}

	private String getPychatterUrl() {
		return pychatterUrl;
	}

	private void setPychatterUrl(String pychatterUrl) {
		this.pychatterUrl = pychatterUrl;
	}
}
