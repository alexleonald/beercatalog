package com.beercatalogue.common.infrastructure;

import com.beercatalogue.common.application.SecurityAccount;
import com.beercatalogue.common.application.UserSourcePort;

import java.util.List;

public class AwsSecretManagerUserSourceAdapter implements UserSourcePort {
    @Override
    public List<SecurityAccount> getAllUsers() {
        // to update
        return List.of();
    }
}
