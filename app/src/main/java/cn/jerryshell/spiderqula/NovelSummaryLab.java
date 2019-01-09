package cn.jerryshell.spiderqula;

import java.util.ArrayList;
import java.util.List;

import cn.jerryshell.spiderqula.spider.NovelSummary;

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
