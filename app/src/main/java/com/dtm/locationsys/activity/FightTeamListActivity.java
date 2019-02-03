package com.dtm.locationsys.activity;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dtm.locationsys.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作战组
 */
public class FightTeamListActivity extends ListActivity {


    private List<Map<String, Object>> mData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mData = getData();
        MyAdapter adapter = new MyAdapter(this);
        setListAdapter(adapter);
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", "退出协作组");
        map.put("img", R.drawable.btn_star_big_on);
        map.put("img_next", R.drawable.ic_menu_forward);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "解散协作组");
        map.put("img", R.drawable.btn_star_big_on);
        map.put("img_next", R.drawable.ic_menu_forward);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "通信记录");
        map.put("img", R.drawable.btn_star_big_on);
        map.put("img_next", R.drawable.ic_menu_forward);
        list.add(map);

        return list;
    }

    // ListView 中某项被选中后的逻辑
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        Log.v("MyListView4-click", (String)mData.get(position).get("title"));
    }

    /**
     * listview中点击按键弹出对话框
     */
    public void showInfo(){
//        new AlertDialog.Builder(this)
//                .setTitle("我的listview")
//                .setMessage("介绍...")
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                })
//                .show();

    }



    public final class ViewHolder{
        public ImageView img;
        public TextView title;
        public ImageView imgNext;
    }


    public class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;


        public MyAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mData.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {

                holder=new ViewHolder();

                convertView = mInflater.inflate(R.layout.activity_fight_team, null);
                holder.img = (ImageView)convertView.findViewById(R.id.img);
                holder.title = (TextView)convertView.findViewById(R.id.title);
                holder.imgNext = (ImageView) convertView.findViewById(R.id.img_next);
//                holder.viewBtn = (Button)convertView.findViewById(R.id.view_btn);
                convertView.setTag(holder);

            }else {

                holder = (ViewHolder)convertView.getTag();
            }


            holder.img.setBackgroundResource((Integer)mData.get(position).get("img"));
            holder.imgNext.setBackgroundResource((Integer)mData.get(position).get("img_next"));
            holder.title.setText((String)mData.get(position).get("title"));
//            holder.info.setText((String)mData.get(position).get("info"));

//            holder.viewBtn.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    showInfo();
//                }
//            });


            return convertView;
        }

    }
}
