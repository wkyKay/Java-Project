package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class advance1 {
    private static int countKeywordMatches(String keyword, Elements results) {
        int count = 0;
        Pattern pattern = Pattern.compile(keyword, Pattern.CASE_INSENSITIVE);

        for (Element result : results) {
            String textContent = result.text();
            Matcher matcher = pattern.matcher(textContent);
            while (matcher.find()) {
                count++;
            }
        }

        return count;
    }

    public static void main(String[] args) {
        String[] dataHeader = {"I/O", "Concurrency", "Database", "Network", "GUI", "Security", "Collection", "Class",
                "Method", "Arraylist", "String", "HashMap", "Scanner", "Lock"};
        Map<String, List<Integer>> keywordCounts = new HashMap<>();
        for (String keyword : dataHeader) {
            keywordCounts.put(keyword, new ArrayList<>());
        }

        try (FileWriter writer = new FileWriter("keyword_counts.csv")) {
            writer.write(String.join(",", dataHeader) + "\n"); // Write header

            int c = 1;
            for (int x = 1; x <= 1; x++) {
                String url = "https://stackoverflow.com/questions/tagged/java?tab=newest&page=" + x + "&pagesize=15";
                Document doc = Jsoup.connect(url).get();
                Elements allThreads = doc.select("div.s-post-summary.js-post-summary");

                for (Element thread : allThreads) {
                    System.out.println(c);
                    c++;

                    Element linkElement = thread.selectFirst("h3.s-post-summary--content-title a");
                    String link = linkElement.attr("href");
                    String threadUrl = "https://stackoverflow.com" + link;
                    Document threadDoc = Jsoup.connect(threadUrl).get();

                    for (String keyword : dataHeader) {
                        Elements threadDetails = threadDoc.select("*");
                        int keywordCount = countKeywordMatches(keyword, threadDetails);
                        keywordCounts.get(keyword).add(keywordCount);
                    }
                }

                // Write keyword counts to the CSV file
                for (int i = 0; i < allThreads.size(); i++) {
                    List<String> row = new ArrayList<>();
                    for (String keyword : dataHeader) {
                        List<Integer> counts = keywordCounts.get(keyword);
                        row.add(counts.get(i).toString());
                    }
                    writer.write(String.join(",", row) + "\n");
                }
            }

            System.out.println("Keyword counts are written to keyword_counts.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
