package ru.practicum.main.service.admin_service;

import org.springframework.stereotype.Service;
import ru.practicum.main.dto.NewUserRequest;
import ru.practicum.main.dto.UserDto;

import java.util.List;

@Service
public interface IAdminUserService {
    List<UserDto> getAll(List<Long> ids, Integer from, Integer size);

    UserDto save(NewUserRequest newUserRequest);

    void delete(Long id);
}
