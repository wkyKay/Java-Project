package cse.java2.project.analyzer;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Users {

    public static void main(String[] args) throws IOException {
        String csvFile = "D:\\Thrid\\Java2\\java-project\\Java-Project\\src\\main\\resources\\data\\data500.csv";

        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {

            List<String[]> lines = reader.readAll();
            // Skip the header line
            lines = lines.subList(1, lines.size());

            //Todo
            // Process the data
            Map<String, Integer> userCount = new HashMap<>();

            for (String[] line : lines) {
                String[] userArray = line[9].replace("[", "").replace("]", "")
                        .replace("'", "").split(", ");
                // 计算每个标签或标签组合的点赞总数
                for (String user : userArray) {
                    if (!user.equals(""))
                        userCount.put(user, userCount.getOrDefault(user, 0) + 1);
                }
            }

            List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(userCount.entrySet());
            sortedEntries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

            String outputFile = "sorted_users.csv";

            try (CSVWriter writer = new CSVWriter(new FileWriter(outputFile))) {
                // 写入CSV文件的表头
                String[] header = {"User", "count"};
                writer.writeNext(header);

                // 写入排序后的标签及其点赞数
                for (Map.Entry<String, Integer> entry : sortedEntries) {
                    String[] row = {entry.getKey(), entry.getValue().toString()};
                    writer.writeNext(row);
                }


            }
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
    }
}
