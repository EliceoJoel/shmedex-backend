package com.eherbas.shmedex.service.implement;

import com.eherbas.shmedex.dto.UserDTO;
import com.eherbas.shmedex.mapper.UserMapper;
import com.eherbas.shmedex.repository.UserRepository;
import com.eherbas.shmedex.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Optional<UserDTO> getById(Long id) {
        return userRepository.findById(id).map(userMapper::toDto);
    }
}
