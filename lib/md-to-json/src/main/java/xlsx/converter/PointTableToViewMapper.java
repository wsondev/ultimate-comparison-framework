package xlsx.converter;

import xlsx.reader.CellData;
import xlsx.reader.ColumnData;
import xlsx.reader.PointTable;
import xlsx.reader.RowData;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public final class PointTableToViewMapper implements Function<PointTable, PointTableView> {

    @Override
    public PointTableView apply(final PointTable pt) {
        List<String> headers = pt.getColumns().stream()
            .map(ColumnData::getLabel)
            .collect(toList());

        List<List<PointTableViewCell>> result = new ArrayList<>();
        Function<CellData, PointTableViewCell> cellMapper = new CellDataToPointTableViewCellMapper();

        for (RowData rd : pt.getRows()) {
            result.add(rd.getCells().stream().map(cellMapper).collect(toList()));
        }

        return new PointTableView(pt.getName(), headers, result);
    }
}
