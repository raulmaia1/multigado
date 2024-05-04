package br.com.multigado.view;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.multigado.bean.OrdemServicoBean;
import br.com.multigado.dao.MovimentoMultigadoProducaoDAOSQLServerJTDS;
import br.com.multigado.dao.OrdemServicoDAOSQLServerJTDS;

@ViewScoped
@ManagedBean
public class DashBoardOrdemAbertasView {
	
	private List<OrdemServicoBean> ordemServicoAbertas = new ArrayList<>();
	
	
	@PostConstruct
	public void filtraOrdemAbertas() {
		ordemServicoAbertas.clear();
		 new OrdemServicoDAOSQLServerJTDS().ordemServicoAbertasEmProducao().ifPresent(lista ->{
			 this.ordemServicoAbertas.addAll(lista);
		 });
	}
	
	public List<OrdemServicoBean> getOrdemServicoAbertas() {
		return ordemServicoAbertas;
	}
	
	public void finalizarProducao(OrdemServicoBean ordem) {
		new OrdemServicoDAOSQLServerJTDS().alteraSituacaoParaExpedicao(ordem);
		new MovimentoMultigadoProducaoDAOSQLServerJTDS().adicionaProducao(ordem);
		filtraOrdemAbertas();
	}
	
}
