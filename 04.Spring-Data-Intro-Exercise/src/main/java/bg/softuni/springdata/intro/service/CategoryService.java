package bg.softuni.springdata.intro.service;

import bg.softuni.springdata.intro.model.entity.Category;

import java.io.IOException;
import java.util.Set;

public interface CategoryService {

    void seedCategories() throws IOException;

    Set<Category> getRandomCategories();
}
