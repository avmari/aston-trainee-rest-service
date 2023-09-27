package servlet.payment;

import com.google.gson.*;
import entity.Payment;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import repository.impl.PaymentRepository;
import service.impl.PaymentService;
import servlet.dto.IncomingPaymentDto;
import servlet.mapper.PaymentDtoMapper;
import util.ServletUtil;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@WebServlet("/payment")
public class PaymentServlet extends HttpServlet {
    
    private final PaymentService paymentService = new PaymentService(PaymentRepository.getInstance());
    private final PaymentDtoMapper paymentDtoMapper = PaymentDtoMapper.getInstance();

    private final ServletUtil servletUtil = new ServletUtil();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        UUID id = UUID.fromString(req.getParameter("id"));
        Optional<Payment> payment = paymentService.findById(id);

        if (payment.isPresent()) {
            String paymentJson = new Gson().toJson(paymentDtoMapper.toDto(payment.get()));
            servletUtil.writeJsonToResponse(paymentJson, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JsonObject jsonData = new Gson().fromJson(req.getReader(), JsonObject.class);

        IncomingPaymentDto paymentDto = new IncomingPaymentDto();
        paymentDto.setAmount(jsonData.get("amount").getAsInt());
        paymentDto.setUserId(UUID.fromString(jsonData.get("user").getAsJsonObject().get("id").getAsString()));

        Payment payment = paymentService.save(paymentDtoMapper.toEntity(paymentDto));

        servletUtil.writeJsonToResponse(new Gson().toJson(paymentDtoMapper.toDto(payment)), resp);
    }

    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JsonObject jsonData = new Gson().fromJson(req.getReader(), JsonObject.class);
        Set<String> jsonKeys = jsonData.keySet();

        IncomingPaymentDto paymentDto = new IncomingPaymentDto();
        paymentDto.setId(UUID.fromString(jsonData.get("id").getAsString()));
        if (jsonKeys.contains("amount"))
            paymentDto.setAmount(jsonData.get("amount").getAsInt());
        if (jsonKeys.contains("user"))
            paymentDto.setUserId(UUID.fromString(jsonData.get("user").getAsJsonObject().get("id").getAsString()));

        paymentService.update(paymentDtoMapper.toEntity(paymentDto));
    }

    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        UUID id = UUID.fromString(req.getParameter("id"));
        Optional<Payment> payment = paymentService.findById(id);

        if (payment.isPresent()) {
            paymentService.deleteById(id);
        }
    }
}
