package br.com.cotiinformatica.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cotiinformatica.dtos.ProdutoRequestDto;
import br.com.cotiinformatica.dtos.ProdutoResponseDto;
import br.com.cotiinformatica.entities.Categoria;
import br.com.cotiinformatica.entities.Produto;
import br.com.cotiinformatica.repositories.ProdutoRepository;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

	@Autowired
	private ModelMapper modelMapper;

	@PostMapping
	public ProdutoResponseDto post(@RequestBody ProdutoRequestDto request) throws Exception {

		// Copiando os dados da class REQUEST para a entidade
		Produto produto = modelMapper.map(request, Produto.class);

		// Relacioando o produto com a categoria
		produto.setCategoria(new Categoria());
		produto.getCategoria().setId(request.getCategoriaId());

		// Gravando o produto no banco de dados
		ProdutoRepository produtoRepository = new ProdutoRepository();
		produtoRepository.create(produto);

		// Consultando os dados do produto cadastrado atraves do ID
		Produto produtoCadastrado = produtoRepository.findById(produto.getId());

		// Retornar os dados do produto cadastrado no banco de dados
		return modelMapper.map(produtoCadastrado, ProdutoResponseDto.class);

	}

	@PutMapping("{id}")

	public ProdutoResponseDto put(@PathVariable Integer id, @RequestBody ProdutoRequestDto request) throws Exception {

		// Copiando os dados da classe REQUEST para a entidade
		Produto produto = modelMapper.map(request, Produto.class);

		// Caturando o id do produto e o id da categoria
		produto.setId(id);
		produto.setCategoria(new Categoria());
		produto.getCategoria().setId(request.getCategoriaId());

		// Atualizando o produto no banco de daods
		ProdutoRepository produtoRepository = new ProdutoRepository();
		produtoRepository.update(produto);

		// Consultando o produto no banco de dados atravves do ID
		Produto produtoAtualizado = produtoRepository.findById(id);

		// Retornar os dados do produto atualizado no banco de dados
		return modelMapper.map(produtoAtualizado, ProdutoResponseDto.class);

	}

	@DeleteMapping("{id}")
	public ProdutoResponseDto delete(@PathVariable ("id") Integer id) throws Exception {

		// Consultando o produto no banco de dados atraves do ID
		ProdutoRepository produtoRepository = new ProdutoRepository();
		Produto produto = produtoRepository.findById(id);

		// Excluindo o produto no banco de dados
		produtoRepository.delete(id);

		// Retornar os dados do produto atualizado no banco de dados
		return modelMapper.map(produto, ProdutoResponseDto.class);

	}

	@GetMapping
	public List<ProdutoResponseDto> getAll() throws Exception {

		// Consultando os produtos no banco de dados
		ProdutoRepository produtoRepository = new ProdutoRepository();
		List<Produto> produtos = produtoRepository.findAll();

		// Copiando os dados dos produtos para uma lista do DTO
		List<ProdutoResponseDto> response = produtos.stream()
				.map(produto -> modelMapper.map(produto, ProdutoResponseDto.class)).collect(Collectors.toList());

		return response;
	}

	@GetMapping("{id}")
	public ProdutoResponseDto getById(@PathVariable Integer id) throws Exception {

		ProdutoRepository produtoRepository = new ProdutoRepository();
		Produto produto = produtoRepository.findById(id);

		return modelMapper.map(produto, ProdutoResponseDto.class);

	}

}
