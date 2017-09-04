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

import cn.jerryshell._qu_la_SpiderForJava.NovelSummary;
import cn.jerryshell._qu_la_SpiderForJava.Spider;

public class NovelSummaryListActivity extends AppCompatActivity {
    private ListView mNovelSummaryListView;

    private static final int WHAT_SET_NOVEL_SUMMARY_LIST = 1;
    private static final int WHAT_GET_NOVEL_SUMMARY_FAIL = 2;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_SET_NOVEL_SUMMARY_LIST:
                    List<NovelSummary> novelSummaryList = (List<NovelSummary>) msg.obj;
                    NovelSummaryLab.getIdentity().getNovelSummaryList().addAll(novelSummaryList);
                    mNovelSummaryListView.setAdapter(new NovelSummaryListViewAdapter(getApplicationContext(), R.layout.item_novel_summary_list, novelSummaryList));
                    break;
                case WHAT_GET_NOVEL_SUMMARY_FAIL:
                    Toast.makeText(NovelSummaryListActivity.this, "获取小说列表失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novel_summary_list);

        mNovelSummaryListView = (ListView) findViewById(R.id.lv_novel_summary);
        mNovelSummaryListView.setEmptyView(findViewById(R.id.empty));
        mNovelSummaryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent startIntent = ChapterSummaryListActivity.getStartIntent(getApplicationContext(), position);
                startActivity(startIntent);
            }
        });

        new Thread(new NovelSummaryListSpiderThread(mHandler)).start();
    }

    private class NovelSummaryListViewAdapter extends ArrayAdapter<NovelSummary> {
        private int mResource;

        NovelSummaryListViewAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<NovelSummary> objects) {
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
            NovelSummary novelSummary = getItem(position);
            TextView titleTextView = (TextView) view.findViewById(R.id.tv_novel_summary_title);
            assert novelSummary != null;
            titleTextView.setText(novelSummary.getTitle());
            return view;
        }
    }

    private class NovelSummaryListSpiderThread implements Runnable {
        private Handler mHandler;

        NovelSummaryListSpiderThread(Handler handler) {
            mHandler = handler;
        }

        @Override
        public void run() {
            try {
                List<NovelSummary> novelSummaryList = Spider.getNovelSummaryList();
                Message message = Message.obtain(mHandler, WHAT_SET_NOVEL_SUMMARY_LIST);
                message.obj = novelSummaryList;
                message.sendToTarget();
            } catch (IOException e) {
                e.printStackTrace();
                Message message = Message.obtain(mHandler, WHAT_GET_NOVEL_SUMMARY_FAIL);
                message.sendToTarget();
            }
        }
    }
}
