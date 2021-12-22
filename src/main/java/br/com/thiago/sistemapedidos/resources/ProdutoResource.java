package br.com.thiago.sistemapedidos.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.thiago.sistemapedidos.domain.Produto;
import br.com.thiago.sistemapedidos.dto.ProdutoDTO;
import br.com.thiago.sistemapedidos.resources.utils.URL;
import br.com.thiago.sistemapedidos.services.ProdutoService;

@RestController
@RequestMapping(value = "/produtos")
public class ProdutoResource {
	
	@Autowired
	private ProdutoService produtoService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Produto> findById(@PathVariable Integer id){
		
		Produto obj = produtoService.findById(id);
		
		return ResponseEntity.ok().body(obj);		
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Page<ProdutoDTO>> findPage(
			@RequestParam(value = "nome", defaultValue = "") String nome,
			@RequestParam(value = "categorias", defaultValue = "") String categorias, 
			@RequestParam(value = "page", defaultValue = "0") Integer page, 
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage, 
			@RequestParam(value = "orderBy", defaultValue = "nome") String orderBy, 
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {
		
		String nomeDecoded = URL.decodeParam(nome);
		List<Integer> ids = URL.decodeIntList(categorias);
		Page<Produto> list = produtoService.search(nomeDecoded,ids,page,linesPerPage,orderBy,direction);
		Page<ProdutoDTO> listDTO = list.map(obj -> new ProdutoDTO(obj));
		
		return ResponseEntity.ok().body(listDTO);
	}
	
}
