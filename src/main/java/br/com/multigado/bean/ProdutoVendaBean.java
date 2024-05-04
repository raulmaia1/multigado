package br.com.multigado.bean;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ProdutoVendaBean {

	private ProdutoBean produtoBean;
	private BigDecimal quantidade = new BigDecimal(1);
	private FuncionarioBean tecnico;
	private String dataAlteracao,lote;
	private int ordem;
	private int numeroLinha;
	private int numeroOS;
	private boolean isConcluido;
	private BigDecimal desconto, valorTotal;
	
	public void setLote(String lote) {
		this.lote = lote;
	}
	
	public String getLote() {
		return lote;
	}
	
	public boolean isConcluido() {
		return isConcluido;
	}
	
	public void setConcluido(boolean isConcluido) {
		this.isConcluido = isConcluido;
	}
	
	public void setTecnico(FuncionarioBean tecnico) {
		this.tecnico = tecnico;
	}
	
	public FuncionarioBean getTecnico() {
		return tecnico;
	}
	
	public ProdutoVendaBean() {
	}
	
	public ProdutoVendaBean(ProdutoBean produtoBean) {
		this.produtoBean = produtoBean;
	}
	
	public ProdutoBean getProdutoBean() {
		return produtoBean;
	}
	
	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
		dataAlteracao = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
	}
	
	public String getCalcularValorTotal() {
		BigDecimal b = quantidade.multiply(new BigDecimal(produtoBean.getPrecoUnitario())).setScale(BigDecimal.ROUND_CEILING);
		return b.toString().replace(".", ",");
	}
	
	public String getQuantidade() {
		return quantidade.setScale(3, RoundingMode.HALF_EVEN).toString();
	}
	
	public void setDataAlteracao(String dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}
	
	public String getDataAlteracao() {
		return dataAlteracao;
	}
	
	@Override
	public String toString() {
		return produtoBean.getDescricaoProduto() + " " +  produtoBean.getPrecoUnitario() + " " + quantidade ;
	}

	public void setOrdem(int ordem) {
		this.ordem = ordem;
	}
	
	public int getOrdem() {
		return ordem;
	}

	public void setNumeroLinha(int numeroLinha) {
		this.numeroLinha = numeroLinha;
	}
	
	public int getNumeroLinha() {
		return numeroLinha;
	}

	public void setNumeroOS(int numeroOS) {
		this.numeroOS = numeroOS;		
	}
	
	public int getNumeroOS() {
		return numeroOS;
	}

	public void setProdutoBean(ProdutoBean produtoBean) {
		this.produtoBean = produtoBean;
		
	}
	
	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}
	
	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;		
	}
	
	public BigDecimal getDesconto() {
		return desconto;
	}
}
