package xlsx.reader;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public final class CellData implements Comparable<CellData> {

    private final ColumnData column;

    private final String value;

    @Override
    public int compareTo(CellData cellData) {
        return this.column.compareTo(cellData.column);
    }
}
