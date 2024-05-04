package br.com.multigado.bean;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ProdutoBean {
//	ok
	private String descricaoProduto, codigoBarra,codigo = "0", nomeTabela, codigoAdicionalUm, codigoAdicionalDois, obs;
	private BigDecimal precoUnitario = new BigDecimal(0.0);
	private Integer ordem;
		
	public String getObs() {
		return obs;
	}
	
	public void setObs(String obs) {
		this.obs = obs;
	}
	
	public String getNomeTabela() {
		return nomeTabela;
	}
	
	public String getCodigoAdicionalDois() {
		return codigoAdicionalDois;
	}
	
	public String getCodigoAdicionalUm() {
		return codigoAdicionalUm;
	}
	
	public Integer getOrdem() {
		return ordem;
	}
	
	public void setDescricaoProduto(String descricaoProduto) {
		this.descricaoProduto = descricaoProduto;
	}
	
	public String getDescricaoProduto() {
		return descricaoProduto;
	}

	public String getCodigoBarra() {
		return codigoBarra;
	}

	public void setCodigoBarra(String codigoBarra) {
		this.codigoBarra = codigoBarra;
	}

	public String getPrecoUnitario() {
		return precoUnitario.setScale(3, RoundingMode.HALF_EVEN).toString().replace(".", ",");
	}

	public String getCodigo() {
		return codigo;
	}
	
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public void setPrecoUnitario(String precoUnitario) {
		this.precoUnitario = new BigDecimal(precoUnitario.replace(",", "."));
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public void setNomeTabela(String nomeTabela) {
		this.nomeTabela = nomeTabela;
	}

	public void setCodigoAdicionalUm(String codigoAdicionalUm) {
		this.codigoAdicionalUm = codigoAdicionalUm;
	}

	public void setCodigoAdicionalDois(String codigoAdicionalDois) {
		this.codigoAdicionalDois = codigoAdicionalDois;
	}

	@Override
	public String toString() {
		return "ProdutoBean [descricaoProduto=" + descricaoProduto + ", codigoBarra=" + codigoBarra + ", codigo="
				+ codigo + ", nomeTabela=" + nomeTabela + ", codigoAdicionalUm=" + codigoAdicionalUm
				+ ", codigoAdicionalDois=" + codigoAdicionalDois + ", precoUnitario=" + precoUnitario + ", ordem="
				+ ordem + "]";
	}
	
	
	
	
}
