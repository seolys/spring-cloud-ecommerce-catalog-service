package seol.ecommerce.catalogservice.service;

import seol.ecommerce.catalogservice.jpa.CatalogEntity;

public interface CatalogService {

	Iterable<CatalogEntity> getAllCatalogs();

}
