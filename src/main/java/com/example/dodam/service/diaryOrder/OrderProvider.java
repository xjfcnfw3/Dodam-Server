package com.example.dodam.service.diaryOrder;

import com.example.dodam.model.GetOrderDetailRes;
import com.example.dodam.model.GetOrderRes;
import com.example.dodam.repository.diaryOrder.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//import static com.example.dodam.config.BaseResponseStatus.DATABASE_ERROR;

/**
 * * select(조회) 구현
 */
@Service
public class OrderProvider {
    private  final OrderDao orderDao;

    @Autowired
    public  OrderProvider(OrderDao orderDao){ this.orderDao=orderDao;}

    //전체주문조회
    public List<GetOrderRes> getOrder(){
        List<GetOrderRes> orderRes=orderDao.orderRes();
        return orderRes;
    }


    //상세주문 전체조회
    public List<GetOrderDetailRes> getOrderDetail(){
        List<GetOrderDetailRes> orderDetailRes=orderDao.orderDetailRes();
        return orderDetailRes;
    }


    // 해당 userId를 갖는 주문 조회
    public List<GetOrderRes> getOrder(int userId)  {
            List<GetOrderRes> getOrderRes= orderDao.getOrder(userId);
            return getOrderRes;
    }

    /////////////[주문변경],[주문삭제] 에서 사용하기 위해 만든 함수/////////////
    public GetOrderRes getOrder2(int orderId) {
        GetOrderRes getOrderRes2 = orderDao.getOrder2(orderId);
        return getOrderRes2;
    }

    // 해당 orderId를 갖는 주문 조회
    public GetOrderDetailRes getOrderDetail(int orderId)  {
            GetOrderDetailRes getOrderDetailRes = orderDao.getOrderDetail(orderId);
            return getOrderDetailRes;
    }
}
