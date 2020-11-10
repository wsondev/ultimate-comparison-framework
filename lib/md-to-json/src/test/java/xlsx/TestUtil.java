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
        Set<ColumnData> columns = new HashSet<>();
        columns.add(new ColumnData("Prozedural", ColumnData.Type.VALUE));
        columns.add(new ColumnData("Funktional", ColumnData.Type.VALUE));
        columns.add(new ColumnData("Objekt Orientiert", ColumnData.Type.VALUE));

        List<RowData> rows = new ArrayList<>();

        List<CellData> javaResults = new ArrayList<>();
        javaResults.add(new CellData(new ColumnData("Prozedural", ColumnData.Type.VALUE), 100.0));
        javaResults.add(new CellData(new ColumnData("Funktional", ColumnData.Type.VALUE), 50.0));
        javaResults.add(new CellData(new ColumnData("Objekt Orientiert", ColumnData.Type.VALUE), 100.0));

        List<CellData> goResults = new ArrayList<>();
        goResults.add(new CellData(new ColumnData("Prozedural", ColumnData.Type.VALUE), 100.0));
        goResults.add(new CellData(new ColumnData("Funktional", ColumnData.Type.VALUE), 80.0));
        goResults.add(new CellData(new ColumnData("Objekt Orientiert", ColumnData.Type.VALUE), 60.0));

        List<CellData> rustResults = new ArrayList<>();
        rustResults.add(new CellData(new ColumnData("Prozedural", ColumnData.Type.VALUE), 100.0));
        rustResults.add(new CellData(new ColumnData("Funktional", ColumnData.Type.VALUE), 50.0));
        rustResults.add(new CellData(new ColumnData("Objekt Orientiert", ColumnData.Type.VALUE), 100.0));

        List<CellData> cppResults = new ArrayList<>();
        cppResults.add(new CellData(new ColumnData("Prozedural", ColumnData.Type.VALUE), 100.0));
        cppResults.add(new CellData(new ColumnData("Funktional", ColumnData.Type.VALUE), 60.0));
        cppResults.add(new CellData(new ColumnData("Objekt Orientiert", ColumnData.Type.VALUE), 100.0));

        List<CellData> pythonResults = new ArrayList<>();
        pythonResults.add(new CellData(new ColumnData("Prozedural", ColumnData.Type.VALUE), 100.0));
        pythonResults.add(new CellData(new ColumnData("Funktional", ColumnData.Type.VALUE), 60.0));
        pythonResults.add(new CellData(new ColumnData("Objekt Orientiert", ColumnData.Type.VALUE), 60.0));

        rows.add(new RowData(javaResults));
        rows.add(new RowData(goResults));
        rows.add(new RowData(rustResults));
        rows.add(new RowData(cppResults));
        rows.add(new RowData(pythonResults));
        return new PointTable(columns, rows);
    }

    protected PointTable expectedPointTableForSheetAtIndexTwo() {
        Set<ColumnData> columns = new TreeSet<>();
        ColumnData paradigmen = new ColumnData("Paradigmen", ColumnData.Type.REFERENCE);
        paradigmen.setReferencingSheetName("2_1_Paradigmen");
        columns.add(paradigmen);
        ColumnData exceptionHandling = new ColumnData("Exception Handling", ColumnData.Type.VALUE);
        columns.add(exceptionHandling);
        ColumnData stdLib = new ColumnData("Std Lib & 3rd Party", ColumnData.Type.REFERENCE);
        stdLib.setReferencingSheetName("2_2_StdLib");
        columns.add(stdLib);
        ColumnData modularisierung = new ColumnData("Modularisierung", ColumnData.Type.VALUE);
        columns.add(modularisierung);
        ColumnData integrationOfCpp = new ColumnData("Integration mit System Libs (C, C++)", ColumnData.Type.VALUE);
        columns.add(integrationOfCpp);
        ColumnData integrationOfOther = new ColumnData("Integration mit anderen Programiersprachen", ColumnData.Type.VALUE);
        columns.add(integrationOfOther);
        ColumnData lernkurve = new ColumnData("Lernkurve", ColumnData.Type.VALUE);
        columns.add(lernkurve);

        List<RowData> rows = new ArrayList<>();

        List<CellData> javaResults = new ArrayList<>();
        javaResults.add(new CellData(paradigmen, 90.341));
        javaResults.add(new CellData(exceptionHandling, 100.0));
        javaResults.add(new CellData(stdLib, 74.727));
        javaResults.add(new CellData(modularisierung, 100.0));
        javaResults.add(new CellData(integrationOfCpp, 70.0));
        javaResults.add(new CellData(integrationOfOther, 50.0));
        javaResults.add(new CellData(lernkurve, 60.0));

        List<CellData> goResults = new ArrayList<>();
        goResults.add(new CellData(paradigmen, 67.196));
        goResults.add(new CellData(exceptionHandling, 100.0));
        goResults.add(new CellData(stdLib, 82.644));
        goResults.add(new CellData(modularisierung, 100.0));
        goResults.add(new CellData(integrationOfCpp, 100.0));
        goResults.add(new CellData(integrationOfOther, 100.0));
        goResults.add(new CellData(lernkurve, 100.0));

        List<CellData> rustResults = new ArrayList<>();
        rustResults.add(new CellData(paradigmen, 90.341));
        rustResults.add(new CellData(exceptionHandling, 100.0));
        rustResults.add(new CellData(stdLib, 82.644));
        rustResults.add(new CellData(modularisierung, 100.0));
        rustResults.add(new CellData(integrationOfCpp, 100.0));
        rustResults.add(new CellData(integrationOfOther, 100.0));
        rustResults.add(new CellData(lernkurve, 30.0));

        List<CellData> cppResults = new ArrayList<>();
        cppResults.add(new CellData(paradigmen, 92.273));
        cppResults.add(new CellData(exceptionHandling, 100.0));
        cppResults.add(new CellData(stdLib, 77.969));
        cppResults.add(new CellData(modularisierung, 100.0));
        cppResults.add(new CellData(integrationOfCpp, 100.0));
        cppResults.add(new CellData(integrationOfOther, 100.0));
        cppResults.add(new CellData(lernkurve, 20.0));

        List<CellData> pythonResults = new ArrayList<>();
        pythonResults.add(new CellData(paradigmen, 63.332));
        pythonResults.add(new CellData(exceptionHandling, 100.0));
        pythonResults.add(new CellData(stdLib, 77.969));
        pythonResults.add(new CellData(modularisierung, 70.0));
        pythonResults.add(new CellData(integrationOfCpp, 80.0));
        pythonResults.add(new CellData(integrationOfOther, 0.0));
        pythonResults.add(new CellData(lernkurve, 50.0));

        rows.add(new RowData(javaResults));
        rows.add(new RowData(goResults));
        rows.add(new RowData(rustResults));
        rows.add(new RowData(cppResults));
        rows.add(new RowData(pythonResults));
        return new PointTable(columns, rows);
    }
}
