package xlsx.converter;

import xlsx.reader.CellData;

import java.util.function.Function;

public final class CellDataToPointTableViewCellMapper implements Function<CellData, PointTableViewCell> {
    @Override
    public PointTableViewCell apply(CellData cellData) {
        return new PointTableViewCell(cellData.getColumn().getLabel(),
            cellData.getValue(),
            cellData.getColumn().isReference(),
            cellData.getColumn().isReference() ? cellData.getColumn().getReferencingSheetName() : null,
            cellData.getColumn().getComment());
    }
}
