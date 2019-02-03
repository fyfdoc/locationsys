package com.dtm.locationsys.cust;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dtm.locationsys.R;
import com.dtm.locationsys.model.TmsiStatisticsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomato on 2017/10/30.
 */

public class TmsiStatisticsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HEADER_POSITION = 0;

    private static final int TYPE_HEADER = 0;

    private static final int TYPE_ITEM = 1;

    // Tmsi统计
    public List<TmsiStatisticsModel> mTmsiStatisticsModels;

    private CellSearchComparator cellSearchComparator;

    public TmsiStatisticsAdapter() {
        mTmsiStatisticsModels = new ArrayList<>( );
    }

    @Override
    public int getItemViewType(int position) {
        int itemViewType = TYPE_ITEM;

        if (HEADER_POSITION == position) {
            itemViewType = TYPE_HEADER;
        }

        return itemViewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder createdViewHolder = null;
        if (TYPE_HEADER == viewType) {
            /* create header view holder */
            createdViewHolder = new TmsiStatisticsHeaderHolder(
                    inflateItemView(parent, R.layout.list_header_tmsistatistics));
        } else if (TYPE_ITEM == viewType) {
            /* create item view holder */
            createdViewHolder = new TmsiStatisticsHolder(
                    inflateItemView(parent, R.layout.list_item_tmsistatistics));
        } else {
            assert(false);
            //TODO: whether throw exception
        }

        return createdViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (HEADER_POSITION != position) {
            TmsiStatisticsHolder tmsiStatisticsHolder = (TmsiStatisticsHolder) holder;
            tmsiStatisticsHolder.bindTimsStatistics(getTmsiStatisticsByPosition(position));
        }
    }

    @Override
    public int getItemCount() {
        return mTmsiStatisticsModels.size() + 1; /* add 1 for header row */
    }

    private View inflateItemView(ViewGroup parent, int layoutID) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutID, parent, false);
    }

    private TmsiStatisticsModel getTmsiStatisticsByPosition(int position) {
        return mTmsiStatisticsModels.get(position - 1); /* sub 1 for header row */
    }

    /**
     * 更新adapter数据
     * @param updateTmsiStatisticsList
     */
    public void update(List<TmsiStatisticsModel> updateTmsiStatisticsList) {
        mTmsiStatisticsModels = updateTmsiStatisticsList;

        notifyDataSetChanged();
    }
}
