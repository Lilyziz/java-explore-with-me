package ru.practicum.main.service.admin;

import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
@Transactional
public class AdminUserService {
    public static final int MAX_USERNAME_LENGTH = 63;
    public static final int MAX_DOMAIN_NAME_LENGTH = 63;
    public static final int MAX_EMAIL_LENGTH = 254;
    private final UserRepository userRepository;

    public AdminUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserDto save(NewUserRequest newUserRequest) {
        String[] emailPart = newUserRequest.getEmail().split("@");
        if (emailPart[0].length() > MAX_USERNAME_LENGTH)
            throw new BadRequestException("Name length must be less then 64");
        String[] domainPart = emailPart[1].split("\\.");
        if (emailPart[1].length() > MAX_DOMAIN_NAME_LENGTH
                && newUserRequest.getEmail().length() != MAX_EMAIL_LENGTH)
            throw new BadRequestException("Domain length must be less then 63");

        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(newUserRequest)));
    }

    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Пользователь с id " + id + " не найден"));

        userRepository.delete(user);
    }

    public List<UserDto> getAll(List<Long> ids, Integer from, Integer size) {
        Page<User> users;
        if (ids == null) {
            users = userRepository.findAll(PageRequest.of(from / size, size));
        } else {
            users = userRepository.findAllByIdIn(ids, PageRequest.of(from / size, size));
        }

        return users.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }
}
