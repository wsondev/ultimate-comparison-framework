package xlsx.converter;

import org.junit.jupiter.api.Test;
import xlsx.TestUtil;
import xlsx.reader.PointTable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static xlsx.reader.ReaderUtil.collectLanguages;
import static xlsx.reader.ReaderUtil.readTable;

public class TreeNodeMapperTest extends TestUtil {

    @Test
    public void map_listWithRoot_expectsOk() {
        List<String> languages = collectLanguages(wb.getSheetAt(0));
        List<PointTableViewFlat> views = new ArrayList<>();
        Function<PointTable, PointTableViewFlat> tableMapper = new PointTableToViewFlatMapper();
        for (int i = 1; i < wb.getNumberOfSheets(); ++i) {
            views.add(tableMapper.apply(readTable(wb.getSheetAt(i), languages)));
        }
        FlatTreeNodeView result = new TreeNodeMapper().apply(views);
        assertEquals(6, result.getChildren().size());
    }
}
