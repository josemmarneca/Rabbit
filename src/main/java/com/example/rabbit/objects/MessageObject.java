package com.example.rabbit.objects;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

public class MessageObject {
	private MessageType type;
	private String content;
	private String sender;

	public enum MessageType{
		CHAT, JOIN, LEAVE
	}



	public MessageObject() {
	}


	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("MessageObject{");
		sb.append("type=").append(type);
		sb.append(", content='").append(content).append('\'');
		sb.append(", sender='").append(sender).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
