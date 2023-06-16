package team.mosk.api.server.domain.category.model.persist;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long>, CustomCategoryRepository {
    List<Category> findByStoreId(Long storeId);
}
