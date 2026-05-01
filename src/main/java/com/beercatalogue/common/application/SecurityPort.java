package com.beercatalogue.common.application;

public interface SecurityPort {

    String getCurrentUserId();

    boolean isAuthenticated();

    boolean hasRole(String role);
}