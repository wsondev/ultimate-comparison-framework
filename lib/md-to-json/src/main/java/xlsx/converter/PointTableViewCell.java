package xlsx.converter;

import lombok.*;

@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
public final class PointTableViewCell {

    private final String column;

    private final String value;

    private final Boolean hasSubTable;

    private String subTableName;

    @Setter
    private String tooltip;
}
