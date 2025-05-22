package com.flowers.shopping.service;

public interface EmailService {
    void sendVerificationEmail(String email, String subject, String text);
}
