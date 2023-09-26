package ru.practicum.main.service.pub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.main.dto.CategoryDto;
import ru.practicum.main.dto.CategoryMapper;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PublicCategoryService {
    private CategoryRepository categoryRepository;

    public PublicCategoryService(
            CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDto> getAll(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return categoryRepository.findAll(pageable).stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    public CategoryDto getById(Long catId) {
        return CategoryMapper.toCategoryDto(categoryRepository.findById(catId).orElseThrow(
                () -> new NotFoundException("Category was not found")));
    }
}
