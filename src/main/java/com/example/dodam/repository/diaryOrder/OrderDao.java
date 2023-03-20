package com.example.dodam.repository.diaryOrder;

import com.example.dodam.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;

@Repository
public class OrderDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){this.jdbcTemplate=new JdbcTemplate(dataSource);}

    //전체주문조회
    public List<GetOrderRes> orderRes(){

        return this.jdbcTemplate.query("select * from dodam.diaryOrder",
                (rs, rowNum)-> new GetOrderRes(

                        rs.getInt("orderId"),
                        rs.getInt("userId"),
                        rs.getString("address"),
                        rs.getString("babyName"),
                        rs.getString("diaryTitle"),
                        rs.getString("startDate"),
                        rs.getString("endDate"),
                        rs.getInt("templateNo"),
                        rs.getString("createTime"),
                        rs.getString("updateTime"),
                        rs.getString("isDeleted"),
                        rs.getString("deletedTime"))

        );
    }


    //상세 전체주문조회
    public List<GetOrderDetailRes> orderDetailRes(){

        return this.jdbcTemplate.query("select * from dodam.orderDetail",
                (rs, rowNum)-> new GetOrderDetailRes(

                        rs.getInt("orderDetailId"),
                        rs.getInt("orderId"),
                        rs.getString("deliveryService"),
                        rs.getString("invoiceNo"),
                        rs.getString("deliveryStatus"))
        );
    }

    //주문등록
    public int addOrder(PostOrderReq postorderReq){
        String createOrderQuery= "Insert into dodam.diaryOrder(userId, address, babyName, diaryTitle, startDate, " +
                "endDate, templateNo) VALUES(?,?,?,?,?,?,?)";
        Object[] createOrderParams=new Object[]{
                postorderReq.getUserId(), postorderReq.getAddress(), postorderReq.getBabyName(), postorderReq.getDiaryTitle(),
                postorderReq.getStartDate(), postorderReq.getEndDate(),postorderReq.getTemplateNo()
        };
        this.jdbcTemplate.update(createOrderQuery, createOrderParams);

        return this.jdbcTemplate.queryForObject("select last_insert_id()",int.class);
    }


    //상세주문등록(주문을 등록 -> 주문번호(orderId)로 주문상세가 생성됨)
    public int addOrderDetail(PostOrderDetailReq postorderDetailReq, int orderId){
        postorderDetailReq.setOrderId(orderId);  //매개변수 orderId로 PostOrderDetailReq의 orderId를 초기화 ex) 주문번호 2
        String createOrderDetailQuery= "Insert into dodam.orderDetail(orderId, deliveryService, " +
                "invoiceNo, deliveryStatus) VALUES(?,?,?,?)";  //네 가지 변수가 들어갈 곳
        Object[] createOrderDetailParams=new Object[]{  //postorderDetailReq.getOrderId()에는 주문번호 2가 들어있음
               orderId, postorderDetailReq.getDeliveryService(), postorderDetailReq.getInvoiceNo(),
                postorderDetailReq.getDeliveryStatus()
        };
        this.jdbcTemplate.update(createOrderDetailQuery, createOrderDetailParams);

        return this.jdbcTemplate.queryForObject("select last_insert_id()",int.class);  ///????

    }


    // 해당 orderId를 갖는 상세주문조회
    public GetOrderDetailRes getOrderDetail(int orderId) {
        String getOrderDetailQuery = "select * from orderDetail where orderId = ?"; // 해당 orderId를 만족하는 상세주문을 조회하는 쿼리문
        int getOrderDetailParams = orderId;
        return this.jdbcTemplate.queryForObject(getOrderDetailQuery,
                (rs, rowNum) -> new GetOrderDetailRes(
                        //sql에서 필요한 변수들
                        rs.getInt("orderDetailId"),
                        rs.getInt("orderId"),
                        rs.getString("deliveryService"),
                        rs.getString("invoiceNo"),
                        rs.getString("deliveryStatus")),
                getOrderDetailParams);
    }


    //주문 변경
    public int modifyOrder(PatchOrderReq patchOrderReq) {
        //update sql문 작성
        String modifyOrderQuery = "update diaryOrder set address = ?, babyName = ?,diaryTitle = ?,startDate = ?," +
                "endDate = ?,templateNo = ?, deletedTime=? where orderId = ? "; // 해당 orderId를 만족하는 주문정보를 해당 ~~~으로 변경한다.

        Object[] modifyOrderParams = new Object[]{patchOrderReq.getAddress(),
                patchOrderReq.getBabyName(),patchOrderReq.getDiaryTitle(),patchOrderReq.getStartDate(),patchOrderReq.getEndDate(),
                patchOrderReq.getTemplateNo(), patchOrderReq.getDeletedTime(), patchOrderReq.getOrderId()}; // 주입될 값들(변경할값, orderId) 순!!!

        return this.jdbcTemplate.update(modifyOrderQuery, modifyOrderParams);
    }

    // 해당 userId를 갖는 주문조회
    public List<GetOrderRes> getOrder(int userId) {
        String getOrderQuery = "select * from diaryOrder where userId = ?"; // 해당 userId를 만족하는 주문을 조회하는 쿼리문
        int getOrderParams = userId;
        return this.jdbcTemplate.query(getOrderQuery,
        //return Collections.singletonList(this.jdbcTemplate.queryForObject(getOrderQuery,
                (rs, rowNum) -> new GetOrderRes(
                        //sql에서 필요한 변수들
                        rs.getInt("orderId"),
                        rs.getInt("userId"),
                        rs.getString("address"),
                        rs.getString("babyName"),
                        rs.getString("diaryTitle"),
                        rs.getString("startDate"),
                        rs.getString("endDate"),
                        rs.getInt("templateNo"),
                        rs.getString("createTime"),
                        rs.getString("updateTime"),
                        rs.getString("isDeleted"),
                        rs.getString("deletedTime")),
                getOrderParams);
    }

    /////////////[주문변경], [주문삭제] 에서 사용하기 위해 만든 함수/////////////
    public GetOrderRes getOrder2(int orderId) {
        String getOrderQuery = "select * from diaryOrder where orderId = ?"; // 해당 userId를 만족하는 주문을 조회하는 쿼리문
        int getOrderParams = orderId;
        return this.jdbcTemplate.queryForObject(getOrderQuery,
                (rs, rowNum) -> new GetOrderRes(
                        //sql에서 필요한 변수들
                        rs.getInt("orderId"),
                        rs.getInt("userId"),
                        rs.getString("address"),
                        rs.getString("babyName"),
                        rs.getString("diaryTitle"),
                        rs.getString("startDate"),
                        rs.getString("endDate"),
                        rs.getInt("templateNo"),
                        rs.getString("createTime"),
                        rs.getString("updateTime"),
                        rs.getString("isDeleted"),
                        rs.getString("deletedTime")),
                getOrderParams);
    }


    // 해당 orderId를 갖는 주문삭제
    public int deleteOrder(DeleteOrderReq deleteOrderReq) {
        //update sql문 작성, 요청이 오면 isDeleted 값을 Y로 만들어버림, **postman에서 Body 입력 없음**
        String deleteOrderQuery = "update diaryOrder set isDeleted=? where orderId = ? "; // 해당 orderId를 만족하는 주문정보를 해당 ~~~으로 변경한다.
        Object[] deleteOrderParams = new Object[]{"Y", deleteOrderReq.getOrderId()}; // 주입될 값들(isDeleted, orderId) 순!!!

        return this.jdbcTemplate.update(deleteOrderQuery, deleteOrderParams);
    }
}