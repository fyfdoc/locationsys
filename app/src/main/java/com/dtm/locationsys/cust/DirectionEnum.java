package com.dtm.locationsys.cust;

/**
 * Created by tomato on 2017/11/3.
 */

public enum DirectionEnum {
    FRONT_ANTENNA("前", 3), BACK_ANTENNA("后", 1), LEFT_ANTENNA("左", 2), RIGHT_ANTENNA("右", 0);

    private String name;
    private int index;

    private DirectionEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (DirectionEnum directionEnum : DirectionEnum.values()) {
            if (directionEnum.getIndex() == index) {
                return directionEnum.getName();
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
