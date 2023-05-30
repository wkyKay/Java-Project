package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StackOverflowScraper {
    private static final String[] dataHeader = {"title", "votes", "num_answer", "views", "tags", "accepted", "q_time",
            "ac_ans_time", "ua", "users", "comment_users"};

    public static void main(String[] args) {
        try {
            FileWriter fileWriter = new FileWriter("data.csv");
            CSVUtils.writeLine(fileWriter, dataHeader);

            for (int x = 1; x < 11; x++) {
                String url = String.format("https://stackoverflow.com/questions/tagged/java?tab=active&page=%d&pagesize=50", x);
                String content = Jsoup.connect(url).get().html();
                Document document = Jsoup.parse(content);

                List<String> users = new ArrayList<>();
                List<String> commentUsers = new ArrayList<>();

                Elements allThreads = document.select("div.s-post-summary.js-post-summary");
                int i = 1;
                for (Element thread : allThreads) {
                    String title = thread.selectFirst("h3.s-post-summary--content-title > a").text();
                    if (title.contains(",")) {
                        title = title.replace(',', '，');
                    }
                    String votes = thread.select("span.s-post-summary--stats-item-number").get(0).text();
                    String numAnswer = thread.select("span.s-post-summary--stats-item-number").get(1).text();
                    String views = thread.select("span.s-post-summary--stats-item-number").get(2).text();

                    Elements tagElements = thread.select("ul.ml0.list-ls-none.js-post-tag-list-wrapper.d-inline > li");
                    StringBuilder tagsBuilder = new StringBuilder();
                    for (Element tagElement : tagElements) {
                        String tag = tagElement.selectFirst("a").text();
                        tagsBuilder.append(tag).append(" ");
                    }
                    String tags = tagsBuilder.toString().trim();

                    boolean accepted = thread.select("svg").size() > 0;

                    String link = thread.selectFirst("h3.s-post-summary--content-title > a").attr("href");
                    String threadHtml = Jsoup.connect("https://stackoverflow.com" + link).get().html();
                    Document threadDetails = Jsoup.parse(threadHtml);

                    String qTime = threadDetails.selectFirst("time[itemprop=dateCreated]").attr("datetime");
                    String acAnsTime = "No ac";
                    String ua = "False";

                    if (accepted) {
                        Element acAnswer = threadDetails.selectFirst("div.answer.js-answer.accepted-answer.js-accepted-answer");
                        String acAnsUpvote = acAnswer.selectFirst("div[itemprop=upvoteCount]").attr("data-value");
                        acAnsTime = acAnswer.selectFirst("time[itemprop=dateCreated]").attr("datetime");

                        Elements answers = threadDetails.select("div.answer.js-answer");
                        for (Element ans : answers) {
                            String ansUpvote = ans.selectFirst("div[itemprop=upvoteCount]").attr("data-value");
                            if (Integer.parseInt(ansUpvote) > Integer.parseInt(acAnsUpvote)) {
                                ua = "True";
                            }
                        }
                    }

                    Elements userElements = threadDetails.select("div.user-details");
                    for (Element userElement : userElements) {
                        Element user = userElement.selectFirst("span[itemprop=name]");
                        if (user != null) {
                            users.add(user.text());
                        }
                    }

                    Elements commentElements = threadDetails.select("ul.comments-list.js-comments-list");
                    for (Element commentElement : commentElements) {
                        Elements commentUserDivs = commentElement.select("div.d-inline-flex.ai-center");
                        for (Element commentUserDiv : commentUserDivs) {
                            String commentUser;
                            if (commentUserDiv.selectFirst("a") != null) {
                                commentUser = commentUserDiv.selectFirst("a").text();
                                commentUsers.add(commentUser);
                            }
                        }
                    }

                    String[] data = {title, votes, numAnswer, views, tags, String.valueOf(accepted), qTime, acAnsTime,
                            ua, String.join("|", users), String.join("|", commentUsers)};

//                    System.out.println(i);
//                    System.out.println(title);
//                    System.out.println(votes);
//                    System.out.println(numAnswer);
//                    System.out.println(views);
//                    System.out.println(tags);
//                    System.out.println(accepted);
//                    System.out.println(qTime);
//                    System.out.println(acAnsTime);
//                    System.out.println(ua);
//                    System.out.println(users);
//                    System.out.println(commentUsers);
//                    System.out.println();

                    System.out.println(data[0]);
                    CSVUtils.writeLine(fileWriter, data);

                    users.clear();
                    commentUsers.clear();

                    i++;
                }

                fileWriter.flush();
            }

            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class CSVUtils {
    private static final char DEFAULT_SEPARATOR = ',';

    public static void writeLine(FileWriter writer, String[] data) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            if (i > 0) {
                sb.append(DEFAULT_SEPARATOR);
            }
            sb.append(escapeSpecialCharacters(data[i]));
        }
        sb.append("\n");
        writer.append(sb.toString());
    }

    private static String escapeSpecialCharacters(String data) {
//        if (data.contains("\"")) {
//            data = data.replace("\"", "\"\"");
//        }
        return data;
    }
}
