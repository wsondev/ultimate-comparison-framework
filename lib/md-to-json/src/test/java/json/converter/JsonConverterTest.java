package json.converter;

import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataSet;
import json.converter.internal.JsonConverterNodeRenderer;
import xlsx.converter.ComparisonCriteria;
import xlsx.converter.ComparisonElement;
import xlsx.converter.PlattformComparison;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.eclipse.collections.impl.factory.Maps;
import org.junit.jupiter.api.Test;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class JsonConverterTest {
	
	private final Path root = Paths.get("uploads");

    private String loadResource(String name) throws Exception {
        Path path = Paths.get(Objects.requireNonNull(this.getClass().getClassLoader().getResource(name)).toURI());
        return new String(Files.readAllBytes(path));
    }
    
    

    @Test
    public void test() throws Exception {
        MutableDataSet options = new MutableDataSet();
        options.set(Parser.EXTENSIONS, Collections.singletonList(JsonConverterExtension.create()));
        options.set(HtmlRenderer.INDENT_SIZE, 2);
        options.set(JsonConverterNodeRenderer.PLAIN_CHILDREN, Maps.mutable.with("item", Maps.mutable.with("1", true)));
        options.set(JsonConverterNodeRenderer.CHILDREN, Maps.mutable.with("item", Maps.mutable.with("1", false)));

        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        Node document = parser.parse(loadResource("test.md"));

        String json = renderer.render(document);

        JSONAssert.assertEquals(loadResource("test.json"), json, false);
    }
    
    @Test
    public void testExcelParsen() throws Exception {
    	
    	try (Stream<Path> paths = Files.walk(this.root)) {
            paths.filter(Files::isRegularFile)
                    .forEach(f -> {
                    	String cleanedFileName = f.getFileName().toString();
                    	
                    	if(cleanedFileName.endsWith("ods")) {
                    		Path templatePath = this.root.resolve(cleanedFileName);
                        	System.out.println(" ---> Filename: " + cleanedFileName);
                        	try {
    							OdfDocument document = OdfDocument.loadDocument(Files.newInputStream(templatePath));
    							document.getTableList().forEach((tab) -> {
    								System.out.println("---> Tabelle: " + tab.getTableName());
    							}); 									
    						} catch (Exception e) {
    							// TODO Auto-generated catch block
    							e.printStackTrace();
    						}
                    	}
                    	
                    });
        }
    	System.out.printf("----> root: ", this.root.getFileName());
    }
    
    @Test
    public void readExcelFile() throws Exception {
    	
    	try (Stream<Path> paths = Files.walk(this.root)) {
    		paths.filter(Files::isRegularFile)
            .forEach((file) -> {
            	String cleanedFileName = file.getFileName().toString();
            	if(cleanedFileName.endsWith("xlsx")) {
            		try {
            			Path templatePath = this.root.resolve(cleanedFileName);
            			getContentFromExcelFile(templatePath.toString());
					} catch (EncryptedDocumentException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	}
            });
        }	
    }
    
    
    
    
    private List<PlattformComparison> getContentFromExcelFile(String filePath) throws EncryptedDocumentException, IOException{
    	
    	
    	Workbook workbookTemp = WorkbookFactory.create(new File(filePath));
    	
        // Retrieving the number of sheets in the Workbook
        System.out.println("Workbook has " + workbookTemp.getNumberOfSheets() + " Sheets : ");
        
        Sheet sheet = workbookTemp.getSheet("1_Platformvergleich");
    	System.out.println("Workbook Element " + sheet.getSheetName());
    	

        System.out.println("\n\nIterating over Rows and Columns using Iterator\n");
        Iterator<Row> rowIterator = sheet.rowIterator();
        List<ComparisonElement> ceElements = new ArrayList<>();
        
        DataFormatter dataFormatter = new DataFormatter();
        
        for (Row row: sheet) {
        	
        	
            for(Cell cell: row) {
            	String langName = "";
            	String critName = "";
            	double critValue = 0;
            	
            	if(cell.getColumnIndex() < 1 && cell.getRowIndex() > 20) {
        			//langName = getCriterium(cell);
        			
            		//printCellValue(cell);
        		}
            	else if(cell.getColumnIndex() > 0 && cell.getColumnIndex() < 9 && cell.getRowIndex() == 20) {
        			//critName = getCriterium(cell);
        			
        		} 
            	else if(cell.getColumnIndex() > 0 && cell.getColumnIndex() < 9 && cell.getRowIndex() > 20) {
        			//critValue = getCritValue(cell);
        			
            		//printCellValue(cell);
        		}
            	
            	if(critName != null) {
            		if(critValue != 0.0) {
            			ComparisonElement ce = new ComparisonElement();
            			ce.setLabel(langName);
            			ce.setCriterium(critName);
            			ce.setElemValue(critValue);
            			//ceElements.add(ce);
            		}
            	}
            	//printCellValue(cell);
            	
//                String cellValue = dataFormatter.formatCellValue(cell);
//                System.out.print("foreach cell: "+cellValue + "\t");
            }
            //System.out.println();
        }
        
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            
            
            Cell firstCol = row.getCell(0);
            String langLabel = "";
            String critName = "";
            double critValue = 0;
            
            
            //get language entry
            if(firstCol != null && firstCol.getRowIndex() > 20) {
            	langLabel = getCriterium(firstCol);
            }

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                //Matrix unten links im ersten Tab
                if(cell.getRowIndex() > 19 && cell.getColumnIndex() < 10 && cell.getColumnIndex() > 0) {
                    if(cell.getCellType() == CellType.FORMULA) {
                        
                        switch(cell.getCachedFormulaResultType()) {
                            case STRING:
                                //System.out.println("Last evaluated in cell as string\"" + cell.getRichStringCellValue().toString() + "\"");
                                //critName = cell.getRichStringCellValue().toString();
                                
                                //printCellValue(cell);
                                break;
                        }
                        
                     }
                    
                    
                    if(langLabel!= null && langLabel.length() > 0) {
                		critValue = getCritValue(cell);
                		critName = getFormulaString(cell);
                		if(critValue != 0.0) {
                			ComparisonElement ce = new ComparisonElement();
                			critName = getFormulaString(cell);
                            ce.setLabel(langLabel);
                            ce.setElemValue(critValue);
                            ce.setCriterium(critName);
                            ceElements.add(ce);

                		}
   		
                	}

            	}
       
            }
        }
        try {
			workbookTemp.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        ceElements.forEach((ComparisonElement ce) -> {
        	System.out.println("-------------------_> CE: "+ ce.toString());
        });
    	
       
        //alle Tabs durchgehen...
        workbookTemp.forEach((el) -> {
        	
//        	Sheet sheet = workbookTemp.getSheet(el.getSheetName());
//        	//Sheet sheet = workbookTemp.getSheet("1_Platformvergleich");
//        	System.out.println("Workbook Element " + sheet.getSheetName());
//        	
//            DataFormatter dataFormatter = new DataFormatter();
//            
//
//         
//            System.out.println("\n\nIterating over Rows and Columns using Iterator\n");
//            Iterator<Row> rowIterator = sheet.rowIterator();
// 
//            
//            while (rowIterator.hasNext()) {
//                Row row = rowIterator.next();
//                Iterator<Cell> cellIterator = row.cellIterator();
//                ComparisonElement ce = new ComparisonElement();
//                
//                Cell firstCol = row.getCell(0);
//                ce.setLabel(getCriterium(firstCol));
//                
//                while (cellIterator.hasNext()) {
//                	
//                    Cell cell = cellIterator.next();
//                    
//                    if(cell.getRowIndex() > 19 && cell.getColumnIndex() > 0 && cell.getColumnIndex() < 9) {
//                    	String critName = getSheetNameOutOfCellFormula(cell.getCellFormula());
//                    	if(critName != null && critName.length() > 0) {
//                    		ce.setCriterium(critName);
//                    	}
//                    	 
//                    	ce.setElemValue(getCritValue(cell));
//                    	//printCellValue(cell);
//                	}
//                           
//                }
//                System.out.println();
//            }
//            try {
//				workbookTemp.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
            
        	
        });
        
        
        
        
     // Getting the Sheet at index zero
        //Sheet sheet = workbookTemp.getSheetAt(0);

		return null;

      }
    
    private static List<String> collectLangElements(Workbook workbookTemp) {
    	List<String> languages = new ArrayList<>();
    	
    	Sheet sheet = workbookTemp.getSheet("Sprachen");
    	System.out.println("Workbook Element " + sheet.getSheetName());
    	
    	System.out.println("\n\nIterating over Rows and Columns using Iterator\n");
        Iterator<Row> rowIterator = sheet.rowIterator();

        
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                if(cell.getColumnIndex() < 2) {
                	languages.add(getLanguage(cell));   
                }    
            }
            System.out.println();
        }
        try {
			workbookTemp.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return languages;
    }
    
    private static String[] getSheetNameOutOfCellFormula(String cellFormula) {
    	System.out.print("-getSheetNameOutOfCellFormula--> stringValue: "+ cellFormula);
    	String[] strings = cellFormula.replace("'", "").split("[\\_!]"); 	
    	return strings;
    }
    
    
    private static String getLanguage(Cell cell) {
    	String langType = "";
    	if(cell.getCellType() == CellType.STRING) {
    		System.out.print("---> stringValue: "+ cell.getRichStringCellValue().getString());
    		langType = cell.getRichStringCellValue().getString();
    	}
    	return langType;
    }
    
    private static String getCriterium(Cell cell) {
    	String critType = "";
    	if(cell.getCellType() == CellType.STRING) {
    		critType = cell.getRichStringCellValue().toString();
    		System.out.print("---> stringValue: "+ cell.getRichStringCellValue().getString());
    	}
    	else if(cell != null && cell.getCellType() == CellType.FORMULA) {
//            System.out.println("Formula is " + cell.getCellFormula()+ " --> colIndex: "
//            		+ cell.getColumnIndex()+ "...rowIndex"+ cell.getRowIndex());  
//            getSheetNameOutOfCellFormula(cell.getCellFormula().toString());
            switch(cell.getCachedFormulaResultType()) {
                case STRING:
                    System.out.println("Last evaluated as \"" + cell.getRichStringCellValue().toString() + "\"");
                    critType = cell.getRichStringCellValue().toString();
                    break;
            }
         }
    	return critType;
    }
    
    private static String getFormulaString(Cell cell) {
    	String formulaString = "";
    	if(cell.getCellType() == CellType.FORMULA) {
    		String[] strings = getSheetNameOutOfCellFormula(cell.getCellFormula()); 
    		formulaString = strings[1] != null ? strings[1] : "";
    		System.out.print("--> formulaString: "+ formulaString);
//			for (int i = 0; i < strings.length; i++) {
//				System.out.print("--> stringsSplittedValue: "+ i + "...."+ strings[i]);
//			}
    		if(strings.length > 0) {
    			

    		}
         }
    	return formulaString;
    }
    
    private static double getCritValue(Cell cell) {
    	double critValue = 0;
    	if(cell.getCellType() == CellType.NUMERIC) {
    		critValue = cell.getNumericCellValue();
    		System.out.print("--> numericValue: "+cell.getNumericCellValue());
    	}
    	else if(cell.getCellType() == CellType.FORMULA) {
            switch(cell.getCachedFormulaResultType()) {
            case NUMERIC:
                System.out.println("Last evaluated as num=>: " + cell.getNumericCellValue());
                critValue = cell.getNumericCellValue();
                break;
            }
         }
    	return critValue;
    }
    
    
    
   private static ComparisonElement createComparisonElement(Cell cell) {
	   
	   ComparisonElement ce = new ComparisonElement();
	   if(cell.getCellType() == CellType.STRING) {
   		
   		System.out.print("---> stringValue: "+ cell.getRichStringCellValue().getString());
	   	}
	   	if(cell.getCellType() == CellType.NUMERIC) {
	   		System.out.print("--> numericValue: "+cell.getNumericCellValue());
	   	}
	   	
	   	if(cell.getCellType() == CellType.FORMULA) {
	           System.out.println("Formula is " + cell.getCellFormula()+ " --> colIndex: "
	           		+ cell.getColumnIndex()+ "...rowIndex"+ cell.getRowIndex());  
	//           String[] strings = getSheetNameOutOfCellFormula(cell.getCellFormula());
	//           for (String str: strings) {
	//       		System.out.print("---> stringSplittedValue: "+ str+"\n");
	//   		}   
	           switch(cell.getCachedFormulaResultType()) {
	               case NUMERIC:
	                   System.out.println("Last evaluated as: " + cell.getNumericCellValue());
	                   break;
	               case STRING:
	                   System.out.println("Last evaluated as string\"" + cell.getRichStringCellValue().toString() + "\"");
	                   
	                   break;
	           }
	    }
	   	return ce;
	   	
   }
    
    
    private static void printCellValue(Cell cell) {
    	    	    	
    	if(cell.getCellType() == CellType.STRING) {
    		
    		System.out.print("---> stringValue: "+ cell.getRichStringCellValue().getString());
    	}
    	if(cell.getCellType() == CellType.NUMERIC) {
    		System.out.print("--> numericValue: "+cell.getNumericCellValue());
    	}
    	
    	if(cell.getCellType() == CellType.FORMULA) {
            System.out.println("Formula is " + cell.getCellFormula()+ " --> colIndex: "
            		+ cell.getColumnIndex()+ "...rowIndex"+ cell.getRowIndex());  
            String[] strings = getSheetNameOutOfCellFormula(cell.getCellFormula());
            for (String str: strings) {
        		System.out.print("---> stringSplittedValue: "+ str+"\n");
    		}   
            switch(cell.getCachedFormulaResultType()) {
                case NUMERIC:
                    System.out.println("Last evaluated as: " + cell.getNumericCellValue());
                    break;
                case STRING:
                    System.out.println("Last evaluated as string\"" + cell.getRichStringCellValue().toString() + "\"");
                    
                    break;
            }
         }
    	

    }
}
