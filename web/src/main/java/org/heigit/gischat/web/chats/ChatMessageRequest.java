package org.heigit.gischat.web.chats;

public class ChatMessageRequest {
	private String user;
	private String text;

	public ChatMessageRequest(String user, String text) {
		this.user = user;
		this.text = text;
	}

	public String getUser() {
		return user;
	}

	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("ChatMessageRequest{");
		sb.append("user='").append(user).append('\'');
		sb.append(", text='").append(text).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
