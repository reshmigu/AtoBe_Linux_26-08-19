package com.test;

public class Transition {
	private int id;
	private String name;
	private To to;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public To getTo() {
		return to;
	}
	public void setTo(To to) {
		this.to = to;
	}
}
