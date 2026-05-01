package com.beercatalogue.common.application;

import java.util.List;

public interface UserSourcePort {
    List<SecurityAccount> getAllUsers();
}
