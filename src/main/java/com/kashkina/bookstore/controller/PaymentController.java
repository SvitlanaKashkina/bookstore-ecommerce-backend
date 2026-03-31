package com.kashkina.bookstore.controller;

import com.kashkina.bookstore.dto.PaymentDTO;
import com.kashkina.bookstore.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private PaymentService paymentService;
    private static final Logger log = LoggerFactory.getLogger(BookController.class);

    // Create a new payment
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentDTO createPayment(@Valid @RequestBody PaymentDTO paymentDTO) {
        log.info("Request to create payment for orderId={}", paymentDTO.getOrderId());
        PaymentDTO result = paymentService.createPayment(paymentDTO);
        log.info("Payment created with id={}", result.getId());
        return result;
    }

    // Get all payments
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<PaymentDTO> getAllPayments() {
        log.info("Request to get all payments");
        List<PaymentDTO> payments = paymentService.getAllPayments();
        log.info("Total payments retrieved: {}", payments.size());
        return payments;
    }

    // Receive payment by id
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public PaymentDTO getPaymentById(@PathVariable Long id) {
        log.info("Request to get payment with id={}", id);
        PaymentDTO payment = paymentService.getPaymentById(id);
        log.info("Payment retrieved: id={}, orderId={}", payment.getId(), payment.getOrderId());
        return payment;
    }

    // Update payment's status
    @PutMapping("/{id}/status")
    public PaymentDTO updatePaymentStatus(@PathVariable Long id, @RequestBody PaymentDTO dto) {
        log.info("Request to update payment status for id={} to {}", id, dto.getStatus());
        PaymentDTO updated = paymentService.updatePaymentStatus(id, dto);
        log.info("Payment status updated for id={} to {}", updated.getId(), updated.getStatus());
        return updated;
    }
}
