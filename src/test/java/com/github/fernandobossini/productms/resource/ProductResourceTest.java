package com.github.fernandobossini.productms.resource;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fernandobossini.productms.model.Product;
import com.github.fernandobossini.productms.repository.filter.ProductFilter;
import com.github.fernandobossini.productms.service.ProductService;
import com.github.fernandobossini.productms.service.exception.ProductNotFound;
import com.github.fernandobossini.productms.utility.Constants;

@WebMvcTest(controllers = ProductResource.class)
@AutoConfigureMockMvc
@WithMockUser(username = Constants.USER, password = Constants.PASSWORD, roles = Constants.ROLES)
public class ProductResourceTest {
	
	private final String PRODUCT_API = "/products";
	
	@Autowired
    MockMvc mvc;

	@MockBean
	private ProductService productService;	

	@Autowired
	ModelMapper modelMapper;

	@Test
	@DisplayName("Deve retornar HTTP 201 quando criar um produto.")
	public void deveRetornarHTTP201_QuandoCriarUmProduto() throws Exception {		
		Product productSaved = getProductSaved();

		BDDMockito
			.given(productService.save(Mockito.any(Product.class)))
			.willReturn(productSaved);

		String json = new ObjectMapper().writeValueAsString(getProductWithoutId());

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
			.post(PRODUCT_API)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.content(json);

		mvc
			.perform(request)
			.andExpect(status().isCreated())
			.andExpect(jsonPath(ProductConstants.ID).value(productSaved.getId()))
			.andExpect(jsonPath(ProductConstants.NAME).value(productSaved.getName()))
			.andExpect(jsonPath(ProductConstants.DESCRIPTION).value(productSaved.getDescription()))
			.andExpect(jsonPath(ProductConstants.PRICE).value(productSaved.getPrice()));
	}	
	
	@Test
	@DisplayName("Deve retornar HTTP 400 quando houver erro de validação ao criar um produto.")
	public void deveRetornarHTTP400_QuandoHouverErroValidacaoAoCriarUmProduto() throws Exception {		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
			.post(PRODUCT_API)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.content(""); 

		mvc
			.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("status_code").value(HttpStatus.BAD_REQUEST.value()));
	}
	
	@Test
	@DisplayName("Deve retornar HTTP 200 quando atualizar um produto.")
	public void deveRetornarHTTP200_QuandoAtualizarUmProduto() throws Exception {		
		Long id = 1L;
		
		Product produtoAtualizando =  new Product();
		produtoAtualizando.setId(id);
		produtoAtualizando.setDescription("Cadeira de madeira");
		produtoAtualizando.setName("Cadeira");
		produtoAtualizando.setPrice(new BigDecimal(90));
		
		Product produtoAtualizar = new Product();
		produtoAtualizar.setId(id);
		produtoAtualizar.setDescription("Cadeira estofada");
		produtoAtualizar.setName("Cadeira");
		produtoAtualizar.setPrice(new BigDecimal(100));

		String json = new ObjectMapper().writeValueAsString(produtoAtualizando);		
		
		BDDMockito
			.given(productService.update(id, produtoAtualizando))
			.willReturn(produtoAtualizar);		
	        
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
			.put(PRODUCT_API.concat("/" + id))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.content(json);

		mvc
			.perform(request)
			.andExpect(status().isOk())
			.andExpect(jsonPath(ProductConstants.ID).value(id))
			.andExpect(jsonPath(ProductConstants.NAME).value(produtoAtualizar.getName()))
			.andExpect(jsonPath(ProductConstants.DESCRIPTION).value(produtoAtualizar.getDescription()))
			.andExpect(jsonPath(ProductConstants.PRICE).value(produtoAtualizar.getPrice()));
	}	
	
	@Test
	@DisplayName("Deve retornar HTTP 404 quando tentar atualizar um produto não localizado.")
	public void deveRetornarHTTP404_QuandoTentarAtualizarUmProdutoNaoLocalizado() throws Exception {		
		Long id = 1L;

		String json = new ObjectMapper().writeValueAsString(getProductWithoutId());		
		
		BDDMockito
			.given(productService.update(id, getProductWithoutId()))
			.willThrow(new ProductNotFound("Produto não encontrado"));		
	        
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
			.put(PRODUCT_API.concat("/" + id))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.content(json);

		mvc
			.perform(request)
			.andExpect(status().isNotFound());
	}	
		
	@Test
	@DisplayName("Deve retornar HTTP 200 quando buscar um produto existente.")
	public void deveRetornarHTTP200_QuandoBuscarUmProdutoExistente() throws Exception {
		Long id = 1L;		
		Product product = getProductSaved();
		
		BDDMockito
			.given(productService.findById(id))
			.willReturn(product);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
			.get(PRODUCT_API.concat("/" + id))
			.accept(MediaType.APPLICATION_JSON);

		mvc
			.perform(request)
			.andExpect(status().isOk())
			.andExpect(jsonPath(ProductConstants.ID).value(id))
			.andExpect(jsonPath(ProductConstants.NAME).value(product.getName()))
			.andExpect(jsonPath(ProductConstants.DESCRIPTION).value(product.getDescription()))
			.andExpect(jsonPath(ProductConstants.PRICE).value(product.getPrice()));
	}	
	
	@Test
	@DisplayName("Deve retornar HTTP 404 quando buscar um produto não localizado.")
	public void deveRetornarHTTP404_QuandoBuscarUmProdutoNaoLocalizado() throws Exception {
		Long id = 1L;		
		
		BDDMockito
			.given(productService.findById(id))
			.willThrow(new ProductNotFound("Produto não encontrado"));

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
			.get(PRODUCT_API.concat("/" + id))
			.accept(MediaType.APPLICATION_JSON);

		mvc
			.perform(request)
			.andExpect(status().isNotFound());		
	}
	
	@Test
	@DisplayName("Deve retornar HTTP 200 quando buscar todos os produtos.")
	public void deveRetornarHTTP200_QuandoBuscarTodosOsProdutos() throws Exception {		
		Product product = getProductSaved();		
		
		BDDMockito
			.given(productService.findAll())
			.willReturn(List.of(product));

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
			.get(PRODUCT_API)
			.accept(MediaType.APPLICATION_JSON);

		mvc
			.perform(request)
			.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("Deve retornar HTTP 200 quando buscar produtos filtrados por nome, descrição e preço.")
	public void deveRetornarHTTP200_QuandoBuscarProdutosFiltrados() throws Exception {
		Product product = getProductSaved();

		BDDMockito
			.given(productService.search(Mockito.any(ProductFilter.class)))
			.willReturn(List.of(product));

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(PRODUCT_API.concat("/search"))
				.param("q", "Cadeira")
				.param("min_price", "1.0")
				.param("max_price", "400.0")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		mvc
		.perform(request)
		.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("Deve retornar HTTP 200 quando deletar um produto.")
	public void deveRetornarHTTP200_QuandoDeletarUmProduto() throws Exception {
		Long id = 1L;				

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
			.delete(PRODUCT_API.concat("/" + id))
			.accept(MediaType.APPLICATION_JSON);

		mvc
			.perform(request)
			.andExpect(status().isOk());
	}
	
	private Product getProductSaved() {
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
	
	public class ProductConstants {
		public static final String ID = "id";
		public static final String NAME = "name";
		public static final String DESCRIPTION = "description";
		public static final String PRICE = "price";
	}
}
