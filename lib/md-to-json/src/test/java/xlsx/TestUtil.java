package xlsx;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import xlsx.reader.CellData;
import xlsx.reader.ColumnData;
import xlsx.reader.PointTable;
import xlsx.reader.RowData;

import java.io.File;
import java.io.IOException;
import java.util.*;

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
        File exelFile = new File(Objects.requireNonNull(cl.getResource("uploads/AIO_Daten_v1_1.xlsx")).getFile());
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

        List<RowData> rows = new ArrayList<>();

        Set<CellData> javaResults = new TreeSet<>();
        javaResults.add(new CellData(new ColumnData("Prozedural", ColumnData.Type.VALUE), 100.0));
        javaResults.add(new CellData(new ColumnData("Funktional", ColumnData.Type.VALUE), 50.0));
        javaResults.add(new CellData(new ColumnData("Objekt Orientiert", ColumnData.Type.VALUE), 100.0));
        javaResults.add(new CellData(new ColumnData("Summe", ColumnData.Type.VALUE), 90.341));

        Set<CellData> goResults = new TreeSet<>();
        goResults.add(new CellData(new ColumnData("Prozedural", ColumnData.Type.VALUE), 100.0));
        goResults.add(new CellData(new ColumnData("Funktional", ColumnData.Type.VALUE), 80.0));
        goResults.add(new CellData(new ColumnData("Objekt Orientiert", ColumnData.Type.VALUE), 60.0));
        goResults.add(new CellData(new ColumnData("Summe", ColumnData.Type.VALUE), 67.196));

        Set<CellData> rustResults = new TreeSet<>();
        rustResults.add(new CellData(new ColumnData("Prozedural", ColumnData.Type.VALUE), 100.0));
        rustResults.add(new CellData(new ColumnData("Funktional", ColumnData.Type.VALUE), 50.0));
        rustResults.add(new CellData(new ColumnData("Objekt Orientiert", ColumnData.Type.VALUE), 100.0));
        rustResults.add(new CellData(new ColumnData("Summe", ColumnData.Type.VALUE), 90.341));

        Set<CellData> cppResults = new TreeSet<>();
        cppResults.add(new CellData(new ColumnData("Prozedural", ColumnData.Type.VALUE), 100.0));
        cppResults.add(new CellData(new ColumnData("Funktional", ColumnData.Type.VALUE), 60.0));
        cppResults.add(new CellData(new ColumnData("Objekt Orientiert", ColumnData.Type.VALUE), 100.0));
        cppResults.add(new CellData(new ColumnData("Summe", ColumnData.Type.VALUE), 92.273));

        Set<CellData> pythonResults = new TreeSet<>();
        pythonResults.add(new CellData(new ColumnData("Prozedural", ColumnData.Type.VALUE), 100.0));
        pythonResults.add(new CellData(new ColumnData("Funktional", ColumnData.Type.VALUE), 60.0));
        pythonResults.add(new CellData(new ColumnData("Objekt Orientiert", ColumnData.Type.VALUE), 60.0));
        pythonResults.add(new CellData(new ColumnData("Summe", ColumnData.Type.VALUE), 63.332));

        rows.add(new RowData(javaResults));
        rows.add(new RowData(goResults));
        rows.add(new RowData(rustResults));
        rows.add(new RowData(cppResults));
        rows.add(new RowData(pythonResults));
        return new PointTable(columns, rows);
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

        List<RowData> rows = new ArrayList<>();

        Set<CellData> javaResults = new TreeSet<>();
        javaResults.add(new CellData(paradigms, 90.341));
        javaResults.add(new CellData(exceptionHandling, 100.0));
        javaResults.add(new CellData(stdLib, 74.727));
        javaResults.add(new CellData(modularization, 100.0));
        javaResults.add(new CellData(integrationOfCpp, 70.0));
        javaResults.add(new CellData(integrationOfOther, 50.0));
        javaResults.add(new CellData(learningCurve, 60.0));
        javaResults.add(new CellData(pointSum, 83.124));

        Set<CellData> goResults = new TreeSet<>();
        goResults.add(new CellData(paradigms, 67.196));
        goResults.add(new CellData(exceptionHandling, 100.0));
        goResults.add(new CellData(stdLib, 82.644));
        goResults.add(new CellData(modularization, 100.0));
        goResults.add(new CellData(integrationOfCpp, 100.0));
        goResults.add(new CellData(integrationOfOther, 100.0));
        goResults.add(new CellData(learningCurve, 100.0));
        goResults.add(new CellData(pointSum, 92.475));

        Set<CellData> rustResults = new TreeSet<>();
        rustResults.add(new CellData(paradigms, 90.341));
        rustResults.add(new CellData(exceptionHandling, 100.0));
        rustResults.add(new CellData(stdLib, 82.644));
        rustResults.add(new CellData(modularization, 100.0));
        rustResults.add(new CellData(integrationOfCpp, 100.0));
        rustResults.add(new CellData(integrationOfOther, 100.0));
        rustResults.add(new CellData(learningCurve, 30.0));
        rustResults.add(new CellData(pointSum, 91.133));

        Set<CellData> cppResults = new TreeSet<>();
        cppResults.add(new CellData(paradigms, 92.273));
        cppResults.add(new CellData(exceptionHandling, 100.0));
        cppResults.add(new CellData(stdLib, 77.969));
        cppResults.add(new CellData(modularization, 100.0));
        cppResults.add(new CellData(integrationOfCpp, 100.0));
        cppResults.add(new CellData(integrationOfOther, 100.0));
        cppResults.add(new CellData(learningCurve, 20.0));
        cppResults.add(new CellData(pointSum, 89.757));

        Set<CellData> pythonResults = new TreeSet<>();
        pythonResults.add(new CellData(paradigms, 63.332));
        pythonResults.add(new CellData(exceptionHandling, 100.0));
        pythonResults.add(new CellData(stdLib, 77.969));
        pythonResults.add(new CellData(modularization, 70.0));
        pythonResults.add(new CellData(integrationOfCpp, 80.0));
        pythonResults.add(new CellData(integrationOfOther, 0.0));
        pythonResults.add(new CellData(learningCurve, 50.0));
        pythonResults.add(new CellData(pointSum, 70.559));

        rows.add(new RowData(javaResults));
        rows.add(new RowData(goResults));
        rows.add(new RowData(rustResults));
        rows.add(new RowData(cppResults));
        rows.add(new RowData(pythonResults));
        return new PointTable(columns, rows);
    }
}
