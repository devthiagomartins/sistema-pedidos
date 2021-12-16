package br.com.thiago.sistemapedidos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.thiago.sistemapedidos.domain.Cidade;
import br.com.thiago.sistemapedidos.domain.Cliente;
import br.com.thiago.sistemapedidos.domain.Endereco;
import br.com.thiago.sistemapedidos.domain.enums.TipoCliente;
import br.com.thiago.sistemapedidos.dto.ClienteDTO;
import br.com.thiago.sistemapedidos.dto.ClienteNewDTO;
import br.com.thiago.sistemapedidos.repositories.ClienteRepository;
import br.com.thiago.sistemapedidos.repositories.EnderecoRepository;
import br.com.thiago.sistemapedidos.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	public Cliente findById(Integer id) {
		Optional<Cliente> obj = clienteRepository.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}
	
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = clienteRepository.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos());
		return obj;
	}

	
	public Cliente update(Cliente obj) {
		Cliente newObj = findById(obj.getId());
		updateData(newObj, obj);
		return clienteRepository.save(newObj);
	}


	public void delete(Integer id) {
		
		findById(id);
		try {
			
			clienteRepository.deleteById(id);
			
		} catch (DataIntegrityViolationException e) {
			
			throw new DataIntegrityViolationException("Não é possivel excluir a cliente "+ id+"-"+findById(id).getNome()+" pois há Pedidos relacionados.");
		}
		
		
	}


	public List<Cliente> findAll() {
		
		return clienteRepository.findAll();
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage,Direction.valueOf(direction), orderBy);
		return clienteRepository.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO objDTO) {
		return new Cliente(objDTO.getId(), objDTO.getNome(),objDTO.getEmail(),null,null);
	}
	

	public Cliente fromDTO(ClienteNewDTO objDTO) {
		Cliente cliente = new Cliente(null,objDTO.getNome(), objDTO.getEmail(),objDTO.getCpfOuCnpj() , TipoCliente.toEnum(objDTO.getTipoCliente()));
		Cidade cidade = new Cidade(objDTO.getCidadeId(), null, null);
		Endereco endereco = new Endereco(null, objDTO.getLogradouro(), objDTO.getNumero(), objDTO.getComplemento(), objDTO.getBairro(), objDTO.getCep(), cliente, cidade);
		cliente.getEnderecos().add(endereco);
		cliente.getTelefones().add(objDTO.getTelefone1());
		if(objDTO.getTelefone2() != null) {
			cliente.getTelefones().add(objDTO.getTelefone2());
		}
		if(objDTO.getTelefone3() != null) {
			cliente.getTelefones().add(objDTO.getTelefone3());
		}
	
		return cliente;
	}
	
	
	
	private void updateData(Cliente newObj, Cliente obj) {
		
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
}
