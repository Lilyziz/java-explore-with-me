package ru.practicum.main.controller.adminController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.NewUserRequest;
import ru.practicum.main.dto.UserDto;
import ru.practicum.main.service.admin.AdminUserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/users")
@AllArgsConstructor
public class AdminUserController {
    private final AdminUserService adminUserService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto save(@Valid @RequestBody NewUserRequest newUserRequest) {
        log.debug("Add user: {}", newUserRequest);
        return adminUserService.save(newUserRequest);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        log.debug("Delete user with id {}", id);
        adminUserService.delete(id);
    }

    @GetMapping
    public List<UserDto> getAll(@RequestParam(required = false) List<Long> ids,
                                @RequestParam(defaultValue = "0") int from,
                                @RequestParam(defaultValue = "10") int size) {

        log.debug("Get all users with ids: {}", ids);
        return adminUserService.getAll(ids, from, size);
    }
}
