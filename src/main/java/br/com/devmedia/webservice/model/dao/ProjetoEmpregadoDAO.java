package br.com.devmedia.webservice.model.dao;

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
