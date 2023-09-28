package service.impl;

import entity.Payment;
import entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.impl.PaymentRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void save() {
        Payment expectedResult = getPayment(UUID.randomUUID());
        when(paymentRepository.save(any(Payment.class))).thenReturn(expectedResult);

        Payment actualResult = paymentService.save(expectedResult);

        assertEquals(expectedResult, actualResult);
        verify(paymentRepository, times(1)).save(expectedResult);
    }

    @Test
    void findById() {
        Payment expectedResult = getPayment(UUID.randomUUID());
        when(paymentRepository.findById(any(UUID.class))).thenReturn(Optional.of(expectedResult));

        Optional<Payment> actualResult = paymentService.findById(expectedResult.getId());

        assertTrue(actualResult.isPresent());
        assertEquals(expectedResult, actualResult.get());
        verify(paymentRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void findAll() {
        List<Payment> expectedResult = List.of(getPayment(UUID.randomUUID()), getPayment(UUID.randomUUID()),
                getPayment(UUID.randomUUID()));
        when(paymentRepository.findAll()).thenReturn(expectedResult);

        List<Payment> actualResult = paymentService.findAll();

        assertEquals(expectedResult, actualResult);
        verify(paymentRepository, times(1)).findAll();
    }

    @Test
    void deleteById() {
        when(paymentRepository.deleteById(any(UUID.class))).thenReturn(true);

        boolean actualResult = paymentService.deleteById(UUID.randomUUID());

        assertTrue(actualResult);
    }

    @Test
    void update() {
        paymentService.update(getPayment(UUID.randomUUID()));

        verify(paymentRepository, times(1)).update(any(Payment.class));
    }

    private Payment getPayment(UUID userId) {
        Payment payment = new Payment();
        payment.setId(UUID.randomUUID());
        payment.setAmount(1000);
        User user = new User();
        user.setId(userId);
        payment.setUser(user);
        return payment;
    }
}