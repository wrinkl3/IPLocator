package export;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import iplocator.IPLocator;
import thread.Worker;

public class TxtExporter {
	private final static int IP_DEF_LENGTH = 6;
	private final static String GAP = "        ";
	
	
	public static void createReport(ArrayList<IPLocator> list, Worker w) {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH_mm_ss");
		PrintWriter writer;
		try {
			File dir = new File("reports");
			dir.mkdir();
			writer = new PrintWriter("reports/Report " + sdfDate.format(new Date()) + ".txt", "UTF-8");
			for(IPLocator loc : list){
				String gap_s = GAP;
				String ip = loc.getIP();
				int dif = ip.length() - IP_DEF_LENGTH;
				
				if(dif>0){
					for(int i = 0; i<dif; i++)
						gap_s = gap_s.concat(" ");
				}else if(dif<0){
					dif = Math.abs(dif);
					gap_s = gap_s.substring(0, dif);
				}
				
				writer.println(loc.getIP() + gap_s + loc.getCountry());
			}
			
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}
