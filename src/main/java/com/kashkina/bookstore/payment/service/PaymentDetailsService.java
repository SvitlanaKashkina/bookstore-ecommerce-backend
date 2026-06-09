package com.kashkina.bookstore.service;

import com.kashkina.bookstore.dto.PaymentDetailsDTO;
import com.kashkina.bookstore.entity.Payment;
import com.kashkina.bookstore.entity.PaymentDetails;
import com.kashkina.bookstore.exception.ResourceNotFoundException;
import com.kashkina.bookstore.repository.PaymentDetailsRepository;
import com.kashkina.bookstore.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentDetailsService {

    private PaymentDetailsRepository paymentDetailsRepository;
    private PaymentRepository paymentRepository;

    private static final Logger log = LoggerFactory.getLogger(PaymentDetailsService.class);

    // Creating payment details
    public PaymentDetailsDTO createPaymentDetails(PaymentDetailsDTO dto) {
        log.info("Request to create payment details for paymentId={}", dto.getPaymentId());

        Payment payment = paymentRepository.findById(dto.getPaymentId())
                .orElseThrow(() -> {
                    log.error("Payment not found with id={}", dto.getPaymentId());
                    return new ResourceNotFoundException("Payment not found with id=" + dto.getPaymentId());
                });

        PaymentDetails details = PaymentDetails.builder()
                .payment(payment)
                .provider(dto.getProvider())
                .transactionId(dto.getTransactionId())
                .payerEmail(dto.getPayerEmail())
                .build();

        PaymentDetails saved = paymentDetailsRepository.save(details);
        dto.setId(saved.getId());

        log.info("Payment details created with id={}", saved.getId());
        return dto;
    }

    // Getting all the details
    public List<PaymentDetailsDTO> getAllPaymentDetails() {
        log.info("Request to get all payment details");

        List<PaymentDetailsDTO> detailsList = paymentDetailsRepository.findAll()
                .stream()
                .map(d -> new PaymentDetailsDTO(
                        d.getId(),
                        d.getPayment().getId(),
                        d.getProvider(),
                        d.getTransactionId(),
                        d.getPayerEmail()
                ))
                .collect(Collectors.toList());

        log.info("Total payment details retrieved: {}", detailsList.size());
        return detailsList;
    }

    // Obtaining a part by ID
    public PaymentDetailsDTO getPaymentDetailsById(Long id) {
        log.info("Request to get payment details with id={}", id);

        PaymentDetails details = paymentDetailsRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Payment details not found with id={}", id);
                    return new ResourceNotFoundException("Payment details not found with id=" + id);
                });

        return new PaymentDetailsDTO(
                details.getId(),
                details.getPayment().getId(),
                details.getProvider(),
                details.getTransactionId(),
                details.getPayerEmail()
        );
    }
}
