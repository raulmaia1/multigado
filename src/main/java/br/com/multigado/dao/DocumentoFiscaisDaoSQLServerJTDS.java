package br.com.multigado.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.multigado.bean.DocumentosFiscais;
import br.com.multigado.bean.FormaPagamento;
import br.com.multigado.bean.ProdutoVendaBean;
import br.com.multigado.factory.S9_REALSQLServerFactory;

public class DocumentoFiscaisDaoSQLServerJTDS {

	private static final String SQL_SELECT_DOCUMENTOS_FISCAIS = "SELECT dF.XML_Documento, mP.Ordem_Prod_Serv, dF.Chave_Acesso, df.Protocolo_Autorizacao, df.Data_Autorizacao, df.Data_Emissao, mP.Ordem, dF.Ordem_Movimento, dF.Modelo, dF.Numero FROM Movimento_Documentos_Fiscais dF"
			+ " LEFT JOIN Movimento_Prod_Serv mP ON mP.Ordem_Movimento = dF.Ordem_Movimento "
			+ "where dF.Modelo = ? AND dF.Numero = ?";

	private static final String SQL_SELECT_LOTE = "SELECT pL.Lote From Movimento_Prod_Serv_Lote mL "
			+ " LEFT JOIN Prod_Serv_Lote pL ON pL.Ordem = mL.Ordem_Prod_Serv_Lote" + " where mL.Ordem_Movimento_Prod_Serv = ?";

	public DocumentosFiscais carregarNFCE(String numero, String modelo) {
		try {
			Optional<Connection> optional = S9_REALSQLServerFactory.getConection();
			if (optional.isPresent()) {
				PreparedStatement prepareStatement = optional.get().prepareStatement(SQL_SELECT_DOCUMENTOS_FISCAIS);
				prepareStatement.setString(1, modelo);
				prepareStatement.setString(2, numero);
				ResultSet resultSet = prepareStatement.executeQuery();

				if (resultSet.next()) {
					DocumentosFiscais documentosFiscais = new DocumentosFiscais();
					documentosFiscais.setXmlDocumento(resultSet.getString(1));
					documentosFiscais.getChave();
					documentosFiscais.getListProdutos().addAll(pegarProdutos(documentosFiscais.getXmlDocumento(),carregaLote(resultSet.getInt(2))));
					documentosFiscais.setDescontoTotal(separaTags(separaTags(documentosFiscais.getXmlDocumento(), "total"), "vDesc"));
					documentosFiscais.setTipoPagamento(new FormaPagamento().getFormaPagamento(separaTags(separaTags(separaTags(documentosFiscais.getXmlDocumento(),"pag" ),"detPag"),"tPag")));
					documentosFiscais.setTroco((separaTags(separaTags(documentosFiscais.getXmlDocumento(),"pag"),"vTroco")));
					documentosFiscais.setValorTotalTributacao(separaTags(separaTags(separaTags(documentosFiscais.getXmlDocumento(),"total"),"ICMSTot"),"vTotTrib"));					
					documentosFiscais.setNumeroNota(separaTags(separaTags(separaTags(documentosFiscais.getXmlDocumento(),"infNFe"),"ide"),"nNF"));					
					documentosFiscais.setSerie(separaTags(separaTags(separaTags(documentosFiscais.getXmlDocumento(),"infNFe"),"ide"),"serie"));
					documentosFiscais.setQrcode(separaTags(separaTags(documentosFiscais.getXmlDocumento(),"infNFeSupl"),"qrCode"));
					documentosFiscais.setCpf(separaTags(separaTags(documentosFiscais.getXmlDocumento(),"dest"),"CPF"));
					documentosFiscais.setNome(separaTags(separaTags(documentosFiscais.getXmlDocumento(),"dest"),"xNome"));
					documentosFiscais.setChaveAcesso(resultSet.getString(3));
					documentosFiscais.setProtocoloAutorizacao(resultSet.getString(4));
					documentosFiscais.setTotal(separaTags(separaTags(separaTags(documentosFiscais.getXmlDocumento(),"pag"),"detPag"),"vPag"));
					documentosFiscais.setDataAutorizacao(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(resultSet.getTimestamp(5)));
					documentosFiscais.setDataEmissao(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(resultSet.getTimestamp(6)));

					
					
					return documentosFiscais;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String carregaLote(Integer movimento) {
		Optional<Connection> optional = S9_REALSQLServerFactory.getConection();
		try {
			if (optional.isPresent()) {
				PreparedStatement prepareStatement = optional.get().prepareStatement(SQL_SELECT_LOTE);
				prepareStatement.setInt(1, movimento);
				ResultSet resultSet = prepareStatement.executeQuery();
				if(resultSet.next()) {
					return resultSet.getString(1);					
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	private List<ProdutoVendaBean> pegarProdutos(String xml, String lote) {
		List<ProdutoVendaBean> lista = new ArrayList<>();

		for (int i = 1; i < xml.length(); i++) {

			int beginIndex = xml.indexOf("<det nItem=\"" + String.valueOf(i));
			if (beginIndex != -1) {
				int endIndex = xml.indexOf("</det", beginIndex) + "det".length() + 3;
				String string = xml.substring(beginIndex, endIndex);

				new ProdutoDAOSQLServerJTDS().procuraProdutoPorID(Integer.valueOf(separaTagsProdutos("cProd", string)))
						.ifPresent(p -> {
							ProdutoVendaBean pV = new ProdutoVendaBean(p);
							pV.setQuantidade(new BigDecimal(separaTagsProdutos("qTrib", string)));
							pV.setDesconto(new BigDecimal(separaTagsProdutos("vDesc", string)));
							pV.setValorTotal(new BigDecimal(separaTagsProdutos("vProd", string)));
							pV.getProdutoBean().setPrecoUnitario(separaTagsProdutos("vUnTrib", string));
							pV.setLote(lote);
							
							lista.add(pV);
							System.out.println(pV);
						});

			} else {
				break;
			}
		}
		return lista;

	}
	
	public String separaTags(String xml, String tag) {
		int beginIndex = xml.indexOf("<"+tag);
		if (beginIndex != -1) {
			int endIndex = xml.indexOf("</"+tag, beginIndex) + tag.length() + 3;	
			return xml.substring(beginIndex, endIndex);
		}
		return "0";
	}

	
	
	public String separaTagsProdutos(String tag, String produto) {

		int inicio = produto.indexOf("<" + tag);
		int fim = produto.indexOf("</" + tag, inicio) + tag.length() + 3;

		String separa = produto.substring(inicio, fim);

		String string = separa.replace("<" + tag + ">", "").replace("</" + tag + ">", "");

		return string;
	}

	

}
