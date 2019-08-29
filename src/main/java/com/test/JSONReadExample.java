package com.test;

import java.io.FileReader;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JSONReadExample {
	private static final Logger LOGGER = LogManager.getLogger(JSONReadExample.class);

	public static void main(String[] args) throws Exception {

		Object obj = new JSONParser().parse(new FileReader(".\\sample.json"));

		JSONObject jo = (JSONObject) obj;

		Long code = (Long) jo.get("code");

		LOGGER.info("code --.-------------" + code);
		String data = jo.get("data").toString().replaceAll("\"", "");
		LOGGER.info("data ---------------" + data);
		JSONObject dataee = (JSONObject) jo.get("data");
		LOGGER.info("dataee ---------------" + dataee);
		JSONArray brands = (JSONArray) dataee.get("brands");
		LOGGER.info("size ---------------" + brands.size());
		for (int j = 0; j < brands.size(); j++) {

			JSONObject cc = (JSONObject) brands.get(j);
			LOGGER.info("id ---------------" + cc.get("id"));
		}

	}
}
