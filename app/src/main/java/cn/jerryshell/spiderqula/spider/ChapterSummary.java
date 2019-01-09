package cn.jerryshell.spiderqula.spider;

public class ChapterSummary {
    private String title;
    private String href;

    public ChapterSummary(String title, String href) {
        this.title = title;
        this.href = href;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @Override
    public String toString() {
        return "ChapterSummary{" +
                "title='" + title + '\'' +
                ", href='" + href + '\'' +
                '}';
    }
}
