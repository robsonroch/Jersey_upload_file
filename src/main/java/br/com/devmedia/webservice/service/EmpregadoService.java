package br.com.devmedia.webservice.service;

import java.util.List;

import br.com.devmedia.webservice.model.dao.EmpregadoDAO;
import br.com.devmedia.webservice.model.domain.Empregado;

public class EmpregadoService {

	private EmpregadoDAO dao = new EmpregadoDAO();

	public List<Empregado> getEmpregados() {
		return dao.getAll();
	}

	public Empregado getEmpregado(Long id) {
		return dao.getById(id);
	}

	public Empregado saveEmpregado(Empregado empregado) {
		return dao.save(empregado);
	}

	public Empregado updateEmpregado(Empregado empregado) {
		return dao.update(empregado);
	}

	public Empregado deleteEmpregado(Long id) {
		return dao.delete(id);
	}

	public List<Empregado> getEmpregadosByPagination(int firstResult, int maxResults) {
		return dao.getByPagination(firstResult, maxResults);
	}

	public List<Empregado> getEmpregadosByName(String name) {
		return dao.getByName(name);
	}
}
