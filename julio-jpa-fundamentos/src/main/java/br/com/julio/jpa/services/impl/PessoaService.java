package br.com.julio.jpa.services.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;

import br.com.julio.jpa.models.Pessoa;
import br.com.julio.jpa.services.interfaces.PessoaBuscaPorNome;
import br.com.julio.jpa.utils.JpaUtils;

public class PessoaService implements PessoaBuscaPorNome {

	@Override
	public List<Pessoa> all() {
		List<Pessoa> pessoas = new ArrayList<Pessoa>();
		EntityManager em = null;
		try {
			em = JpaUtils.getEntityManager();
			pessoas = em.createQuery("from Pessoa", Pessoa.class).getResultList();//select via JPQL
			return pessoas;
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}
	//pesquisar pessoa
	@Override
	public Pessoa byId(Integer id) {
		EntityManager em = null;
		try {
			em = JpaUtils.getEntityManager();
			return em.find(Pessoa.class, id);
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}
	//inserir pessoa no banco de dados
	@Override
	public Pessoa insert(Pessoa entity) {
		EntityManager em = null;
		try {
			em = JpaUtils.getEntityManager();
			em.getTransaction().begin();
			em.persist(entity);
			em.getTransaction().commit();
			return entity;
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}
	//atualizar dados de pessoa
	@Override
	public Pessoa update(Pessoa entity) {
		EntityManager em = null;
		try {
			em = JpaUtils.getEntityManager();
			em.getTransaction().begin();
			// em.merge(entity); - metodo especifico da JPA
			em.unwrap(Session.class).update(entity);
			em.getTransaction().commit();
			return entity;
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	@Override
	public void delete(Pessoa entity) {
		EntityManager em = null;
		try {
			em = JpaUtils.getEntityManager();
			em.getTransaction().begin();
			em.remove(entity);
			em.getTransaction().commit();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	@Override
	public void deleteByKey(Integer id) {
		EntityManager em = null;
		try {
			em = JpaUtils.getEntityManager();
			Pessoa pessoaASerDeletada = em.find(Pessoa.class, id);//pesquisa a pessoa para ser deletada
			if (pessoaASerDeletada != null) {
				em.getTransaction().begin();
				em.remove(pessoaASerDeletada);
				em.getTransaction().commit();
			}
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	@Override
	public List<Pessoa> searchByName(String name) {
		EntityManager em = null;
		try {
			em = JpaUtils.getEntityManager();
			//mais usual no mercado
//			List<Pessoa> pessoas = em
//					.createQuery("from Pessoa p where lower(p.nome) like lower(concat('%', :nome, '%'))", Pessoa.class)
//					.setParameter("nome", name).getResultList(); 
			//API Criteria recomendação conforme a JPA 2.0
			CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
			CriteriaQuery<Pessoa> buscaPorNomeCriteria = criteriaBuilder.createQuery(Pessoa.class);
			Root<Pessoa> raiz = buscaPorNomeCriteria.from(Pessoa.class);
			buscaPorNomeCriteria.where(
					criteriaBuilder.like(criteriaBuilder.lower(raiz.get("nome")), "%" + name.toLowerCase() + "%"));
			List<Pessoa> pessoas = em.createQuery(buscaPorNomeCriteria).getResultList();
			return pessoas;
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

}
