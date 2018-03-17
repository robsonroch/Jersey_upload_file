package br.com.devmedia.webservice.resources;

import br.com.devmedia.webservice.model.domain.Empregado;
import br.com.devmedia.webservice.service.RlProjetoEmpregadoService;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Produces(MediaType.APPLICATION_JSON + ";charsert=utf-8")
public class ProjetoEmpregadoResource {

	private RlProjetoEmpregadoService rlProjetoEmpregadoService = new RlProjetoEmpregadoService();
	
	@POST
	@Path("{empregadoId}")
	public Response save(@PathParam("projetoId") Long projetoId, @PathParam("empregadoId") Long empregadoId) {
		rlProjetoEmpregadoService.saveRelationshipProjetoEmpregado(projetoId, empregadoId);
		return Response.noContent().build();
	}	
	
	@GET
	public List<Empregado> getEmpregados(@PathParam("projetoId") Long projetoId){
		return rlProjetoEmpregadoService.getEmpregados(projetoId);
	}
	
	@DELETE
	@Path("{empregadoId}")
	public Response delete(@PathParam("projetoId") Long projetoId, @PathParam("empregadoId") Long empregadoId) {
		rlProjetoEmpregadoService.deleteRelationshiProjetoEmpregado(projetoId, empregadoId);
		return Response.noContent().build();
	}
}
