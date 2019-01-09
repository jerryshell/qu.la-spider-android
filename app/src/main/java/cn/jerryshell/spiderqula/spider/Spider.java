package cn.jerryshell.spiderqula.spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Spider {
    private static String baseUrl = "https://www.qu.la";
    private static String paihangbangUrl = baseUrl + "/paihangbang/";

    private static Map<String, String> headersMap = new HashMap<>();

    static {
        headersMap.put("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3198.0 Safari/537.36");
        headersMap.put("Host", "www.qu.la");
    }

    /**
     * 获取小说概述列表
     *
     * @return summaryList
     * @throws IOException 网络错误
     */
    public static List<NovelSummary> getNovelSummaryList() throws IOException {
        Document document = Jsoup.connect(paihangbangUrl)
                .headers(headersMap)
                .get();
        Elements a_elements = document.select(".topbooks li a");
        List<NovelSummary> novelSummaryList = new ArrayList<>();
        for (Element a_element : a_elements) {
            String title = a_element.attr("title");
            String href = baseUrl + a_element.attr("href");
            novelSummaryList.add(new NovelSummary(title, href));
        }
        return novelSummaryList;
    }

    /**
     * 获取章节概述列表
     *
     * @param novelSummary 被解析的小说概述对象
     * @return chapterSummaryList
     * @throws IOException 网络错误
     */
    public static List<ChapterSummary> getChapterSummaryList(NovelSummary novelSummary) throws IOException {
        Document document = Jsoup.connect(novelSummary.getHref())
                .headers(headersMap)
                .get();
        List<ChapterSummary> chapterSummaryList = new ArrayList<>();
        Elements div_dd_a = document.select("div dd a");
        for (Element element : div_dd_a) {
//            System.out.println(element);
            String title = element.text();
            String href = baseUrl + element.attr("href");
            ChapterSummary chapterSummary = new ChapterSummary(title, href);
            chapterSummaryList.add(chapterSummary);
        }
        return chapterSummaryList;
    }

    /**
     * 获取章节对象
     *
     * @param chapterSummary 被解析的章节概述对象
     * @return chapter
     * @throws IOException 网络错误
     */
    public static Chapter getChapter(ChapterSummary chapterSummary) throws IOException {
        Document document = Jsoup.connect(chapterSummary.getHref())
                .headers(headersMap)
                .get();
        String title = chapterSummary.getTitle();
        String content = document.select("#content").get(0).text();
        // 格式美化
        content = content.replaceAll(" 　　 　　    ", "\n\n");
        content = content.replaceAll(" 　　 　　", "\n\n");
        content = content.replaceAll("    ", "");
        return new Chapter(title, content);
    }

    /**
     * 下载指定章节的文章
     *
     * @param chapter 被下载的文章
     * @throws IOException          网络错误
     * @throws InterruptedException 线程错误
     */
    public static void downloadChapter(Chapter chapter) throws IOException, InterruptedException {
        System.out.println("正在下载 " + chapter.getTitle());
        File file = new File(chapter.getTitle() + ".txt");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(chapter.getContent());
        fileWriter.close();
        Thread.sleep(1000);
    }
}
