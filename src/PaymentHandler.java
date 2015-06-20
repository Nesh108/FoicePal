import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentHandler {

	String PAYMENT_URL = "http://battlehack-beeru-andreyors.c9.io/api/payment";
	String STATUS_URL = "http://battlehack-beeru-andreyors.c9.io/api/payment/status";

	public PaymentHandler() {
	}

	public boolean sendPayment(String customer_name, String customer_data,
			ArrayList<Object[]> prod_data) {

		String c_first_name;
		String c_last_name;
		String c_email;
		String c_phone_num;

		if (customer_name.split(" ").length == 2) {
			c_first_name = customer_name.split(" ")[0];
			c_last_name = customer_name.split(" ")[1];
		} else {
			c_first_name = customer_name.split(" ")[0];
			c_last_name = "Last";
		}

		if (customer_data.split(",").length == 2) {
			c_email = customer_data.split(",")[0];
			c_phone_num = customer_data.split(",")[1];
		} else {
			c_email = customer_data.split(",")[0];
			c_phone_num = "Phone";
		}

		if (Config.DEBUG) {
			System.out.println("FName: " + c_first_name);
			System.out.println("LName: " + c_last_name);
			System.out.println("Email: " + c_email);
			System.out.println("Phone: " + c_phone_num);
		}

		// First part of JSON
		String json = "{'customer':{'first_name':'" + c_first_name
				+ "','last_name':'" + c_last_name + "','email':'" + c_email
				+ "'," + "'phone':'" + c_phone_num + "'}," + "'items':[";

		for (int i = 0; i < prod_data.size(); i++) {
			if (i != 0)
				json += ",";

			json += "{'title':'" + prod_data.get(i)[2] + "','price':'"
					+ prod_data.get(i)[3] + "','currency':'"
					+ prod_data.get(i)[4] + "'}";

		}
		json += "]}";
		System.out.println("Sending: " + json);

		String response_json = executePost(PAYMENT_URL, json);
		System.out.println("Got: " + response_json);

		try {
			JSONObject object = new JSONObject(response_json);

			if (object.getInt("_status") == 200) {
				System.out.println("Got: " + object.getString("token"));

				// Payment sent
				json = "{'token':'" + object.getString("token") + "'}";

				boolean isPaid = false;
				while (!isPaid) {
					JSONObject response = new JSONObject(executePost(STATUS_URL, json));
					System.out.println("Payment response" + response.toString());
					
					if(response.getString("status").equals("paid"))
						isPaid = true;
					else
						Thread.sleep(1000);
				}
			} else {
				System.out.println("Error!");
				return false;
			}

		} catch (JSONException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return true;

	}

	protected String executePost(String targetURL, String urlParameters) {
		URL url;
		HttpURLConnection connection = null;
		try {
			// Create connection
			url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			connection.setRequestProperty("Content-Length",
					"" + Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(
					connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();

		} catch (Exception e) {

			e.printStackTrace();
			return null;

		} finally {

			if (connection != null) {
				connection.disconnect();
			}
		}
	}
}
