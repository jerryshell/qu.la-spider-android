package cn.jerryshell.spider_qula;

import java.util.ArrayList;
import java.util.List;

import cn.jerryshell._qu_la_SpiderForJava.NovelSummary;

class NovelSummaryLab {
    private static final NovelSummaryLab identity = new NovelSummaryLab();
    private List<NovelSummary> mNovelSummaryList;

    private NovelSummaryLab() {
        mNovelSummaryList = new ArrayList<>();
    }

    public static NovelSummaryLab getIdentity() {
        return identity;
    }

    public List<NovelSummary> getNovelSummaryList() {
        return mNovelSummaryList;
    }

    public void setNovelSummaryList(List<NovelSummary> novelSummaryList) {
        mNovelSummaryList = novelSummaryList;
    }
}
