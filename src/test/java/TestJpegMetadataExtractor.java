import org.dirs.JpegMetadataExtractor;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.fail;

public class TestJpegMetadataExtractor {

    @Test
    public void testDateTaken() {
        try {
            File input = new File(getClass().getClassLoader().getResource("20230825_144841.jpg").getPath());
            String actualDate = JpegMetadataExtractor.readJpegDateTaken(input);
        } catch (IOException e) {
            e.printStackTrace(System.err);
            fail("Could not read file");
        }
    }

}
