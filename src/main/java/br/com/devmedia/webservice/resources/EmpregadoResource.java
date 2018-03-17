package br.com.devmedia.webservice.resources;

import java.util.List;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.com.devmedia.webservice.model.domain.Empregado;
import br.com.devmedia.webservice.resources.beans.FilterBean;
import br.com.devmedia.webservice.service.EmpregadoService;

@Path("/empregados")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class EmpregadoResource {

    private EmpregadoService service = new EmpregadoService();

    @GET
    public List<Empregado> getEmpregados(@BeanParam FilterBean empregadoFilter) {
        if ((empregadoFilter.getOffset() >= 0) && (empregadoFilter.getLimit() > 0)) {
            return service.getEmpregadosByPagination(empregadoFilter.getOffset(), empregadoFilter.getLimit());
        }
        if (empregadoFilter.getNome() != null) {
            return service.getEmpregadosByName(empregadoFilter.getNome());
        }

        return service.getEmpregados();
    }

    @GET
    @Path("{empregadoId}")
    public Empregado getEmpregado(@PathParam("empregadoId") long empregadoId) {
        return service.getEmpregado(empregadoId);
    }

    @DELETE
    @Path("{empregadoId}")
    public Response delete(@PathParam("empregadoId") long id) {
        service.deleteEmpregado(id);
        return Response.noContent().build();
    }

    @POST
    public Response save(Empregado empregado) {
        empregado = service.saveEmpregado(empregado);
        return Response.status(Status.CREATED)
                .entity(empregado)
                .build();
    }

    @PUT
    @Path("{empregadoId}")
    public Response update(@PathParam("empregadoId") long id, Empregado empregado) {
        empregado.setId(id);
        service.updateEmpregado(empregado);
        return Response.noContent().build();
    }
    
    @Path("{projetoId}")
    public EmpregadoProjetoResource getEmpregadoProjetoResource() {
    	return new EmpregadoProjetoResource();
    }	

}
