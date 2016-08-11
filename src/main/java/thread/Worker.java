package thread;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import export.ExcelExporter;
import gui.IPgui;
import iplocator.IP2CLocator;
import iplocator.IPLocator;
import utilities.IPUtils;

public class Worker extends Thread{
	public static final String STAT_START = "Starting up..\n";
	public static final String STAT_PARSE = "Parsing items..\n";
	public static final String STAT_FETCH = "Fetching IP info..\n";
	public static final String STAT_REPORT = "Generating report..\n";
	public static final String STAT_SUCC = "Done!\n";
	
	public static final String STAT_FETCH_ERR = "Download failed for: ";
	public static final String STAT_REPORT_ERR = "Report could not be generated.\n";
	
	String s;
	IPgui frame;
	
	public Worker(String s, IPgui frame){
		this.s = s;
		this.frame = frame;
	}
	
	public void run(){
		if(!frame.equals(null))
		 frame.clearStatusArea();
		updateStatus(STAT_START);
		updateStatus(STAT_PARSE);
		Set<String> set = getIPSet(s);
		updateStatus(STAT_FETCH);
		ArrayList<IPLocator> locList =  fetchInfo(set);
		updateStatus(STAT_REPORT);
		ExcelExporter.createReport(locList, this);
		updateStatus(STAT_SUCC);
		if(!frame.equals(null))
			frame.fetchModeOff();
		
	}
	
	public void updateStatus(String mes){
		if(!frame.equals(null)){
			frame.addStatusLine(mes);
		}
		else{
			System.out.println(mes);
		}
	}

	private ArrayList<IPLocator> fetchInfo(Set<String> set) {
		ArrayList<IPLocator> locSet = new ArrayList<IPLocator>();
		for(String ip : set){
			try {
				IPLocator loc = IP2CLocator.locate(ip);
				locSet.add(loc);
			} catch (Exception e) {
				updateStatus(STAT_FETCH_ERR + " " + ip + "\n");
				e.printStackTrace();
			}
			
		}
		return locSet;
	}

	private Set<String> getIPSet(String s) {
		s = s.replace(',', ' ').replace('\n', ' ');
		String[] addrs = s.split(" ");
		Set<String> set = new HashSet<String>();
		for(int i = 0; i<addrs.length; i++){
			String ad = addrs[i].trim();
			if(!ad.isEmpty() && IPUtils.validIP(ad)){
				set.add(ad);
			}
		}
		return set;
	}

}
