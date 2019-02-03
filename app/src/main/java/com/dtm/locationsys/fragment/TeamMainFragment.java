package com.dtm.locationsys.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dtm.locationsys.R;
import com.dtm.locationsys.activity.FightTeamListActivity;

public class TeamMainFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_team_main, container, false);

        Button btnTeamAdd = (Button)rootView.findViewById(R.id.btn_team_add);

        btnTeamAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // 创建启动作战组Activity的Intent
                Intent intent = new Intent(getActivity(), FightTeamListActivity.class);
                startActivity(intent);

            }
        });

        return rootView;
    }

}
