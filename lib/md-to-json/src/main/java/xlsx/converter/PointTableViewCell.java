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

    private final Boolean hasSubTable;

    private String subTableName;
}
