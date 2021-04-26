package seol.ecommerce.catalogservice.controller;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import seol.ecommerce.catalogservice.jpa.CatalogEntity;
import seol.ecommerce.catalogservice.service.CatalogService;
import seol.ecommerce.catalogservice.vo.ResponseCatalog;

@RequiredArgsConstructor
@RestController
@RequestMapping("/catalog-service")
public class CatalogController {

	private final Environment environment;
	private final ModelMapper mapper;
	private final CatalogService catalogService;

	@GetMapping("/health_check")
	public String status() {
		return String.format("It's working in Catalog Service on PORT %s", environment.getProperty("local.server.port"));
	}

	@GetMapping("/catalogs")
	public ResponseEntity<List<ResponseCatalog>> getCatalogs() {
		Iterable<CatalogEntity> userList = catalogService.getAllCatalogs();

		List<ResponseCatalog> responseUsers = new ArrayList<>();
		userList.forEach(v -> {
			responseUsers.add(mapper.map(v, ResponseCatalog.class));
		});

		return ResponseEntity
				.status(HttpStatus.OK)
				.body(responseUsers);
	}

}
