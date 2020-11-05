package json.converter;

import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataSet;
import json.converter.internal.JsonConverterNodeRenderer;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;
import org.eclipse.collections.impl.factory.Maps;
import org.junit.jupiter.api.Test;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.skyscreamer.jsonassert.JSONAssert;
import xlsx.converter.ComparisonElement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
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

                    if (cleanedFileName.endsWith("ods")) {
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
                    if (cleanedFileName.endsWith("xlsx")) {
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

    private void getContentFromExcelFile(String filePath) throws EncryptedDocumentException, IOException {
        try (Workbook workbookTemp = WorkbookFactory.create(new File(filePath))) {

            // Retrieving the number of sheets in the Workbook
            System.out.println("Workbook has " + workbookTemp.getNumberOfSheets() + " Sheets : ");

            List<String> languages = collectLanguages(workbookTemp.getSheetAt(0));
            System.out.printf("Language count is %2d\n", languages.size());

            for (int i = 1; i < workbookTemp.getNumberOfSheets(); i++) {
                Sheet sheet = workbookTemp.getSheetAt(i);
                System.out.println("Workbook Element " + sheet.getSheetName());
                List<ComparisonElement> ceElements = collectComparisonElements(workbookTemp.getSheetAt(i), languages);
                ceElements.forEach((ComparisonElement ce) -> System.out.println("CE: " + ce.toString()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the number of criteria for a given sheet. The criteria are located in the first row.
     * First row first cell is metadata. The criteria are encoded in the following cells until the fist empty.
     * The empty cells are not stored in the final excel. This <a href="http://poi.apache.org/components/spreadsheet/quick-guide.html#Iterator">page</a>
     * explains how to iterate over them.
     *
     * @param sheet Sheet with criteria
     * @return criteria count
     */
    private List<String> collectCriteria(Sheet sheet) {
        List<String> result = new ArrayList<>();
        Row row = sheet.getRow(0);
        for (int i = 1; i < 100; i ++) {
            Cell c = row.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

            if (c == null || !CellType.STRING.equals(c.getCellType())) {
                break;
            }

            result.add(c.getStringCellValue());
        }
        return result;
    }

    /**
     * Returns the number of languages. The languages are beneath each other.
     *
     * @param sheet Languages sheet
     * @return language count
     */
    private List<String> collectLanguages(Sheet sheet) {
        List<String> result = new ArrayList<>();
        Iterator<Row> rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Cell c = row.getCell(0);
            if (CellType.STRING.equals(c.getCellType())) {
                if (c.getStringCellValue().isEmpty()) {
                    break;
                }
                result.add(c.getStringCellValue());
            }
        }
        return result;
    }

    private List<ComparisonElement> collectComparisonElements(Sheet sheet, List<String> languages) {

        List<String> criteria = collectCriteria(sheet);
        System.out.printf("Criteria count is %2d\n", criteria.size());

        System.out.println("\n\nIterating over Rows and Columns using Iterator\n");

        List<ComparisonElement> ceElements = new ArrayList<>();

        for (int i = 25, languageIndex = 0; languageIndex < languages.size(); i++, languageIndex++) {
            Row row = sheet.getRow(i);
            for (int j = 1; j <= criteria.size(); j++) {
                Cell cell = row.getCell(j);
                ComparisonElement ce = new ComparisonElement();
                ce.setLabel(languages.get(languageIndex));
                ce.setCriterium(criteria.get(j - 1));
                ce.setElemValue(getCritValue(cell));
                ceElements.add(ce);
            }
        }
        return ceElements;
    }

    private static String[] getSheetNameOutOfCellFormula(String cellFormula) {
        String[] strings = cellFormula.replace("'", "").split("[\\_!]");
        return strings;
    }

    private static String getLanguage(Cell cell) {
        String langType = "";
        if (cell.getCellType() == CellType.STRING) {
            System.out.print("---> stringValue: " + cell.getRichStringCellValue().getString());
            langType = cell.getRichStringCellValue().getString();
        }
        return langType;
    }

    private static String getCriterium(Cell cell) {
        if (cell.getCellType() == CellType.STRING) {
            return cell.getRichStringCellValue().toString();
        } else if (cell.getCellType() == CellType.FORMULA) {
            if (cell.getCachedFormulaResultType() == CellType.STRING) {
                return cell.getRichStringCellValue().toString();
            }
        }
        return "";
    }

    private static String getFormulaString(Cell cell) {
        if (cell.getCellType() == CellType.FORMULA) {
            String[] strings = getSheetNameOutOfCellFormula(cell.getCellFormula());
            return strings[1] != null ? strings[1] : "";
        }
        return "";
    }

    private static double getCritValue(Cell cell) {
        double critValue = 0;
        if (cell.getCellType() == CellType.NUMERIC) {
            critValue = cell.getNumericCellValue();
        } else if (cell.getCellType() == CellType.FORMULA) {
            switch (cell.getCachedFormulaResultType()) {
                case NUMERIC:
                    critValue = cell.getNumericCellValue();
                    break;
            }
        }
        return critValue;
    }


    private static ComparisonElement createComparisonElement(Cell cell) {

        ComparisonElement ce = new ComparisonElement();
        if (cell.getCellType() == CellType.STRING) {

            System.out.print("---> stringValue: " + cell.getRichStringCellValue().getString());
        }
        if (cell.getCellType() == CellType.NUMERIC) {
            System.out.print("--> numericValue: " + cell.getNumericCellValue());
        }

        if (cell.getCellType() == CellType.FORMULA) {
            System.out.println("Formula is " + cell.getCellFormula() + " --> colIndex: "
                + cell.getColumnIndex() + "...rowIndex" + cell.getRowIndex());
            //           String[] strings = getSheetNameOutOfCellFormula(cell.getCellFormula());
            //           for (String str: strings) {
            //       		System.out.print("---> stringSplittedValue: "+ str+"\n");
            //   		}
            switch (cell.getCachedFormulaResultType()) {
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

        if (cell.getCellType() == CellType.STRING) {

            System.out.print("---> stringValue: " + cell.getRichStringCellValue().getString());
        }
        if (cell.getCellType() == CellType.NUMERIC) {
            System.out.print("--> numericValue: " + cell.getNumericCellValue());
        }

        if (cell.getCellType() == CellType.FORMULA) {
            System.out.println("Formula is " + cell.getCellFormula() + " --> colIndex: "
                + cell.getColumnIndex() + "...rowIndex" + cell.getRowIndex());
            String[] strings = getSheetNameOutOfCellFormula(cell.getCellFormula());
            for (String str : strings) {
                System.out.print("---> stringSplittedValue: " + str + "\n");
            }
            switch (cell.getCachedFormulaResultType()) {
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
