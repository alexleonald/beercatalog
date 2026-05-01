package com.beercatalogue.common.infrastructure;

public final class ApiConstants {
    private ApiConstants() {}

    public static final String API_V1 = "/api/v1";

    // Manufacturers
    public static final String MANUFACTURERS = API_V1 + "/manufacturers";

    // Beers
    public static final String BEERS = API_V1 + "/beers";

    // Roles
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_MANUFACTURER = "MANUFACTURER";
}
