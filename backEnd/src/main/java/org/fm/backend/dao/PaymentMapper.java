package org.fm.backend.dao;

import org.apache.ibatis.annotations.*;
import org.fm.backend.model.Payment;

import java.util.List;

@Mapper
public interface PaymentMapper {

    // 根据 bookID 获取支付记录
    @Select("""
        SELECT paymentID, bookID, Amount, payMethod, paymentStatus, payTime
        FROM Payment
        WHERE bookID = #{bookID}
        """)
    Payment getByBookID(@Param("bookID") int bookID);

    // 插入新的支付记录并返回自增ID
    @Insert("""
        INSERT INTO Payment(bookID, Amount, payMethod, paymentStatus, payTime)
        VALUES(#{bookID}, #{Amount}, #{payMethod}, #{paymentStatus}, #{payTime})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "paymentID")
    int insert(Payment payment);

    // 获取所有支付记录
    @Select("SELECT * FROM Payment")
    List<Payment> getAll();

    // 更新支付状态
    @Update("""
        UPDATE Payment 
        SET paymentStatus = #{paymentStatus}
        WHERE paymentID = #{paymentID}
        """)
    boolean updatePaymentStatus(
            @Param("paymentID") int paymentID,
            @Param("paymentStatus") int paymentStatus
    );

}
