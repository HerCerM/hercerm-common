package hercerm.common.file;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.util.List;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.io.FileInputStream;
import java.util.InputMismatchException;

public class XLSXConverter {

    public CSVFile convertToCSV(File xLSXFile, int sheet)
            throws
            IOException, // File not found or any other IO issue
            InputMismatchException // XLSX has varying column counts per row
    {

        CSVFile cSVFile = null;

        try(XSSFWorkbook workBook = new XSSFWorkbook(new FileInputStream(xLSXFile));) {

            Path cSVFilePath = Paths.get(xLSXFile.getName().replaceAll("[.]xlsx", ".csv"));

            // Delete files if already exists
            Files.deleteIfExists(cSVFilePath);
            // Access object for CSV conversion
            cSVFile = new CSVFile(cSVFilePath.toString());

            // Get sheet
            XSSFSheet selSheet = workBook.getSheetAt(sheet);

            // Iterate through all the rows in the selected sheet
            for(Row row : selSheet) {

                // Store fields (cell values on current row)
                List<String> fields = new LinkedList<>();

                for(Cell cell : row) {

                    switch (cell.getCellType()) {
                        case STRING:
                            fields.add(cell.getStringCellValue());
                            break;
                        case NUMERIC:
                            fields.add(Double.toString(cell.getNumericCellValue()));
                            break;
                        case BOOLEAN:
                            fields.add(Boolean.toString(cell.getBooleanCellValue()));
                            break;
                        default:
                    }
                }

                // Append built line to CSV file
                cSVFile.append(fields.toArray(new String[0]));
            }
        }
        catch(InputMismatchException e) {
            if(cSVFile != null)
                cSVFile.delete();

            throw e; // Rethrow exception
        }
        catch(IOException e) {
            if(cSVFile != null)
                cSVFile.delete();

            throw e; // Rethrow exception
        }

        return cSVFile;
    }
}
