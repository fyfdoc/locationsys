package com.dtm.locationsys.fragment;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dtm.locationsys.R;
import com.dtm.locationsys.utils.Constants;
import com.dtm.locationsys.utils.SharedPrefHelper;

/**
 * 网络IP配置
 */
public class NetworkConfigFragment extends Fragment {
    public static final String ARG_SECTION_NUMBER = "SECTION_NUMBER";

    // 目的IP
    String strDestIp;
    // 目的端口
    String strDestPort;
    // 本地端口
    String strLocalPort;
    // 扰码
//    String scrambleCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //网络配置参数
        SharedPreferences sharedPreferences;
        final SharedPreferences.Editor editor;

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_network_config, container, false);
        // 目的地址
        final EditText destIpEt = view.findViewById(R.id.txt_dest_ip);
        // 目的端口
        final EditText destPortEt = view.findViewById(R.id.txt_dest_port);
        // 本地端口
        final EditText localPortEt = view.findViewById(R.id.txt_local_port);
        // 扰码
//        final EditText scrambleCodeEt = view.findViewById(R.id.txt_scramble_code);;

        // 保存按钮
        Button btnSave = view.findViewById(R.id.btn_save);

        // 获取网络配置参数
        strDestIp = SharedPrefHelper.getNetworkInfo(getActivity(), Constants.DEST_IP);
        strDestPort = SharedPrefHelper.getNetworkInfo(getActivity(), Constants.DEST_PORT);
        strLocalPort = SharedPrefHelper.getNetworkInfo(getActivity(), Constants.LOCAL_PORT);
        // 扰码
//        scrambleCode = SharedPrefHelper.getLoginInfo(getActivity(), Constants.SRCAMBLE_CODE);

        //TODO:打桩数据===============start
        if("".equals(strDestIp)){
            strDestIp = "192.168.42.253";
        }
        if("".equals(strDestPort)){
            strDestPort = "30000";
        }
        if("".equals(strLocalPort)){
            strLocalPort = "5000";
        }
//        if("".equals(scrambleCode)){
//            scrambleCode = "123";
//        }
        //TODO:打桩数据===============end

        destIpEt.setText(strDestIp);
        destPortEt.setText(strDestPort);
        localPortEt.setText(strLocalPort);
//        scrambleCodeEt.setText(scrambleCode);

        // 保存按钮事件
        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                strDestIp = destIpEt.getText().toString();
                strDestPort = destPortEt.getText().toString();
                strLocalPort = localPortEt.getText().toString();
//                scrambleCode = scrambleCodeEt.getText().toString();

                if(strDestIp == null || "".equals(strDestIp)){
                    MyToast("目标地址不能为空");
                    return;
                }
                if(strDestPort == null || "".equals(strDestPort)){
                    MyToast("目标端口不能为空");
                    return;
                }
                if(strLocalPort == null || "".equals(strLocalPort)){
                    MyToast("本地端口不能为空");
                    return;
                }
//                if(scrambleCode == null || "".equals(scrambleCode)){
//                    MyToast("扰码不能为空");
//                    return;
//                }

                // 保存网络信息
                SharedPrefHelper.setNetworkInfo(getActivity(), Constants.DEST_IP, strDestIp);
                SharedPrefHelper.setNetworkInfo(getActivity(), Constants.DEST_PORT, strDestPort);
                SharedPrefHelper.setNetworkInfo(getActivity(), Constants.LOCAL_PORT, strLocalPort);
//              SharedPrefHelper.setLoginInfo(getActivity(), Constants.SRCAMBLE_CODE, scrambleCode);

                MyToast("保存成功");

            }
        });

        return view;
    }

    /**
     * 自定义Toast
     * @param info
     */
    private void MyToast(String info){
        Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT).show();

    }

}
