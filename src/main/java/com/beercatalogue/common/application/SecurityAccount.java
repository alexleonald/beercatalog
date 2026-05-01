package com.beercatalogue.common.application;

public record SecurityAccount(
        String username,
        String password,
        String roles
) {
}
