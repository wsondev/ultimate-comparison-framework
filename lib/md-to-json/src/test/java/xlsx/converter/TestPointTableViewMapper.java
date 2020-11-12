package xlsx.converter;

import org.junit.jupiter.api.Test;
import xlsx.TestUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPointTableViewMapper extends TestUtil {

    @Test
    public void apply_pointTableGiven_expectsOk() {
        PointTableView expected = this.expectedPointTableViewForSheetAtIndexTwo();
        PointTableView result = new PointTableToViewMapper().apply(expectedPointTableForSheetAtIndexTwo());
        assertEquals(expected, result);
    }
}
