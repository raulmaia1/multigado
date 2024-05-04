package br.com.multigado.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.multigado.bean.ClienteBean;
import br.com.multigado.factory.S9_REALSQLServerFactory;

public class ClienteDAOServerJTDS {

	private static final String SQL_SELECT_NOME_CLIENTES = "SELECT Ordem, Codigo, Nome, Fantasia, Endereco, Numero, Complemento, Bairro, Cidade, Estado, CEP, CPF, CNPJ, Fone_1, Fone_2 FROM CLI_FOR c WHERE c.Nome LIKE ?;";
	private static final String SQL_SELECT_ID_CLIENTES = "SELECT Ordem, Codigo, Nome, Fantasia, Endereco, Numero, Complemento, Bairro, Cidade, Estado, CEP, CPF, CNPJ, Fone_1, Fone_2 FROM CLI_FOR c WHERE c.Ordem = ?;";

	public List<ClienteBean> procuraClientes(String pesquisa) {
		List<ClienteBean> list = new ArrayList<>();
		try {
			Optional<Connection> optional = S9_REALSQLServerFactory.getConection();
			if (optional.isPresent()) {
				PreparedStatement prepareStatement = optional.get().prepareStatement(SQL_SELECT_NOME_CLIENTES);
				prepareStatement.setString(1, "%" + pesquisa + "%");
				ResultSet resultSet = prepareStatement.executeQuery();

				while (resultSet.next()) {
					ClienteBean c = new ClienteBean();
					c.setOrdem(resultSet.getInt(1));
					c.setCodigo(resultSet.getString(2));
					c.setNome(resultSet.getString(3));
					c.setFantasia(resultSet.getString(4));

					c.setEndereco(resultSet.getString(5));
					c.setNumero(resultSet.getString(6));
					c.setComplemento(resultSet.getString(7));
					c.setBairro(resultSet.getString(8));
					c.setCidade(resultSet.getString(9));
					c.setEstado(resultSet.getString(10));
					c.setCep(resultSet.getString(11));
					c.setCpf(resultSet.getString(12));
					c.setCnpj(resultSet.getString(13));
					c.setFone1(resultSet.getString(14));
					c.setFone2(resultSet.getString(15));

					list.add(c);

				}

				prepareStatement.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return list;
	}

	public Optional<ClienteBean> procuraClientePorID(Integer id) {
		try {
			Optional<Connection> optional = S9_REALSQLServerFactory.getConection();
			if (optional.isPresent()) {
				PreparedStatement prepareStatement = optional.get().prepareStatement(SQL_SELECT_ID_CLIENTES);
				prepareStatement.setInt(1, id);
				ResultSet resultSet = prepareStatement.executeQuery();

				if (resultSet.next()) {
					ClienteBean c = new ClienteBean();
					c.setOrdem(resultSet.getInt(1));
					c.setCodigo(resultSet.getString(2));
					c.setNome(resultSet.getString(3));
					c.setFantasia(resultSet.getString(4));

					c.setEndereco(resultSet.getString(5));
					c.setNumero(resultSet.getString(6));
					c.setComplemento(resultSet.getString(7));
					c.setBairro(resultSet.getString(8));
					c.setCidade(resultSet.getString(9));
					c.setEstado(resultSet.getString(10));
					c.setCep(resultSet.getString(11));
					c.setCpf(resultSet.getString(12));
					c.setCnpj(resultSet.getString(13));
					c.setFone1(resultSet.getString(14));
					c.setFone2(resultSet.getString(15));
					return Optional.ofNullable(c);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return Optional.empty();
	}

}
