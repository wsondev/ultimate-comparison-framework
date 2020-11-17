package xlsx.converter;

import org.junit.jupiter.api.Test;
import xlsx.TestUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPointTableViewFlatMapper extends TestUtil {

    @Test
    public void apply_pointTableGiven_expectsOk() {
        PointTableViewFlat expected = this.expectedPointTableViewFlatForSheetAtIndexTwo();
        PointTableViewFlat result = new PointTableToViewFlatMapper().apply(expectedPointTableForSheetAtIndexTwo());
        assertEquals(expected, result);
    }
}
