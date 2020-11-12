package xlsx.converter;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
public final class PointTableViewCell {

    private final String column;

    private final String value;

    private final Boolean hasSubTable;

    private String subTableName;
}
