package ru.practicum.main.service.adminService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.dto.NewUserRequest;
import ru.practicum.main.dto.UserDto;
import ru.practicum.main.dto.UserMapper;
import ru.practicum.main.exception.BadRequestException;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.model.User;
import ru.practicum.main.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserService implements IAdminUserService {
    private static final int MAX_USERNAME_LENGTH = 63;
    private static final int MAX_DOMAIN_NAME_LENGTH = 63;
    private static final int MAX_EMAIL_LENGTH = 254;
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAll(List<Long> ids, Integer from, Integer size) {
        Page<User> users;
        if (ids == null) {
            users = userRepository.findAll(PageRequest.of(from / size, size));
        } else {
            users = userRepository.findAllByIdIn(ids, PageRequest.of(from / size, size));
        }

        return users.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto save(NewUserRequest newUserRequest) {
        String[] emailPart = newUserRequest.getEmail().split("@");
        if (emailPart[0].length() > MAX_USERNAME_LENGTH)
            throw new BadRequestException("Name length must be less then 64");
        String[] domainPart = emailPart[1].split("\\.");
        if (emailPart[1].length() > MAX_DOMAIN_NAME_LENGTH
                && newUserRequest.getEmail().length() != MAX_EMAIL_LENGTH)
            throw new BadRequestException("Domain length must be less then 64");

        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(newUserRequest)));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Пользователь с id " + id + " не найден"));

        userRepository.deleteById(id);
    }
}
