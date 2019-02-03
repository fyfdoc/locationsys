package com.dtm.locationsys.cust;

import com.dtm.locationsys.msgDef.RptCellSearchInfo;

import java.util.Comparator;

/**
 * Created by tomato on 2017/10/30.
 */

public class CellSearchComparator implements Comparator<RptCellSearchInfo> {
    @Override
    public int compare(RptCellSearchInfo cell1, RptCellSearchInfo cell2) {
        return (cell1.getCell_power() > cell2.getCell_power()) ? -1 : 1;
    }
}
