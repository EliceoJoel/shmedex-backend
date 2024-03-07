package com.eherbas.shmedex.service;

import com.eherbas.shmedex.dto.UserDTO;

import java.util.Optional;

public interface UserService {
    Optional<UserDTO> getById(Long id);
}
