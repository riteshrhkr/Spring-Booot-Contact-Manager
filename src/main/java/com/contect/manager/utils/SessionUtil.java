package com.contect.manager.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import jakarta.servlet.http.HttpSession;

@Controller
public class SessionUtil {
    @Autowired
    private HttpSession session;

    public boolean setMessage(String message, String type) {
        session.setAttribute("message", new Message(message, type));
        return true;
    }

    public Message getMessage() {
            return (Message) session.getAttribute("message");
    }

    public void removeMessage() {
        session.removeAttribute("message");
    }
}
