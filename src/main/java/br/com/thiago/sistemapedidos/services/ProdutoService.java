package br.com.thiago.sistemapedidos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.thiago.sistemapedidos.domain.Categoria;
import br.com.thiago.sistemapedidos.domain.Produto;
import br.com.thiago.sistemapedidos.repositories.CategoriaRepository;
import br.com.thiago.sistemapedidos.repositories.ProdutoRepository;
import br.com.thiago.sistemapedidos.services.exceptions.ObjectNotFoundException;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	public Produto findById(Integer id) {
		Optional<Produto> obj = produtoRepository.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Produto.class.getName()));
		
	}
	
	
	public Page<Produto> search(String nome, List<Integer> ids ,Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage,Direction.valueOf(direction), orderBy);
		List<Categoria> categorias = categoriaRepository.findAllById(ids);
		return produtoRepository.findDistinctByNomeContainingAndCategoriasIn(nome, categorias, pageRequest);
		
	}
	
}
