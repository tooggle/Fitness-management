package org.fm.backend.model;

import java.util.Date;

public class Payment {
    private int paymentID ;

    private int bookID ;

    private int Amount ;

    private String payMethod ;

    private int paymentStatus ;

    private Date payTime ;

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public int getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public int getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentID=" + paymentID +
                ", bookID=" + bookID +
                ", Amount=" + Amount +
                ", payMethod='" + payMethod + '\'' +
                ", paymentStatus=" + paymentStatus +
                ", payTime=" + payTime +
                '}';
    }
}
