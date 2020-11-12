package xlsx.reader;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public final class PointTable {

    private final String name;

    private final Set<ColumnData> columns;

    private final List<RowData> rows;
}
