package com.opendaylight.managers;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jettison.json.JSONObject;

import com.opendaylight.settings.ODL;

public class FlowManager {

	// REST NBI Hydrogen for FlowGrogrammerNorthbound. More details at
	// http://opendaylight.nbi.sdngeeks.com/
	private static String FLOW_PROGRAMMER_REST_API = "/controller/nb/v2/flowprogrammer/default/node/OF/";

	// HTTP statuses for checking the call output
	private static final int NO_CONTENT = 204;
	private static final int CREATED = 201;
	private static final int OK = 200;

	public static boolean installFlow(String nodeId, String flowName,
			JSONObject postData) {

		HttpURLConnection connection = null;
		int callStatus = 0;

		// Creating the actual URL to call
		String baseURL = ODL.URL + FLOW_PROGRAMMER_REST_API + nodeId
				+ "/staticFlow/" + flowName;

		try {

			// Create URL = base URL + container
			URL url = new URL(baseURL);

			// Create authentication string and encode it to Base64
			String authStr = ODL.USERNAME + ":" + ODL.PASSWORD;
			String encodedAuthStr = Base64.encodeBase64String(authStr
					.getBytes());

			// Create Http connection
			connection = (HttpURLConnection) url.openConnection();

			// Set connection properties
			connection.setRequestMethod("PUT");
			connection.setRequestProperty("Authorization", "Basic "
					+ encodedAuthStr);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Set Post Data
			OutputStream os = connection.getOutputStream();
			os.write(postData.toString().getBytes());
			os.close();

			// Getting the response code
			callStatus = connection.getResponseCode();

		} catch (Exception e) {
			System.err.println("Unexpected error while flow installation.. "
					+ e.getMessage());
			e.printStackTrace();
		} finally {
			if (connection != null)
				connection.disconnect();
		}

		if (callStatus == CREATED) {
			System.out.println("Flow installed Successfully");
			return true;
		} else {
			System.err.println("Failed to install flow.. " + callStatus);
			return false;
		}
	}

	public static boolean deleteFlow(String flowName, String nodeId) {

		HttpURLConnection connection = null;
		int callStatus = 0;
		String baseURL = ODL.URL + FLOW_PROGRAMMER_REST_API + nodeId
				+ "/staticFlow/" + flowName;

		try {

			// Create URL = base URL + container
			URL url = new URL(baseURL);

			// Create authentication string and encode it to Base64
			String authStr = ODL.USERNAME + ":" + ODL.PASSWORD;
			String encodedAuthStr = Base64.encodeBase64String(authStr
					.getBytes());

			// Create Http connection
			connection = (HttpURLConnection) url.openConnection();

			// Set connection properties
			connection.setRequestMethod("DELETE");
			connection.setRequestProperty("Authorization", "Basic "
					+ encodedAuthStr);
			connection.setRequestProperty("Content-Type", "application/json");

			callStatus = connection.getResponseCode();

		} catch (Exception e) {
			System.err.println("Unexpected error while flow deletion.."
					+ e.getMessage());
			e.printStackTrace();
		} finally {
			if (connection != null)
				connection.disconnect();
		}

		if (callStatus == NO_CONTENT) {
			System.out.println("Flow deleted Successfully");
			return true;
		} else {
			System.err.println("Failed to delete the flow..." + callStatus);
			return false;
		}
	}

}
