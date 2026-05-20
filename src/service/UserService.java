package service;

import dto.UserDto;

public interface UserService {
	void add(UserDto user);

    UserDto searchByEmail(String email);

    void update(UserDto user);

    void remove(int userId);
}
