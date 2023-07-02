package com.bookmygift.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "OrderInfo")
public class Order implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String orderId;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String emailId;

    @Column
    @Enumerated(EnumType.STRING)
    private GiftType giftType;

    @Column
    private Double amountPaid;

    @Column
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @JsonCreator
    public Order(@JsonProperty("orderId") String orderId,
                 @JsonProperty("username") String username,
                 @JsonProperty("emailId") String emailId,
                 @JsonProperty("giftType") GiftType giftType,
                 @JsonProperty("amountPaid") Double amountPaid,
                 @JsonProperty("orderStatus") OrderStatus orderStatus
    ) {
        this.orderId = orderId;
        this.username = username;
        this.emailId = emailId;
        this.giftType = giftType;
        this.amountPaid = amountPaid;
        this.orderStatus = orderStatus;
    }

    @JsonCreator
    public Order(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Order order = mapper.readValue(json, Order.class);
        this.orderId = order.getOrderId();
        this.username = order.getUsername();
        this.emailId = order.getEmailId();
        this.giftType = order.getGiftType();
        this.amountPaid = order.getAmountPaid();
        this.orderStatus = order.getOrderStatus();
    }

}

