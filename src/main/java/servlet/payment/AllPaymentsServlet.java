package servlet.payment;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import repository.impl.PaymentRepository;
import service.impl.PaymentService;
import servlet.dto.OutgoingPaymentDto;
import servlet.mapper.PaymentDtoMapper;
import util.ServletUtil;

import java.util.List;

@WebServlet("/payments")
public class AllPaymentsServlet extends HttpServlet {

    private final PaymentService paymentService = new PaymentService(PaymentRepository.getInstance());
    private final PaymentDtoMapper paymentDtoMapper = PaymentDtoMapper.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        List<OutgoingPaymentDto> payments = paymentService.findAll().stream().map(paymentDtoMapper::toDto).toList();
        String paymentsJson = new Gson().toJson(payments);

        ServletUtil.writeJsonToResponse(paymentsJson, resp);
    }
}
