package br.com.multigado.view;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.multigado.bean.DocumentosFiscais;
import br.com.multigado.dao.DocumentoFiscaisDaoSQLServerJTDS;

@ManagedBean
@ViewScoped
public class NFView {
	
	private DocumentosFiscais nfe;

	@PostConstruct
	private void init() {
		 nfe = new DocumentoFiscaisDaoSQLServerJTDS().carregarNFCE("100","55");
	}
	
	public DocumentosFiscais getNfe() {
		return nfe;
	}
	
//	public String getQuantItens() {
//		return String.valueOf(nfe.getListProdutos().size());
//	}
}
