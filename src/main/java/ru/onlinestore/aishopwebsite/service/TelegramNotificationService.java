package ru.onlinestore.aishopwebsite.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.onlinestore.aishopwebsite.model.CartItem;
import ru.onlinestore.aishopwebsite.model.Order;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;


@Service
public class TelegramNotificationService {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.chat.id}")
    private String chatId;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendOrderNotification(Order order, List<CartItem> cart, double total) {
        StringBuilder message = new StringBuilder();
        message.append("<b>📦 НОВЫЙ ЗАКАЗ</b>\n\n");
        message.append("<b>👤 Имя:</b> ").append(order.getName()).append("\n");
        message.append("<b>📞 Телефон:</b> <code>").append(order.getPhone()).append("</code>\n\n");
        message.append("<b>🛒 Товары:</b>\n");
        for (CartItem item : cart) {
            message.append("• ").append(item.getProduct().getName())
                    .append(" — ").append(item.getQuantity()).append(" шт. ")
                    .append(String.format("%.2f", item.getTotalPrice())).append(" ₽\n");
        }
        message.append("\n");
        message.append("<b>💰 Итого:</b> ").append(String.format("%.2f", total)).append(" ₽");

        sendMessage(message.toString());
    }

    private String escapeMarkdown(String text) {
        return text.replaceAll("[_\\-*]", "\\\\$0"); // экранируем _, *, -
    }

    private String formatPrice(double price) {
        return String.format("%.2f", price).replace(",", ".");
    }

    private void sendMessage(String message) {
        try {
            String url = String.format(
                    "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s&parse_mode=HTML",
                    botToken,
                    chatId,
                    message
            );

            restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}