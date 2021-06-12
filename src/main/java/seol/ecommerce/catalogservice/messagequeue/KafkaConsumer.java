package seol.ecommerce.catalogservice.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seol.ecommerce.catalogservice.jpa.CatalogEntity;
import seol.ecommerce.catalogservice.jpa.CatalogRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumer {

	private final CatalogRepository catalogRepository;

	@KafkaListener(topics = "example-catalog-topic") // example-catalog-topic의 메시지를 읽어들인다.
	@Transactional
	public void updateQty(String kafkaMessage) {
		log.info("Kafka Message: {}", kafkaMessage);

		Map<Object, Object> map = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {
			});
		} catch (JsonProcessingException e) {
			log.error("ObjectMapper readValue Error", e);
		}

		Optional<CatalogEntity> findEntity = catalogRepository.findByProductId((String) map.get("productId"));
		if (findEntity.isEmpty()) {
			// Exception
			return;
		}
		CatalogEntity catalogEntity = findEntity.get();
		catalogEntity.setStock(catalogEntity.getStock() - (Integer) map.get("qty")); // 현재고 = 기존재고 - 주문재고
		catalogRepository.save(catalogEntity);
	}

}
