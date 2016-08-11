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



public abstract class IPLocator {

	protected static final String CRLF = "\r\n";
	
	private String ip;
	private String city;
	private String country;
	private String code;
	private float longitude;
	private float latitude;
	
	/**
	 * This is a singleton class therefore make the constructor private 
	 */
	IPLocator() {} 

	/**
	 * Other than the getters & setters, this is the only method visible to the outside world  
	 * 
	 * @param ip The ip address to be located
	 * @return IPLocator instance
	 * @throws Exception in case of any error/exception
	 */
	public static IPLocator locate(String ip) throws Exception {
	/*
		String url = HOSTIP_LOOKUP_URL + ip;
		URL u = new URL(url);
		IPLocator ipl = new IPLocator();

		String response = ipl.getContent(u);
		JSONParser parser=new JSONParser();
		Object obj = parser.parse(response);
        JSONObject map = (JSONObject)obj;
        ipl.setIP(ip);
        ipl.setCountry((String)map.get(KEY_COUNTRY));
        ipl.setCode((String)map.get(KEY_CODE));
        ipl.setCity((String)map.get(KEY_CITY));
        	
		return ipl;
	*/	
		return null;
	}	
	
	

	/**
     * Gets the content for a given url. This method makes a connection, gets the response from the url.
     * 
     * A RuntimeException is throws is the status code of the response is not 200.
     * 
     * @param url The url to open.
     * @return HTML response
     */
    protected String getContent(URL url) throws  Exception {
    	
        HttpURLConnection http = (HttpURLConnection) url.openConnection ();
        http.connect ();

        int code = http.getResponseCode();
        if(code != 200) throw new RuntimeException("IP Locator failed to get the location. Http Status code : " + code);
        return this.getContent(http);
    }
    
    
    /**
     * Gets the content for a given HttpURLConnection. 
     *  
     * @param connection Http URL Connection.
     * @return HTML response
     */
    
    private String getContent(HttpURLConnection connection) throws IOException {
    	InputStream in = connection.getInputStream ();
        StringBuffer sbuf = new StringBuffer();
    	InputStreamReader isr = new InputStreamReader ( in );
    	BufferedReader bufRead = new BufferedReader ( isr );
    	
    	String aLine = null;
    	do {
    	    aLine = bufRead.readLine();
    	    if ( aLine != null ) {
    	    	
    	    	sbuf.append(aLine).append(CRLF);
    	    }
    	} 
    	while ( aLine != null );
    	
    	return sbuf.toString();
    }

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getCode(){
		return code;
	}
	
	public void setCode(String code){
		this.code = code;
	}

	/**
	 * @return the longitude
	 */
	public float getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the latitude
	 */
	public float getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	
	public void setIP(String ip) {
		this.ip = ip;
		
	}
    
    public String getIP(){
    	return this.ip;
    }
	

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