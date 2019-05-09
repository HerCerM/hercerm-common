import hercerm.common.file.CSVFile;
import hercerm.common.file.XLSXConverter;

import java.io.File;
import java.io.IOException;

public class XLSConverterTest {
    public static void main(String[] args) throws IOException {
        convertToCSVTest();
    }

    private static void convertToCSVTest() throws IOException {
        String inputPath = "C:\\Users\\hjcer\\Desktop\\input.xlsx";

        XLSXConverter converter = new XLSXConverter();
        CSVFile file = converter.convertToCSV(new File(inputPath), 0);

        System.out.println("ORDER: " + file.getOrder());

        for(String s : file.readLine(1))
            System.out.print(s + " ");
    }
}
