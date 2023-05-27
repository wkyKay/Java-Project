package cse.java2.project.analyzer;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class SortTogether {
    public static void main(String[] args) throws FileNotFoundException {
        String csvFile = "D:\\Thrid\\Java2\\java-project\\Java-Project\\src\\main\\resources\\static\\js\\tag_view.csv";

        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {

            List<String[]> lines = reader.readAll();

            //Todo
            // Process the data for Tag T1
            Map<String, Integer> tag_upvote = new HashMap<>();

            for (String[] line : lines) {
                String tags = line[0]; // 获取标签列的数据
                int upvote = Integer.parseInt(line[1]);
                tag_upvote.put(tags, upvote);
            }
            List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(tag_upvote.entrySet());
            sortedEntries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
            String outputFile = "tag_view.csv";

            CSVWriter writer = new CSVWriter(new FileWriter(outputFile));
            // 写入CSV文件的表头
            String[] header = {"Tag", "Upvote"};
            writer.writeNext(header);

            // 写入排序后的标签及其出现次数
            for (Map.Entry<String, Integer> entry : sortedEntries) {
                String[] row = {entry.getKey(), "#"+entry.getValue().toString()};
                writer.writeNext(row);
                writer.flush();
            }

        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }
}