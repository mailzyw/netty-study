package com.zhangyiwen.study.disruptor.demo_01;

/**
 * DEMO中使用的 消息全假定是一条交易
 * Created by zhangyiwen on 17/3/3.
 */
public class TradeTransaction {

    private String id;
    private double price;

    public TradeTransaction() {
    }

    public TradeTransaction(String id, double price) {
        this.id = id;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "TradeTransaction{" +
                "id='" + id + '\'' +
                ", price=" + price +
                '}';
    }
}
