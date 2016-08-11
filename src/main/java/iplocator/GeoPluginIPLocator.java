package iplocator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;



public class GeoPluginIPLocator extends IPLocator {

	private static final String HOSTIP_LOOKUP_URL = "http://www.geoplugin.net/json.gp?ip=";
	private static final String KEY_COUNTRY = "geoplugin_countryName";
	private static final String KEY_CITY = "geoplugin_city";
	private static final String KEY_LATITUDE = "geoplugin_latitude";
	private static final String KEY_LONGITUDE = "geoplugin_longitude";
	private static final String KEY_CODE = "geoplugin_countryCode";
	
	
	GeoPluginIPLocator() {} 

	public static IPLocator locate(String ip) throws Exception {
		String url = HOSTIP_LOOKUP_URL + ip;
		URL u = new URL(url);
		GeoPluginIPLocator ipl = new GeoPluginIPLocator();

		String response = ipl.getContent(u);
		JSONParser parser=new JSONParser();
		Object obj = parser.parse(response);
        JSONObject map = (JSONObject)obj;
        ipl.setIP(ip);
        ipl.setCountry((String)map.get(KEY_COUNTRY));
        ipl.setCode((String)map.get(KEY_CODE));
        ipl.setCity((String)map.get(KEY_CITY));
        
/*		
		String[] tokens = response.split(CRLF);

		String TOKEN_DELIMITER=":";
		for(int i = 0; i < tokens.length; i++) {
			String token = tokens[i].trim();
			String[] keyValue = token.split(TOKEN_DELIMITER);
			String key = keyValue[0];
			if(keyValue.length<2)
				continue;
			String value = keyValue[1];
			if(key.equalsIgnoreCase(KEY_COUNTRY)) {
				ipl.setCountry(value);
			}
			else if(key.equalsIgnoreCase(KEY_CITY)) {
				ipl.setCity(value);
			}
			else if(key.equalsIgnoreCase(KEY_LATITUDE)) {
				try {
					ipl.setLatitude(Float.parseFloat(value));
				}
				catch(Exception e) {
					ipl.setLatitude(-1);
				}
			}
			else if(key.equalsIgnoreCase(KEY_LONGITUDE)) {
				try {
					ipl.setLongitude(Float.parseFloat(value));
				}
				catch(Exception e) {
					ipl.setLongitude(-1);
				}
			}
		}
*/		
		return ipl;
				
	}	


	
/*	private String parseCode(String country){
		String code = "";
		Boolean sentinel = false;
		for(int i = 0; i<country.length(); i++){
			if(country.charAt(i) == '('){
				sentinel = true;
				continue;
			}
			if(country.charAt(i) == ')'){
				break;
			}
			if(sentinel){
				code = code.concat(String.valueOf(country.charAt(i)));
			}
		}
		return code;
	}
*/
    
	/**
	 * For unit testing purposes only
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		
		try {
			IPLocator ipl = IPLocator.locate("12.215.42.19");
			System.out.println("City="+ipl.getCity());
			System.out.println("Country="+ipl.getCountry());
			//System.out.println("Latitude="+ipl.getLatitude());
			//System.out.println("Longitude="+ipl.getLongitude());
			System.out.println("Code=" + ipl.getCode());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}