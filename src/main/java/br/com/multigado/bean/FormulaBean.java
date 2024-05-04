package br.com.multigado.bean;

import java.math.BigDecimal;

public class FormulaBean {
	private ProdutoBean produto, materialPrima;
	private String tipo;
	private BigDecimal valor;
	private Integer ordem;
	
	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
	
	public Integer getOrdem() {
		return ordem;
	}

	public ProdutoBean getProduto() {
		return produto;
	}

	public void setProduto(ProdutoBean produto) {
		this.produto = produto;
	}

	public ProdutoBean getMaterialPrima() {
		return materialPrima;
	}

	public void setMaterialPrima(ProdutoBean materialPrima) {
		this.materialPrima = materialPrima;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public String getTipo() {
		return tipo;
	}
	
	public BigDecimal getValor() {
		return valor;
	}
	
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
}
