import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PaymentHandler {

	String PAYMENT_URL = "http://battlehack-beeru-andreyors.c9.io/api/payment";

	public PaymentHandler() {
	}

	public String sendPayment(String customer_name, String customer_data, ArrayList<Object[]> prod_data){
		
		String c_first_name;
		String c_last_name;
		String c_email;
		String c_phone_num;
		
		if(customer_name.split(" ").length == 2){
			c_first_name = customer_name.split(" ")[0];
			c_last_name = customer_name.split(" ")[1];
		}
		else{
			c_first_name = customer_name.split(" ")[0];
			c_last_name = "Last";
		}
		
		if(customer_data.split(",").length == 2){
			c_email = customer_data.split(",")[0];
			c_phone_num = customer_data.split(",")[1];
		}
		else
		{
			c_email = customer_data.split(",")[0];
			c_phone_num = "Phone";
		}
		
		if(Config.DEBUG)
		{
			System.out.println("FName: " + c_first_name);
			System.out.println("LName: " + c_last_name);
			System.out.println("Email: " + c_email);
			System.out.println("Phone: " + c_phone_num);
		}
		
		// First part of JSON
		String json = "{'customer':{'first_name':'"+ c_first_name + "','last_name':'" + c_last_name + "','email':'" + c_email +"',"
				+ "'phone':'"+ c_phone_num +"'},"
				+ "'items':[";
		
		for(int i = 0; i < prod_data.size(); i++){
			if(i != 0)
				json += ",";
			
			
			json += "{'title':'" + prod_data.get(i)[2] + "','price':'" +  prod_data.get(i)[3] + "','currency':'" + prod_data.get(i)[4] + "'}";
				
			
		}
		json += "]}";
		System.out.println("Sending: " + json);
				
				
		System.out.println("Got: " + executePost(PAYMENT_URL, json));
		
		return "";

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
