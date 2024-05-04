package br.com.multigado.view;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.com.multigado.bean.DocumentosFiscais;
import br.com.multigado.dao.DocumentoFiscaisDaoSQLServerJTDS;

@ManagedBean
@ViewScoped
public class NFCEView {
	
	private DocumentosFiscais nfce;

	@PostConstruct
	private void init() {
		
		String numero = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("numero");

		 nfce = new DocumentoFiscaisDaoSQLServerJTDS().carregarNFCE(numero,"65");
		 System.out.println(nfce.getQrcode());
	}
	
	public DocumentosFiscais getNfce() {
		return nfce;
	}
	
	public String getQuantItens() {
		return String.valueOf(nfce.getListProdutos().size());
	}
}
