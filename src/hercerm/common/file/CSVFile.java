package hercerm.common.file;

import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.file.StandardOpenOption;

public class CSVFile implements Iterable<String[]> {

    private final String filePath;
    private int order; // Number of columns

    public CSVFile(String filePath) {
        this.filePath = filePath;
    }

    public String[][] read() {
        String[][] data = null;
        int fields = 0;

        try (Scanner inputFirstPass = new Scanner(Paths.get(filePath));
             Scanner inputSecondPass = new Scanner(Paths.get(filePath));) {
            while (inputFirstPass.hasNext()) {
                ++fields;
                inputFirstPass.nextLine();
            }

            data = new String[fields][];

            for (int i = 0; i < fields; i++)
                data[i] = inputSecondPass.nextLine().split(",");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return data;
    }

    public String[] readLine(int lineNumber) {

        String[] line = null;

        try(Scanner input = new Scanner(Paths.get(filePath))) {
            int currentLine = 0;

            while(input.hasNextLine()) {

                // Current line
                String rawLine = input.nextLine();
                // Increment current line counter
                currentLine++;

                if(currentLine == lineNumber)
                    line = rawLine.split(",");

            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        return line;
    }

    public void append(String[] output) {

        // Check for CSV consistency
        if(order == 0)
            order = output.length;
        else
            if(order != output.length)
                throw new InputMismatchException("Jagged CSV not allowed.");

        try {
            String pOutput = "";

            for (String field : output)
                pOutput += String.format("%s,", field);
            pOutput = pOutput.substring(0, pOutput.length() - 1);

            Files.write(Paths.get(filePath), (pOutput + System.lineSeparator()).getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public boolean delete() {
        boolean successStatus = true;

        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            successStatus = false;
        }

        return successStatus;
    }

    public int lines() {
        int count = 0;

        for(String[] line : this)
            count++;

        return count;
    }

    @Override
    public Iterator<String[]> iterator() {
        return new CSVFileIterator();
    }

    public int getOrder() {
        return order;
    }

    private class CSVFileIterator implements Iterator<String[]> {

        private Scanner input;

        private CSVFileIterator() {
            try {
                input = new Scanner(Paths.get(filePath));
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public boolean hasNext() {
            if(!input.hasNextLine()) {
                input.close();
                return false;
            }

            return input.hasNextLine();
        }

        @Override
        public String[] next() {
            return input.nextLine().split(",");
        }

    }
}
