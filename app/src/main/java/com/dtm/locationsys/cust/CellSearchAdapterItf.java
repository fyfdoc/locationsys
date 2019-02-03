package com.dtm.locationsys.cust;

import com.dtm.locationsys.msgDef.RptCellSearchInfo;

/**
 * Created by tomato on 2017/10/31.
 */

public interface CellSearchAdapterItf {
    public void switchButtonChange(RptCellSearchInfo cell, boolean isChecked);
}
