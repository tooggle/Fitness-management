package org.fm.backend.model;

import java.util.Date;

public class Book {
    private int bookID ;

    private int classID ;

    private int traineeID ;

    private int paymentID ;

    private String payMethod ;

    private int bookStatus ;

    private Date bookTime ;

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public int getClassID() {
        return classID;
    }

    public void setClassID(int classID) {
        this.classID = classID;
    }

    public int getTraineeID() {
        return traineeID;
    }

    public void setTraineeID(int traineeID) {
        this.traineeID = traineeID;
    }

    public int getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    public int getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(int bookStatus) {
        this.bookStatus = bookStatus;
    }

    public Date getBookTime() {
        return bookTime;
    }

    public void setBookTime(Date bookTime) {
        this.bookTime = bookTime;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookID=" + bookID +
                ", classID=" + classID +
                ", traineeID=" + traineeID +
                ", paymentID=" + paymentID +
                ", payMethod='" + payMethod + '\'' +
                ", bookStatus=" + bookStatus +
                ", bookTime=" + bookTime +
                '}';
    }
}
