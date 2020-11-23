package xlsx.converter;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public final class PointTableViewCell {

    @Getter
    private final String column;

    private final String value;

    @Getter
    private final Boolean hasSubTable;

    @Getter
    private String subTableName;
}
