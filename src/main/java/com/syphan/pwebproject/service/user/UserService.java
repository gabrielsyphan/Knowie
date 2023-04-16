package com.syphan.pwebproject.service.user;

import com.syphan.pwebproject.model.dto.UserDto;
import com.syphan.pwebproject.service.GenericService;
import jakarta.servlet.http.HttpSession;

public interface UserService extends GenericService<UserDto> {

    UserDto findByEmailAndPassword(String email, String password);

    UserDto login(UserDto userDto, HttpSession session);

    void resetPassword(UserDto userDto, HttpSession session) throws RuntimeException;

    UserDto forgotPassword(UserDto userDto) throws RuntimeException;
}
