package export;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import iplocator.IPLocator;
import thread.Worker;

public class ExcelExporter {

	public ExcelExporter() {

	}
	
	private static HashMap<String,Integer> countryStatistic(ArrayList<IPLocator> list){
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for(IPLocator loc : list){
			String country = loc.getCountry();
			if(country.isEmpty())
				country = "N/A";
			if(map.containsKey(country)){
				map.put(country, map.get(country)+1);
			}
			else{
				map.put(country, 1);
			}
		}
		return map;
	}

	public static void createReport(ArrayList<IPLocator> list, Worker w) {
		// create a new file
		FileOutputStream out;
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH_mm_ss");
		HashMap<String, Integer> map = countryStatistic(list);
		ValueComparator bvc = new ValueComparator(map);
		
		TreeMap<String, Integer> tMap = new TreeMap<String, Integer>(bvc);
		tMap.putAll(map);
		
		int currRow = 0;
		try {
			File dir = new File("reports");
			dir.mkdir();
			String filename = "reports/Report " + sdfDate.format(new Date()) + ".xls";
			out = new FileOutputStream(filename);

			// create a new workbook
			Workbook wb = new XSSFWorkbook();
			// create a new sheet
			Sheet s = wb.createSheet();
			// declare a row object reference
			Row r = null;
			// declare a cell object reference
			Cell c = null;
			
			//declare style
			CellStyle cellstyle = wb.createCellStyle();
			cellstyle.setAlignment(CellStyle.ALIGN_CENTER);
			
			//print statistic
			r = s.createRow((short)currRow);
			c = r.createCell(0);
			c.setCellValue("Country");
			c = r.createCell(1);
			c.setCellValue("Number of Incidents");
			currRow++;
			for(Entry<String, Integer> country : tMap.entrySet()){
				r = s.createRow((short)currRow);
				c = r.createCell(0);
				c.setCellValue(country.getKey());
				c = r.createCell(1);
				c.setCellValue(country.getValue());
				currRow++;
			}
			currRow++;
			
			//create address table
			r = s.createRow((short)currRow);
			c = r.createCell(0);
			c.setCellValue("IP Address");
			c.setCellStyle(cellstyle);
			c = r.createCell(1);
			c.setCellValue("City");
			c.setCellStyle(cellstyle);
			c = r.createCell(2);
			c.setCellValue("Country");
			c.setCellStyle(cellstyle);
			c = r.createCell(3);
			c.setCellValue("Code");
			c.setCellStyle(cellstyle);
			
			for(int i = 0; i<list.size(); i++){
				IPLocator loc = list.get(i);
				currRow++;
				r = s.createRow((short) currRow);
				
				
				c = r.createCell(0);
				c.setCellValue(loc.getIP());
				c.setCellStyle(cellstyle);
				c = r.createCell(1);
				c.setCellValue(loc.getCity());
				c.setCellStyle(cellstyle);
				c = r.createCell(2);
				c.setCellValue(loc.getCountry());
				c.setCellStyle(cellstyle);
				c = r.createCell(3);
				c.setCellValue(loc.getCode());
				c.setCellStyle(cellstyle);
			}
			
			for(int i = 0; i<4; i++){
				s.autoSizeColumn((short)i);
			}
			
			/*
			// create 3 cell styles
			CellStyle cs = wb.createCellStyle();
			CellStyle cs2 = wb.createCellStyle();
			CellStyle cs3 = wb.createCellStyle();
			DataFormat df = wb.createDataFormat();
			// create 2 fonts objects
			Font f = wb.createFont();
			Font f2 = wb.createFont();

			// set font 1 to 12 point type
			f.setFontHeightInPoints((short) 12);
			// make it blue
			f.setColor((short) 0xCC0066);
			// make it bold
			// arial is the default font
			f.setBoldweight(Font.BOLDWEIGHT_BOLD);

			// set font 2 to 10 point type
			f2.setFontHeightInPoints((short) 10);
			// make it red
			f2.setColor((short) Font.COLOR_RED);
			// make it bold
			f2.setBoldweight(Font.BOLDWEIGHT_BOLD);

			f2.setStrikeout(true);

			// set cell stlye
			cs.setFont(f);
			// set the cell format
			cs.setDataFormat(df.getFormat("#,##0.0"));

			// set a thin border
			cs2.setBorderBottom(CellStyle.BORDER_THIN);
			// fill w fg fill color
			cs2.setFillPattern((short) CellStyle.SOLID_FOREGROUND);
			// set the cell format to text see DataFormat for a full list
			cs2.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));

			// set the font
			cs2.setFont(f2);

			// set the sheet name in Unicode
			wb.setSheetName(0, "\u0422\u0435\u0441\u0442\u043E\u0432\u0430\u044F "
					+ "\u0421\u0442\u0440\u0430\u043D\u0438\u0447\u043A\u0430");
			// in case of plain ascii
			// wb.setSheetName(0, "HSSF Test");
			// create a sheet with 30 rows (0-29)
			int rownum;
			for (rownum = (short) 0; rownum < 30; rownum++) {
				// create a row
				r = s.createRow(rownum);
				// on every other row
				if ((rownum % 2) == 0) {
					// make the row height bigger (in twips - 1/20 of a point)
					r.setHeight((short) 0x249);
				}

				// r.setRowNum(( short ) rownum);
				// create 10 cells (0-9) (the += 2 becomes apparent later
				for (short cellnum = (short) 0; cellnum < 10; cellnum += 2) {
					// create a numeric cell
					c = r.createCell(cellnum);
					// do some goofy math to demonstrate decimals
					c.setCellValue(rownum * 10000 + cellnum + (((double) rownum / 1000) + ((double) cellnum / 10000)));

					String cellValue;

					// create a string cell (see why += 2 in the
					c = r.createCell((short) (cellnum + 1));

					// on every other row
					if ((rownum % 2) == 0) {
						// set this cell to the first cell style we defined
						c.setCellStyle(cs);
						// set the cell's string value to "Test"
						c.setCellValue("Test");
					} else {
						c.setCellStyle(cs2);
						// set the cell's string value to
						// "\u0422\u0435\u0441\u0442" c.setCellValue("\u0422\u0435\u0441\u0442");
					}

					// make this column a bit wider
					s.setColumnWidth((short) (cellnum + 1), (short) ((50 * 8) / ((double) 1 / 20)));
				}
			}

			// draw a thick black border on the row at the bottom using BLANKS
			// advance 2 rows
			rownum++;
			rownum++;

			r = s.createRow(rownum);

			// define the third style to be the default
			// except with a thick black border at the bottom
			cs3.setBorderBottom(CellStyle.BORDER_THICK);

			// create 50 cells
			for (short cellnum = (short) 0; cellnum < 50; cellnum++) {
				// create a blank type cell (no value)
				c = r.createCell(cellnum);
				// set it to the thick black border style
				c.setCellStyle(cs3);
			}

			// end draw thick black border

			// demonstrate adding/naming and deleting a sheet
			// create a sheet, set its title then delete it
			s = wb.createSheet();
			wb.setSheetName(1, "DeletedSheet");
			wb.removeSheetAt(1);
			// end deleted sheet

	*/
			// write the workbook to the output stream
			// close our file (don't blow out our file handles
			wb.write(out);
			wb.close();
			out.close();
			//Desktop.getDesktop().open(new File(filename));
		} catch (IOException e) {
			w.updateStatus(e.getLocalizedMessage());
		}
	}
	
	static class ValueComparator implements Comparator {
	    Map<String, Integer> base;

	    public ValueComparator(Map<String, Integer> base) {
	        this.base = base;
	    }


		@Override
		public int compare(Object o1, Object o2) {
			String a = (String) o1;
			String b = (String) o2;
			if (base.get(a) >= base.get(b)) {
	            return -1;
	        } else {
	            return 1;
	        } // returning 0 would merge keys
		}
	}
}
