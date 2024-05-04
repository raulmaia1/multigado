package br.com.multigado.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.multigado.bean.FuncionarioBean;
import br.com.multigado.factory.S9_REALSQLServerFactory;

public class FuncionarioDAOSQLServerJTDS {

	private static final String SQL_SELECT_CLIENTES = "SELECT ordem, codigo, Nome,Apelido FROM Funcionarios f WHERE f.Nome LIKE ? OR f.Apelido LIKE ?;";
	private static final String SQL_SELECT_ID_FUNCIONARIO = "SELECT f.Ordem, f.Codigo, f.Nome, f.Apelido, foto.Local "
			+ "FROM Funcionarios f "
			+ "LEFT JOIN Funcionarios_Fotos foto ON f.ordem = foto.Ordem_Funcionario "
			+ "WHERE f.Ordem =  ?;";

	public List<FuncionarioBean> procuraTecnico(String pesquisa) {
		List<FuncionarioBean> list = new ArrayList<>();
		try {
			Optional<Connection> optional = S9_REALSQLServerFactory.getConection();
			if (optional.isPresent()) {
				PreparedStatement prepareStatement = optional.get().prepareStatement(SQL_SELECT_CLIENTES);
				prepareStatement.setString(1, "%" + pesquisa + "%");
				prepareStatement.setString(2, "%" + pesquisa + "%");
				ResultSet resultSet = prepareStatement.executeQuery();

				while (resultSet.next()) {
					FuncionarioBean funcionarioBean = new FuncionarioBean();
					funcionarioBean.setOrdem(String.valueOf(resultSet.getInt(1)));
					funcionarioBean.setCodigo(String.valueOf(resultSet.getInt(2)));
					funcionarioBean.setNome(resultSet.getString(3));
					funcionarioBean.setApelido(resultSet.getString(4));
					list.add(funcionarioBean);
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public static void main(String[] args) {
		System.out.println(new FuncionarioDAOSQLServerJTDS().procuraTecnicoPorID(5).get());
	}

	public Optional<FuncionarioBean> procuraTecnicoPorID(int id) {
		try {
			Optional<Connection> optional = S9_REALSQLServerFactory.getConection();
			if (optional.isPresent()) {
				PreparedStatement prepareStatement = optional.get().prepareStatement(SQL_SELECT_ID_FUNCIONARIO);
				prepareStatement.setInt(1, id);
				ResultSet resultSet = prepareStatement.executeQuery();
				if (resultSet.next()) {
					FuncionarioBean funcionarioBean = new FuncionarioBean();
					funcionarioBean.setOrdem(String.valueOf(resultSet.getInt(1)));
					funcionarioBean.setCodigo(String.valueOf(resultSet.getInt(2)));
					funcionarioBean.setNome(resultSet.getString(3));
					funcionarioBean.setApelido(resultSet.getString(4));
					funcionarioBean.setLocalFoto(resultSet.getString(5));
					return Optional.ofNullable(funcionarioBean);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}
}
