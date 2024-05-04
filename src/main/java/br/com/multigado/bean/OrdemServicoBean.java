package br.com.multigado.bean;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.tools.ant.util.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import br.com.multigado.util.DataUtil;

public class OrdemServicoBean  {
	private String situcaoOs = "Verificacao", tipoOS = "Serviço", obs, placa, chassis, cor, problemasRelatados ="", detalhe1, detalhe2, detalhe3, detalhe4, detalhe5, detalhe6, detalhe7,
			nomeProdutoServico = "", numeroOS;
	
	private List<FormulaBean> formulas = new ArrayList<>();
	
	
	public List<FormulaBean> getFormulas() {
		return formulas;
	}
	
	public BigDecimal getQuantKilos() {
		return new BigDecimal(detalhe5);
	}
	
	public String getDetalhe3() {
		return detalhe3;
	}

	public void setDetalhe3(String detalhe3) {
		this.detalhe3 = detalhe3;
	}

	public String getDetalhe4() {
		return detalhe4;
	}

	public void setDetalhe4(String detalhe4) {
		this.detalhe4 = detalhe4;
	}

	public String getDetalhe5() {
		return detalhe5;
	}

	public void setDetalhe5(String detalhe5) {
		this.detalhe5 = detalhe5;
	}

	public String getDetalhe6() {
		return detalhe6;
	}

	public void setDetalhe6(String detalhe6) {
		this.detalhe6 = detalhe6;
	}

	public String getDetalhe7() {
		return detalhe7;
	}

	public void setDetalhe7(String detalhe7) {
		this.detalhe7 = detalhe7;
	}

	public String getDetalhe2() {
		return detalhe2;
	}

	public void setDetalhe2(String detalhe2) {
		this.detalhe2 = detalhe2;
	}

	private ProdutoBean produtoBean;

	private ClienteBean clienteBean;

	private Integer ordem,ordemMovimento;

	private String dataGravacao;

	private boolean isFechada;
	

	private List<ProdutoVendaBean> listProdutoServico = new ArrayList<>();

	public void geraDataGravacao() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		this.dataGravacao = simpleDateFormat.format(Calendar.getInstance().getTime());
	}

	public String getDuracaoOS() {

		return DataUtil.pegarDuracao(getDataGravacao());
	}
	
	/*
	 * Getters and Setters
	 */

	public String getProblemasRelatados() {
		return problemasRelatados;
	}

	public void setNumeroOS(String numeroOS) {
		this.numeroOS = numeroOS;
	}DateTimeFormatter forPattern = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");

	public String getNumeroOS() {
		return numeroOS;
	}

	@Override
	public String toString() {
		return "OrdemServicoBean [os=" + numeroOS + ", placa=" + placa + ", chassis=" + chassis + ", cor=" + cor
				+ ", detalhe1=" + detalhe1 + ", nomeProdutoServico=" + nomeProdutoServico + ", isFechada=" + isFechada
				+ "]";
	}

	public void setProblemasRelatados(String problemasRelatados) {
		this.problemasRelatados = problemasRelatados;
	}

	public String getDetalhe1() {
		return detalhe1;
	}

	public void setDetalhe1(String detalhe1) {
		this.detalhe1 = detalhe1;
	}

	public String getSitucaoOs() {
		return situcaoOs;
	}

	public void setSitucaoOs(String situcaoOs) {
		this.situcaoOs = situcaoOs;
	}

	public String getTipoOS() {
		return tipoOS;
	}

	public void setTipoOS(String tipoOS) {
		this.tipoOS = tipoOS;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getChassis() {
		return chassis;
	}

	public void setChassis(String chassis) {
		this.chassis = chassis;
	}

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}

	public ProdutoBean getProdutoBean() {
		return produtoBean;
	}

	public void setProdutoBean(ProdutoBean produtoBean) {
		this.produtoBean = produtoBean;
	}

	public ClienteBean getClienteBean() {
		return clienteBean;
	}

	public void setClienteBean(ClienteBean clienteBean) {
		this.clienteBean = clienteBean;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setNomeProdutoServico(String nomeProdutoServico) {
		this.nomeProdutoServico = nomeProdutoServico;
	}

	public String getNomeProdutoServico() {
		return nomeProdutoServico;
	}

	public void setDataGravacao(String dataGravacao) {
		this.dataGravacao = dataGravacao;
	}

	public void setOSFechada(boolean isFechada) {
		this.isFechada = isFechada;
	}

	public String getDataGravacao() {
		return dataGravacao;
	}
	
	public String getDataGravacaoBR() {
		return DataUtil.getDataGravacaoBR(dataGravacao);
	}
	
	public boolean isFechada() {
		return isFechada;
	}
	
	public void setOrdemMovimento(Integer ordemMovimento) {
		this.ordemMovimento = ordemMovimento;
	}
	
	public Integer getOrdemMovimento() {
		return ordemMovimento;
	}

	public void adicionaProdutoServicoOrdem(ProdutoVendaBean p) {
		listProdutoServico.add(p);
	}
	

	
	public List<ProdutoVendaBean> getListProdutoServico() {
		return listProdutoServico;
	}
	
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrdemServicoBean other = (OrdemServicoBean) obj;
		if (numeroOS == null) {
			if (other.numeroOS != null)
				return false;
		} else if (!numeroOS.equals(other.numeroOS))
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numeroOS == null) ? 0 : numeroOS.hashCode());
		return result;
	}

}
