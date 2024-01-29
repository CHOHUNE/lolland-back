package com.example.lollandback.board.order.controller;

import com.example.lollandback.board.order.dto.OrderRequestDto;
import com.example.lollandback.board.order.dto.OrderResDto;
import com.example.lollandback.board.order.dto.PaymentRequestDto;
import com.example.lollandback.board.order.exception.CustomLogicException;
import com.example.lollandback.board.order.service.OrderService;
import com.example.lollandback.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/toss")
    public ResponseEntity createOrderInfo(@SessionAttribute("login") Member login, @RequestBody OrderRequestDto dto) {
        System.out.println("OrderController.createOrderInfo");
        System.out.println("dto = " + dto);
        if(login.getMember_login_id().equals(dto.getMember_login_id())) {
            try {
                OrderResDto res = orderService.createOrderInfo(login.getId(), dto);
                if(res != null) {
                    return ResponseEntity.ok(res);
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            } catch (CustomLogicException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("error", e.getMessage()));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping("/toss/success")
    public ResponseEntity tossPaymentSuccess(@RequestBody PaymentRequestDto dto)  {
        String paymentKey = dto.getPaymentKey();
        String orderId = dto.getOrderId();
        Long amount = dto.getAmount();
        orderService.tossPaymentSuccess(paymentKey, orderId, amount);
        return null;
    }
//
//    public ResponseEntity<JSONObject> confirmPayment(@RequestBody String jsonBody) throws Exception {
//
//        JSONParser parser = new JSONParser();
//        String orderId;
//        String amount;
//        String paymentKey;
//        try {
//            // 클라이언트에서 받은 JSON 요청 바디입니다.
//            JSONObject requestData = (JSONObject) parser.parse(jsonBody);
//            paymentKey = (String) requestData.get("paymentKey");
//            orderId = (String) requestData.get("orderId");
//            amount = (String) requestData.get("amount");
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        };
//        JSONObject obj = new JSONObject();
//        obj.put("orderId", orderId);
//        obj.put("amount", amount);
//        obj.put("paymentKey", paymentKey);
//
//        // 토스페이먼츠 API는 시크릿 키를 사용자 ID로 사용하고, 비밀번호는 사용하지 않습니다.
//        // 비밀번호가 없다는 것을 알리기 위해 시크릿 키 뒤에 콜론을 추가합니다.
//        String widgetSecretKey = "test_sk_Ba5PzR0ArnBZ5YJBGJPzrvmYnNeD";
//        Base64.Encoder encoder = Base64.getEncoder();
//        byte[] encodedBytes = encoder.encode((widgetSecretKey + ":").getBytes("UTF-8"));
//        String authorizations = "Basic " + new String(encodedBytes, 0, encodedBytes.length);
//
//        // 결제를 승인하면 결제수단에서 금액이 차감돼요.
//        URL url = new URL("https://api.tosspayments.com/v1/payments/confirm");
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestProperty("Authorization", authorizations);
//        connection.setRequestProperty("Content-Type", "application/json");
//        connection.setRequestMethod("POST");
//        connection.setDoOutput(true);
//
//        OutputStream outputStream = connection.getOutputStream();
//        outputStream.write(obj.toString().getBytes("UTF-8"));
//
//        int code = connection.getResponseCode();
//        boolean isSuccess = code == 200 ? true : false;
//
//        InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();
//
//        // 결제 성공 및 실패 비즈니스 로직을 구현하세요.
//        Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
//        JSONObject jsonObject = (JSONObject) parser.parse(reader);
//        responseStream.close();
//
//        return ResponseEntity.status(code).body(jsonObject);
//    }
//
//}
}
