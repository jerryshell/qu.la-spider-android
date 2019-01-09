package cn.jerryshell.spiderqula.spider;

public class NovelSummary {
    private String title;
    private String href;

    public NovelSummary(String title, String href) {
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
        return "NovelSummary{" +
                "title='" + title + '\'' +
                ", href='" + href + '\'' +
                '}';
    }
}
