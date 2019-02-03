package com.dtm.locationsys.cust;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.dtm.locationsys.R;
import com.dtm.locationsys.model.TmsiStatisticsModel;
import com.dtm.locationsys.msgDef.RptCellSearchInfo;
import com.kyleduo.switchbutton.SwitchButton;

/**
 * Tmsi次数统计
 * Created by tomato on 2017/10/27.
 */

public class TmsiStatisticsHolder extends RecyclerView.ViewHolder {
    // Tmsi次数统计
    private TmsiStatisticsModel mTmsiStatisticsModel;

    private TextView mTacCidPciTextView;

    private TextView mChBandTextView;

    private TextView mTmsiTextView;

    private TextView mTmsiCountTextView;

    public TmsiStatisticsHolder(View itemView) {
        super(itemView);

        mTacCidPciTextView = itemView.findViewById(R.id.list_item_tmsistatistics_tac_cid_pci_textview);

        mChBandTextView = itemView.findViewById(R.id.list_item_tmsistatistics_ch_band_textview);

        mTmsiTextView = itemView.findViewById(R.id.list_item_tmsistatistics_tmsi_textview);

        mTmsiCountTextView = itemView.findViewById(R.id.list_item_tmsistatistics_count_textview);
    }

    /**
     * 绑定页面数据
     */
    public void bindTimsStatistics(TmsiStatisticsModel tmsiStatisticsModel) {
        mTmsiStatisticsModel = tmsiStatisticsModel;

        setTacCidPciText();

        setChBandText();

        setTmsiText();

        setTmsiCountText();
    }

    private void setTacCidPciText() {
        String tacCidPciText = String.format("%d/%d/%d"
                , mTmsiStatisticsModel.getTac()
                , mTmsiStatisticsModel.getCid()
                , mTmsiStatisticsModel.getPci());
        mTacCidPciTextView.setText(tacCidPciText);
    }

    private void setChBandText() {
        String chBandText = String.format("%d/%d"
                , mTmsiStatisticsModel.getCh()
                , mTmsiStatisticsModel.getBandId());
        mChBandTextView.setText(chBandText);
    }

    private void setTmsiText() {
        mTmsiTextView.setText(Long.toString(mTmsiStatisticsModel.getTmsi()));
    }

    private void setTmsiCountText() {
        mTmsiCountTextView.setText(Integer.toString(mTmsiStatisticsModel.getTmsiCount()));
    }
}
