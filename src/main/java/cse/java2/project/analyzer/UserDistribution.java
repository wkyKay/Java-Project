package cse.java2.project.analyzer;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class UserDistribution {
    public static void main(String[] args) throws FileNotFoundException {
        Map<Integer, Integer> count_frequency = new HashMap();
        Map<Integer, List<String>> count_user = new HashMap<>();

        String csvFile = "D:\\Thrid\\Java2\\java-project\\Java-Project\\user_comment.csv";

        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {

            List<String[]> user_lines = reader.readAll();
            // Skip the header line
            user_lines = user_lines.subList(1, user_lines.size());
            //Todo

            for (String[] user_line : user_lines) {
                String user = user_line[0];
                int count = Integer.parseInt(user_line[1]);

                count_frequency.merge(count, 1, Integer::sum);


                if (count_user.get(count) == null) {
                    List<String> a = new ArrayList<>();
                    a.add(user);
                    count_user.put(count, a);
                } else {
                    List<String> a = count_user.get(count);
                    a.add(user);
                    count_user.put(count, a);
                }
            }

            List<Map.Entry<Integer, Integer>> sortedEntries = new ArrayList<>(count_frequency.entrySet());
            List<Map.Entry<Integer, List<String>>> sortedEntries1 = new ArrayList<>(count_user.entrySet());
            sortedEntries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

            String outputFile = "distributed_user_comment.csv";

            CSVWriter writer = new CSVWriter(new FileWriter(outputFile));
            // 写入CSV文件的表头
            String[] header = {"Count", "Frequency", "User"};
            writer.writeNext(header);

            // 写入排序后的标签及其出现次数
            for (Map.Entry<Integer, Integer> entry : sortedEntries) {
                for (Map.Entry<Integer, List<String>> entry1 : sortedEntries1) {
                    if (entry.getKey() == entry1.getKey()) {
                        String[] row = {String.valueOf(entry.getKey()), entry.getValue().toString(), entry1.getValue().toString()};
                        writer.writeNext(row);
                        writer.flush();
                    }
                }
            }

        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }

}
