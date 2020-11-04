package json.converter;

import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataSet;
import json.converter.internal.JsonConverterNodeRenderer;
import xlsx.converter.PlattformComparison;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
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
        
        workbookTemp.forEach((el) -> {
        	Sheet sheet = workbookTemp.getSheet(el.getSheetName());
        	System.out.println("Workbook Element " + sheet.getSheetName());
        	
            DataFormatter dataFormatter = new DataFormatter();
            

         
            System.out.println("\n\nIterating over Rows and Columns using Iterator\n");
            Iterator<Row> rowIterator = sheet.rowIterator();
            
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    printCellValue(cell);       
                }
                System.out.println();
            }
            try {
				workbookTemp.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
        	
        });
        
        
        
        
     // Getting the Sheet at index zero
        //Sheet sheet = workbookTemp.getSheetAt(0);

        
		return null;

      }
    
    private static void printCellValue(Cell cell) {
        switch (cell.getCellTypeEnum()) {
            case BOOLEAN:
                System.out.print(cell.getBooleanCellValue());
                break;
            case STRING:
                System.out.print(cell.getRichStringCellValue().getString());
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    System.out.print(cell.getDateCellValue());
                } else {
                    System.out.print(cell.getNumericCellValue());
                }
                break;
            case FORMULA:
            	//System.out.println("Formula is " + cell.getCellFormula());
                switch(cell.getCachedFormulaResultType()) {
                    case NUMERIC:
                        System.out.println("Last evaluated as: " + cell.getNumericCellValue());
                        break;
                    case STRING:
                        System.out.println("Last evaluated string as \"" + cell.getRichStringCellValue() + "\"");
                        break;
                }
                break;
            case BLANK:
                System.out.print("");
                break;
            default:
                //System.out.print("");
        }

        //System.out.print("\t");
    }
}
