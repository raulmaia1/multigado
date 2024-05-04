package br.com.multigado.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.multigado.bean.FuncionarioVendaBean;
import br.com.multigado.bean.OrdemServicoBean;
import br.com.multigado.bean.ProdutoVendaBean;
import br.com.multigado.factory.S9_REALSQLServerFactory;

public class MovimentoProdutoServicoSQLServerJTDSDAO {

	private static final String SQL_SELECT_MOVIMENTO_PRODUTOS_FUNCIONARIOS = "SELECT TOP(5)" + "movimento.Ordem"
			+ "		       ,movimento.Ordem_Prod_Serv" + "            ,movimento.Numero_Linha"
			+ "      	   ,movimento.Quantidade_Itens" + "			   ,movimento.Data_Alteracao"
			+ "			   ,movimento.Numero_Ordem_Servico" + " ,movimento.Ordem_Tecnico1 "
			+ " 		FROM Movimento_Prod_Serv movimento "
			+ "			LEFT JOIN Prod_Serv produto on produto.Ordem = movimento.Ordem_Prod_Serv"
			+ "			WHERE movimento.Ordem_Movimento = ? AND movimento.Ordem_Tecnico1 = ? "
			+ "AND produto.Tipo = 'V' AND Linha_Excluida = 0 AND movimento.Servico_Concluido = ? ORDER BY movimento.Ordem DESC;";

	private static final String SQL_SELECT_MOVIMENTO_SERVICOS = "SELECT TOP(5)" + "movimento.Ordem"
			+ "		       ,movimento.Ordem_Prod_Serv" + "            ,movimento.Numero_Linha"
			+ "      	   ,movimento.Quantidade_Itens" + "			   ,movimento.Data_Alteracao"
			+ "			   ,movimento.Numero_Ordem_Servico" + " ,movimento.Ordem_Tecnico1 , movimento.Servico_Concluido  "
			+ " 		FROM Movimento_Prod_Serv movimento "
			+ "			LEFT JOIN Prod_Serv produto on produto.Ordem = movimento.Ordem_Prod_Serv"
			+ "			WHERE movimento.Ordem_Movimento = ? "
			+ "AND produto.Tipo = 'V' AND Linha_Excluida = 0 ORDER BY movimento.Ordem DESC;";

	private static final String SQL_SELECT_MOVIMENTO_PRODUTOS = "SELECT TOP(5)" + "movimento.Ordem"
			+ "		       ,movimento.Ordem_Prod_Serv" + "            ,movimento.Numero_Linha"
			+ "      	   ,movimento.Quantidade_Itens" + "			   ,movimento.Data_Alteracao"
			+ "			   ,movimento.Numero_Ordem_Servico" + " ,movimento.Ordem_Tecnico1 , movimento.Servico_Concluido  "
			+ " 		FROM Movimento_Prod_Serv movimento "
			+ "			LEFT JOIN Prod_Serv produto on produto.Ordem = movimento.Ordem_Prod_Serv"
			+ "			WHERE movimento.Ordem_Movimento = ? "
			+ "AND produto.Tipo = 'N' AND Linha_Excluida = 0 ORDER BY movimento.Ordem DESC;";
	
	public Optional<List<OrdemServicoBean>> pesquisaServicosAbertosPorFuncionarios(int id) {
		
		return Optional.ofNullable(
				this.getMovimentoServico(id, new OrdemServicoDAOSQLServerJTDS().ordemServicoAbertas().get(), 1));

	}

	public Optional<List<OrdemServicoBean>> pesquisaServicosConcluidosPorFuncionarios(int id) {
		List<OrdemServicoBean> lista = new ArrayList<>();

		lista.addAll(this.getMovimentoServico(id, new OrdemServicoDAOSQLServerJTDS().ordemServicoAbertas().get(), 1));

		lista.addAll(this.getMovimentoServico(id, new OrdemServicoDAOSQLServerJTDS().ordemServicoFechadas().get(), 0));

		return Optional.ofNullable(lista);
	}

	public Optional<List<FuncionarioVendaBean>> ultimosServicosPorFuncionario() {
		List<FuncionarioVendaBean> list = new ArrayList<>();
		List<OrdemServicoBean> listTemp = new ArrayList<>();

		new OrdemServicoDAOSQLServerJTDS().ordemServicoAbertas().ifPresent(listaOrdem -> listTemp.addAll(listaOrdem));
		new OrdemServicoDAOSQLServerJTDS().ordemServicoFechadas().ifPresent(listaOrdem -> listTemp.addAll(listaOrdem));

		listTemp.forEach(ordem -> {

			try {
				Optional<Connection> optional = S9_REALSQLServerFactory.getConection();
				if (optional.isPresent()) {
					PreparedStatement prepareStatement = optional.get().prepareStatement(SQL_SELECT_MOVIMENTO_SERVICOS);
					prepareStatement.setInt(1, ordem.getOrdemMovimento());

					ResultSet resultSet = prepareStatement.executeQuery();

					while (resultSet.next()) {
						ProdutoVendaBean produtoVendaBean = new ProdutoVendaBean();
						produtoVendaBean.setOrdem(resultSet.getInt(1));

						produtoVendaBean.setProdutoBean(
								new ProdutoDAOSQLServerJTDS().procuraServicoPorID(resultSet.getInt(2)).get());

						produtoVendaBean.setNumeroLinha(resultSet.getInt(3));
						produtoVendaBean.setQuantidade(new BigDecimal(resultSet.getInt(4)));
						produtoVendaBean.setDataAlteracao(String.valueOf(resultSet.getTimestamp(5)));
						produtoVendaBean.setNumeroOS(resultSet.getInt(6));
						new FuncionarioDAOSQLServerJTDS().procuraTecnicoPorID(resultSet.getInt(7)).ifPresent(t -> {
							produtoVendaBean.setTecnico(t);
						});
						
						produtoVendaBean.setConcluido(resultSet.getBoolean(8));
						
						ordem.adicionaProdutoServicoOrdem(produtoVendaBean);
						
						FuncionarioVendaBean funcionarioVendaBean = new FuncionarioVendaBean();
						funcionarioVendaBean.setTecnico(produtoVendaBean.getTecnico());
						
						if(list.contains(funcionarioVendaBean)) {
							for (FuncionarioVendaBean f : list) {								
								if(f.getTecnico().equals(funcionarioVendaBean.getTecnico())) {
									f.getOrdens().add(ordem);
									break;
								}
							}
						}else {					
							funcionarioVendaBean.getOrdens().add(ordem);					
							list.add(funcionarioVendaBean);							
						}
					}
					prepareStatement.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		return Optional.ofNullable(list);
	}
	
	
	public List<ProdutoVendaBean> pesquisaServicosPorIDMovimento(OrdemServicoBean ordem) {
		return pesquisaPorIDMovimento(ordem, SQL_SELECT_MOVIMENTO_SERVICOS);
	}
	
	public List<ProdutoVendaBean> pesquisaProdutosPorIDMovimento(OrdemServicoBean ordem) {
		return pesquisaPorIDMovimento(ordem, SQL_SELECT_MOVIMENTO_PRODUTOS);
	}
	
	
	private List<ProdutoVendaBean> pesquisaPorIDMovimento(OrdemServicoBean ordem, String sql) {
		List<ProdutoVendaBean> list = new ArrayList<>();
			try {
				Optional<Connection> optional = S9_REALSQLServerFactory.getConection();
				if (optional.isPresent()) {
					PreparedStatement prepareStatement = optional.get().prepareStatement(sql);
					prepareStatement.setInt(1, ordem.getOrdemMovimento());

					ResultSet resultSet = prepareStatement.executeQuery();

					while (resultSet.next()) {
						ProdutoVendaBean produtoVendaBean = new ProdutoVendaBean();
						produtoVendaBean.setOrdem(resultSet.getInt(1));
						
						/*
						 * Aqui tem que coloca o serviço
						 * 
						 */
						new ProdutoDAOSQLServerJTDS().procuraProdutoPorID(resultSet.getInt(2)).ifPresent(p ->{
							produtoVendaBean.setProdutoBean(p);							
						});

						produtoVendaBean.setNumeroLinha(resultSet.getInt(3));
						produtoVendaBean.setQuantidade(new BigDecimal(resultSet.getInt(4)));
						produtoVendaBean.setDataAlteracao(String.valueOf(resultSet.getTimestamp(5)));
						produtoVendaBean.setNumeroOS(resultSet.getInt(6));
						new FuncionarioDAOSQLServerJTDS().procuraTecnicoPorID(resultSet.getInt(7)).ifPresent(t -> {
							produtoVendaBean.setTecnico(t);
						});
						
						produtoVendaBean.setConcluido(resultSet.getBoolean(8));
						
						list.add(produtoVendaBean);
						
					}
					prepareStatement.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		return list;	
	}

	private List<OrdemServicoBean> getMovimentoServico(int id, List<OrdemServicoBean> listaOrdem, int concluida) {
		List<OrdemServicoBean> list = new ArrayList<>();

		listaOrdem.forEach(ordem -> {
			try {
				Optional<Connection> optional = S9_REALSQLServerFactory.getConection();
				if (optional.isPresent()) {
					PreparedStatement prepareStatement = optional.get()
							.prepareStatement(SQL_SELECT_MOVIMENTO_PRODUTOS_FUNCIONARIOS);
					prepareStatement.setInt(1, ordem.getOrdemMovimento());
					prepareStatement.setInt(2, id);
					prepareStatement.setInt(3, concluida);

					ResultSet resultSet = prepareStatement.executeQuery();

					while (resultSet.next()) {
						ProdutoVendaBean produtoVendaBean = new ProdutoVendaBean();
						produtoVendaBean.setOrdem(resultSet.getInt(1));

						produtoVendaBean.setProdutoBean(
								new ProdutoDAOSQLServerJTDS().procuraServicoPorID(resultSet.getInt(2)).get());

						produtoVendaBean.setNumeroLinha(resultSet.getInt(3));
						produtoVendaBean.setQuantidade(new BigDecimal(resultSet.getInt(4)));
						produtoVendaBean.setDataAlteracao(String.valueOf(resultSet.getTimestamp(5)));
						produtoVendaBean.setNumeroOS(resultSet.getInt(6));
						new FuncionarioDAOSQLServerJTDS().procuraTecnicoPorID(id).ifPresent(t -> {
							produtoVendaBean.setTecnico(t);
						});

						ordem.adicionaProdutoServicoOrdem(produtoVendaBean);
						list.add(ordem);
					}
					prepareStatement.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return list;
	}

	public static void main(String[] args) {
		
//		110
//		94933
//		
//		new OrdemServicoDAOSQLServerJTDS().ordemServicoAbertas().ifPresent(ordens ->{
//		
//			ordens.forEach(ordem ->{
//				System.out.println(ordem.getNumeroOS());
//				System.out.println(ordem.getOrdemMovimento());
////				new MovimentoProdutoServicoSQLServerJTDSDAO().pesquisaProdutosPorIDMovimento(ordem).forEach(pV ->{
////									
////				});				
//			});
//			
//			
//		});
		
		
		
//		new MovimentoProdutoServicoSQLServerJTDSDAO().ultimosServicosPorFuncionario().ifPresent(lista ->{
//			lista.forEach(f -> {
//				System.out.println(f.getNumeroOS() +"  "+ o.getListProdutoServico());							
//			});
//		});

//		for (int i = 5; i < 9; i++) {

//			new MovimentoProdutoServicoSQLServerJTDSDAO().pesquisaServicosAbertosPorFuncionarios(i).ifPresent(lista -> {
//				lista.forEach(o -> {
//				});
//			});

//			new MovimentoProdutoServicoSQLServerJTDSDAO().pesquisaServicosConcluidosPorFuncionarios(i)
//					.ifPresent(lista -> {
//						lista.forEach(o -> {
//							System.out.println(o.getNumeroOS() + "  " + o.getListProdutoServico());
//						});
//					});
//		}
	}



}