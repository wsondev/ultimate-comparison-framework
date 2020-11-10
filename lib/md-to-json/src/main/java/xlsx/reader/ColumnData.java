package xlsx.reader;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ColumnData implements Comparable<ColumnData> {

    @EqualsAndHashCode.Include
    @Getter
    private final String label;

    @EqualsAndHashCode.Include
    @Getter
    private final Type type;

    @Setter
    private String referencingSheetName;

    public String getReferencingSheetName() {
        if (!isReference()) {
            throw new RuntimeException("The column is not a reference");
        }
        return referencingSheetName;
    }

    public Boolean isReference() {
        return Type.REFERENCE.equals(this.type);
    }

    @Override
    public int compareTo(ColumnData columnData) {
        if (columnData == null) {
            return -1;
        }
        return this.label.compareTo(columnData.label);
    }

    public enum Type {
        REFERENCE,VALUE
    }
}
