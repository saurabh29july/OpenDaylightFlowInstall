package com.opendaylight.testers;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.opendaylight.managers.FlowManager;

public class Tester {

	public static void main(String[] args) throws JSONException {
		// Sample post data.
		JSONObject postData = new JSONObject();
		postData.put("name", "SaurabhTestFlow1");
		postData.put("nwSrc", "10.0.0.1");
		postData.put("nwDst", "10.0.0.2");
		postData.put("installInHw", "true");
		postData.put("priority", "501");
		postData.put("etherType", "0x800");
		postData.put("actions", new JSONArray().put("DROP"));

		// Node on which this flow should be installed
		JSONObject node = new JSONObject();
		node.put("id", "00:00:00:00:00:00:00:01");
		node.put("type", "OF");
		postData.put("node", node);

		// Actual flow install
		FlowManager.installFlow("00:00:00:00:00:00:00:01",
				"SaurabhTestFlow1", postData);

		/*FlowManager.deleteFlow("SaurabhTestFlow1", "00:00:00:00:00:00:00:01");*/

	}

}
