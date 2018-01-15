package com.chinatelecom.pimtest.model;

/**
 * Created by Shuo on 2018/1/15.
 */

public enum APPChannel {

    OFFICIAL("00", "官网"),
    COM_WANDOUJIA("01", "豌豆荚"),
    COM_ANZHI("02", "安智市场"),          //工信部
    COM_NDUOA("03", "N多市场"),
    CN_360("04", "360手机助手"),
    COM_BAIDU("05", "百度应用"),
    COM_HICLOUD("06", "智汇云"),         //工信部
    COM_APPCHINA("07", "应用汇"),
    COM_EOEMARKET("08", "优亿市场"),
    COM_LENOVOMM("09", "联想乐商店"),
    COM_HIAPK("10", "安卓市场"),
    COM_MYAPP("11", "应用宝"),           //工信部
    COM_189STORE("12", "天翼空间"),
    COM_OPPO("13", "OPPO"),             //工信部
    COM_VIVO("14", "VIVO"),             //工信部
    COM_XIAOMI("15", "小米"),           //工信部
    COM_MEIZU("16", "魅族"),            //工信部
    NEW_YEAR_SELL("95", "新年活动"),   //工信部
    CS_POPULARIZE("96", "客服推广"),   //工信部
    E_PUSH("97", "E推送"),              //工信部
    OTHER("98", "其他市场"),
    PRE_INSTALL("99", "预装");          //工信部

    String code;
    String desc;

    APPChannel(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
