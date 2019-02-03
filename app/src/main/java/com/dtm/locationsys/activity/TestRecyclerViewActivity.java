package com.dtm.locationsys.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dtm.locationsys.R;

import java.util.ArrayList;
import java.util.List;

public class TestRecyclerViewActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<String> mDatas;
    private HomeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_recycler_view);

        Button btnDataChange = (Button) findViewById(R.id.btn_test);

        initData();
        mAdapter = new HomeAdapter();
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

        btnDataChange.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mAdapter.update();
            }
        });
    }

    protected void initData() {
        mDatas = new ArrayList<>();
        for (int i = 'A'; i < 'Z'; i++) {
            mDatas.add("" + (char)i);
        }
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>
    {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    TestRecyclerViewActivity.this).inflate(R.layout.item_home, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position)
        {
            holder.tv.setText(mDatas.get(position));
        }

        @Override
        public int getItemCount()
        {
            return mDatas.size();
        }

        private void update() {
            for (int i = 0; i < 10; i++) {
                mDatas.set(i, Integer.toString(i));
            }

            notifyDataSetChanged();

        }

        class MyViewHolder extends ViewHolder
        {

            TextView tv;

            public MyViewHolder(View view)
            {
                super(view);
                tv = view.findViewById(R.id.id_num);
            }
        }
    }



 }
