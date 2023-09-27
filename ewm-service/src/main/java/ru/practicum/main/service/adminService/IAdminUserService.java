package ru.practicum.main.service.adminService;

import ru.practicum.main.dto.NewUserRequest;
import ru.practicum.main.dto.UserDto;

import java.util.List;

public interface IAdminUserService {
    List<UserDto> getAll(List<Long> ids, Integer from, Integer size);
    UserDto save(NewUserRequest newUserRequest);
    void delete(Long id);
}
