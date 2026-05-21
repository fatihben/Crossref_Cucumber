package utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {

    public static List<String> getDoisFromCsv(String path) {

        List<String> doiList = new ArrayList<>();

        try {

            BufferedReader br =
                    new BufferedReader(new FileReader(path));

            String line;

            br.readLine();

            while ((line = br.readLine()) != null) {

                doiList.add(line.trim());
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return doiList;
    }
}