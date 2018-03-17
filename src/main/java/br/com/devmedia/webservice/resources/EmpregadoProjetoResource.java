package br.com.devmedia.webservice.resources;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import br.com.devmedia.webservice.service.RlProjetoEmpregadoService;

public class EmpregadoProjetoResource {

	private RlProjetoEmpregadoService rlProjetoEmpregadoService = new RlProjetoEmpregadoService();
	
	@POST
	@Path("{projetoId}")
	public Response save(@PathParam("projetoId") Long projetoId, @PathParam("empregadoId") Long empregadoId) {
		rlProjetoEmpregadoService.saveRelationshipProjetoEmpregado(projetoId, empregadoId);
		return Response.noContent().build();
	}
}
