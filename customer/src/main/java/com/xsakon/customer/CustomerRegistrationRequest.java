package com.xsakon.customer;

public record CustomerRegistrationRequest(
        String firstName,
        String lastName,
        String email
) {
}
