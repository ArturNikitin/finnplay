package com.example.finplay.controller;

public enum View {
	SIGNUP("signup"),
	INDEX("index"),
	PROFILE("profile"),
	LOGIN("login"),
	UPDATE_EMAIL("updateEmail"),
	UPDATE_PROFILE("updateProfile"),
	UPDATE_PASSWORD("updatePassword"),
	;

	public final String value;
	View(String value) {
		this.value = value;
	}
}
