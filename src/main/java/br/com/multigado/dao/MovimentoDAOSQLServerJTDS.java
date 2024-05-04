package br.com.multigado.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import br.com.multigado.bean.ClienteBean;
import br.com.multigado.bean.OrdemServicoBean;
import br.com.multigado.factory.S9_REALSQLServerFactory;

public class MovimentoDAOSQLServerJTDS {
	
	private static final String SQL_SELECT_IDCLIENTE_TABELA_MOVIMENTO = "SELECT Ordem_Cli_For FROM Movimento WHERE Ordem = ?";
	private static final String SQL_INSERT_MOVIMENTO_ORDEM =
//		    "  SET IDENTITY_INSERT Movimento ON  " 
					          " INSERT Movimento " + " (Tipo_Arredondamento, Ordem_Filial, " // 1
							+ " Tipo_Operacao," // ODS
							+ " Ordem_Operacao, " // 2
							+ " Ordem_Operador, "// 3 Junior
							+ " Ordem_Cli_For, " + " Ordem_Tabela_Preco, " + " Ordem_Vendedor1, " // 3 jUNIOR
							+ " Finalidade, " // 1
							+ " Data, " + " Interno_Gerado_Por, " // Classe_Movimento_Saida
							+ " Data_Criacao, " + " Criado_Por, " // JUNIOR
							+ " Ultima_Gravacao, " // SERVJERUSALEM + HORA
							+ " Ordem_Funcionario_Alteracao, " // 3 JUNIOR
							+ " CRT, " // 0
							+ " Situacao_Expedicao, " // S
							+ " Movimenta_Estoque, " // 1
							+ " Natureza_Operacao, " // Ordem de Serviço
							+ " Versao_Gravacao, " // 9.5.9.19
							+ " Ordem_Tabela_Custo, " // 8
							+ " Ordem_Tabela_Custo_Medio) " // 5
							+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
//			+ " SET IDENTITY_INSERT Movimento OFF ";

	private static final String SQL_INSERT_MOVIMENTO_SAIDA =
			"INSERT Movimento (Ordem_Filial,"
			+ " Tipo_Operacao, " //='VND'
			+ " Ordem_Operacao," //7
			+ " Ordem_Operador," // 1035
			+ " Ordem_Cli_For,"
			+ " Ordem_Tabela_Preco,"//3
			+ " Ordem_Vendedor1,"
			+ " Sequencia, "
			+ " Data,"
			+ " Interno_Gerado_Por," //Classe_Movimento_Saida
			+ " Preco_Final_Somado,"
			+ "" 
			+ ""; //1035
		
	public Optional<ClienteBean> procuraClientePorIDDoMovimento(Integer id) {
		try {
			Optional<Connection> optional = S9_REALSQLServerFactory.getConection();
			if (optional.isPresent()) {
				PreparedStatement smt = optional.get().prepareStatement(SQL_SELECT_IDCLIENTE_TABELA_MOVIMENTO);
				smt.setInt(1, id);
				smt.executeQuery().next();
				return new ClienteDAOServerJTDS().procuraClientePorID(smt.getResultSet().getInt(1));
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}
	
	public int adicionaMovimento(OrdemServicoBean ordem) {
		
		try {
			Optional<Connection> optional = S9_REALSQLServerFactory.getConection();
			if (optional.isPresent()) {
				PreparedStatement smt = optional.get().prepareStatement(SQL_INSERT_MOVIMENTO_ORDEM);
				smt.setInt(1, 0);
				smt.setInt(2, 1);// tabela Movimento
				smt.setString(3, "ODS");
				smt.setInt(4, 2);
				smt.setInt(5, 3); // Junior
				smt.setInt(6, ordem.getClienteBean().getOrdem()); // Junior
				smt.setInt(7, 2); // Tabela A VISTA
				smt.setInt(8, 3); // Ordem Vendedor
				smt.setInt(9, 1); // Finalidade
				smt.setTimestamp(10, Timestamp.valueOf(ordem.getDataGravacao()));
				smt.setString(11, "Classe_Movimento_Saida");
				smt.setTimestamp(12, Timestamp.valueOf(ordem.getDataGravacao()));
				smt.setString(13, "JUNIOR");
				smt.setString(14,
						"SERVJERUSALEM - " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
				smt.setInt(15, 3); // Junior
				smt.setInt(16, 0); // CRT
				smt.setString(17, "S");// Situacao Expedicao
				smt.setInt(18, 1);
				smt.setString(19, "Ordem de Serviço");
				smt.setString(20, "9.5.9.19");
				smt.setInt(21, 8);
				smt.setInt(22, 5);
				return smt.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public static void main(String[] args) {
		System.out.println(
				new MovimentoDAOSQLServerJTDS().procuraClientePorIDDoMovimento(814302)
		);
	}
}
