package com.example.dodam.controller.diaryOrder;
import com.example.dodam.service.diaryOrder.OrderProvider;
import com.example.dodam.service.diaryOrder.OrderService;
import com.example.dodam.config.BaseResponse;
import com.example.dodam.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 필요한 모든 기능을 보이는 곳
 */

@RestController
public class OrderController {
    private OrderProvider orderProvider;
    private OrderService orderService;  //*추가
    private Object deleteOrder;


    @Autowired
    public OrderController(OrderProvider orderProvider, OrderService orderService) {
        this.orderProvider = orderProvider;  //provider와 연결
        this.orderService = orderService;      //*추가: service와 연결(원래는 provider하나에 service가 함께 있었는데 분리시켰기 때문에 추가)
    }


    //전체주문조회
    @GetMapping("/orders")  //복수형으로 작성
    public List<GetOrderRes> getOrder() {
        List<GetOrderRes> orderRes = orderProvider.getOrder();
        return orderRes;
    }


    //상세주문 전체조회
    @GetMapping("/order-details")  //하이픈, 복수형으로 작성
    public List<GetOrderDetailRes> getOrderDetail() {
        List<GetOrderDetailRes> orderDetailRes = orderProvider.getOrderDetail();
        return orderDetailRes;
    }


    //주문등록
    @ResponseBody
    @PostMapping("/orders") //복수형으로 작성
    public PostOrderRes postOrder(@RequestBody PostOrderReq postOrderReq) {
        PostOrderRes postOrderRes = orderService.postOrder(postOrderReq);
        return postOrderRes;
    }


    //상세주문등록   //orderId를 입력하면 해당 주문에 대한 상세주문 테이블이 생성된다.
    @ResponseBody
    @PostMapping("/order-details/{orderId}") //복수형으로 작성
    public PostOrderDetailRes postOrderDetail(@PathVariable("orderId") int orderId, @RequestBody PostOrderDetailReq postOrderDetailReq) {
        PostOrderDetailRes postOrderDetailRes = orderService.postOrderDetail(postOrderDetailReq, orderId);
        return postOrderDetailRes;
    }


    //userId로 주문조회
    @ResponseBody
    @GetMapping("/orders/{userId}")
    public List<GetOrderRes> getOrder(@PathVariable("userId") int userId) {

        List<GetOrderRes> getOrderRes = orderProvider.getOrder(userId);
        for (int i = 0; i < getOrderRes.size(); i++) {
            String isDeletedSign = getOrderRes.get(i).getIsDeleted().toString();
            if (isDeletedSign.equals("Y")) {  //삭제된 주문일 경우
                getOrderRes.remove(i);   //해당 데이터를 리스트에서 제외
                i-=1;
            }
        } //for문 종료
        return getOrderRes;  //나머지 출력=결국은 isDeleted값이 'N'인 경우만 출력됨
    }


    //orderId로 상세주문 1개 조회
    @ResponseBody
    @GetMapping("/orders-details/{orderId}")
    public Object getOrderDetail(@PathVariable("orderId") int orderId)  {

        GetOrderRes getOrderRes2 = orderProvider.getOrder2(orderId);
        String checkIsDeleted = getOrderRes2.getIsDeleted();
        if (checkIsDeleted.equals("Y")) {  //삭제된 주문일 경우
            BaseResponse baseResponse = new BaseResponse("이미 삭제된 주문");
            return baseResponse;
        } else { //존재하는 주문일 경우='N'
            GetOrderDetailRes getOrderDetailRes = null;
            getOrderDetailRes = orderProvider.getOrderDetail(orderId);
            return getOrderDetailRes;
        }
    }


    /**
     * 주문변경
     * 이미 삭제된 주문은 에러메시지를 출력한다.
     * 주문이 존재하는 경우에만 주문변경 가능하도록 한다.
     * 변경은 변경하려는 칼럼만 가져와서 다시 원래 JSON 데이터에 집어넣는 방식으로 이루어진다.
     */
    @ResponseBody
    @PatchMapping("/orders/{orderId}") //복수형으로 작성
    public Object modifyOrder(@PathVariable("orderId") int orderId, @RequestBody Order order)  {
        GetOrderRes getOrderRes2 = orderProvider.getOrder2(orderId); //***diaryOrder 테이블의 데이터 가져오는 방법***
        String checkIsDeleted= getOrderRes2.getIsDeleted();
        //System.out.println("컨트롤러에서  checkIsDeleted 값을 출력:"+checkIsDeleted);//IsDeleted 값이 출력됨('Y' 또는 'N')

        if (checkIsDeleted.equals("Y")) {  //삭제된 주문일 경우
            BaseResponse baseResponse=new BaseResponse("이미 삭제된 주문");
            return baseResponse;
        }
        else { //존재하는 주문일 경우='N'
            PatchOrderReq patchOrderReq = new PatchOrderReq(orderId, order.getAddress(), order.getBabyName(), order.getDiaryTitle(),
                    order.getStartDate(), order.getEndDate(), order.getTemplateNo(), order.getIsDeleted(),order.getDeletedTime());   //**변경할때는 삭제일시 null로 유지**, 변경할 변수들 모두 order.get___()형태로 입력
            orderService.modifyOrder(patchOrderReq);
            BaseResponse baseResponse=new BaseResponse("주문 수정 성공");
            return baseResponse;
        }
    }


    /**
     * 주문삭제
     * 이 함수는 테이블에 있는 해당 행을 모두 데이터베이스에서 삭제하지 않고
     *  isDeleted 칼럼만을 [yer] or [no] 로 업데이트해두는 코드이다.
     * 주문 1개 삭제 API(isDeleted 칼럼 값을 'N'에서 'Y'로 변경한다.
     */
    @ResponseBody
    @PatchMapping("/orders/delete/{orderId}") //복수형으로 작성, 삭제이지만 칼럼을 수정하기 때문에 patch를 사용한다.
    public Object deleteOrder(@PathVariable("orderId") int orderId, @RequestBody Order order) {
        DeleteOrderReq deleteOrderReq = new DeleteOrderReq(orderId, order.getDeletedTime());   //변경할 변수들 모두 order.get___()형태로 입력

        GetOrderRes getOrderRes2 = orderProvider.getOrder2(orderId);
        String checkIsDeleted = getOrderRes2.getIsDeleted();
        if (checkIsDeleted.equals("Y")) {  //삭제된 주문일 경우
            BaseResponse baseResponse = new BaseResponse("이미 삭제된 주문");
            return baseResponse;
        } else { //존재하는 주문일 경우='N'
            orderService.deleteOrder(deleteOrderReq);
            BaseResponse baseResponse = new BaseResponse("주문 삭제 성공");
            return baseResponse;
        }
    }
}

