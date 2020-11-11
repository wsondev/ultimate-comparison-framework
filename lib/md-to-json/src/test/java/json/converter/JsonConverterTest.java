package json.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import xlsx.converter.ComparisonCriteria;
import xlsx.converter.ComparisonElement;
import xlsx.converter.PlatformComparison;
import xlsx.reader.ReaderUtil;

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

    //@Test
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

    //@Test
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

    //@Test
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

    @Test
    public void convertObjects2JsonString() throws EncryptedDocumentException, IOException {

    	String fileName = "";


    	try (Stream<Path> paths = Files.walk(this.root, 2)) {
            fileName = paths.map(path -> path.toString()).filter(f -> f.endsWith(".xlsx")).findFirst().get();

        }
    	System.out.println("fileName " + fileName);


    	PlatformComparison pc = getContentFromExcelFile(fileName);
    	//PlatformComparison pc = new PlatformComparison();
        writeObjects2JsonFile(pc, "platform-comparison.json");
        //return jsonString;
    }

    private static void writeObjects2JsonFile(PlatformComparison pc, String pathFile) {
        ObjectMapper mapper = new ObjectMapper();

        File file = new File(pathFile);
        try {
            // Serialize Java object info JSON file.
            mapper.writeValue(file, pc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param filePath
     * @return PlatformComparison
     * @throws EncryptedDocumentException
     * @throws IOException
     */
    private PlatformComparison getContentFromExcelFile(String filePath) throws EncryptedDocumentException, IOException {
    	PlatformComparison pc = new PlatformComparison();
    	try (Workbook workbookTemp = WorkbookFactory.create(new File(filePath))) {

            // Retrieving the number of sheets in the Workbook
            System.out.println("Workbook has " + workbookTemp.getNumberOfSheets() + " Sheets : ");

            List<String> languages = ReaderUtil.collectLanguages(workbookTemp.getSheetAt(0));
            System.out.printf("Language count is %2d\n", languages.size());

            pc.setLabel("Platformvergleich");
            List<ComparisonCriteria> ccList = new ArrayList<>();
            for (int i = 1; i < workbookTemp.getNumberOfSheets(); i++) {
                Sheet sheet = workbookTemp.getSheetAt(i);
                System.out.println("Workbook Element " + sheet.getSheetName());
                ComparisonCriteria cc = new ComparisonCriteria();
                cc.setLabel(sheet.getSheetName());
                List<ComparisonElement> ceElements = collectComparisonElements(workbookTemp.getSheetAt(i), languages);
                ceElements.forEach((ComparisonElement ce) -> {
                	System.out.println("CE: " + ce.toString());
                });
                cc.setCriteriaElements(ceElements);
                ccList.add(cc);
            }
            pc.setCriteria(ccList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pc;
    }

    private List<ComparisonElement> collectComparisonElements(Sheet sheet, List<String> languages) {
        List<String> criteria = ReaderUtil.collectCriteria(sheet);
        System.out.printf("Criteria count is %2d\n", criteria.size());

        List<ComparisonElement> ceElements = new ArrayList<>();

        for (int i = 25, languageIndex = 0; languageIndex < languages.size(); i++, languageIndex++) {
            Row row = sheet.getRow(i);
            for (int j = 1; j <= criteria.size(); j++) {
                Cell cell = row.getCell(j);
                ComparisonElement ce = new ComparisonElement();
                ce.setLabel(languages.get(languageIndex));
                ce.setCriterium(criteria.get(j - 1));
                ce.setElemValue(Double.valueOf(ReaderUtil.extractPointsFromCell(cell)));
                ceElements.add(ce);
            }
        }
        return ceElements;
    }
}
