package br.com.multigado.view;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.multigado.bean.ExpedicaoBean;
import br.com.multigado.dao.MovimentoMultigadoProducaoDAOSQLServerJTDS;
import br.com.multigado.dao.OrdemServicoDAOSQLServerJTDS;

@ViewScoped
@ManagedBean
public class ExpedicaoView {
	
	private List<ExpedicaoBean> expedicaoBeans = new ArrayList<>();
	
	@PostConstruct
	public void filtraExpedicoesAbertas() {
		 expedicaoBeans.clear();
		 expedicaoBeans.addAll(new MovimentoMultigadoProducaoDAOSQLServerJTDS().expedicoesAbertas());
	}
	
	public List<ExpedicaoBean> getExpedicaoBeans() {
		return expedicaoBeans;
	}
	
	public void finalizarCarregamento(ExpedicaoBean expedicaoBean) {
		
		new OrdemServicoDAOSQLServerJTDS().finalizarOrdemServico(expedicaoBean);
		new MovimentoMultigadoProducaoDAOSQLServerJTDS().finalizarCarregamento(expedicaoBean);
		this.filtraExpedicoesAbertas();
	}
	
}
