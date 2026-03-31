package com.kashkina.bookstore.service;

import com.kashkina.bookstore.dto.PaymentDTO;
import com.kashkina.bookstore.entity.Order;
import com.kashkina.bookstore.entity.Payment;
import com.kashkina.bookstore.exception.ResourceNotFoundException;
import com.kashkina.bookstore.repository.OrderRepository;
import com.kashkina.bookstore.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private PaymentRepository paymentRepository;
    private OrderRepository orderRepository;

    private static final Logger log = LoggerFactory.getLogger(BookService.class);

    // Create a payment
    public PaymentDTO createPayment(PaymentDTO paymentDTO) {
        log.info("Creating payment for orderId={}", paymentDTO.getOrderId());

        Order order = orderRepository.findById(paymentDTO.getOrderId())
                .orElseThrow(() -> {
                    log.error("Order not found with id={}", paymentDTO.getOrderId());
                    return new ResourceNotFoundException("Order not found with id=" + paymentDTO.getOrderId());
                });

        Payment payment = Payment.builder()
                .order(order)
                .amount(paymentDTO.getAmount())
                .method(paymentDTO.getMethod())
                .status(paymentDTO.getStatus() != null ? paymentDTO.getStatus() : paymentDTO.getStatus().PENDING)
                .build();

        Payment saved = paymentRepository.save(payment);

        paymentDTO.setId(saved.getId());
        paymentDTO.setCreatedAt(saved.getCreatedAt());

        log.info("Payment created with id={}", saved.getId());
        return paymentDTO;
    }

    // Receive all payments
    public List<PaymentDTO> getAllPayments() {
        log.info("Retrieving all payments");
        List<PaymentDTO> payments = paymentRepository.findAll()
                .stream()
                .map(p -> new PaymentDTO(
                        p.getId(),
                        p.getOrder().getId(),
                        p.getAmount(),
                        p.getStatus(),
                        p.getMethod(),
                        p.getCreatedAt()
                ))
                .collect(Collectors.toList());

        log.info("Total payments found: {}", payments.size());
        return payments;
    }

    // Receiving payment by ID
    public PaymentDTO getPaymentById(Long id) {
        log.info("Retrieving payment with id={}", id);

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Payment not found with id={}", id);
                    return new ResourceNotFoundException("Payment not found with id=" + id);
                });

        return new PaymentDTO(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getMethod(),
                payment.getCreatedAt()
        );
    }

    // Updating payment status after pay
    public PaymentDTO updatePaymentStatus(Long id, PaymentDTO dto) {
        log.info("Updating payment status for id={}", id);

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Payment not found with id={}", id);
                    return new ResourceNotFoundException("Payment not found with id=" + id);
                });

        if (dto.getStatus() != null) {
            payment.setStatus(dto.getStatus());
            Payment saved = paymentRepository.save(payment);
            log.info("Payment status updated for id={} to {}", id, dto.getStatus());
            return new PaymentDTO(
                    saved.getId(),
                    saved.getOrder().getId(),
                    saved.getAmount(),
                    saved.getStatus(),
                    saved.getMethod(),
                    saved.getCreatedAt()
            );
        } else {
            log.warn("No status provided for payment id={}", id);
            return new PaymentDTO(
                    payment.getId(),
                    payment.getOrder().getId(),
                    payment.getAmount(),
                    payment.getStatus(),
                    payment.getMethod(),
                    payment.getCreatedAt()
            );
        }
    }
}
