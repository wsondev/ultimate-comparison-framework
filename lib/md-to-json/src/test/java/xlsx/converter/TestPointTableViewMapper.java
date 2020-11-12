package xlsx.converter;

import com.google.gson.Gson;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import xlsx.TestUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TestPointTableViewMapper extends TestUtil {

    private final Gson gson = new Gson();

    @Test
    public void apply_pointTableGiven_expectsOk() throws IOException, JSONException {
        List<PointTableView> result = new ArrayList<>();
        result.add(new PointTableToViewMapper().apply(this.expectedPointTableForSheetAtIndexTwo()));
        Path testJson = Path.of(Objects.requireNonNull(this.getClass().getClassLoader().getResource("one_sheet.json")).getPath());
        JSONAssert.assertEquals(new String(Files.readAllBytes(testJson)), gson.toJson(result), false);
    }
}
