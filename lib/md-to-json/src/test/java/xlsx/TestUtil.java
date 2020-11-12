package xlsx;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import xlsx.converter.PointTableView;
import xlsx.converter.PointTableViewCell;
import xlsx.reader.CellData;
import xlsx.reader.ColumnData;
import xlsx.reader.PointTable;
import xlsx.reader.RowData;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public abstract class TestUtil {

    protected Workbook wb;

    @BeforeEach
    public void setUp() {
        this.wb = openWorkbook();
    }

    @AfterEach
    public void tearDown() {
        try {
            this.wb.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected Workbook openWorkbook() {
        ClassLoader cl = this.getClass().getClassLoader();
        File exelFile = new File(Objects.requireNonNull(cl.getResource("AIO_Daten_Arbeitsmappe.xlsx")).getFile());
        try (Workbook w = WorkbookFactory.create(exelFile)) {
            return w;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected PointTable expectedPointTableForSheetAtIndexThree() {
        Set<ColumnData> columns = new TreeSet<>();
        columns.add(new ColumnData("Prozedural", ColumnData.Type.VALUE));
        columns.add(new ColumnData("Funktional", ColumnData.Type.VALUE));
        columns.add(new ColumnData("Objekt Orientiert", ColumnData.Type.VALUE));
        columns.add(new ColumnData("Summe", ColumnData.Type.VALUE));
        columns.add(new ColumnData("Sprachen", ColumnData.Type.VALUE));

        List<RowData> rows = new ArrayList<>();

        Set<CellData> javaResults = new TreeSet<>();
        javaResults.add(new CellData(new ColumnData("Prozedural", ColumnData.Type.VALUE), "100.0"));
        javaResults.add(new CellData(new ColumnData("Funktional", ColumnData.Type.VALUE), "50.0"));
        javaResults.add(new CellData(new ColumnData("Objekt Orientiert", ColumnData.Type.VALUE), "100.0"));
        javaResults.add(new CellData(new ColumnData("Summe", ColumnData.Type.VALUE), "90.341"));
        javaResults.add(new CellData(new ColumnData("Sprachen", ColumnData.Type.VALUE), "Java"));

        Set<CellData> goResults = new TreeSet<>();
        goResults.add(new CellData(new ColumnData("Prozedural", ColumnData.Type.VALUE), "100.0"));
        goResults.add(new CellData(new ColumnData("Funktional", ColumnData.Type.VALUE), "80.0"));
        goResults.add(new CellData(new ColumnData("Objekt Orientiert", ColumnData.Type.VALUE), "60.0"));
        goResults.add(new CellData(new ColumnData("Summe", ColumnData.Type.VALUE), "67.196"));
        goResults.add(new CellData(new ColumnData("Sprachen", ColumnData.Type.VALUE), "Go"));

        Set<CellData> rustResults = new TreeSet<>();
        rustResults.add(new CellData(new ColumnData("Prozedural", ColumnData.Type.VALUE), "100.0"));
        rustResults.add(new CellData(new ColumnData("Funktional", ColumnData.Type.VALUE), "50.0"));
        rustResults.add(new CellData(new ColumnData("Objekt Orientiert", ColumnData.Type.VALUE), "100.0"));
        rustResults.add(new CellData(new ColumnData("Summe", ColumnData.Type.VALUE), "90.341"));
        rustResults.add(new CellData(new ColumnData("Sprachen", ColumnData.Type.VALUE), "Rust"));

        Set<CellData> cppResults = new TreeSet<>();
        cppResults.add(new CellData(new ColumnData("Prozedural", ColumnData.Type.VALUE), "100.0"));
        cppResults.add(new CellData(new ColumnData("Funktional", ColumnData.Type.VALUE), "60.0"));
        cppResults.add(new CellData(new ColumnData("Objekt Orientiert", ColumnData.Type.VALUE), "100.0"));
        cppResults.add(new CellData(new ColumnData("Summe", ColumnData.Type.VALUE), "92.273"));
        cppResults.add(new CellData(new ColumnData("Sprachen", ColumnData.Type.VALUE), "C++"));

        Set<CellData> pythonResults = new TreeSet<>();
        pythonResults.add(new CellData(new ColumnData("Prozedural", ColumnData.Type.VALUE), "100.0"));
        pythonResults.add(new CellData(new ColumnData("Funktional", ColumnData.Type.VALUE), "60.0"));
        pythonResults.add(new CellData(new ColumnData("Objekt Orientiert", ColumnData.Type.VALUE), "60.0"));
        pythonResults.add(new CellData(new ColumnData("Summe", ColumnData.Type.VALUE), "63.332"));
        pythonResults.add(new CellData(new ColumnData("Sprachen", ColumnData.Type.VALUE), "Python"));

        rows.add(new RowData(javaResults));
        rows.add(new RowData(goResults));
        rows.add(new RowData(rustResults));
        rows.add(new RowData(cppResults));
        rows.add(new RowData(pythonResults));
        return new PointTable("2_1_Paradigmen", columns, rows);
    }

    protected PointTable expectedPointTableForSheetAtIndexTwo() {
        Set<ColumnData> columns = new TreeSet<>();
        //Paradigms
        ColumnData paradigms = new ColumnData("Paradigmen", ColumnData.Type.REFERENCE);
        paradigms.setReferencingSheetName("2_1_Paradigmen");
        columns.add(paradigms);
        //Exception handling
        ColumnData exceptionHandling = new ColumnData("Exception Handling", ColumnData.Type.VALUE);
        columns.add(exceptionHandling);
        //Standard lib
        ColumnData stdLib = new ColumnData("Std Lib & 3rd Party", ColumnData.Type.REFERENCE);
        stdLib.setReferencingSheetName("2_2_StdLib");
        columns.add(stdLib);
        //Modularization
        ColumnData modularization = new ColumnData("Modularisierung", ColumnData.Type.VALUE);
        columns.add(modularization);
        //Integration with C and C++
        ColumnData integrationOfCpp = new ColumnData("Integration von System libs (C, C++)", ColumnData.Type.VALUE);
        columns.add(integrationOfCpp);
        //Integration with other programming languages
        ColumnData integrationOfOther = new ColumnData("Integration mit anderen Programiersprachen", ColumnData.Type.VALUE);
        columns.add(integrationOfOther);
        //Learning curve
        ColumnData learningCurve = new ColumnData("Lernkurve", ColumnData.Type.VALUE);
        columns.add(learningCurve);
        //Points sum
        ColumnData pointSum = new ColumnData("Summe", ColumnData.Type.VALUE);
        columns.add(pointSum);
        //Language
        ColumnData language = new ColumnData("Sprachen", ColumnData.Type.VALUE);
        columns.add(language);

        List<RowData> rows = new ArrayList<>();

        Set<CellData> javaResults = new TreeSet<>();
        javaResults.add(new CellData(paradigms, "90.341"));
        javaResults.add(new CellData(exceptionHandling, "100.0"));
        javaResults.add(new CellData(stdLib, "74.727"));
        javaResults.add(new CellData(modularization, "100.0"));
        javaResults.add(new CellData(integrationOfCpp, "70.0"));
        javaResults.add(new CellData(integrationOfOther, "50.0"));
        javaResults.add(new CellData(learningCurve, "60.0"));
        javaResults.add(new CellData(pointSum, "83.124"));
        javaResults.add(new CellData(language, "Java"));

        Set<CellData> goResults = new TreeSet<>();
        goResults.add(new CellData(paradigms, "67.196"));
        goResults.add(new CellData(exceptionHandling, "100.0"));
        goResults.add(new CellData(stdLib, "82.644"));
        goResults.add(new CellData(modularization, "100.0"));
        goResults.add(new CellData(integrationOfCpp, "100.0"));
        goResults.add(new CellData(integrationOfOther, "100.0"));
        goResults.add(new CellData(learningCurve, "100.0"));
        goResults.add(new CellData(pointSum, "92.475"));
        goResults.add(new CellData(language, "Go"));

        Set<CellData> rustResults = new TreeSet<>();
        rustResults.add(new CellData(paradigms, "90.341"));
        rustResults.add(new CellData(exceptionHandling, "100.0"));
        rustResults.add(new CellData(stdLib, "82.644"));
        rustResults.add(new CellData(modularization, "100.0"));
        rustResults.add(new CellData(integrationOfCpp, "100.0"));
        rustResults.add(new CellData(integrationOfOther, "100.0"));
        rustResults.add(new CellData(learningCurve, "30.0"));
        rustResults.add(new CellData(pointSum, "91.133"));
        rustResults.add(new CellData(language, "Rust"));

        Set<CellData> cppResults = new TreeSet<>();
        cppResults.add(new CellData(paradigms, "92.273"));
        cppResults.add(new CellData(exceptionHandling, "100.0"));
        cppResults.add(new CellData(stdLib, "68.616"));
        cppResults.add(new CellData(modularization, "100.0"));
        cppResults.add(new CellData(integrationOfCpp, "100.0"));
        cppResults.add(new CellData(integrationOfOther, "100.0"));
        cppResults.add(new CellData(learningCurve, "20.0"));
        cppResults.add(new CellData(pointSum, "87.670"));
        cppResults.add(new CellData(language, "C++"));

        Set<CellData> pythonResults = new TreeSet<>();
        pythonResults.add(new CellData(paradigms, "63.332"));
        pythonResults.add(new CellData(exceptionHandling, "100.0"));
        pythonResults.add(new CellData(stdLib, "77.969"));
        pythonResults.add(new CellData(modularization, "70.0"));
        pythonResults.add(new CellData(integrationOfCpp, "80.0"));
        pythonResults.add(new CellData(integrationOfOther, "0.0"));
        pythonResults.add(new CellData(learningCurve, "50.0"));
        pythonResults.add(new CellData(pointSum, "70.559"));
        pythonResults.add(new CellData(language, "Python"));

        rows.add(new RowData(javaResults));
        rows.add(new RowData(goResults));
        rows.add(new RowData(rustResults));
        rows.add(new RowData(cppResults));
        rows.add(new RowData(pythonResults));
        return new PointTable("2_Programmiersprache", columns, rows);
    }

    protected PointTableView expectedPointTableViewForSheetAtIndexTwo() {
        List<String> headers = Stream.of("Sprachen", "Paradigmen", "Exception Handling", "Std Lib & 3rd Party",
            "Modularisierung", "Integration von System libs (C, C++)", "Integration mit anderen Programiersprachen",
            "Lernkurve", "Summe").sorted().collect(toList());
        List<List<PointTableViewCell>> rows = new ArrayList<>();

        List<PointTableViewCell> javaResults = new ArrayList<>();
        javaResults.add(new PointTableViewCell("Sprachen", "Java", false));
        javaResults.add(new PointTableViewCell("Paradigmen", "90.341", true, "2_1_Paradigmen"));
        javaResults.add(new PointTableViewCell("Exception Handling", "100.0", false));
        javaResults.add(new PointTableViewCell("Std Lib & 3rd Party", "74.727", true, "2_2_StdLib"));
        javaResults.add(new PointTableViewCell("Modularisierung", "100.0", false));
        javaResults.add(new PointTableViewCell("Integration von System libs (C, C++)", "70.0", false));
        javaResults.add(new PointTableViewCell("Integration mit anderen Programiersprachen", "50.0", false));
        javaResults.add(new PointTableViewCell("Lernkurve", "60.0", false));
        javaResults.add(new PointTableViewCell("Summe", "83.124", false));

        List<PointTableViewCell> goResults = new ArrayList<>();
        goResults.add(new PointTableViewCell("Sprachen", "Go", false));
        goResults.add(new PointTableViewCell("Paradigmen", "67.196", true, "2_1_Paradigmen"));
        goResults.add(new PointTableViewCell("Exception Handling", "100.0", false));
        goResults.add(new PointTableViewCell("Std Lib & 3rd Party", "82.644", true, "2_2_StdLib"));
        goResults.add(new PointTableViewCell("Modularisierung", "100.0", false));
        goResults.add(new PointTableViewCell("Integration von System libs (C, C++)", "100.0", false));
        goResults.add(new PointTableViewCell("Integration mit anderen Programiersprachen", "100.0", false));
        goResults.add(new PointTableViewCell("Lernkurve", "100.0", false));
        goResults.add(new PointTableViewCell("Summe", "92.475", false));

        List<PointTableViewCell> rustResults = new ArrayList<>();
        rustResults.add(new PointTableViewCell("Sprachen", "Rust", false));
        rustResults.add(new PointTableViewCell("Paradigmen", "90.341", true, "2_1_Paradigmen"));
        rustResults.add(new PointTableViewCell("Exception Handling", "100.0", false));
        rustResults.add(new PointTableViewCell("Std Lib & 3rd Party", "82.644", true, "2_2_StdLib"));
        rustResults.add(new PointTableViewCell("Modularisierung", "100.0", false));
        rustResults.add(new PointTableViewCell("Integration von System libs (C, C++)", "100.0", false));
        rustResults.add(new PointTableViewCell("Integration mit anderen Programiersprachen", "100.0", false));
        rustResults.add(new PointTableViewCell("Lernkurve", "30.0", false));
        rustResults.add(new PointTableViewCell("Summe", "91.133", false));

        List<PointTableViewCell> cppResults = new ArrayList<>();
        cppResults.add(new PointTableViewCell("Sprachen", "C++", false));
        cppResults.add(new PointTableViewCell("Paradigmen", "92.273", true, "2_1_Paradigmen"));
        cppResults.add(new PointTableViewCell("Exception Handling", "100.0", false));
        cppResults.add(new PointTableViewCell("Std Lib & 3rd Party", "68.616", true, "2_2_StdLib"));
        cppResults.add(new PointTableViewCell("Modularisierung", "100.0", false));
        cppResults.add(new PointTableViewCell("Integration von System libs (C, C++)", "100.0", false));
        cppResults.add(new PointTableViewCell("Integration mit anderen Programiersprachen", "100.0", false));
        cppResults.add(new PointTableViewCell("Lernkurve", "20.0", false));
        cppResults.add(new PointTableViewCell("Summe", "87.670", false));

        List<PointTableViewCell> pythonResults = new ArrayList<>();
        pythonResults.add(new PointTableViewCell("Sprachen", "Python", false));
        pythonResults.add(new PointTableViewCell("Paradigmen", "63.332", true, "2_1_Paradigmen"));
        pythonResults.add(new PointTableViewCell("Exception Handling", "100.0", false));
        pythonResults.add(new PointTableViewCell("Std Lib & 3rd Party", "77.969", true, "2_2_StdLib"));
        pythonResults.add(new PointTableViewCell("Modularisierung", "70.0", false));
        pythonResults.add(new PointTableViewCell("Integration von System libs (C, C++)", "80.0", false));
        pythonResults.add(new PointTableViewCell("Integration mit anderen Programiersprachen", "0.0", false));
        pythonResults.add(new PointTableViewCell("Lernkurve", "50.0", false));
        pythonResults.add(new PointTableViewCell("Summe", "70.559", false));

        rows.add(javaResults.stream().sorted(Comparator.comparing(PointTableViewCell::getColumn)).collect(toList()));
        rows.add(goResults.stream().sorted(Comparator.comparing(PointTableViewCell::getColumn)).collect(toList()));
        rows.add(rustResults.stream().sorted(Comparator.comparing(PointTableViewCell::getColumn)).collect(toList()));
        rows.add(cppResults.stream().sorted(Comparator.comparing(PointTableViewCell::getColumn)).collect(toList()));
        rows.add(pythonResults.stream().sorted(Comparator.comparing(PointTableViewCell::getColumn)).collect(toList()));
        return new PointTableView("2_Programmiersprache", headers, rows);
    }
}
