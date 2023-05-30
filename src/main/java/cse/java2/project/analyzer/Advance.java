package cse.java2.project.analyzer;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Advance {
    public static void main(String[] args) throws IOException, CsvException {
        String csvFile = "D:\\Thrid\\Java2\\java-project\\Java-Project\\src\\main\\resources\\answer\\keyword_counts.csv";

        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {

            List<String[]> lines = reader.readAll();

            String [] line0 = lines.get(0);
            lines = lines.subList(1, lines.size());

            Map<String, Integer> frequency = new HashMap<>();
            for(String tag: line0){
                frequency.put(tag, 0);
            }

            for(String[] line: lines){
                for(int i = 0; i < line.length; i++){
                    frequency.put(line0[i], frequency.get(line0[i]) + Integer.parseInt(line[i]));
                }
            }

            List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(frequency.entrySet());
            sortedEntries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
            String outputFile = "tag_freq.csv";

            CSVWriter writer = new CSVWriter(new FileWriter(outputFile));
            // 写入CSV文件的表头
            String[] header = {"Tag", "Frequency"};
            writer.writeNext(header);

            // 写入排序后的标签及其出现次数
            for (Map.Entry<String, Integer> entry : sortedEntries) {
                String[] row = {entry.getKey(), entry.getValue().toString()};
                writer.writeNext(row);
                writer.flush();
            }
        }


    }
}
