package br.com.multigado.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.multigado.bean.ExpedicaoBean;
import br.com.multigado.bean.OrdemServicoBean;
import br.com.multigado.factory.S9_REALSQLServerFactory;

public class MovimentoMultigadoProducaoDAOSQLServerJTDS {

	private static final String SQL_INSERT_Producao = "INSERT Movimento_Multigado_Producao (Ordem_Movimento_Ordem_Servico, Data_Abertura_Producao, Data_Conclusao_Producao) VALUES (?,?,?);";
	
	private static final String SQL_EXPEDICAO_ABERTAS = "SELECT TOP(100) o.Ordem, o.Nome_Prod_Serv, o.Tipo_Geral, o.Detalhe_1, o.Detalhe_2, o.Detalhe_3, o.Detalhe_4, o.Detalhe_5, o.Detalhe_6, o.Detalhe_7, o.Problema_Relatado_1, o.Data_Gravacao, o.Fechada, Numero, o.Ordem_Movimento, o.Ordem_Prod_Serv, "
			+ "multigado.Ordem, multigado.Ordem_Movimento_Ordem_Servico, multigado.Data_Abertura_Producao, multigado.Data_Conclusao_Producao, multigado.Data_Conclusao_Carregamento FROM Movimento_Multigado_Producao multigado "
			+ "LEFT JOIN Movimento_Ordem_Servico o on o.Ordem = multigado.Ordem_Movimento_Ordem_Servico "
			+ "RIGHT JOIN Movimento movimento on movimento.Ordem = o.Ordem_Movimento "
			+ "WHERE o.Ordem_Situacao_OS = 2 AND o.Cancelada != 1 AND movimento.Ordem_Filial = 1;";
	
	private static final String SQL_UPDATE_DATA_CONCLUSAO_CARREGAMENTO = "UPDATE Movimento_Multigado_Producao SET Data_Conclusao_Carregamento = ? WHERE Ordem = ?";
	
	public void adicionaProducao(OrdemServicoBean ordem) {
		
		try {
			Optional<Connection> optional = S9_REALSQLServerFactory.getConection();
			if (optional.isPresent()) {
				
				PreparedStatement smt = optional.get().prepareStatement(SQL_INSERT_Producao);
				smt.setInt(1, ordem.getOrdem());
				smt.setString(2, ordem.getDataGravacao());
//			https://stackoverflow.com/a/32018485
				smt.setTimestamp(3, Timestamp.from(Instant.now()));
				smt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<ExpedicaoBean> expedicoesAbertas(){
		List<ExpedicaoBean> list = new ArrayList<>();
		try {
			Optional<Connection> optional = S9_REALSQLServerFactory.getConection();
			if (optional.isPresent()) {
				PreparedStatement prepareStatement = optional.get().prepareStatement(SQL_EXPEDICAO_ABERTAS);
				ResultSet resultSet = prepareStatement.executeQuery();

				while (resultSet.next()) {
					OrdemServicoBean ordem = new OrdemServicoDAOSQLServerJTDS().adiciona(resultSet);
					ExpedicaoBean expedicaoBean = new ExpedicaoBean();
					expedicaoBean.setOrdemServicoBean(ordem);
					expedicaoBean.setOrdem(resultSet.getInt(17));
					expedicaoBean.setOrdemMovimentoOrdemServico(resultSet.getInt(18));
					
					expedicaoBean.setDataAberturaProducao(resultSet.getTimestamp(19).toString());
					expedicaoBean.setDataConclusaoProducao(resultSet.getTimestamp(20).toString());
					
					System.out.println(resultSet.getTimestamp(20).toString());
					
					list.add(expedicaoBean);
				}
				optional.get().close();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public static void main(String[] args) {
		new MovimentoMultigadoProducaoDAOSQLServerJTDS().expedicoesAbertas().forEach(e -> System.out.println(e.getOrdem()));
	}

	public void finalizarCarregamento(ExpedicaoBean expedicaoBean) {
		try {
			Optional<Connection> optional = S9_REALSQLServerFactory.getConection();
			if (optional.isPresent()) {
				PreparedStatement smt = optional.get().prepareStatement(SQL_UPDATE_DATA_CONCLUSAO_CARREGAMENTO);
				smt.setTimestamp(1, Timestamp.from(Instant.now()));
				smt.setInt(2, expedicaoBean.getOrdem());
				smt.executeUpdate();
				optional.get().close();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
