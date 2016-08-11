package utilities;

public class IPUtils {
	
	public static boolean validIP (String ip) {
	    try {
	        if ( ip == null || ip.isEmpty() ) {
	            return false;
	        }

	        String[] parts = ip.split( "\\." );
	        if ( parts.length != 4 ) {
	            return false;
	        }

	        for ( String s : parts ) {
	            int i = Integer.parseInt( s );
	            if ( (i < 0) || (i > 255) ) {
	                return false;
	            }
	        }
	        if ( ip.endsWith(".") ) {
	            return false;
	        }

	        return true;
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	}
	
	
	public static long ipToLong(String ipAddress) {
			
			long result = 0;
				
			String[] ipAddressInArray = ipAddress.split("\\.");

			for (int i = 3; i >= 0; i--) {
					
				long ip = Long.parseLong(ipAddressInArray[3 - i]);
				
				result |= ip << (i * 8);
				
			}
			return result;
		}
}
