package SOLTS;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class importToDB {
	private static String instructor;
	private static String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	List<String> exfiles, csvfiles;
	
	public importToDB(List<String> in, String inst) throws IOException {
		this.exfiles = in;
		this.instructor = inst;
		
		csvfiles = ConvertCSV(exfiles);
	}
	
	public static String getTeacher() { return instructor; }
	public static int getYear() { return Integer.parseInt(currentDate.substring(0,4)); }
	
	private static List<String> ConvertCSV(List<String> f) throws IOException {
		List<String> outCSVfiles = new ArrayList<String>();
		
		for(String input : f) {
			String in = input.substring(input.lastIndexOf('\\') + 1);
			String out = input.substring(0,(input.lastIndexOf('\\') + 1)) + instructor + "_" + input.substring((input.indexOf('_') + 1),input.indexOf('.')) + "_" + currentDate + ".csv";
			
			File inputFile = new File(in);
			File outputFile = new File(out);
			
			outCSVfiles.add(out);
			
			/*[DEBUG]*/
			System.out.println("input file: " + input);
			System.out.println("output file: " +in);
			System.out.println("out file: " +out);
		
			StringBuffer data = new StringBuffer();
			try {
				FileOutputStream fos = new FileOutputStream(outputFile, false);
				
				HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(input));
				HSSFSheet sheet = workbook.getSheetAt(0);
				Cell cell;
				Row row;
				
				int skip = 0;
				
				Iterator<Row> rowIterator = sheet.iterator();
				while(rowIterator.hasNext()) {
					row = rowIterator.next();
					
					skip++;
					
					Iterator<Cell> cellIterator = row.cellIterator();
					if(skip > 1) {
						while(cellIterator.hasNext()) {
							cell = cellIterator.next();
							
							switch(cell.getCellType()) {
								case Cell.CELL_TYPE_BOOLEAN:
									data.append(cell.getBooleanCellValue() + ",");
									break;
								case Cell.CELL_TYPE_NUMERIC:
									data.append(cell.getNumericCellValue() + ",");
									break;
								case Cell.CELL_TYPE_STRING:
									String nameCheck = cell.getStringCellValue();
									if(nameCheck.equalsIgnoreCase("name")) {
										data.append("Last Name" + "," + "First Name" + ",");
									}
									else {
										data.append(cell.getStringCellValue() + ",");
									}
									break;
								case Cell.CELL_TYPE_BLANK:
									data.append("" + ",");
									break;
								default:
									data.append(cell + ",");	
							}
						}
						data.append('\n');
					}
				}
				
				fos.write(data.toString().getBytes());
				
				/*[DEBUG]*/
				System.out.println(data.toString());
				
				fos.close();
				
				/*[DEBUG]*/
				System.out.println("Worked...");
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return outCSVfiles;
	}
}