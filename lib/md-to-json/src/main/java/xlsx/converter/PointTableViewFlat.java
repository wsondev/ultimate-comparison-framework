package xlsx.converter;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
@EqualsAndHashCode
public class PointTableViewFlat {

    String sheetName;

    List<String> header;

    List<Map<String, PointTableViewCell>> rows;
}
