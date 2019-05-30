package com.example.kaifa.slidedeletedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> mData;
    // 继续有多少个条目的delete被展示出来的集合
    List<SlideDelete> slideDeleteArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.lv);




        mData=new ArrayList<>();
        for(int i=0;i<200;i++){
            mData.add("文本"+i);
        }
        listView.setAdapter(new MyAdapter());

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == SCROLL_STATE_FLING || scrollState == SCROLL_STATE_TOUCH_SCROLL){
                    closeOtherItem();
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if(mData!=null){
                return mData.size();
            }
            return 0;
        }
        @Override
        public Object getItem(int position) {
            if(mData!=null){
                return mData.get(position);
            }
            return null;
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView == null){
                viewHolder = new ViewHolder();
                convertView = View.inflate(MainActivity.this,R.layout.item,null);
                viewHolder.mSlideDelete = (SlideDelete) convertView.findViewById(R.id.mSlideDelete);
                viewHolder.mTvContent = (TextView) convertView.findViewById(R.id.mTvContent);
                viewHolder.mLlDelete = (LinearLayout) convertView.findViewById(R.id.mLlDelete);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.mTvContent.setText(mData.get(position));
            viewHolder.mSlideDelete.setOnSlideDeleteListener(new SlideDelete.OnSlideDeleteListener() {
                @Override
                public void onOpen(SlideDelete slideDelete) {
                    closeOtherItem();
                    slideDeleteArrayList.add(slideDelete);
                    Log.d("Slide", "slideDeleteArrayList当前数量：" + slideDeleteArrayList.size());
                }
                @Override
                public void onClose(SlideDelete slideDelete) {
                    slideDeleteArrayList.remove(slideDelete);
                    Log.d("Slide", "slideDeleteArrayList当前数量：" + slideDeleteArrayList.size());
                }
            });
            viewHolder.mLlDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mData.remove(position);
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }
    }
    class ViewHolder{
        SlideDelete mSlideDelete;
        TextView mTvContent;
        LinearLayout mLlDelete;
    }
    private void closeOtherItem(){
        // 采用Iterator的原因是for是线程不安全的，迭代器是线程安全的
        ListIterator<SlideDelete> slideDeleteListIterator = slideDeleteArrayList.listIterator();
        while(slideDeleteListIterator.hasNext()){
            SlideDelete slideDelete = slideDeleteListIterator.next();
            slideDelete.isShowDelete(false);
        }
        slideDeleteArrayList.clear();
    }
}
