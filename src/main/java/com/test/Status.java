package com.test;

public class Status {
private String self;
private String description;
private String iconUrl;
private String name;
private int id;
private StatusCategory statusCategory;

public String getSelf() {
	return self;
}
public void setSelf(String self) {
	this.self = self;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public String getIconUrl() {
	return iconUrl;
}
public void setIconUrl(String iconUrl) {
	this.iconUrl = iconUrl;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public StatusCategory getStatusCategory() {
	return statusCategory;
}
public void setStatusCategory(StatusCategory statusCategory) {
	this.statusCategory = statusCategory;
}
}
