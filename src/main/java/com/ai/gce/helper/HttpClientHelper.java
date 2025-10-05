package com.ai.gce.helper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClientHelper {

	public static String sendGet(String endpoint) throws Exception {

		URL url = new URL(endpoint);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");

		try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
			StringBuilder result = new StringBuilder();
			String line;
			while ((line = in.readLine()) != null)
				result.append(line);
			return result.toString();
		}
	}

	public static String sendPost(String endpoint, String json) throws Exception {
		URL url = new URL(endpoint);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-Type", "application/json");

		try (OutputStream os = conn.getOutputStream()) {
			os.write(json.getBytes());
		}

		try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
			StringBuilder result = new StringBuilder();
			String line;
			while ((line = in.readLine()) != null)
				result.append(line);
			return result.toString();
		}
	}
}
