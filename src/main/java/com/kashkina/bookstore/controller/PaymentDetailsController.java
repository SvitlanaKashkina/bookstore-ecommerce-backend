package com.kashkina.bookstore.controller;

import com.kashkina.bookstore.dto.PaymentDetailsDTO;
import com.kashkina.bookstore.service.PaymentDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/payment-details")
@RequiredArgsConstructor
public class PaymentDetailsController {

    private PaymentDetailsService paymentDetailsService;
    private static final Logger log = LoggerFactory.getLogger(PaymentDetailsController.class);

    // Creating a new payment detail
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentDetailsDTO createPaymentDetails(@Valid @RequestBody PaymentDetailsDTO dto) {
        log.info("Request to create payment details for paymentId={}", dto.getPaymentId());
        PaymentDetailsDTO result = paymentDetailsService.createPaymentDetails(dto);
        log.info("Payment details created with id={}", result.getId());
        return result;
    }

    // Receiving all payment details
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<PaymentDetailsDTO> getAllPaymentDetails() {
        log.info("Request to get all payment details");
        List<PaymentDetailsDTO> details = paymentDetailsService.getAllPaymentDetails();
        log.info("Total payment details retrieved: {}", details.size());
        return details;
    }

    // Receiving payment details by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public PaymentDetailsDTO getPaymentDetailsById(@PathVariable Long id) {
        log.info("Request to get payment details with id={}", id);
        PaymentDetailsDTO dto = paymentDetailsService.getPaymentDetailsById(id);
        log.info("Payment details retrieved: id={}, paymentId={}", dto.getId(), dto.getPaymentId());
        return dto;
    }
}
