package cn.jerryshell.spider_qula;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import cn.jerryshell._qu_la_SpiderForJava.Chapter;
import cn.jerryshell._qu_la_SpiderForJava.ChapterSummary;
import cn.jerryshell._qu_la_SpiderForJava.Spider;

public class ChapterActivity extends AppCompatActivity {
    private TextView mTitleTextView;
    private TextView mContentTextView;

    private static final int WHAT_SET_CHAPTER = 1;
    private static final int WHAT_GET_CHAPTER_FAIL = 2;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_SET_CHAPTER:
                    Chapter chapter = (Chapter) msg.obj;
                    mTitleTextView.setText(chapter.getTitle());
                    mContentTextView.setText(chapter.getContent());
                    break;
                case WHAT_GET_CHAPTER_FAIL:
                    Toast.makeText(ChapterActivity.this, "获取小说内容失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public static Intent getStartIntent(Context context, int chapterSummaryPosition) {
        Intent intent = new Intent(context, ChapterActivity.class);
        intent.putExtra("chapterSummaryPosition", chapterSummaryPosition);
        return intent;
    }

    private int getChapterSummaryPositionFormIntent() {
        return getIntent().getIntExtra("chapterSummaryPosition", -1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        mTitleTextView = (TextView) findViewById(R.id.tv_title);
        mContentTextView = (TextView) findViewById(R.id.tv_content);

        int chapterSummaryPosition = getChapterSummaryPositionFormIntent();
        ChapterSummary chapterSummary = ChapterSummaryLab.getIdentity().getChapterSummaryList().get(chapterSummaryPosition);

        new Thread(new ChapterSpiderThread(mHandler, chapterSummary)).start();
    }

    private class ChapterSpiderThread implements Runnable {
        private Handler mHandler;
        private ChapterSummary mChapterSummary;

        ChapterSpiderThread(Handler handler, ChapterSummary chapterSummary) {
            mHandler = handler;
            mChapterSummary = chapterSummary;
        }

        @Override
        public void run() {
            try {
                Chapter chapter = Spider.getChapter(mChapterSummary);
                Message message = Message.obtain(mHandler, WHAT_SET_CHAPTER);
                message.obj = chapter;
                message.sendToTarget();
            } catch (IOException e) {
                e.printStackTrace();
                Message message = Message.obtain(mHandler, WHAT_GET_CHAPTER_FAIL);
                message.sendToTarget();
            }
        }
    }
}
