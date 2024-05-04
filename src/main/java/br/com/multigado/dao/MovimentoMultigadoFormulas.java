package br.com.multigado.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.multigado.bean.FormulaBean;
import br.com.multigado.bean.ProdutoBean;
import br.com.multigado.factory.S9_REALSQLServerFactory;

public class MovimentoMultigadoFormulas {

	private static final String SQL_INSERT_FORMULA = " INSERT Movimento_Multigado_Formulas (Ordem_Prod_Serv_Principal, Ordem_Prod_Formula,"
			+ " VALOR," + " TIPO) " + " VALUES (?,?,?,?)";
	private static final String SQL_SELECT_FORMULA = " SELECT mF.Ordem, pM.Ordem, pM.Codigo, pM.Nome, mF.VALOR, mF.TIPO FROM Movimento_Multigado_Formulas mF "
			+ "LEFT JOIN Prod_Serv pM on mF.Ordem_Prod_Formula = pM.Ordem" + " where mF.Ordem_Prod_Serv_Principal = ?";

	private static final String SQL_UPDATE_VALOR = "UPDATE Movimento_Multigado_Formulas SET Valor = ? WHERE Ordem = ?";

	public void adiciona(FormulaBean formulaBean) {
		Optional<Connection> optional = S9_REALSQLServerFactory.getConection();
		try {
			if (optional.isPresent()) {
				PreparedStatement smt = optional.get().prepareStatement(SQL_INSERT_FORMULA);
				smt.setInt(1, formulaBean.getProduto().getOrdem());
				smt.setInt(2, formulaBean.getMaterialPrima().getOrdem());
				smt.setBigDecimal(3, formulaBean.getValor());
				smt.setString(4, formulaBean.getTipo());
				smt.executeUpdate();
				optional.get().close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<FormulaBean> carregaFormulaProdutos(ProdutoBean produtoBean) {
		List<FormulaBean> lista = new ArrayList<>();
		Optional<Connection> optional = S9_REALSQLServerFactory.getConection();
		try {
			if (optional.isPresent()) {
				PreparedStatement smt = optional.get().prepareStatement(SQL_SELECT_FORMULA);
				smt.setInt(1, produtoBean.getOrdem());
				ResultSet resultSet = smt.executeQuery();

				while (resultSet.next()) {
					FormulaBean formulaBean = new FormulaBean();
					formulaBean.setOrdem(resultSet.getInt(1));

					ProdutoBean mP = new ProdutoBean();
					mP.setOrdem(resultSet.getInt(2));
					mP.setCodigo(resultSet.getString(3));
					mP.setDescricaoProduto(resultSet.getString(4));

					formulaBean.setMaterialPrima(mP);
					formulaBean.setProduto(produtoBean);
					formulaBean.setValor(resultSet.getBigDecimal(5));
					formulaBean.setTipo(resultSet.getString(6));

					lista.add(formulaBean);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lista;
	}

	public void editarValor(FormulaBean materia) {
		try {
		Optional<Connection> optional = S9_REALSQLServerFactory.getConection();
			if (optional.isPresent()) {
				PreparedStatement smt = optional.get().prepareStatement(SQL_UPDATE_VALOR);
				smt.setBigDecimal(1, materia.getValor());
				smt.setInt(2, materia.getOrdem());
				smt.executeUpdate();
					optional.get().close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
	}
}
