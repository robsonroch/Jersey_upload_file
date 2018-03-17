package br.com.devmedia.webservice.resources;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.devmedia.webservice.model.domain.Projeto;
import br.com.devmedia.webservice.service.RlProjetoEmpregadoService;

@Produces(MediaType.APPLICATION_JSON + ";charsert=utf-8")
public class EmpregadoProjetoResource {

	private RlProjetoEmpregadoService rlProjetoEmpregadoService = new RlProjetoEmpregadoService();
	
	@POST
	@Path("{projetoId}")
	public Response save(@PathParam("projetoId") Long projetoId, @PathParam("empregadoId") Long empregadoId) {
		rlProjetoEmpregadoService.saveRelationshipProjetoEmpregado(projetoId, empregadoId);
		return Response.noContent().build();
	}
	
	@GET
	public List<Projeto> getProjetos(@PathParam("empregadoId") Long empregadoId){
		return rlProjetoEmpregadoService.getProjetos(empregadoId);
	}
	
	@DELETE
	@Path("{projetoId}")
	public Response delete(@PathParam("projetoId") Long projetoId, @PathParam("empregadoId") Long empregadoId) {
		rlProjetoEmpregadoService.deleteRelationshiProjetoEmpregado(projetoId, empregadoId);
		return Response.noContent().build();
	}
}
