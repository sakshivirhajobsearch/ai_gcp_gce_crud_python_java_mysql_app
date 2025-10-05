package com.ai.gce.repository;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GCERepository {

	private String apiBaseUrl;

	public GCERepository() {
		// Load config.properties from classpath
		Properties props = new Properties();
		try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
			if (input == null) {
				throw new RuntimeException("❌ config.properties not found in classpath!");
			}
			props.load(input);
			apiBaseUrl = props.getProperty("API_BASE_URL");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Fetch analyzed instances from Flask AI backend
	public List<String> fetchAnalyzedInstances() {
		List<String> displayList = new ArrayList<>();
		try {
			URL url = new URL(apiBaseUrl + "/instances/analyze");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");

			Scanner scanner = new Scanner(con.getInputStream());
			StringBuilder response = new StringBuilder();
			while (scanner.hasNext())
				response.append(scanner.nextLine());
			scanner.close();

			// Parse JSON response
			Gson gson = new Gson();
			Type listType = new TypeToken<List<Map<String, Object>>>() {
			}.getType();
			List<Map<String, Object>> instances = gson.fromJson(response.toString(), listType);

			// Convert to displayable strings
			for (Map<String, Object> instance : instances) {
				String name = instance.get("name").toString();
				String status = instance.get("status").toString();
				String aiComment = instance.getOrDefault("ai_comment", "").toString();
				displayList.add(name + " | " + status + " | " + aiComment);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return displayList;
	}

	// Start instance
	public String startInstance(String name) {
		return sendPostRequest("/instances/start", "{\"name\": \"" + name + "\"}");
	}

	// Stop instance
	public String stopInstance(String name) {
		return sendPostRequest("/instances/stop", "{\"name\": \"" + name + "\"}");
	}

	// Helper: Send POST request
	private String sendPostRequest(String endpoint, String jsonBody) {
		try {
			URL url = new URL(apiBaseUrl + endpoint);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setDoOutput(true);

			try (OutputStream os = con.getOutputStream()) {
				os.write(jsonBody.getBytes("utf-8"));
			}

			Scanner scanner = new Scanner(con.getInputStream());
			StringBuilder response = new StringBuilder();
			while (scanner.hasNext())
				response.append(scanner.nextLine());
			scanner.close();
			return response.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return "❌ Error sending POST request: " + e.getMessage();
		}
	}
}
