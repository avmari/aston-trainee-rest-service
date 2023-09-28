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

    private final PaymentService paymentService;
    private final PaymentDtoMapper paymentDtoMapper;
    private final ServletUtil servletUtil;
    private final Gson gson;

    public AllPaymentsServlet(PaymentService paymentService, PaymentDtoMapper paymentDtoMapper,
                              ServletUtil servletUtil, Gson gson) {
        this.paymentService = paymentService;
        this.paymentDtoMapper = paymentDtoMapper;
        this.servletUtil = servletUtil;
        this.gson = gson;
    }

    public AllPaymentsServlet() {
        this.paymentService = new PaymentService(PaymentRepository.getInstance());
        this.paymentDtoMapper = PaymentDtoMapper.getInstance();
        this.servletUtil = new ServletUtil();
        this.gson = new Gson();
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        List<OutgoingPaymentDto> payments = paymentService.findAll().stream().map(paymentDtoMapper::toDto).toList();
        String paymentsJson = gson.toJson(payments);

        servletUtil.writeJsonToResponse(paymentsJson, resp);
    }
}
