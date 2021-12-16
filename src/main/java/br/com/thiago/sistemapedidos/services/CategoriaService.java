package br.com.thiago.sistemapedidos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.thiago.sistemapedidos.domain.Categoria;
import br.com.thiago.sistemapedidos.dto.CategoriaDTO;
import br.com.thiago.sistemapedidos.repositories.CategoriaRepository;
import br.com.thiago.sistemapedidos.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	

	public Categoria findById(Integer id){
		
		Optional<Categoria> obj = categoriaRepository.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
		
	}


	public Categoria insert(Categoria obj) {
		obj.setId(null);
		return categoriaRepository.save(obj);
	}


	public Categoria update(Categoria obj) {
		Categoria newObj = findById(obj.getId());
		updateData(newObj, obj);
		return categoriaRepository.save(newObj);
	}



	public void delete(Integer id) {
		
		findById(id);
		try {
			
			categoriaRepository.deleteById(id);
			
		} catch (DataIntegrityViolationException e) {
			
			throw new DataIntegrityViolationException("Não é possivel excluir a categoria "+ id+"-"+findById(id).getNome()+" pois ela possui produtos.");
		}
		
		
	}


	public List<Categoria> findAll() {
		
		return categoriaRepository.findAll();
	}
	
	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage,Direction.valueOf(direction), orderBy);
		return categoriaRepository.findAll(pageRequest);
	}
	
	public Categoria fromDTO(CategoriaDTO objDTO) {
		return new Categoria(objDTO.getId(), objDTO.getNome());
	}
	
private void updateData(Categoria newObj, Categoria obj) {
		
		newObj.setNome(obj.getNome());
	}
}
