package cn.jerryshell.spider_qula;

import java.util.ArrayList;
import java.util.List;

import cn.jerryshell._qu_la_SpiderForJava.ChapterSummary;

class ChapterSummaryLab {
    private static final ChapterSummaryLab identity = new ChapterSummaryLab();
    private List<ChapterSummary> mChapterSummaryList;

    private ChapterSummaryLab() {
        mChapterSummaryList = new ArrayList<>();
    }

    public static ChapterSummaryLab getIdentity() {
        return identity;
    }

    public List<ChapterSummary> getChapterSummaryList() {
        return mChapterSummaryList;
    }

    public void setChapterSummaryList(List<ChapterSummary> ChapterSummaryList) {
        mChapterSummaryList = ChapterSummaryList;
    }
}
