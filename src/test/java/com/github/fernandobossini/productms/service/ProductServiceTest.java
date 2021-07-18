package com.github.fernandobossini.productms.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.github.fernandobossini.productms.model.Product;
import com.github.fernandobossini.productms.repository.ProductRepository;
import com.github.fernandobossini.productms.service.exception.ProductNotFound;

@SpringBootTest
public class ProductServiceTest {	

	@InjectMocks
	private ProductService service;

	@Mock
	private ProductRepository repository;

	@Test
	@DisplayName("Deve salvar um produto.")
	public void deveSalvar() {
		Product product = getProductRepository();

		doReturn(product)
		.when(repository)
		.save(any());

		Product productSaved = service.save(product);

		assertThat(productSaved.getId()).isNotNull();
		assertThat(productSaved.getName()).isEqualTo(product.getName());
		assertThat(productSaved.getDescription()).isEqualTo(product.getDescription());
	}

	@Test
	@DisplayName("Deve ocorrer erro ao salvar um produto sem informações.")
	public void deveOcorrerErroAoSalvar() {
		Product product = new Product();		

		doReturn(product)
		.when(repository)
		.save(any());

		Product productSaved = service.save(product);

		assertThat(productSaved.getId()).isNull();
	}

	@Test
	@DisplayName("Deve atualizar um produto.")
	public void deveAtualizar() {
		Product product = getProductRepository();

		Product newProduct = getProductRepository();
		newProduct.setDescription("Descrição alterada");

		given(repository.findById(product.getId()))
		.willReturn(Optional.of(product));

		service.update(product.getId(), newProduct);

		verify(repository).save(newProduct);
		verify(repository).findById(product.getId());
	}

	@Test
	@DisplayName("Deve buscar todos os produtos")
	void deveBuscarTodos() {
		List<Product> products = new ArrayList<>();
		products.add(getProductRepository());

		doReturn(products)
		.when(repository)
		.findAll();

		List<Product> expected = service.findAll();

		assertEquals(expected, products);
		verify(repository).findAll();
	}

	@Test
	@DisplayName("Deve buscar por id")
	void deveBuscarPorId() {
		Product product = getProductRepository();

		when(repository.findById(product.getId()))
		.thenReturn(Optional.of(product));

		Product expected = service.findById(product.getId());

		assertThat(expected).isSameAs(product);
		verify(repository).findById(product.getId());
	}

	@Test
	@DisplayName("Deve deletar um produto.")
	public void deveDeletar() {
		Product product = getProductRepository();		

		when(repository.findById(product.getId()))
		.thenReturn(Optional.of(product));

		service.delete(product.getId());
		verify(repository).delete(product);
	}

	@Test
	@DisplayName("Deve ocorrer erro ao tentar deletar um produto inexistente.")
	public void deveOcorrerErroAoDeletar() {
		Long id = 1L;
		assertThrows(ProductNotFound.class, () -> service.delete(id));
		verify(repository, never()).deleteById(id);
	}

	private Product getProductRepository() {
		Product product = getProductWithoutId();	
		product.setId(1L);				
		return product;
	}

	private Product getProductWithoutId() {
		Product product = new Product();	
		product.setName("Teclado");
		product.setDescription("Teclado de cor preta");
		product.setPrice(new BigDecimal(10));		
		return product;
	}

}
