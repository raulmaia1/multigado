package br.com.multigado.bean;

import java.util.ArrayList;
import java.util.List;

public class FuncionarioVendaBean {
	private FuncionarioBean tecnico;
	
	private List<OrdemServicoBean> ordens = new ArrayList<>();

	public FuncionarioBean getTecnico() {
		return tecnico;
	}

	public void setTecnico(FuncionarioBean tecnico) {
		this.tecnico = tecnico;
	}

	public List<OrdemServicoBean> getOrdens() {
		return ordens;
	}

	public void setOrdens(List<OrdemServicoBean> ordens) {
		this.ordens = ordens;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tecnico == null) ? 0 : tecnico.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FuncionarioVendaBean other = (FuncionarioVendaBean) obj;
		if (tecnico == null) {
			if (other.tecnico != null)
				return false;
		} else if (!tecnico.equals(other.tecnico))
			return false;
		return true;
	}
	
	
	
}
