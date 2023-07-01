package com.example.andlinker;

/**
 * @Description:
 * @Author: CJ
 * @CreateDate: 2023/7/2 0:46
 */
public class NormalObj {
    private int mType;
    private float mValue;
    private String mMsg;

    public NormalObj(int type, float value, String msg) {
        mType = type;
        mValue = value;
        mMsg = msg;
    }

    @Override
    public String toString() {
        return "NormalObj{" +
                "mType=" + mType +
                ", mValue=" + mValue +
                ", mMsg='" + mMsg + '\'' +
                '}';
    }
}
