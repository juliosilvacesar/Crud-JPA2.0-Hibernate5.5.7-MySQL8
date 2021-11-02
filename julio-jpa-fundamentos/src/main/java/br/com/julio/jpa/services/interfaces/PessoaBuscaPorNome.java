package br.com.julio.jpa.services.interfaces;

import java.util.List;

import br.com.julio.jpa.models.Pessoa;

public interface PessoaBuscaPorNome extends CrudService<Pessoa, Integer> {

	List<Pessoa> searchByName(String name);

}
