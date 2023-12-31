package ru.practicum.main.service.admin_service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.dto.CategoryDto;
import ru.practicum.main.dto.CategoryMapper;
import ru.practicum.main.dto.NewCategoryDto;
import ru.practicum.main.exception.ConflictException;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.model.Category;
import ru.practicum.main.repository.CategoryRepository;
import ru.practicum.main.repository.EventRepository;

@Service
@RequiredArgsConstructor
public class AdminCategoryService implements IAdminCategoryService {
    private final CategoryRepository categoryRepository;
    private final AdminEventService eventService;

    @Override
    @Transactional
    public CategoryDto save(NewCategoryDto newCategoryDto) {
        Category category = categoryRepository.save(CategoryMapper.toCategory(newCategoryDto));

        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    @Transactional
    public CategoryDto update(Long id, NewCategoryDto newCategoryDto) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Category with id = " + id + " was not found"));

        if (newCategoryDto.getName() != null) {
            category.setName(newCategoryDto.getName());
        }
        categoryRepository.save(category);

        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        categoryRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Category with id = " + id + " was not found"));
        if (eventService.getFirstByCategoryId(id) != null) {
            throw new ConflictException("There is a conflict with category");
        }

        categoryRepository.deleteById(id);
    }
}
