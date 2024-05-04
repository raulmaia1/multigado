package br.com.multigado.view;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@ViewScoped
public class FiscalView {
	private String numero =  "";
	
	
	public void consultaNota() {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("numero", numero);
	}
		
	public String getNumero() {
		return numero;
	}
	
	public void setNumero(String numero) {
		this.numero = numero;
	}
}
