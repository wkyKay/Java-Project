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
        String csvFile = "D:\\Thrid\\Java2\\java-project\\Java-Project\\src\\main\\resources\\data\\data_active_new.csv";

        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {

            List<String[]> lines = reader.readAll();
            // Skip the header line
            lines = lines.subList(1, lines.size());

            //Todo
            // Process the data
            Map<String, Integer> userPostCount = new HashMap<>();
            Map<String, Integer> userReplyCount = new HashMap<>();
            Map<String, Integer> userCommentCount = new HashMap<>();
            Map<String, Integer> userAllCount = new HashMap<>();

            for (String[] line : lines) {
                if (line[9] != null) {
                    String[] userArray = line[9].replace("|",",").split(",");

                    for (int i = 0; i < userArray.length; i++) {
                        if(userArray[i] == null || Objects.equals(userArray[i], ""))
                            continue;
                        if (i == 0) {
                            userPostCount.put(userArray[0], userPostCount.getOrDefault(userArray[0], 0) + 1);
                        } else {
                            userReplyCount.put(userArray[i], userReplyCount.getOrDefault(userArray[i], 0) + 1);
                        }
                        userAllCount.put(userArray[i], userAllCount.getOrDefault(userArray[i], 0) + 1);
                    }

                }

                if (line[10] != null) {
                    String[] userArray = line[10].replace("|",",").split(",");
                    for (int i = 1; i < userArray.length; i++) {
                        if(userArray[i] == null || Objects.equals(userArray[i], ""))
                            continue;
                        userCommentCount.put(userArray[i], userCommentCount.getOrDefault(userArray[i], 0) + 1);
                        userAllCount.put(userArray[i], userAllCount.getOrDefault(userArray[i], 0) + 1);
                    }
                }
            }

            List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(userPostCount.entrySet());
            sortedEntries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

            String outputFile = "user_post.csv";

            try (CSVWriter writer = new CSVWriter(new FileWriter(outputFile))) {
                // 写入CSV文件的表头
                String[] header = {"User", "count"};
                writer.writeNext(header);

                // 写入排序后的标签及其点赞数
                for (Map.Entry<String, Integer> entry : sortedEntries) {
                    String[] row = {entry.getKey(), String.valueOf(entry.getValue())};
                    writer.writeNext(row);
                }


            }

            List<Map.Entry<String, Integer>> sortedEntries1 = new ArrayList<>(userReplyCount.entrySet());
            sortedEntries1.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

            String outputFile1 = "user_reply.csv";

            try (CSVWriter writer1 = new CSVWriter(new FileWriter(outputFile1))) {
                // 写入CSV文件的表头
                String[] header1 = {"User", "count"};
                writer1.writeNext(header1);

                // 写入排序后的标签及其点赞数
                for (Map.Entry<String, Integer> entry : sortedEntries1) {
                    String[] row = {entry.getKey(), String.valueOf(entry.getValue())};
                    writer1.writeNext(row);
                }


            }

            List<Map.Entry<String, Integer>> sortedEntries2 = new ArrayList<>(userCommentCount.entrySet());
            sortedEntries2.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

            String outputFile2 = "user_comment.csv";

            try (CSVWriter writer2 = new CSVWriter(new FileWriter(outputFile2))) {
                // 写入CSV文件的表头
                String[] header2 = {"User", "count"};
                writer2.writeNext(header2);

                // 写入排序后的标签及其点赞数
                for (Map.Entry<String, Integer> entry : sortedEntries2) {
                    String[] row = {entry.getKey(), String.valueOf(entry.getValue())};
                    writer2.writeNext(row);
                }


            }

            List<Map.Entry<String, Integer>> sortedEntries3 = new ArrayList<>(userAllCount.entrySet());
            sortedEntries3.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

            String outputFile3 = "user_all.csv";

            try (CSVWriter writer3 = new CSVWriter(new FileWriter(outputFile3))) {
                // 写入CSV文件的表头
                String[] header3 = {"User", "count"};
                writer3.writeNext(header3);

                // 写入排序后的标签及其点赞数
                for (Map.Entry<String, Integer> entry : sortedEntries3) {
                    String[] row = {entry.getKey(), String.valueOf(entry.getValue())};
                    writer3.writeNext(row);
                }


            }
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
    }
}
