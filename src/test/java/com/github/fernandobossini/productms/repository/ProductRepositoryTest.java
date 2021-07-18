package com.github.fernandobossini.productms.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.github.fernandobossini.productms.model.Product;
import com.github.fernandobossini.productms.repository.filter.ProductFilter;

@DataJpaTest
public class ProductRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private ProductRepository repository;

	@Test
	@DisplayName("Deve salvar um produto.")
	public void deveSalvar() {
		Product product = getProduct();
		Product productSave = repository.save(product);
		assertThat(productSave.getId()).isNotNull();
	}

	@Test
	@DisplayName("Deve deletar um produto.")
	public void deveDeletar() {
		Product product = getProduct();	

		entityManager.persist(product);

		Product productFind = entityManager.find(Product.class, product.getId());

		repository.delete(productFind);

		Product productDeleted = entityManager.find(Product.class, product.getId());
		assertThat(productDeleted).isNull();
	}

	@Test
	@DisplayName("Deve buscar um produto pelo id.")
	public void deveBuscarPorId() {
		Product product = getProduct();	

		entityManager.persist(product);

		Optional<Product> productFind = repository.findById(product.getId());

		assertThat(productFind.isPresent()).isTrue();
	}

	@Test
	@DisplayName("Deve buscar todos os produtos.")
	public void deveBuscarTodos() {
		Product product = getProduct();	

		entityManager.persist(product);

		List<Product> products = repository.findAll();

		assertThat(products).hasSize(1);
		assertThat(products).contains(product);
	}
	
	@Test
	@DisplayName("Deve buscar todos os produtos por nome.")
	public void deveBuscarTodosPorNome() {
		Product product = getProduct();	
		
		ProductFilter productFilter = new ProductFilter();
		productFilter.setQ(product.getName());

		entityManager.persist(product);

		List<Product> products = repository.search(productFilter);

		assertThat(products).hasSize(1);
		assertThat(products).contains(product);
	}

	private Product getProduct() {
		Product product = new Product();	
		product.setName("Cadeira");
		product.setDescription("Produto de cor vermelha");
		product.setPrice(new BigDecimal(10));		
		return product;
	}
}
