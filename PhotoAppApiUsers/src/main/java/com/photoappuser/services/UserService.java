package com.photoappuser.services;

import com.photoappuser.dto.UserDto;

public interface UserService {

    UserDto createUser(UserDto userDto);

	UserDto getUserByUserId(Long userId);
}
