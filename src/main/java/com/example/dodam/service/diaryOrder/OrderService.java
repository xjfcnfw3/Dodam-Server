package com.example.dodam.service.diaryOrder;

import com.example.dodam.repository.diaryOrder.OrderDao;
import com.example.dodam.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *  *추가한 파일(원래는 provider와 같은 파일에 있었음 -> 분리시킴)
 *  * 조회 기능을 제회한 나머지 insert, update, delete 구현
 */
@Service
public class OrderService {
    private final OrderProvider orderProvider;
    private OrderDao orderDao;

    @Autowired
    public OrderService(OrderDao orderDao, OrderProvider orderProvider) {
        this.orderDao = orderDao;
        this.orderProvider = orderProvider;
    }


    //주문등록
    public PostOrderRes postOrder(PostOrderReq postOrderReq){
        int orderId=orderDao.addOrder(postOrderReq);  //필요함
        String result="주문 등록 성공";
        PostOrderRes postOrderRes=new PostOrderRes(result);
        return postOrderRes;
    }


    //상세주문등록
    public PostOrderDetailRes postOrderDetail(PostOrderDetailReq postOrderDetailReq, int orderId){
        int orderDetailId=orderDao.addOrderDetail(postOrderDetailReq, orderId);
        String result="상세 주문 등록 성공";
        PostOrderDetailRes postOrderDetailRes=new PostOrderDetailRes(result);

        return postOrderDetailRes;

    }


    //주문변경
    public void modifyOrder(PatchOrderReq patchOrderReq) {
           orderDao.modifyOrder(patchOrderReq);
    }


    //주문삭제
    public void deleteOrder(DeleteOrderReq deleteOrderReq) {
        orderDao.deleteOrder(deleteOrderReq);
    }
}