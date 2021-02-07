import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(DataProviderRunner.class)
public class PolishNotationTest {
    private static final String EXCEL_WRONG = "src/test/resources/ExcelWrong.xls";
    private static final String EXCEL_RIGHT = "src/test/resources/ExcelRight.xls";
    private static final String INVALID_POLISH_NOTATION = "( 3 + 2 ) )";
    private static final String DIVISION_BY_ZERO = "2 + 10 / 0";

    @DataProvider
    public static Object[][] getPathToPolishNotation() {
        return new Object[][]{

                {EXCEL_RIGHT, true},
                {EXCEL_WRONG, false}
        };
    }

    @Test
    @UseDataProvider("getPathToPolishNotation")
    public void testPolishNotation(final String PATH, boolean expectResult) throws IOException {
        double result;
        try (HSSFWorkbook wb = new HSSFWorkbook(
                new FileInputStream(PATH))) {
            Sheet sheet = wb.getSheetAt(0);
            for (Row row : sheet) {
                String input = row.getCell(0).getStringCellValue();
                if ("".equals(input)) break;
                double output = row.getCell(1).getNumericCellValue();
                result = PolishNotation.evaluateNPN((PolishNotation.createNPN(input)).toString());
                assertThat(Objects.equals(result, output), is(expectResult));
            }
        }
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void testInvalidAccuracy() {
        PolishNotation.evaluateNPN(PolishNotation.createNPN(null).toString());
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testInvalidInputLine() {
        PolishNotation.createNPN(INVALID_POLISH_NOTATION);
    }

    @Test(expected = java.lang.UnsupportedOperationException.class)
    public void testInvalidOperation() {
        PolishNotation.evaluateNPN(PolishNotation.createNPN(DIVISION_BY_ZERO).toString());
    }
}
