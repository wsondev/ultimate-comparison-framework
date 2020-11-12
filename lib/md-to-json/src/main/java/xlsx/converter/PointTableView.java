package xlsx.converter;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

@Value
@EqualsAndHashCode
public class PointTableView {

    String sheetName;

    List<String> header;

    List<List<PointTableViewCell>> rows;
}
