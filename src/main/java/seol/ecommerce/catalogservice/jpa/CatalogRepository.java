package seol.ecommerce.catalogservice.jpa;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface CatalogRepository extends CrudRepository<CatalogEntity, Long> {

	Optional<CatalogEntity> findByProductId(String productId);

}



