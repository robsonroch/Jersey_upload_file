package br.com.devmedia.webservice.service;

import java.util.List;

import br.com.devmedia.webservice.model.dao.ProjetoEmpregadoDAO;
import br.com.devmedia.webservice.model.domain.Empregado;
import br.com.devmedia.webservice.model.domain.Projeto;

public class rlProjetoEmpregadoService {
	private ProjetoEmpregadoDAO dao = new ProjetoEmpregadoDAO();
	
	public void saveRelationshipProjetoEmpregado(Long projetoId, Long empregadoId) {
		dao.saveRelationship(projetoId, empregadoId);
	}
	
	public List<Empregado> getEmpregados(Long projetoId){
		return dao.getEmpregados(projetoId);
	}
	
	public List<Projeto> getProjetos(Long empregadoId){
		return dao.getProjetos(empregadoId);
	}
	
	public void deleteRelationshiProjetoEmpregado(Long projetoId, Long empregadoId) {
		dao.deleteRelationship(projetoId, empregadoId);
	}
}
