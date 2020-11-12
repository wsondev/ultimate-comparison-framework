package xlsx.converter;

import lombok.Value;

import java.util.List;

@Value
public class PointTableView {

    String sheetName;

    List<String> header;

    List<List<PointTableViewCell>> rows;
}
