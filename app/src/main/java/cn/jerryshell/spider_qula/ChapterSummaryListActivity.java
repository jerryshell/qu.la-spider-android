package cn.jerryshell.spider_qula;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import cn.jerryshell._qu_la_SpiderForJava.ChapterSummary;
import cn.jerryshell._qu_la_SpiderForJava.NovelSummary;
import cn.jerryshell._qu_la_SpiderForJava.Spider;

public class ChapterSummaryListActivity extends AppCompatActivity {
    private static final int WHAT_SET_Chapter_SUMMARY_LIST = 1;
    private static final int WHAT_GET_Chapter_SUMMARY_FAIL = 2;
    private ListView mChapterSummaryListView;
    private NovelSummaryLab mNovelSummaryLab = NovelSummaryLab.getIdentity();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_SET_Chapter_SUMMARY_LIST:
                    List<ChapterSummary> ChapterSummaryList = (List<ChapterSummary>) msg.obj;
                    ChapterSummaryLab.getIdentity().getChapterSummaryList().addAll(ChapterSummaryList);
                    mChapterSummaryListView.setAdapter(new ChapterSummaryListViewAdapter(getApplicationContext(), R.layout.item_novel_summary_list, ChapterSummaryList));
                    break;
                case WHAT_GET_Chapter_SUMMARY_FAIL:
                    Toast.makeText(ChapterSummaryListActivity.this, "获取小说列表失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public static Intent getStartIntent(Context context, int novelPosition) {
        Intent intent = new Intent(context, ChapterSummaryListActivity.class);
        intent.putExtra("novelPosition", novelPosition);
        return intent;
    }

    private int getNovelPositionFormIntent() {
        Intent intent = getIntent();
        return intent.getIntExtra("novelPosition", 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novel_summary_list);

        int novelPosition = getNovelPositionFormIntent();
        NovelSummary novelSummary = mNovelSummaryLab.getNovelSummaryList().get(novelPosition);

        mChapterSummaryListView = (ListView) findViewById(R.id.lv_novel_summary);
        mChapterSummaryListView.setEmptyView(findViewById(R.id.empty));
        mChapterSummaryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent startIntent = ChapterActivity.getStartIntent(getApplicationContext(), position);
                startActivity(startIntent);
            }
        });

        new Thread(new ChapterSummaryListSpiderThread(mHandler, novelSummary)).start();
    }

    private class ChapterSummaryListViewAdapter extends ArrayAdapter<ChapterSummary> {
        private int mResource;

        ChapterSummaryListViewAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ChapterSummary> objects) {
            super(context, resource, objects);
            mResource = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = View.inflate(parent.getContext(), mResource, null);
            } else {
                view = convertView;
            }
            ChapterSummary ChapterSummary = getItem(position);
            TextView titleTextView = (TextView) view.findViewById(R.id.tv_novel_summary_title);
            assert ChapterSummary != null;
            titleTextView.setText(ChapterSummary.getTitle());
            return view;
        }
    }

    private class ChapterSummaryListSpiderThread implements Runnable {
        private Handler mHandler;
        private NovelSummary mNovelSummary;

        ChapterSummaryListSpiderThread(Handler handler, NovelSummary novelSummary) {
            mHandler = handler;
            mNovelSummary = novelSummary;
        }

        @Override
        public void run() {
            try {
                List<ChapterSummary> ChapterSummaryList = Spider.getChapterSummaryList(mNovelSummary);
                Message message = Message.obtain(mHandler, WHAT_SET_Chapter_SUMMARY_LIST);
                message.obj = ChapterSummaryList;
                message.sendToTarget();
            } catch (IOException e) {
                e.printStackTrace();
                Message message = Message.obtain(mHandler, WHAT_GET_Chapter_SUMMARY_FAIL);
                message.sendToTarget();
            }
        }
    }
}
