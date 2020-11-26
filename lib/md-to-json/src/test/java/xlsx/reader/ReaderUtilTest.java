package xlsx.reader;

import org.apache.poi.ss.usermodel.Sheet;
import org.junit.jupiter.api.Test;
import xlsx.TestUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static xlsx.reader.ReaderUtil.authorPattern;

public class ReaderUtilTest extends TestUtil {

    @Test
    public void extractWorksheetName_cellWithFormulaGiven_expectsOk() {
        Sheet sheet = this.wb.getSheetAt(1);
        String result = ReaderUtil.extractWorksheetName(sheet.getRow(25).getCell(1));
        assertEquals("2_Programmiersprache", result);
    }

    @Test
    public void extractWorksheetName_cellWithoutFormulaGiven_expectsException() {
        Sheet sheet = this.wb.getSheetAt(1);
        assertThrows(RuntimeException.class, () -> ReaderUtil.extractWorksheetName(sheet.getRow(25).getCell(7)));
    }

    @Test
    public void collectCriteria_sheetWithCriteriaGiven_expectsOk() {
        List<String> result = ReaderUtil.collectCriteria(this.wb.getSheetAt(2));
        assertEquals(7, result.size());
        assertTrue(result.contains("Std & 3rd Party Lib"));
        assertEquals(2, result.indexOf("Std & 3rd Party Lib"));
    }

    @Test
    public void collectLanguages_sheetWithLanguagesGiven_expectsOk() {
        List<String> result = ReaderUtil.collectLanguages(this.wb.getSheetAt(0));
        assertEquals(5, result.size());
        assertTrue(result.contains("Rust"));
        assertEquals(3, result.indexOf("C++"));
    }

    @Test
    public void extractPointsFromCell_cellWithPointsGiven_expectsOk() {
        Double expected = 83.124;
        String result = ReaderUtil.extractPointsFromCell(this.wb.getSheetAt(1).getRow(25).getCell(1));
        assertEquals(expected.toString(), result);
    }

    @Test
    public void readTable_sheetWithPointTableGiven_expectsOk() {
        PointTable expected = expectedPointTableForSheetAtIndexThree();
        PointTable result = ReaderUtil.readTable(this.wb.getSheetAt(3), ReaderUtil.collectLanguages(this.wb.getSheetAt(0)));
        assertEquals(expected, result);
    }

    @Test
    public void readTable_sheetWithPointTableWithReferenceGiven_expectsOk() {
        PointTable expected = expectedPointTableForSheetAtIndexTwo();
        PointTable result = ReaderUtil.readTable(this.wb.getSheetAt(2), ReaderUtil.collectLanguages(this.wb.getSheetAt(0)));
        assertEquals(expected, result);
    }

    @Test
    public void removeAuthorNameFromText() {
        String input = "Surename, Name: Hello world!";
        String expected = "Hello world!";
        assertEquals(expected, input.replaceAll(authorPattern, ""));
    }
}
