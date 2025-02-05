package com.bookmygift.service;

import com.bookmygift.entity.GiftTypeEnum;
import com.bookmygift.entity.Order;
import com.bookmygift.entity.OrderStatusEnum;
import com.bookmygift.entity.User;
import com.bookmygift.exception.BadRequestException;
import com.bookmygift.exception.UnAuthorizedException;
import com.bookmygift.repository.OrderRepository;
import com.bookmygift.repository.UserRepository;
import com.bookmygift.request.PlaceOrderRequest;
import com.bookmygift.request.ShowOrderRequest;
import com.bookmygift.response.OrderResponse;
import com.bookmygift.utils.ErrorEnums;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final QueueService queueService;

    public OrderResponse placeOrder(PlaceOrderRequest orderRequest) {

        String username = orderRequest.getUsername();

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UnAuthorizedException(ErrorEnums.INVALID_CREDENTIALS));

        Order order = Order.builder().orderId(username.substring(0, 3).toUpperCase() + "_" + UUID.randomUUID()).username(username).emailId(user.getEmail()).
                giftType(GiftTypeEnum.valueOf(orderRequest.getGiftType())).amountPaid(orderRequest.getAmountPaid()).orderStatus(OrderStatusEnum.ORDER_RECEIVED).build();

        orderRepository.save(order);

        queueService.sendPlaceOrderSuccessNotification(order);

        return OrderResponse.builder().order(order).build();

    }

    public OrderResponse showMyOrders(ShowOrderRequest orderRequest) {

        List<Order> orders = orderRepository.findOrdersByCriteria(orderRequest);

        return OrderResponse.builder().orders(orders).build();

    }

    public OrderResponse cancelOrder(String orderId, String username) {

        Order order = orderRepository.findByOrderIdAndUsername(orderId, username).orElseThrow(() -> new BadRequestException(ErrorEnums.INVALID_ORDER_ID));

        if (OrderStatusEnum.CANCELLED.equals(order.getOrderStatus())) {

            throw new BadRequestException(ErrorEnums.ORDER_ID_ALREADY_CANCELLED);

        }

        order.setOrderStatus(OrderStatusEnum.CANCELLED);

        orderRepository.save(order);

        queueService.sendOrderCancelledNotification(order);

        return OrderResponse.builder().order(order).build();

    }

}
