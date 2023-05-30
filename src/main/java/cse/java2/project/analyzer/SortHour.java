package cse.java2.project.analyzer;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class SortHour {
    public static void main(String[] args) throws IOException {
        String csvFile = "D:\\Thrid\\Java2\\java-project\\Java-Project\\src\\main\\resources\\answer\\hour_distribution.csv";

        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {

            List<String[]> lines = reader.readAll();
            lines = lines.subList(1, lines.size());
            Map<String, Integer> time_distribution = new HashMap<>();

            for (String[] line : lines) {
                int day = Integer.parseInt(line[0]);
                int hour = Integer.parseInt(line[1]);
                int Frequency = Integer.parseInt(line[2]);

                if(day == 0 && hour == 0){
                    time_distribution.put("< 1h", time_distribution.getOrDefault("< 1h", 0) + Frequency);
                }else if(day == 0){
                    time_distribution.put("1h <= t < 1d",time_distribution.getOrDefault("1h <= t < 1d", 0) + Frequency);
                }else if(day > 0 && day < 10){
                    time_distribution.put("1d <= t < 10d",time_distribution.getOrDefault("1d <= t < 10d", 0) + Frequency);
                }else if(day >= 10 && day < 100){
                    time_distribution.put("10d <= t < 100d",time_distribution.getOrDefault("10d <= t < 100d", 0) + Frequency);
                }else {
                    time_distribution.put("t >= 100d",time_distribution.getOrDefault("t >= 100d", 0) + Frequency);
                }

            }

            List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(time_distribution.entrySet());
            sortedEntries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
            String outputFile = "time_distribution.csv";

            CSVWriter writer = new CSVWriter(new FileWriter(outputFile));
            // 写入CSV文件的表头
            String[] header = {"Time", "Frequency"};
            writer.writeNext(header);

            // 写入排序后的标签及其出现次数
            for (Map.Entry<String, Integer> entry : sortedEntries) {
                String[] row = {entry.getKey(), entry.getValue().toString()};
                writer.writeNext(row);
                writer.flush();
            }
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
    }
}
