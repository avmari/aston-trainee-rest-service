package service.impl;

import entity.Payment;
import repository.impl.PaymentRepository;
import service.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PaymentService implements Service<Payment> {

    private static final PaymentRepository paymentRepository = PaymentRepository.getInstance();
    private static final PaymentService INSTANCE = new PaymentService();

    private PaymentService() {}

    public static PaymentService getInstance() {
        return INSTANCE;
    }

    @Override
    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public Optional<Payment> findById(UUID id) {
        return paymentRepository.findById(id);
    }

    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    @Override
    public boolean deleteById(UUID id) {
        return paymentRepository.deleteById(id);
    }

    @Override
    public void update(Payment payment) {

    }
}
