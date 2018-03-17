package br.com.devmedia.webservice.resources;

import br.com.devmedia.webservice.service.RlProjetoEmpregadoService;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

public class ProjetoEmpregadoResource {

	private RlProjetoEmpregadoService rlProjetoEmpregadoService = new RlProjetoEmpregadoService();
	
	@POST
	@Path("{empregadoId}")
	public Response save(@PathParam("projetoId") Long projetoId, @PathParam("empregadoId") Long empregadoId) {
		rlProjetoEmpregadoService.saveRelationshipProjetoEmpregado(projetoId, empregadoId);
		return Response.noContent().build();
	}
	
	@GET
	public void testeSubRecurso() {
		System.out.println("Testando subrecurso");
	}
	
	
}
