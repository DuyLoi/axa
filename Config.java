package com.tpb.config;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Config {

	private static JSONArray jsonArrayConfig = null;

	public static void main(String[] args) {

//		loadConfig();
//
//		JSONObject flow = getFlowById("INDIVIDUAL"); // INDIVIDUAL |
//														// ORGANISATION
//
//		System.out.println("flow: " + flow);
//		System.out.println("flowName: " + getValue(flow, "flowName"));
//
//		JSONObject step = getStep(flow, "2");
//		System.out.println("step: " + step);
//		System.out.println("stepName: " + getValue(step, "stepName"));
	}
	
	static {
		loadConfig();
	}

	public static void loadConfig() {
		JSONParser parser = new JSONParser();

		try {

			InputStream is = Config.class.getClassLoader().getResourceAsStream(
					"config.json");
			Object obj = parser.parse(new InputStreamReader(is));

			jsonArrayConfig = (JSONArray) obj;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static JSONArray getJsonConfig(){
		return jsonArrayConfig;
	}

}
