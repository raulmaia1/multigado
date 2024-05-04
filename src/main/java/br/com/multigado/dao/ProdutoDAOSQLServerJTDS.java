package br.com.multigado.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.multigado.bean.ProdutoBean;
import br.com.multigado.factory.S9_REALSQLServerFactory;

public class ProdutoDAOSQLServerJTDS {

	private static final String SQL_SELECT_PRODUTOS_TABELA = "SELECT TOP 5 produto.Ordem" + "      ,produto.Codigo"
			+ "      ,produto.Nome" + "	     ,precos.Preco" + "	 	 ,tabelas.Nome" + "      ,produto.Codigo_Adicional1, produto.Observacao"
			+ "      ,produto.Codigo_Adicional2" + "      ,produto.Codigo_Barras" + "  FROM Prod_Serv produto "
			+ "  LEFT JOIN Prod_Serv_Precos precos on produto.Ordem = precos.Ordem_Prod_Serv"
			+ "  RIGHT JOIN Tabelas_Preco tabelas on precos.Ordem_Tabela_Preco = tabelas.Ordem"
			+ "  WHERE produto.Inativo = 0 AND tabelas.Nome = ?;";

	private static final String SQL_SELECT_PRODUTOS = "SELECT TOP 5 produto.Ordem" + "      ,produto.Codigo"
			+ "      ,produto.Nome" + "	     ,precos.Preco" + "      ,tabelas.Nome "
			+ "       ,produto.Codigo_Adicional1" + "      ,produto.Codigo_Adicional2" + "      ,produto.Codigo_Barras, produto.Observacao"
			+ "  FROM Prod_Serv produto "
			+ "  LEFT JOIN Prod_Serv_Precos precos on produto.Ordem = precos.Ordem_Prod_Serv"
			+ "  RIGHT JOIN Tabelas_Preco tabelas on precos.Ordem_Tabela_Preco = tabelas.Ordem"
			+ "  WHERE produto.Nome LIKE ? AND tabelas.Nome = 'ESPECIAL';";

	private static final String SQL_SELECT_SERVICOS = "SELECT TOP 5 produto.Ordem" + "      ,produto.Codigo"
			+ "      ,produto.Nome" + "	     ,precos.Preco" + " ,tabelas.Nome " + " ,produto.Codigo_Adicional1"
			+ "      ,produto.Codigo_Adicional2" + "      ,produto.Codigo_Barras, produto.Observacao" + "  FROM Prod_Serv produto "
			+ "  LEFT JOIN Prod_Serv_Precos precos on produto.Ordem = precos.Ordem_Prod_Serv"
			+ "  RIGHT JOIN Tabelas_Preco tabelas on precos.Ordem_Tabela_Preco = tabelas.Ordem"
			+ "  WHERE produto.Nome LIKE ? AND tabelas.Nome = 'A VISTA' AND produto.Tipo = 'V'";

	private static final String SQL_SELECT_SERVICOS_POR_ID = "SELECT produto.Ordem" + "      ,produto.Codigo"
			+ "      ,produto.Nome" + "	     ,precos.Preco" + "      ,tabelas.Nome "
			+ "       ,produto.Codigo_Adicional1" + "      ,produto.Codigo_Adicional2" + "      ,produto.Codigo_Barras, produto.Observacao"
			+ "  FROM Prod_Serv produto "
			+ "  LEFT JOIN Prod_Serv_Precos precos on produto.Ordem = precos.Ordem_Prod_Serv"
			+ "  RIGHT JOIN Tabelas_Preco tabelas on precos.Ordem_Tabela_Preco = tabelas.Ordem"
			+ "  WHERE produto.Ordem = ? AND produto.Tipo = 'V' AND tabelas.Nome = 'A VISTA';";

	private static final String SQL_SELECT_PRODUTOS_POR_ORDEM = "SELECT produto.Ordem " + ", produto.Codigo"
			+ "      ,produto.Nome" + "	     ,precos.Preco" + "      ,tabelas.Nome "
			+ "       ,produto.Codigo_Adicional1" + "      ,produto.Codigo_Adicional2" + "      ,produto.Codigo_Barras, produto.Observacao "
			+ "  FROM Prod_Serv produto "
			+ "  LEFT JOIN Prod_Serv_Precos precos on produto.Ordem = precos.Ordem_Prod_Serv"
			+ "  RIGHT JOIN Tabelas_Preco tabelas on precos.Ordem_Tabela_Preco = tabelas.Ordem"
			+ "  WHERE produto.Ordem = ? AND tabelas.Nome = 'ESPECIAL';";
	
	
	public List<ProdutoBean> carregaListProdutos(String nomeTabela) {
		List<ProdutoBean> list = new ArrayList<>();

		try {
			Optional<Connection> optional = S9_REALSQLServerFactory.getConection();
			if (optional.isPresent()) {
				PreparedStatement prepareStatement = optional.get().prepareStatement(SQL_SELECT_PRODUTOS_TABELA);
				prepareStatement.setString(1, nomeTabela);
				ResultSet resultSet = prepareStatement.executeQuery();

				list.add(adicionarProdutos(resultSet));

				prepareStatement.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return list;
	}
//	
//	public static void main(String[] args) {
//		new SQLServerJTDSDAO().carregaListProdutos(new ConfiguracaoBean(), "VAREJO").forEach(System.out::println);
//	}

	public List<ProdutoBean> procuraProdutos(String input) {
		List<ProdutoBean> list = new ArrayList<>();

		try {
			Optional<Connection> optional = S9_REALSQLServerFactory.getConection();
			if (optional.isPresent()) {
				PreparedStatement prepareStatement;
				prepareStatement = optional.get().prepareStatement(SQL_SELECT_PRODUTOS);
				prepareStatement.setString(1, "%" + input + "%");
				ResultSet resultSet = prepareStatement.executeQuery();
				while (resultSet.next()) {
					list.add(adicionarProdutos(resultSet));
				}

				prepareStatement.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<ProdutoBean> procuraServicos(String input) {
		List<ProdutoBean> list = new ArrayList<>();

		try {
			Optional<Connection> optional = S9_REALSQLServerFactory.getConection();
			if (optional.isPresent()) {
				PreparedStatement prepareStatement;
				prepareStatement = optional.get().prepareStatement(SQL_SELECT_SERVICOS);
				prepareStatement.setString(1, "%" + input + "%");
				ResultSet resultSet = prepareStatement.executeQuery();

				while (resultSet.next()) {
					list.add(adicionarProdutos(resultSet));
				}
				prepareStatement.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	private ProdutoBean adicionarProdutos(ResultSet resultSet) throws SQLException {

		ProdutoBean p = new ProdutoBean();
		p.setOrdem(resultSet.getInt(1));
		p.setCodigo(resultSet.getString(2));
		p.setDescricaoProduto(resultSet.getString(3));
		p.setPrecoUnitario(resultSet.getBigDecimal(4).toString());

		p.setNomeTabela(resultSet.getString(5).toString());
		p.setCodigoAdicionalUm(resultSet.getString(6));
		p.setCodigoAdicionalDois(resultSet.getString(7));
		p.setCodigoBarra(resultSet.getString(8));
		p.setObs(resultSet.getString(9));
		
		return p;

	}

	public Optional<ProdutoBean> procuraServicoPorID(int id) {
		return procuraPorID(id, SQL_SELECT_SERVICOS_POR_ID);
	}
	
	public Optional<ProdutoBean> procuraProdutoPorID(int id) {
		return procuraPorID(id, SQL_SELECT_PRODUTOS_POR_ORDEM);
	}
	
	private Optional<ProdutoBean> procuraPorID(int id, String sql) {
		try {
			Optional<Connection> optional = S9_REALSQLServerFactory.getConection();
			if (optional.isPresent()) {
				PreparedStatement prepareStatement;
				prepareStatement = optional.get().prepareStatement(sql);
				prepareStatement.setString(1, String.valueOf(id));
				ResultSet resultSet = prepareStatement.executeQuery();
				if (resultSet.next()) {
					System.out.println(resultSet.getInt(1)+"Ok");
					return Optional.ofNullable(adicionarProdutos(resultSet));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}
	
	public static void main(String[] args) {
		System.out.println(new ProdutoDAOSQLServerJTDS().procuraProdutoPorID(5470));
	}

}
