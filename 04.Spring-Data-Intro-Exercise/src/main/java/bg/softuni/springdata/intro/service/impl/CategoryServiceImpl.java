package bg.softuni.springdata.intro.service.impl;

import bg.softuni.springdata.intro.model.entity.Category;
import bg.softuni.springdata.intro.repository.CategoryRepository;
import bg.softuni.springdata.intro.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final String CATEGORIES_FILE = "src/main/resources/files/categories.txt";

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void seedCategories() throws IOException {
        if (this.categoryRepository.count() > 0) {
            return;
        }

        Files.readAllLines(Path.of(CATEGORIES_FILE))
                .forEach(row -> {
                    if (!row.isEmpty()) {
                        Category category = new Category(row);
                        this.categoryRepository.save(category);
                    }
                });

    }

    @Override
    public Set<Category> getRandomCategories() {
        int randCount = ThreadLocalRandom
                .current()
                .nextInt(1, 4);

        Set<Category> categories = new HashSet<>();
        long categoriesCnt = this.categoryRepository.count();

        for (int i = 0; i < randCount; i++) {
            long categoryId = ThreadLocalRandom
                    .current()
                    .nextLong(1, categoriesCnt + 1);

            Category category = this.categoryRepository.findCategoryById(categoryId);
            categories.add(category);
        }

        return categories;
    }
}
