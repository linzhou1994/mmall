package com.mmall.util;

import java.math.BigDecimal;

/**
 * 　　　　　　　　┏┓　　　┏┓+ +
 * 　　　　　　　┏┛┻━━━┛┻┓ + +
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　━　　　┃ ++ + + +
 * 　　　　　　 ████━████ ┃+
 * 　　　　　　　┃　　　　　　　┃ +
 * 　　　　　　　┃　　　┻　　　┃
 * 　　　　　　　┃　　　　　　　┃ + +
 * 　　　　　　　┗━┓　　　┏━┛
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃ + + + +
 * 　　　　　　　　　┃　　　┃　　　　Code is far away from bug with the animal protecting
 * 　　　　　　　　　┃　　　┃ + 　　　　神兽保佑,代码无bug
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃　　+
 * 　　　　　　　　　┃　 　　┗━━━┓ + +
 * 　　　　　　　　　┃ 　　　　　　　┣┓
 * 　　　　　　　　　┃ 　　　　　　　┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛ + + + +
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛+ + + +
 * 佛曰:
 * 写字楼里写字间，写字间里程序员；
 * 程序人员写程序，又拿程序换酒钱。
 * 酒醒只在网上坐，酒醉还来网下眠；
 * 酒醉酒醒日复日，网上网下年复年。
 * 但愿老死电脑间，不愿鞠躬老板前；
 * 奔驰宝马贵者趣，公交自行程序员。
 * 别人笑我忒疯癫，我笑自己命太贱；
 * 不见满街漂亮妹，哪个归得程序员？
 * ---------------------------
 * 项目名： mmall
 * 包名：   com.mmall.util
 * 创建者:  linzhou
 * 创建时间:17/10/17
 * 描述:   BigDecimal工具类
 */
public class BigDecimalUtil {
    private BigDecimalUtil(){

    }

    /**
     * 两个double类型的加法运算
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal add(double v1,double v2){
        BigDecimal b1 =new BigDecimal(Double.toString(v1));
        BigDecimal b2 =new BigDecimal(Double.toString(v2));
        return b1.add(b2);
    }

    /**
     * 两个double类型的减法运算
     * @param v1 被减数
     * @param v2 减数
     * @return
     */
    public static BigDecimal sub(double v1,double v2){
        BigDecimal b1 =new BigDecimal(Double.toString(v1));
        BigDecimal b2 =new BigDecimal(Double.toString(v2));
        return b1.subtract(b2);
    }

    /**
     * 两个double类型的乘法运算
     * @param v1  被乘数
     * @param v2  乘数
     * @return
     */
    public static BigDecimal mul(double v1,double v2){
        BigDecimal b1 =new BigDecimal(Double.toString(v1));
        BigDecimal b2 =new BigDecimal(Double.toString(v2));
        return b1.multiply(b2);
    }

    /**
     * 两个double类型的除法运算（保留两位小数，四舍五入）
     * @param v1 被除数
     * @param v2 除数
     * @return
     */
    public static BigDecimal div(double v1,double v2){
        BigDecimal b1 =new BigDecimal(Double.toString(v1));
        BigDecimal b2 =new BigDecimal(Double.toString(v2));
        return b1.divide(b2,2,BigDecimal.ROUND_HALF_UP);
    }
}
