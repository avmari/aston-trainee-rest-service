package repository;

import db.ConnectionManager;
import entity.Payment;
import entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import repository.impl.PaymentRepository;
import repository.impl.UserRepository;
import util.AbstractTest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentRepositoryTest extends AbstractTest {

    private final PaymentRepository paymentRepository = PaymentRepository.getInstance();
    private final UserRepository userRepository = UserRepository.getInstance();
    @BeforeEach
    void clean() {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM payment; " +
                     "DELETE FROM users")) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void findById() {
        User user = userRepository.save(getUser("test_username"));
        Payment expectedResult = paymentRepository.save(getPayment(user.getId()));

        Optional<Payment> actualResult = paymentRepository.findById(expectedResult.getId());

        assertTrue(actualResult.isPresent());
        assertEquals(expectedResult, actualResult.get());
    }

    @Test
    void save() {
        User user = userRepository.save(getUser("test_username"));
        Payment payment = getPayment(user.getId());

        Payment actualResult = paymentRepository.save(payment);

        assertNotNull(actualResult.getId());
    }

    @Test
    void findAll() {
        User user = userRepository.save(getUser("test_username"));
        Payment payment1 = paymentRepository.save(getPayment(user.getId()));
        Payment payment2 = paymentRepository.save(getPayment(user.getId()));
        Payment payment3 = paymentRepository.save(getPayment(user.getId()));

        List<Payment> actualResults = paymentRepository.findAll();

        assertEquals(3, actualResults.size());

        List<UUID> actualResultIds = actualResults.stream().map(Payment::getId).toList();
        assertTrue(actualResultIds.containsAll(List.of(payment1.getId(), payment2.getId(), payment3.getId())));
    }

    @Test
    void deleteById() {
        User user = userRepository.save(getUser("test_username"));
        Payment payment = paymentRepository.save(getPayment(user.getId()));

        boolean actualResult = paymentRepository.deleteById(payment.getId());

        assertTrue(actualResult);
    }

    @Test
    void deleteByIdIfNotExists() {
        User user = userRepository.save(getUser("test_username"));
        Payment payment = paymentRepository.save(getPayment(user.getId()));

        boolean actualResult = paymentRepository.deleteById(UUID.fromString("11111111-1111-1111-1111-111111111111"));

        assertFalse(actualResult);
    }

    @ParameterizedTest
    @MethodSource("getPaymentAttributesToUpdate")
    void update(Integer amount, boolean userId) {
        User user = userRepository.save(getUser("test_username"));
        Payment payment = paymentRepository.save(getPayment(user.getId()));
        if (amount != null)
            payment.setAmount(amount);
        if (userId) {
            User newUser = userRepository.save(getUser("new_test_username"));
            payment.setUser(newUser);
        }

        paymentRepository.update(payment);

        Payment actualResult = paymentRepository.findById(payment.getId()).get();
        assertEquals(payment, actualResult);
    }

    private Payment getPayment(UUID userId) {
        Payment payment = new Payment();
        payment.setAmount(1000);
        User user = new User();
        user.setId(userId);
        payment.setUser(user);
        return payment;
    }

    private User getUser(String username) {
        User user = new User();
        user.setUsername(username);
        user.setFirstName("test_first_name");
        user.setLastName("test_last_name");
        return user;
    }

    private static Stream<Arguments> getPaymentAttributesToUpdate() {
        return Stream.of(
                Arguments.of(2000, false),
                Arguments.of(null, true),
                Arguments.of(2000, true)
        );
    }
}
