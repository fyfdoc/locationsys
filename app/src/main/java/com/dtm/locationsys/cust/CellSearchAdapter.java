package com.dtm.locationsys.cust;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dtm.locationsys.R;
import com.dtm.locationsys.msgDef.RptCellSearchInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by tomato on 2017/10/30.
 */

public class CellSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements CellSearchAdapterItf {
    private static final int HEADER_POSITION = 0;

    private static final int TYPE_HEADER = 0;

    private static final int TYPE_ITEM = 1;

    //守控小区
    public List<RptCellSearchInfo> mCellSearchList;

    public List<RptCellSearchInfo> mLockedCellSearchList;

    public List<RptCellSearchInfo> mUnlockedCellSearchList;

    private CellSearchComparator cellSearchComparator;

    // 调用本类的Fragment
    private FragmentItf mFragment;

    public CellSearchAdapter(FragmentItf fragment) {
        mCellSearchList = new ArrayList<>( );

        mLockedCellSearchList = new ArrayList<>();

        mUnlockedCellSearchList = new ArrayList<>();

        cellSearchComparator = new CellSearchComparator();

        mFragment = fragment;
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
            createdViewHolder = new CellSearchHeaderHolder(
                    inflateItemView(parent, R.layout.list_header_cellsearch));
        } else if (TYPE_ITEM == viewType) {
            /* create item view holder */
            createdViewHolder = new CellSearchHolder(
                    inflateItemView(parent, R.layout.list_item_cellsearch), CellSearchAdapter.this);
        } else {
            assert(false);
            //TODO: whether throw exception
        }

        return createdViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (HEADER_POSITION != position) {
            CellSearchHolder cellSearchHolder = (CellSearchHolder)holder;
            cellSearchHolder.bindCellSearch(getCellSearchByPosition(position));
        }
    }

    @Override
    public int getItemCount() {
        return mCellSearchList.size() + 1; /* add 1 for header row */
    }

    /**
     * 锁定按钮开关事件
     * @param cell
     * @param isChecked
     */
    @Override
    public void switchButtonChange(RptCellSearchInfo cell, boolean isChecked) {
        // 锁频改变处理
        cellSearchLockChange(cell, isChecked);

        // TODO: 下发命令是否应该放在前面？
        // 调用Fragment的回调方法，下发参数配置
        mFragment.callbakHandler();
    }

    /**
     * 锁频改变处理
     * @param cell
     * @param isChecked
     */
    private void cellSearchLockChange(RptCellSearchInfo cell, boolean isChecked) {

        if (isChecked) { // 锁定
            // 将对象从未锁定列表移到锁定列表
            for(RptCellSearchInfo item : mUnlockedCellSearchList) {
                if (item.getPCI() == cell.getPCI() && item.getEARFCN() == cell.getEARFCN()) { // 包含
                    item.setLocked(true);
                    mLockedCellSearchList.add(item);
                    mUnlockedCellSearchList.remove(item);
                    break;
                }
            }
        } else { // 解锁
            // 将对象从锁定列表移到未锁定列表
            for(RptCellSearchInfo item : mLockedCellSearchList) {
                if (item.getPCI() == cell.getPCI() && item.getEARFCN() == cell.getEARFCN()) { // 包含
                    item.setLocked(false);
                    mUnlockedCellSearchList.add(item);
                    mLockedCellSearchList.remove(item);
                    break;
                }
            }
        }

        joinCellSearch();

        update(mCellSearchList);
    }


    private View inflateItemView(ViewGroup parent, int layoutID) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutID, parent, false);
    }

    private RptCellSearchInfo getCellSearchByPosition(int position) {
        return mCellSearchList.get(position - 1); /* sub 1 for header row */
    }

    /**
     * 设置守控小区锁频数据
     * @param updateCellSearch
     */
    private void selectLockedCellSearch(List<RptCellSearchInfo> updateCellSearch) {
        mLockedCellSearchList.clear();

        for (RptCellSearchInfo cellSearch : updateCellSearch) {
            if (cellSearch.isLocked()) {
                mLockedCellSearchList.add(cellSearch);
            }
        }

        Collections.sort(mLockedCellSearchList, cellSearchComparator);
    }

    /**
     * 设置守控小区非锁频数据
     * @param updateCellSearch
     */
    private void selectUnlockedCellSearch(List<RptCellSearchInfo> updateCellSearch) {
        mUnlockedCellSearchList.clear();

        for (RptCellSearchInfo cell : updateCellSearch) {
            if (!cell.isLocked()) {
                mUnlockedCellSearchList.add(cell);
            }
        }

        Collections.sort(mUnlockedCellSearchList, cellSearchComparator);
    }

    /**
     * 组装守控小区缓存列表
     */
    private void joinCellSearch() {
        mCellSearchList.clear();

        for (RptCellSearchInfo lockedCellSearch : mLockedCellSearchList) {
            mCellSearchList.add(lockedCellSearch);
        }

        for (RptCellSearchInfo unlockedCellSearch : mUnlockedCellSearchList) {
            mCellSearchList.add(unlockedCellSearch);
        }
    }

    /**
     * 将缓存中的小区锁定信息，更新到基站上报的小区信息中
     * @param reportCellList
     */
    private void updateLockedInfo(List<RptCellSearchInfo> reportCellList) {
        // 基站上报的守控小区列表
        for (RptCellSearchInfo item : reportCellList) {
            // 缓存中锁定的守控小区列表
            for (RptCellSearchInfo lockedItem : mLockedCellSearchList) {
                if (item.getPCI() == lockedItem.getPCI()
                        && item.getEARFCN() == item.getEARFCN()) { // 同一个守控小区
                    item.setLocked(true);
                    break;
                }
            }
        }
    }

    /**
     * 更新adapter数据
     * @param updateCellList
     */
    public void update(List<RptCellSearchInfo> updateCellList) {
        // 更新小区的锁定信息
        updateLockedInfo(updateCellList);

        // 按锁定状态和power值排序
        selectLockedCellSearch(updateCellList);

        selectUnlockedCellSearch(updateCellList);

        joinCellSearch();

        notifyDataSetChanged();
    }
}
