package xlsx.reader;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import xlsx.converter.PointTableToViewFlatMapper;
import xlsx.converter.PointTableViewFlat;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static xlsx.reader.ReaderUtil.collectLanguages;
import static xlsx.reader.ReaderUtil.readTable;

public final class ReaderUtilFlat implements Function<Path, List<PointTableViewFlat>> {

    private final Function<PointTable, PointTableViewFlat> tableMapper = new PointTableToViewFlatMapper();

    private ReaderUtilFlat() {
    }

    public static Function<Path, List<PointTableViewFlat>> create() {
        return new ReaderUtilFlat();
    }

    @Override
    public List<PointTableViewFlat> apply(Path path) {
        try (Workbook wb = WorkbookFactory.create(path.toFile())) {
            List<String> languages = collectLanguages(wb.getSheetAt(0));
            List<PointTableViewFlat> views = new ArrayList<>();
            for (int i = 1; i < wb.getNumberOfSheets(); ++i) {
                views.add(tableMapper.apply(readTable(wb.getSheetAt(i), languages)));
            }
            return views;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
