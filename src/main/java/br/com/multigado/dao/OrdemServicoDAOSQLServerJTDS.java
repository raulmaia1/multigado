package br.com.multigado.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.joda.time.DateTime;

import br.com.multigado.bean.ExpedicaoBean;
import br.com.multigado.bean.OrdemServicoBean;
import br.com.multigado.factory.S9_REALSQLServerFactory;

public class OrdemServicoDAOSQLServerJTDS {

	private static final String SQL_SELECT_ORDEM_SERVICO = "SELECT TOP(100) o.Ordem, o.Nome_Prod_Serv, o.Tipo_Geral, o.Detalhe_1, o.Detalhe_2, o.Detalhe_3, o.Detalhe_4, o.Detalhe_5, o.Detalhe_6, o.Detalhe_7, o.Problema_Relatado_1, o.Data_Gravacao, o.Fechada, Numero, o.Ordem_Movimento, o.Ordem_Prod_Serv FROM Movimento_Ordem_Servico o "
			+ "LEFT JOIN Movimento movimento on movimento.Ordem = o.Ordem_Movimento "
			+ "WHERE o.Fechada = ? AND o.Cancelada != 1 AND movimento.Ordem_Filial = 1;";

	private static final String SQL_SELECT_ORDEM_SERVICO_SITUACAO = "SELECT TOP(100) o.Ordem, o.Nome_Prod_Serv, o.Tipo_Geral, o.Detalhe_1, o.Detalhe_2, o.Detalhe_3, o.Detalhe_4, o.Detalhe_5, o.Detalhe_6, o.Detalhe_7, o.Problema_Relatado_1, o.Data_Gravacao, o.Fechada, Numero, o.Ordem_Movimento, o.Ordem_Prod_Serv FROM Movimento_Ordem_Servico o "
			+ "LEFT JOIN Movimento movimento on movimento.Ordem = o.Ordem_Movimento "
			+ "WHERE o.Fechada = ? AND o.Cancelada != 1 AND movimento.Ordem_Filial = 1 AND o.Ordem_Situacao_OS = ?;";
//	
	private static final String SQL_INSERT_ORDEM = " INSERT Movimento_Ordem_Servico (Ordem_Movimento," + " Numero,"
			+ " Ordem_Funcionario_Aberta_Por," + " Ordem_Funcionario_Ultima_Alterecao," + " Nome_Prod_Serv,"
			+ " Tipo_Geral, " + " Ordem_Tipo_OS," + " Ordem_Situacao_OS," + " Detalhe_1, " + " Detalhe_2, "
			+ " Detalhe_3," + " Detalhe_4," + " Detalhe_5," + " Problema_Relatado_1," + " Data_Gravacao, "
			+ " Fechada, Cancelada) " + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

	private static final String SQL_UPDATE_SITUACAO = "UPDATE Movimento_Ordem_Servico SET Ordem_Situacao_OS = ? WHERE Ordem = ?";

	private static final String SQL_ID_MOVIMENTO = "SELECT Ordem FROM Movimento WHERE Ordem = (SELECT MAX(Ordem) FROM Movimento);";
	private static final String SQL_NUMERO_OS = "SELECT Numero FROM Movimento_Ordem_Servico WHERE Numero = (SELECT MAX(Numero) FROM Movimento_Ordem_Servico);";

	private static final String SQL_UPDATE_FECHA_OS = "UPDATE Movimento_Ordem_Servico SET Ordem_Situacao_OS = ?, Fechada = 1, Fechada_Data = ? WHERE Ordem = ?";

	private static final String SQL_SELECT_ORDEM_SERVICO_POR_DATA = "SELECT o.Ordem, o.Nome_Prod_Serv, o.Tipo_Geral, o.Detalhe_1, o.Detalhe_2, o.Detalhe_3, o.Detalhe_4, o.Detalhe_5, o.Detalhe_6, o.Detalhe_7, o.Problema_Relatado_1, o.Data_Gravacao, o.Fechada, Numero, o.Ordem_Movimento, o.Ordem_Prod_Serv FROM Movimento_Ordem_Servico o "
			+ "LEFT JOIN Movimento movimento on movimento.Ordem = o.Ordem_Movimento where o.Data_Gravacao >= ? AND o.Data_Gravacao <= ?;";

	public Optional<List<OrdemServicoBean>> ordemServicoAbertas() {
		return getOrdens(0);
	}

	public Optional<List<OrdemServicoBean>> ordemServicoFechadas() {
		return getOrdens(1);
	}

	private Optional<List<OrdemServicoBean>> getOrdens(int fechada) {
		List<OrdemServicoBean> list = new ArrayList<>();
		try {
			Optional<Connection> optional = S9_REALSQLServerFactory.getConection();
			if (optional.isPresent()) {
				PreparedStatement prepareStatement = optional.get().prepareStatement(SQL_SELECT_ORDEM_SERVICO);
				prepareStatement.setInt(1, fechada);
				ResultSet resultSet = prepareStatement.executeQuery();

				while (resultSet.next()) {
					OrdemServicoBean ordem = new OrdemServicoBean();

					ordem.setOrdem(resultSet.getInt(1));
					ordem.setNomeProdutoServico(resultSet.getString(2));
					ordem.setTipoOS(resultSet.getString(3));
					ordem.setDetalhe1(resultSet.getString(4));
					ordem.setDetalhe2(resultSet.getString(5));
					ordem.setDetalhe3(resultSet.getString(6));
					ordem.setDetalhe4(resultSet.getString(7));
					ordem.setDetalhe5(resultSet.getString(8));
					ordem.setDetalhe6(resultSet.getString(9));
					ordem.setDetalhe7(resultSet.getString(10));

					ordem.setProblemasRelatados(resultSet.getString(11));
					ordem.setDataGravacao(
							new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(resultSet.getTimestamp(12)));
					ordem.setOSFechada(resultSet.getBoolean(13));
					ordem.setNumeroOS(String.valueOf(resultSet.getInt(14)));
					ordem.setOrdemMovimento(resultSet.getInt(15));

					new MovimentoDAOSQLServerJTDS().procuraClientePorIDDoMovimento(resultSet.getInt(15))
							.ifPresent(c -> {
								ordem.setClienteBean(c);
							});
					new ProdutoDAOSQLServerJTDS().procuraProdutoPorID(resultSet.getInt(16)).ifPresent(produtoBean -> {
						ordem.setProdutoBean(produtoBean);
					});
					ordem.getListProdutoServico().addAll(
							new MovimentoProdutoServicoSQLServerJTDSDAO().pesquisaProdutosPorIDMovimento(ordem));
					list.add(ordem);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Optional.ofNullable(list);
	}

	public int adicionaOrdem(OrdemServicoBean ordem) {
//		ordem.setOrdem(this.getOrdemID(SQL_ID_MOVIMENTO_OS));

		new MovimentoDAOSQLServerJTDS().adicionaMovimento(ordem);

		ordem.setNumeroOS(String.valueOf(this.getOrdemID(SQL_NUMERO_OS) + 1));
		ordem.setOrdemMovimento(this.getOrdemID(SQL_ID_MOVIMENTO));

		try {
			Optional<Connection> optional = S9_REALSQLServerFactory.getConection();
			if (optional.isPresent()) {
				PreparedStatement smt = optional.get().prepareStatement(SQL_INSERT_ORDEM);
				smt.setInt(1, ordem.getOrdemMovimento());// tabela Movimento
				smt.setInt(2, Integer.valueOf(ordem.getNumeroOS()));// produto
				smt.setInt(3, 3); // Junior
				smt.setInt(4, 3); // Junior
				smt.setString(5, "CARRO"); // Junior
				smt.setString(6, "D");
				smt.setInt(7, 4);
				smt.setInt(8, 3);
				smt.setString(9, ordem.getDetalhe1());
				smt.setString(10, ordem.getPlaca());
				smt.setString(11, ordem.getCor());
				smt.setString(12, ordem.getObs());
				smt.setString(13, ordem.getChassis());
				smt.setString(14, ordem.getProblemasRelatados());
				smt.setString(15, ordem.getDataGravacao());
				smt.setBoolean(16, ordem.isFechada());
				smt.setInt(17, 0);
				return smt.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	private int getOrdemID(String sql) {
		try {
			Optional<Connection> optional = S9_REALSQLServerFactory.getConection();
			if (optional.isPresent()) {
				PreparedStatement smt = optional.get().prepareStatement(sql);
				smt.executeQuery();
				smt.getResultSet().next();
				return smt.getResultSet().getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static void main(String[] args) {
		new OrdemServicoDAOSQLServerJTDS().ordemServicoAbertasEmProducao().ifPresent(c -> {

		});

		new ProdutoDAOSQLServerJTDS().procuraProdutoPorID(132).ifPresent(System.out::println);
	}

	public Optional<List<OrdemServicoBean>> ordemServicoAbertasEmProducao() {
		// 0 Os Fechada
		// 1 Situacao producao
		return getOrdensPorTipo(0, 1);
	}

	private Optional<List<OrdemServicoBean>> getOrdensPorTipo(int fechada, int idSituacao) {
		List<OrdemServicoBean> list = new ArrayList<>();
		try {
			Optional<Connection> optional = S9_REALSQLServerFactory.getConection();
			if (optional.isPresent()) {
				PreparedStatement prepareStatement = optional.get().prepareStatement(SQL_SELECT_ORDEM_SERVICO_SITUACAO);
				prepareStatement.setInt(1, fechada);
				prepareStatement.setInt(2, idSituacao);
				ResultSet resultSet = prepareStatement.executeQuery();

				while (resultSet.next()) {
					System.out.println(resultSet.getInt(16));
					OrdemServicoBean ordem = adiciona(resultSet);
					ordem.getFormulas()
							.addAll(new MovimentoMultigadoFormulas().carregaFormulaProdutos(ordem.getProdutoBean()));
					list.add(ordem);
				}
				optional.get().close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Optional.ofNullable(list);
	}

	public OrdemServicoBean adiciona(ResultSet resultSet) throws SQLException {
		OrdemServicoBean ordem = new OrdemServicoBean();

		ordem.setOrdem(resultSet.getInt(1));
		ordem.setNomeProdutoServico(resultSet.getString(2));
		ordem.setTipoOS(resultSet.getString(3));
		ordem.setDetalhe1(resultSet.getString(4));
		ordem.setDetalhe2(resultSet.getString(5));
		ordem.setDetalhe3(resultSet.getString(6));
		ordem.setDetalhe4(resultSet.getString(7));
		ordem.setDetalhe5(resultSet.getString(8));
		ordem.setDetalhe6(resultSet.getString(9));
		ordem.setDetalhe7(resultSet.getString(10));

		ordem.setProblemasRelatados(resultSet.getString(11));
		ordem.setDataGravacao(resultSet.getTimestamp(12).toString());
		ordem.setOSFechada(resultSet.getBoolean(13));
		ordem.setNumeroOS(String.valueOf(resultSet.getInt(14)));
		ordem.setOrdemMovimento(resultSet.getInt(15));

		new MovimentoDAOSQLServerJTDS().procuraClientePorIDDoMovimento(resultSet.getInt(15)).ifPresent(c -> {
			ordem.setClienteBean(c);
		});
		new ProdutoDAOSQLServerJTDS().procuraProdutoPorID(resultSet.getInt(16)).ifPresent(produtoBean -> {
			ordem.setProdutoBean(produtoBean);
		});
		ordem.getListProdutoServico()
				.addAll(new MovimentoProdutoServicoSQLServerJTDSDAO().pesquisaProdutosPorIDMovimento(ordem));
		return ordem;
	}

	public Optional<List<OrdemServicoBean>> ordemServicoAbertasParaExpedicao() {
		return getOrdensPorTipo(0, 1);
	}

	public void alteraSituacaoParaExpedicao(OrdemServicoBean ordem) {

		try {
			Optional<Connection> optional = S9_REALSQLServerFactory.getConection();
			if (optional.isPresent()) {
				PreparedStatement prepareStatement = optional.get().prepareStatement(SQL_UPDATE_SITUACAO);
				prepareStatement.setInt(1, 2);
				prepareStatement.setInt(2, ordem.getOrdem());
				prepareStatement.executeUpdate();
				optional.get().close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void finalizarOrdemServico(ExpedicaoBean expedicaoBean) {
		try {
			Optional<Connection> optional = S9_REALSQLServerFactory.getConection();
			if (optional.isPresent()) {
				PreparedStatement prepareStatement = optional.get().prepareStatement(SQL_UPDATE_FECHA_OS);
				prepareStatement.setInt(1, Integer.valueOf(expedicaoBean.getNomeSituacao()));
				prepareStatement.setDate(2, new java.sql.Date(new Date().getTime()));
				prepareStatement.setInt(3, expedicaoBean.getOrdemServicoBean().getOrdem());
				prepareStatement.executeUpdate();
				optional.get().close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<OrdemServicoBean> carregaQuantidadeDeProducoesPorData(DateTime dataInicio, DateTime dataFinal) {
//		https://stackoverflow.com/a/974989
		List<OrdemServicoBean> lista = new ArrayList<OrdemServicoBean>();
		try {
			Optional<Connection> optional = S9_REALSQLServerFactory.getConection();
			if (optional.isPresent()) {
				PreparedStatement prepareStatement = optional.get().prepareStatement(SQL_SELECT_ORDEM_SERVICO_POR_DATA);
				System.out.println(dataInicio.toString("yyyy-MM-dd HH:mm:ss.SSS"));
				prepareStatement.setTimestamp(1, Timestamp.valueOf(dataInicio.toString("yyyy-MM-dd HH:mm:ss.SSS")));
				prepareStatement.setTimestamp(2, Timestamp.valueOf(dataFinal.toString("yyyy-MM-dd HH:mm:ss.SSS")));
				ResultSet resultSet = prepareStatement.executeQuery();
				System.out.println("A");

				while (resultSet.next()) {
					lista.add(adiciona(resultSet));

				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lista;
	}

//	+ "SET IDENTITY_INSERT Movimento_Ordem_Servico OFF";
//	"SET IDENTITY_INSERT Movimento_Ordem_Servico ON "
//private static final String SQL_SELECT_ORDEM_SERVICO_ABERTAS = "SELECT Ordem, Nome_Prod_Serv, Tipo_Geral, Detalhe_1, Detalhe_2, Detalhe_3, Detalhe_4, Detalhe_5, Problema_Relatado_1, Data_Gravacao, Fechada, Numero, Ordem_Movimento FROM Movimento_Ordem_Servico o WHERE o.Fechada = 0 AND o.Cancelada != 1;";
//+ " Preco_Total_Sem_Desconto_Somado, "
//+ " Desconto_Valor_Somado, "
//+ " Acrescimo_Valor_Somado, "
//+ " Preco_Total_Com_Desconto_Somado, "
//+ " Preco_Final_Somado, "
//+ " Desconto_Total, "
//+ " Qtde_Total_Prod, "
//+" Qtde_Total_Serv, "
//+ " [Qtde_Total_Geral],"
//+ " [Preco_Total_Serv_Sem_Desconto_Somado],"
//+ " [Preco_Total_Prod_Sem_Desconto_Somado], "
//+ " [Preco_Custo_Somado], "
//+ " [Frete_Valor_Somado], "
//+ " [Desconto_Total_Serv], "
//+ " [Desconto_Total_Prod], "
//+ " [Desconto_Total_Geral],  "
//+ " Preco_Total_Serv, "
//+ " Preco_Total_Prod], "
//[FET_Base]
//	private static final String SQL_ID_MOVIMENTO_OS = "SELECT Ordem FROM Movimento_Ordem_Servico WHERE Ordem = (SELECT MAX(Ordem) FROM Movimento_Ordem_Servico);";

}
