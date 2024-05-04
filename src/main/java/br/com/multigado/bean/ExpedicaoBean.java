package br.com.multigado.bean;

import br.com.multigado.dao.MovimentoMultigadoProducaoDAOSQLServerJTDS;
import br.com.multigado.util.DataUtil;

public class ExpedicaoBean {
	private OrdemServicoBean ordemServicoBean;
	
	private Integer Ordem, OrdemMovimentoOrdemServico;
	private String dataAberturaProducao = "0", dataConclusaoProducao = "0", dataConclusaoCarregamento, nomeSituacao;
	
	public String getDuracaoExpedicao() {
		return DataUtil.pegarDuracao(dataConclusaoProducao);
	}
	
	public String getNomeSituacao() {
		return nomeSituacao;
	}
	
	public void setNomeSituacao(String nomeSituacao) {
		this.nomeSituacao = nomeSituacao;
	}
	
	public OrdemServicoBean getOrdemServicoBean() {
		return ordemServicoBean;
	}
	public void setOrdemServicoBean(OrdemServicoBean ordemServicoBean) {
		this.ordemServicoBean = ordemServicoBean;
	}
	public Integer getOrdem() {
		return Ordem;
	}
	public void setOrdem(Integer ordem) {
		Ordem = ordem;
	}
	public Integer getOrdemMovimentoOrdemServico() {
		return OrdemMovimentoOrdemServico;
	}
	public void setOrdemMovimentoOrdemServico(Integer ordemMovimentoOrdemServico) {
		OrdemMovimentoOrdemServico = ordemMovimentoOrdemServico;
	}
	public String getDataAberturaProducao() {
		return dataAberturaProducao;
	}
	
	public String getDataAberturaProducaoBR() {
		return DataUtil.getDataGravacaoBR(dataAberturaProducao);
	}
	
	public void setDataAberturaProducao(String dataAberturaProducao) {
		this.dataAberturaProducao = dataAberturaProducao;
	}
	public String getDataConclusaoProducao() {
		return dataConclusaoProducao;
	}
	
	public String getDataConclusaoProducaoBR() {
		return DataUtil.getDataGravacaoBR(dataConclusaoProducao);
	}
	
	public void setDataConclusaoProducao(String dataConclusaoProducao) {
		this.dataConclusaoProducao = dataConclusaoProducao;
	}
	public String getDataConclusaoCarregamento() {
		return dataConclusaoCarregamento;
	}

	public void setDataConclusaoCarregamento(String dataConclusaoCarregamento) {
		this.dataConclusaoCarregamento = dataConclusaoCarregamento;
	}
		
	
	public static void main(String[] args) {
		new MovimentoMultigadoProducaoDAOSQLServerJTDS().expedicoesAbertas().forEach(exp ->{
			System.out.println(exp.getDuracaoExpedicao());
		});
	}
	
}
