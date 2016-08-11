package iplocator;

import java.net.URL;

import utilities.IPUtils;

public class IP2CLocator extends IPLocator{
	private static final String LOOKUP_URL = "http://ip2c.org/";
	private static final int LOOKUP_CODE_INDEX = 1;
	private static final int LOOKUP_FULL_NAME_INDEX = 3;
	
	public IP2CLocator(){}
	
	
	public static IPLocator locate(String ip) throws Exception {
		long decIP = IPUtils.ipToLong(ip);
		String url = LOOKUP_URL + decIP;
		URL u = new URL(url);
		IP2CLocator ipl = new IP2CLocator();
		
		String response = ipl.getContent(u);
		String[] parsed = response.split(";");
		ipl.setIP(ip);
		ipl.setCode(parsed[LOOKUP_CODE_INDEX]);
		ipl.setCountry(parsed[LOOKUP_FULL_NAME_INDEX].trim());
		
		
		return ipl;
	}
}
