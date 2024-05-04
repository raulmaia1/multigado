package br.com.multigado.bean;

import java.util.ArrayList;
import java.util.List;

public class DocumentosFiscais {
	private List<ProdutoVendaBean> listProdutos = new ArrayList<>();
	private String cpf, nome, xmlDocumento, chave = "";
	private String descontoTotal;
	private String formaPagamento;
	private String troco = "0", valorTotalTributacao, numeroNota, serie,qrcode;
	private String protocoloAutorizacao;
	private String dataAutorizacao;
	private String dataEmissao;
	private String totalNfe;
	
	public String getQrcode() {
		return qrcode;
	}
	
	public void setQrcode(String qrcode) {
		this.qrcode = qrcode.replace("<qrCode>", "").replace("</qrCode>", "");
	}
	
	public String getSerie() {
		return serie;
	}
	
	public void setSerie(String serie) {
		this.serie = serie.replace("<serie>", "").replace("</serie>", "");
	}
	
	public String getNumeroNota() {
		return numeroNota;
	}
	
	
	public void setNumeroNota(String numeroNota) {
		this.numeroNota = numeroNota.replace("<nNF>", "").replace("</nNF>", "");
	}

	public String getXmlDocumento() {
		return xmlDocumento;
	}
	
	public void setXmlDocumento(String xmlDocumento) {
		this.xmlDocumento = xmlDocumento;
	}
	
	public List<ProdutoVendaBean> getListProdutos() {
		return listProdutos;
	}

	public void setDescontoTotal(String descontoTotal) {
		this.descontoTotal = descontoTotal;		
	}
	
	public String getDescontoTotal() {
		return descontoTotal.replace("<vDesc>", "").replace("</vDesc>", "");
	}

	public void setTipoPagamento(String formaPagamento) {
		this.formaPagamento = formaPagamento;		
	}
	
	public String getFormaPagamento() {
		return formaPagamento;
	}

	public void setTroco(String troco) {
		this.troco = troco;
	}
	
	public String getTroco() {
		return troco;
	}

	public void setValorTotalTributacao(String valorTotalTributacao) {
		this.valorTotalTributacao = valorTotalTributacao.replace("<vTotTrib>", "").replace("</vTotTrib>", "");
	}
	
	public String getValorTotalTributacao() {
		return valorTotalTributacao;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf.replace("<CPF>", "").replace("</CPF>", "");
	}
	
	public String getCpf() {
		return cpf;
	}

	public void setNome(String nome) {
		this.nome = nome.replace("<xNome>", "").replace("</xNome>", "");
	}
	
	public String getNome() {
		return nome;
	}

	public void setChaveAcesso(String chave) {
		int espaco = 0;
		for (int i = 1; i < chave.length(); i++) {
			
			if (i < ((chave.length()/2)+3) ) {
				this.chave = this.chave.concat(String.valueOf(chave.charAt(i)));
			} else if (i == ((chave.length()/2)+3)) {
				this.chave = this.chave.concat("\n                         ");
			}else {
				this.chave = this.chave.concat(String.valueOf(chave.charAt(i)));
			}
			espaco++;
			if(espaco == 4) {
				this.chave = this.chave.concat(" ");
				espaco = 0;
			}
		}
		
	}
	
	public String getChave() {
		return chave;
	}

	public void setProtocoloAutorizacao(String protocoloAutorizacao) {
		this.protocoloAutorizacao = protocoloAutorizacao;
	}
	
	public String getProtocoloAutorizacao() {
		return protocoloAutorizacao;
	}

	public void setDataAutorizacao(String dataAutorizacao) {
		this.dataAutorizacao = dataAutorizacao;
	}
	
	public String getDataAutorizacao() {
		return dataAutorizacao;
	}

	public void setDataEmissao(String dataEmissao) {
		this.dataEmissao = dataEmissao;		
	}
	
	public String getDataEmissao() {
		return dataEmissao;
	}

	public void setTotal(String totalNfe) {
		this.totalNfe = totalNfe.replace("<vPag>", "R$ ").replace("</vPag>", "");
	}
	
	public String getTotalNfe() {
		return totalNfe;
	}
	
}
