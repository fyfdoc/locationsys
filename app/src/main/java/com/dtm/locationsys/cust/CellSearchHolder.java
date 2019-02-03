package com.dtm.locationsys.cust;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.dtm.locationsys.msgDef.RptCellSearchInfo;
import com.kyleduo.switchbutton.SwitchButton;

import com.dtm.locationsys.R;

/**
 * 守控小区
 * Created by tomato on 2017/10/27.
 */

public class CellSearchHolder extends RecyclerView.ViewHolder {
    //守控小区
    private RptCellSearchInfo mSearchCell;

    private TextView mTacCidPciTextView;

    private TextView mChBandTextView;

    private TextView mPowerTextView;

    private SwitchButton mLockButton;


    public CellSearchHolder(View itemView, final CellSearchAdapterItf  cellSearchAdapter) {
        super(itemView);

        mTacCidPciTextView = itemView.findViewById(R.id.list_item_cellsearch_tac_cid_pci_textview);

        mChBandTextView = itemView.findViewById(R.id.list_item_cellsearch_ch_band_textview);

        mPowerTextView = itemView.findViewById(R.id.list_item_cellsearch_rsrp_textview);

        mLockButton = itemView.findViewById(R.id.list_item_cellsearch_lock_switch_button);
        mLockButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                cellSearchAdapter.switchButtonChange(mSearchCell, isChecked);
            }
        });
    }

    /**
     * 绑定页面数据
     * @param cell
     */
    public void bindCellSearch(RptCellSearchInfo cell) {
        mSearchCell = cell;

        setTacCidPciText();

        setChBandText();

        setPowerText();

        setLockStatus();
    }

    private void setTacCidPciText() {
        String tacCidPciText = String.format("%d/%d/%d", mSearchCell.getTrackingAreaCode()
                , mSearchCell.getCellIdentity()
                , mSearchCell.getPCI());
        mTacCidPciTextView.setText(tacCidPciText);
    }

    private void setChBandText() {
        String chBandText = String.format("%d/%d", mSearchCell.getBandId(), mSearchCell.getNrfcnN());
        mChBandTextView.setText(chBandText);
    }

    private void setPowerText() {
        mPowerTextView.setText(mSearchCell.getRSRP());
    }

    private void setLockStatus() {
        mLockButton.setCheckedImmediatelyNoEvent(mSearchCell.isLocked());
    }

}
