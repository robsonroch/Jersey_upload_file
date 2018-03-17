package br.com.devmedia.webservice.model.dao;

import javax.persistence.EntityManager;

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
		
		Projeto projeto = em.find(Projeto.class, projetoId);
		Empregado empregado = em.find(Empregado.class, empregadoId);
		
		if(empregado == null) {
			throw new NullPointerException();
		}
		
		em.getTransaction().begin();
		projeto.getEmpregados.add(empregado);
	}
}
