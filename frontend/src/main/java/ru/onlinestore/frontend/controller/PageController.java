package ru.onlinestore.frontend.controller;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.onlinestore.frontend.service.PageService;

@Controller
public class PageController {

    private final PageService pageService;

    public PageController(PageService pageService) {
        this.pageService = pageService;
    }
    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        String sessionId = session.getId();
        return pageService.fetchCartAndRender(model, sessionId, "index", "home");
    }

    @GetMapping("/constructor")
    public String constructor(Model model, HttpSession session) {
        String sessionId = session.getId();
        return pageService.fetchCartAndRender(model, sessionId, "constructor", "constructor");
    }

    @GetMapping("/contacts")
    public String contacts(Model model, HttpSession session) {
        String sessionId = session.getId();
        return pageService.fetchCartAndRender(model, sessionId, "contacts", "contacts");
    }

}
