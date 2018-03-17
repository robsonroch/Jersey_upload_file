package br.com.devmedia.webservice.model.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import br.com.devmedia.webservice.exceptions.DAOException;
import br.com.devmedia.webservice.exceptions.ErrorCode;
import br.com.devmedia.webservice.model.domain.Empregado;
import br.com.devmedia.webservice.model.domain.Projeto;

public class ProjetoEmpregadoDAO {

	public void saveRelationship(Long projetoId, Long empregadoId) {
		if(projetoId <=0 || empregadoId <=0) {
			throw new DAOException("O id do projeto e o id do empregado precisam ser maiores que zero,",  ErrorCode.BAD_REQUEST.getCode());
		}
		
		EntityManager em = JPAUtil.getEntityManager();
		
		if(relationshiExist(projetoId, empregadoId, em)) {
			throw new DAOException("Relacionamento solicitado para criação já existe.", ErrorCode.CONFLICT.getCode());
		}
		
		try {
			Projeto projeto = em.find(Projeto.class, projetoId);
			Empregado empregado = em.find(Empregado.class, empregadoId);
			
			if(empregado == null) {
				throw new NullPointerException();
			}
			
			em.getTransaction().begin();
			projeto.getEmpregados().add(empregado);
			em.persist(projeto);
			em.getTransaction().commit();
		} catch (NullPointerException e) {
			em.getTransaction().rollback();
			throw new DAOException("Id do projeto ou do empregado não existe.", ErrorCode.NOT_FOUND.getCode());
		}catch (RuntimeException e) {
			em.getTransaction().rollback();
			throw new DAOException("Erro ao salvar relacionamento entre projeto e empregado no banco de dados:" + e.getMessage(), ErrorCode.SERVER_ERROR.getCode());
		}finally {
			em.close();
		}
	}

	public List<Empregado> getEmpregados(Long projetoId){
		if(projetoId<=0) {
			throw new DAOException("O id do projeto precisa ser maior do que 0.", ErrorCode.BAD_REQUEST.getCode());	
		}
		
		EntityManager em = JPAUtil.getEntityManager();
		
		try {
			return em
					.createQuery("Select e from Empregado e JOIN e.projetos p where p.id = :projetoId", Empregado.class)
					.setParameter("projetoId", projetoId)
					.getResultList();
		} catch (RuntimeException e) {
			throw new DAOException("Erro ao recuperar os empregados do projeto de id " + projetoId + " do banco: " + e.getMessage(), ErrorCode.SERVER_ERROR.getCode());
		} finally {
			em.close();
		}
	}
	
	public List<Projeto> getProjetos(Long empregadoId){
		if(empregadoId<=0) {
			throw new DAOException("O id do projeto precisa ser maior do que 0.", ErrorCode.BAD_REQUEST.getCode());	
		}
		
		EntityManager em = JPAUtil.getEntityManager();
		
		try {
			return em
					.createQuery("Select p from Projeto p JOIN p.empregados d where e.id = :empregadoId", Projeto.class)
					.setParameter("empregadoId", empregadoId)
					.getResultList();
		} catch (RuntimeException e) {
			throw new DAOException("Erro ao recuperar os projetos do empregado de id " + empregadoId + " do banco: " + e.getMessage(), ErrorCode.SERVER_ERROR.getCode());
		} finally {
			em.close();
		}
	}

	public void deleteRelationship(Long projetoId, Long empregadoId) {
		if(projetoId <= 0 || empregadoId <=0) {
			throw new DAOException("O id do projeto e do id do empregado precisam ser maiores que 0.", ErrorCode.BAD_REQUEST.getCode());
		}
		
		EntityManager em = JPAUtil.getEntityManager();
		
		if(!relationshiExist(projetoId, empregadoId, em)){
			throw new DAOException("Relacionamento informado para remoção não existe.", ErrorCode.NOT_FOUND.getCode());
		}
		
		try {
			Projeto projeto = em.find(Projeto.class, projetoId);
			Empregado empregado = em.find(Empregado.class, empregadoId);
			em.getTransaction().begin();
			projeto.getEmpregados().remove(empregado);
			em.persist(projeto);
			em.getTransaction().commit();
		} catch (RuntimeException e) {
			throw new DAOException("Erro ao remover relacionamento entre projeto e empregado no banco de dados: " + e.getMessage(), ErrorCode.SERVER_ERROR.getCode());
		} finally {
			em.close();
		}
	}
	
	private boolean relationshiExist(Long projetoId, Long empregadoId, EntityManager em) {

		try {
			em.createQuery(
					"select e from Empregado e join e.projetos p where p.id = :projetoId and e.id = :empregadoId")
					.setParameter("projetoId", projetoId).setParameter("empregadoId", empregadoId)
					.getSingleResult();
		} catch (NoResultException e) {
			return false;
		}
		return true;
	}
}
