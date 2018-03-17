package br.com.devmedia.webservice.resources.beans;

import javax.ws.rs.QueryParam;

public class FilterBean {

	private @QueryParam("offset") int offset;
	private @QueryParam("limit") int limit;
	private @QueryParam("name") String nome;
	
	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setName(String nome) {
		this.nome = nome;
	}
	
}