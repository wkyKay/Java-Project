package cse.java2.project.analyzer;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Tags {

    public static void main(String[] args) throws IOException {
        String csvFile = "D:\\Thrid\\Java2\\java-project\\Java-Project\\src\\main\\resources\\data\\data500.csv";

        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {

            List<String[]> lines = reader.readAll();
            // Skip the header line
            lines = lines.subList(1, lines.size());

            //Todo
            // Process the data for Tag T1
            Map<String, Integer> tagFrequency = new HashMap<>();

            for (String[] line : lines) {
                String tags = line[4]; // 获取标签列的数据
                String[] tagArray = tags.split(" ");

                for (String tag : tagArray) {
                    if (!tag.equals("java")) { // 排除"java"标签本身
                        tagFrequency.put(tag, tagFrequency.getOrDefault(tag, 0) + 1);
                    }
                }
            }
            List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(tagFrequency.entrySet());
            sortedEntries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
            String outputFile = "sorted_tag_frequency.csv";

            CSVWriter writer = new CSVWriter(new FileWriter(outputFile));
            // 写入CSV文件的表头
            String[] header = {"Tag", "Frequency"};
            writer.writeNext(header);

            // 写入排序后的标签及其出现次数
            for (Map.Entry<String, Integer> entry : sortedEntries) {
                String[] row = {entry.getKey(), entry.getValue().toString()};
                writer.writeNext(row);
            }

            //Todo
            // Tag T2
            Map<String, Integer> upvoteCount = new HashMap<>();

            for (String[] line : lines) {
                String tags = line[4]; // 获取标签列的数据
                int upvotes = Integer.parseInt(line[1]); // 获取点赞数

                // 假设标签之间用空格分隔
                String[] tagArray = tags.split(" ");

                // 计算每个标签或标签组合的点赞总数
                for (String tag : tagArray) {
                    if (!tag.equals("java")) {
                        upvoteCount.put(tag, upvoteCount.getOrDefault(tag, 0) + upvotes);
                    }
                }
            }

            List<Map.Entry<String, Integer>> sortedEntries1 = new ArrayList<>(upvoteCount.entrySet());
            sortedEntries1.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

            String outputFile1 = "sorted_upvoted_tags.csv";

            try (CSVWriter writer1 = new CSVWriter(new FileWriter(outputFile1))) {
                // 写入CSV文件的表头
                String[] header1 = {"Tag", "Upvotes"};
                writer1.writeNext(header1);

                // 写入排序后的标签及其点赞数
                for (Map.Entry<String, Integer> entry : sortedEntries1) {
                    String[] row = {entry.getKey(), entry.getValue().toString()};
                    writer1.writeNext(row);
                }

                //Todo
                // Tag T3

                Map<String, Integer> viewCount = new HashMap<>();

                for (String[] line : lines) {
                    String tags = line[4]; // 获取标签列的数据
                    String views = line[3]; // 获取浏览量
                    int view;
                    String last = views.substring(views.length() - 1);
                    double l1 = Double.parseDouble((views.substring(0, views.length() - 1)));
                    if (last.equals("m")) {
                        view = (int) (l1 * 1000);
                    } else if (last.equals("k")) {
                        view = (int) (l1);
                    } else {
                        view = (int) ((int) Long.parseLong(views) * 0.001);
                    }

                    // 假设标签之间用空格分隔
                    String[] tagArray = tags.split(" ");

                    // 计算每个标签或标签组合的浏览量总数
                    for (String tag : tagArray) {
                        if (!tag.equals("java")) {
                            viewCount.put(tag, (viewCount.getOrDefault(tag, 0) + view));
                        }
                    }
                }


                List<Map.Entry<String, Integer>> sortedEntries2 = new ArrayList<>(viewCount.entrySet());
                sortedEntries2.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

                String outputFile2 = "sorted_view_tags.csv";

                try (CSVWriter writer2 = new CSVWriter(new FileWriter(outputFile2))) {
                    // 写入CSV文件的表头
                    String[] header2 = {"Tag", "view/k"};
                    writer2.writeNext(header2);

                    // 写入排序后的标签及其点赞数
                    for (Map.Entry<String, Integer> entry : sortedEntries2) {
                        String[] row = {entry.getKey(), entry.getValue().toString()};
                        writer2.writeNext(row);
                    }


                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
    }
}
