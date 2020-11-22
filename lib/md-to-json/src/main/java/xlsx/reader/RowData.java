package xlsx.reader;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class RowData {

    private final Set<CellData> cells;
}
