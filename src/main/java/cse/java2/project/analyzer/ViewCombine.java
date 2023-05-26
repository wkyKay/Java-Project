package cse.java2.project.analyzer;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ViewCombine {

    public static void main(String[] args) throws IOException, CsvException {
        String csvFile = "D:\\Thrid\\Java2\\java-project\\Java-Project\\src\\main\\resources\\data\\data500.csv";
        String tag_frequency = "D:\\Thrid\\Java2\\java-project\\Java-Project\\sorted_view_tags.csv";

        CSVReader reader = new CSVReader(new FileReader(csvFile));
        List<String[]> lines = reader.readAll();
        // Skip the header line
        lines = lines.subList(1, lines.size());


        CSVReader tag_frequency_reader = new CSVReader(new FileReader(tag_frequency));
        List<String[]> tag_frequency_lines = tag_frequency_reader.readAll();
        // Skip the header line
        tag_frequency_lines = tag_frequency_lines.subList(1, tag_frequency_lines.size());
        //Todo
        // Process the data
        List<String> tags = new ArrayList<>();
        for (String[] tag_frequency_line : tag_frequency_lines) {
            String tag = tag_frequency_line[0];
            String frequency = tag_frequency_line[1];
            if (Integer.parseInt(frequency) >= 3000) {
                tags.add(tag);
            } else {
                break;
            }
        }
        //2个tag的情况
        Map<String[], Integer> tag_combine2 = new HashMap<>();
        for (int i = 0; i < tags.size() - 1; i++) {
            for (int j = i + 1; j < tags.size(); j++) {
                String[] a = new String[]{tags.get(i), tags.get(j)};
                tag_combine2.put(a, 0);
            }
        }
        //2个tag的情况
        Map<String[], Integer> tag_combine3 = new HashMap<>();
        for (int i = 0; i < tags.size() - 2; i++) {
            for (int j = i + 1; j < tags.size()-1; j++) {
                for(int k = j + 1; k < tags.size(); k++ ){
                    String[] a = new String[]{tags.get(i), tags.get(j), tags.get(k)};
                    tag_combine2.put(a, 0);
                }
            }
        }

        for (String[] line : lines) {
            String tag = line[4];
            String[] tags1 = tag.split(" ");
            int upvote = Integer.parseInt(line[1]);

            Set<String> allTags = new HashSet<>(Arrays.asList(tags1));

            for (Map.Entry<String[], Integer> entry : tag_combine2.entrySet()) {
                String[] key = entry.getKey();
                Integer value = entry.getValue();
                Set<String> tagKey = new HashSet<>(Arrays.asList(key));

                if (allTags.containsAll(tagKey)) {
                    tag_combine2.put(key, value + upvote);
                }
            }


            for (Map.Entry<String[], Integer> entry : tag_combine3.entrySet()) {
                String[] key = entry.getKey();
                Integer value = entry.getValue();
                Set<String> tagKey = new HashSet<>(Arrays.asList(key));

                if (allTags.containsAll(tagKey)) {
                    tag_combine3.put(key, value + upvote);
                }
            }
        }

        List<Map.Entry<String[], Integer>> sortedEntries = new ArrayList<>(tag_combine2.entrySet());
        sortedEntries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        String outputFile = "sorted_view_combine.csv";

        CSVWriter writer = new CSVWriter(new FileWriter(outputFile));
        // 写入CSV文件的表头
        String[] header = {"Tag", "View/k"};
        writer.writeNext(header);

        // 写入排序后的标签及其出现次数
        for (Map.Entry<String[], Integer> entry : sortedEntries) {
            if(entry.getValue() > 0){
                String[] row = {Arrays.toString(entry.getKey()), entry.getValue().toString()};
                writer.writeNext(row);
                writer.flush();
            }
        }

        List<Map.Entry<String[], Integer>> sortedEntries1 = new ArrayList<>(tag_combine3.entrySet());
        sortedEntries1.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        for (Map.Entry<String[], Integer> entry : sortedEntries1) {
            if(entry.getValue() > 0){
                String[] row = {Arrays.toString(entry.getKey()), entry.getValue().toString()};
                writer.writeNext(row);
                writer.flush();
            }

        }

    }
}
