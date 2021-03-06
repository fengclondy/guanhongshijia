package cn.com.dyninfo.o2o.communication;

import cn.com.dyninfo.o2o.communication.common.BaseResult;
import cn.com.dyninfo.o2o.entity.Coupon;

import java.util.List;

/**
 * Created by Administrator on 2016/7/29.
 */
public class CreateOrderResult extends BaseResult {

    //可使用的优惠券列表
    private List<Coupon> couponList;

    //商品总金额
    private double totalPrice;

    //本次支付金额
    private double payPrice;

    //交易ID
    private String tradeNo;

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public List<Coupon> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<Coupon> couponList) {
        this.couponList = couponList;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(double payPrice) {
        this.payPrice = payPrice;
    }

    @Override
    public String toString() {
        return "CreateOrderResult{" +
                "couponList=" + couponList +
                ", totalPrice=" + totalPrice +
                ", payPrice=" + payPrice +
                ", tradeNo='" + tradeNo + '\'' +
                '}';
    }
}
