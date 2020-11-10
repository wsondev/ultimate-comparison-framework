package xlsx.reader;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.text.DecimalFormat;
import java.util.*;

public final class ReaderUtil {

    private static final DecimalFormat decimalFormat = new DecimalFormat("###.###");

    /**
     * Returns the referenced sheet name from the formula.
     *
     * @param cell Cell with formula
     * @return referenced sheet name
     */
    public static String extractWorksheetName(final Cell cell) {
        if (CellType.FORMULA.equals(cell.getCellType())) {
            if (cell.getCellFormula().indexOf("'") == 0) {
                return cell.getCellFormula().substring(1, cell.getCellFormula().indexOf("'", 1));
            } else {
                return cell.getCellFormula().substring(0, cell.getCellFormula().indexOf("!", 1));
            }
        }

        throw new RuntimeException(String.format("The given cell type must be %s", CellType.FORMULA));
    }

    /**
     * Returns points from the given cell. If the cell has an formula attached to it, then the formula result will be
     * returned.
     *
     * @param cell Cell with value to extract
     * @return Points
     */
    public static Double extractPointsFromCell(Cell cell) {
        if (cell.getCellType() == CellType.NUMERIC) {
            return Double.valueOf(decimalFormat.format(cell.getNumericCellValue()));
        } else if (cell.getCellType() == CellType.FORMULA) {
            if (cell.getCachedFormulaResultType() == CellType.NUMERIC) {
                return Double.valueOf(decimalFormat.format(cell.getNumericCellValue()));
            }
        }
        throw new RuntimeException("There is no value to extract from the cell");
    }

    /**
     * Returns the list of criteria for a given sheet. The criteria are located in the first row.
     * First row first cell is metadata. The criteria are encoded in the following cells until the fist empty.
     * The empty cells are not stored in the final excel. This <a href="http://poi.apache.org/components/spreadsheet/quick-guide.html#Iterator">page</a>
     * explains how to iterate over them.
     *
     * @param sheet Sheet with criteria
     * @return criteria count
     */
    public static List<String> collectCriteria(Sheet sheet) {
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
     * Returns the list of languages. The languages are beneath each other.
     *
     * @param sheet Languages sheet
     * @return language count
     */
    public static List<String> collectLanguages(Sheet sheet) {
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

    public static PointTable readTable(final Sheet sheet, final List<String> languages) {
        List<String> columnNames = collectCriteria(sheet);
        Set<ColumnData> columns = new TreeSet<>();
        List<RowData> rows = new ArrayList<>();
        boolean columnAdded = true;
        for (int i = 25, languageIndex = 0; languageIndex < languages.size(); i++, languageIndex++) {
            Row row = sheet.getRow(i);
            List<CellData> languageResults = new ArrayList<>();
            for (int j = 1; j <= columnNames.size(); j++) {
                Cell cell = row.getCell(j);
                ColumnData cd = new ColumnData(columnNames.get(j - 1), CellType.FORMULA.equals(cell.getCellType()) ? ColumnData.Type.REFERENCE : ColumnData.Type.VALUE);
                if (cd.isReference()) {
                    cd.setReferencingSheetName(extractWorksheetName(cell));
                }
                languageResults.add(new CellData(cd, extractPointsFromCell(cell)));
                if (columnAdded) {
                    columns.add(cd);
                }
            }
            columnAdded = false;
            rows.add(new RowData(languageResults));
        }
        return new PointTable(columns, rows);
    }
}
