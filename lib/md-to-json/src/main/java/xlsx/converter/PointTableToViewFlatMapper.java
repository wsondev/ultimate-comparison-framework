package xlsx.converter;

import xlsx.reader.CellData;
import xlsx.reader.ColumnData;
import xlsx.reader.PointTable;
import xlsx.reader.RowData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public final class PointTableToViewFlatMapper implements Function<PointTable, PointTableViewFlat> {

    @Override
    public PointTableViewFlat apply(final PointTable pt) {
        List<String> headers = pt.getColumns().stream()
            .map(ColumnData::getLabel)
            .collect(toList());

        List<Map<String, PointTableViewCell>> result = new ArrayList<>();
        Function<CellData, PointTableViewCell> cellMapper = new CellDataToPointTableViewCellMapper();

        for (RowData rd : pt.getRows()) {
            result.add(rd.getCells().stream().map(cellMapper).collect(toMap(PointTableViewCell::getColumn, ptv -> ptv)));
        }

        return new PointTableViewFlat(pt.getName(), headers, result);
    }
}
