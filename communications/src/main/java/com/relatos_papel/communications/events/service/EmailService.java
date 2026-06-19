package com.relatos_papel.communications.events.service;

import com.relatos_papel.communications.events.model.OrderCreatedEvent;
import com.relatos_papel.communications.events.model.OrderItemEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${email.notification.to}")
    private String defaultEmailTo;

    @Value("${email.notification.from}")
    private String emailFrom;

    public void sendOrderCreatedNotification(OrderCreatedEvent event) {
        try {
            String recipient = StringUtils.hasText(event.getBody().getEmail())
                    ? event.getBody().getEmail()
                    : defaultEmailTo;

            if (!StringUtils.hasText(recipient)) {
                log.warn("Sin destinatario de correo; se omite envío para orderId={}", event.getBody().getOrderId());
                return;
            }

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(recipient);
            message.setFrom(emailFrom);
            message.setSubject("Confirmación de pedido #" + event.getBody().getOrderId());
            message.setText(buildEmailContent(event));
            mailSender.send(message);
            log.info("Correo de pedido enviado orderId={} a {}", event.getBody().getOrderId(), recipient);
        } catch (Exception e) {
            log.error("Error al enviar correo orderId={}", event.getBody().getOrderId(), e);
        }
    }

    private String buildEmailContent(OrderCreatedEvent event) {
        StringBuilder content = new StringBuilder();
        NumberFormat currency = NumberFormat.getCurrencyInstance(new Locale("es", "ES"));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        // Logo oficial extraído directamente de la web de Relatos de Papel
        String logoUrl = "https://es.wikipedia.org/wiki/Archivo:LA-Logo-libri-wiki.png";

        content.append("<!DOCTYPE html>");
        content.append("<html>");
        content.append("<head>");
        content.append("  <meta charset='UTF-8'>");
        content.append("  <style>");
        content.append("    body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; background-color: #f8fafc; color: #1e293b; margin: 0; padding: 0; -webkit-font-smoothing: antialiased; }");
        content.append("    .wrapper { width: 100%; table-layout: fixed; background-color: #f8fafc; padding: 40px 0; }");
        content.append("    .container { max-width: 600px; background-color: #ffffff; border-radius: 12px; overflow: hidden; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.05), 0 2px 4px -1px rgba(0,0,0,0.03); margin: 0 auto; border: 1px solid #e2e8f0; }");
        content.append("    .header { background-color: #ffffff; padding: 35px 30px 20px 30px; text-align: center; border-bottom: 1px solid #f1f5f9; }");
        content.append("    .header img { max-height: 50px; width: auto; margin-bottom: 15px; }");
        content.append("    .header h1 { margin: 0; font-size: 24px; font-weight: 700; color: #0f172a; }");
        content.append("    .content { padding: 35px 30px; }");
        content.append("    .welcome { font-size: 18px; color: #0f172a; font-weight: 600; margin-bottom: 10px; }");
        content.append("    .intro-text { color: #64748b; font-size: 15px; line-height: 1.6; margin-bottom: 25px; }");
        content.append("    .meta-box { background-color: #f8fafc; border: 1px solid #e2e8f0; padding: 20px; margin-bottom: 30px; border-radius: 8px; }");
        content.append("    .meta-box p { margin: 6px 0; font-size: 14px; color: #334155; }");
        content.append("    .meta-box strong { color: #0f172a; }");
        content.append("    .order-table { width: 100%; border-collapse: collapse; margin-bottom: 30px; }");
        content.append("    .order-table th { text-align: left; border-bottom: 2px solid #edf2f7; padding: 12px 8px; font-size: 12px; color: #64748b; text-transform: uppercase; letter-spacing: 0.05em; }");
        content.append("    .order-table td { padding: 16px 8px; border-bottom: 1px solid #f1f5f9; font-size: 14px; color: #334155; vertical-align: middle; }");
        content.append("    .book-title { font-weight: 600; color: #0f172a; }");
        content.append("    .total-row td { font-weight: 700; font-size: 16px; border-top: 2px solid #e2e8f0; border-bottom: none; padding-top: 20px; color: #0f172a; }");
        // El color #f97316 (Naranja) hace juego con los botones principales de la web
        content.append("    .total-amount { color: #f97316; font-size: 20px; font-weight: 800; }");
        content.append("    .address-section { background-color: #f8fafc; border: 1px solid #e2e8f0; padding: 20px; border-radius: 8px; font-size: 14px; line-height: 1.6; color: #334155; }");
        content.append("    .address-section h3 { margin-top: 0; margin-bottom: 10px; color: #0f172a; font-size: 15px; font-weight: 600; }");
        content.append("    .footer { text-align: center; padding: 25px 30px; font-size: 12px; color: #94a3b8; background-color: #f8fafc; border-top: 1px solid #f1f5f9; line-height: 1.5; }");
        content.append("  </style>");
        content.append("</head>");
        content.append("<body>");

        content.append("<table class='wrapper' width='100%' cellpadding='0' cellspacing='0'>");
        content.append("  <tr>");
        content.append("    <td>");
        content.append("      <table class='container' width='600' cellpadding='0' cellspacing='0'>");

        // Header con el logo oficial de la app en Vercel
        content.append("        <tr>");
        content.append("          <td class='header'>");
        content.append("            <img src='").append(logoUrl).append("' alt='Relatos de Papel'><br>");
        content.append("            <h1>¡Confirmación de Pedido!</h1>");
        content.append("          </td>");
        content.append("        </tr>");

        // Cuerpo del correo
        content.append("        <tr>");
        content.append("          <td class='content'>");
        content.append("            <p class='welcome'>¡Hola!</p>");
        content.append("            <p class='intro-text'>Muchas gracias por confiar en nosotros. Tu pedido ha sido registrado con éxito en <strong>Relatos de Papel</strong>. A continuación, te detallamos la información de tu compra:</p>");

        // Detalles del pedido
        content.append("            <div class='meta-box'>");
        content.append("              <p><strong>Número de pedido:</strong> #").append(event.getBody().getOrderId()).append("</p>");
        content.append("              <p><strong>Fecha de compra:</strong> ").append(event.getBody().getOrderDate().format(dateFormatter)).append("</p>");
        content.append("            </div>");

        // Tabla de ítems del pedido
        content.append("            <table class='order-table'>");
        content.append("              <thead>");
        content.append("                <tr>");
        content.append("                  <th>Libro</th>");
        content.append("                  <th style='text-align: center;'>Cantidad:</th>");
        content.append("                  <th style='text-align: right;'>Precio:</th>");
        content.append("                  <th style='text-align: right;'>Subtotal:</th>");
        content.append("                </tr>");
        content.append("              </thead>");
        content.append("              <tbody>");

        for (OrderItemEvent item : event.getBody().getOrderItems()) {
            content.append("                <tr>");
            content.append("                  <td><span class='book-title'>").append(item.getBookId()).append("</span></td>");// TODO: Reemplazar por el nombre
            content.append("                  <td style='text-align: center; color: #64748b;'>").append(item.getQuantity()).append("</td>");
            content.append("                  <td style='text-align: right;'>").append(currency.format(item.getPrice())).append("</td>");
            content.append("                  <td style='text-align: right; font-weight: 500;'>").append(currency.format(item.getSubTotal())).append("</td>");
            content.append("                </tr>");
        }

        // Fila de Totales
        content.append("                <tr class='total-row'>");
        content.append("                  <td colspan='2'></td>");
        content.append("                  <td style='text-align: right; vertical-align: middle;'>Total:</td>");
        content.append("                  <td style='text-align: right;' class='total-amount'>").append(currency.format(event.getBody().getTotal())).append("</td>");
        content.append("                </tr>");
        content.append("              </tbody>");
        content.append("            </table>");

        // Sección de Dirección de Envío
        content.append("            <div class='address-section'>");
        content.append("              <h3>Dirección de envío</h3>");
        content.append("              <p style='margin: 0;'>");
        content.append("                ").append(event.getBody().getAddress()).append("<br>");
        content.append("                ").append(event.getBody().getCity()).append(", ").append(event.getBody().getCountry());
        if (StringUtils.hasText(event.getBody().getPhone())) {
            content.append("<br style='margin-bottom: 5px;'><strong>Teléfono de contacto:</strong> ").append(event.getBody().getPhone());
        }
        content.append("              </p>");
        content.append("            </div>");

        content.append("          </td>");
        content.append("        </tr>");

        // Pie de página (Footer)
        content.append("        <tr>");
        content.append("          <td class='footer'>");
        content.append("            <strong>Relatos de Papel</strong><br>");
        content.append("            Este es un correo automático generado por nuestro sistema, por favor no respondas directamente a este mensaje.");
        content.append("          </td>");
        content.append("        </tr>");

        content.append("      </table>");
        content.append("    </td>");
        content.append("  </tr>");
        content.append("</table>");

        content.append("</body>");
        content.append("</html>");

        return content.toString();
    }

}
