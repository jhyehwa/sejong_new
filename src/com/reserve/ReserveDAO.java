package com.reserve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class ReserveDAO {
	private Connection conn = DBConn.getConnection();

	public void insertReserve(ReserveDTO dto, String mode) {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "INSERT INTO reservation (r_num, r_id, r_name, r_email, r_tel, r_request, r_date, r_time, r_count) VALUES "
					+ "(seq_res.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, dto.getR_id());
			pstmt.setString(2, dto.getR_name());
			pstmt.setString(3, dto.getR_email());
			pstmt.setString(4, dto.getR_tel());
			pstmt.setString(5, dto.getR_request());
			pstmt.setString(6, dto.getR_date());
			pstmt.setString(7, dto.getR_time());
			pstmt.setString(8, dto.getR_count());

			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
	}

	public ReserveDTO checkReserve(String r_name, String r_tel) {
		ReserveDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		StringBuffer sb = new StringBuffer();

		try {
			sb.append("SELECT r_num, r_name, r_tel, r_email, r_request, r_date, r_time, r_count");
			sb.append(" FROM reservation");
			sb.append(" WHERE r_name = ? AND r_tel = ?");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, r_name);
			pstmt.setString(2, r_tel);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new ReserveDTO();				

				dto.setR_num(rs.getInt("r_num"));
				dto.setR_name(rs.getString("r_name"));
				dto.setR_tel(rs.getString("r_tel"));
				dto.setR_email(rs.getString("r_email"));
				dto.setR_request(rs.getString("r_request"));
				dto.setR_date(rs.getString("r_date"));
				dto.setR_time(rs.getString("r_time"));
				dto.setR_count(rs.getString("r_count"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}

			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e) {
				}
			}
		}

		return dto;
	}

	public ReserveDTO checkReserve(int r_num) {
		ReserveDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		StringBuffer sb = new StringBuffer();

		try {
			sb.append("SELECT r_num, r_name, r_tel, r_email, r_request, r_date, r_time, r_count");
			sb.append(" FROM reservation");
			sb.append(" WHERE r_num=?");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, r_num);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new ReserveDTO();
				
				String r_date2 = rs.getString("r_date").replaceAll("-", "");

				dto.setR_num(Integer.parseInt(r_date2+(rs.getString("r_num"))));
				dto.setR_name(rs.getString("r_name"));
				dto.setR_tel(rs.getString("r_tel"));
				dto.setR_email(rs.getString("r_email"));
				dto.setR_request(rs.getString("r_request"));
				dto.setR_date(rs.getString("r_date"));
				dto.setR_time(rs.getString("r_time"));
				dto.setR_count(rs.getString("r_count"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}

			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e) {
				}
			}
		}

		return dto;
	}

	public void updateReserve(ReserveDTO dto) {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE reservation SET r_name=?, r_tel=?, r_email=?, r_request=?, r_date=?, r_time=?, r_count=? WHERE r_num=?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getR_name());
			pstmt.setString(2, dto.getR_tel());
			pstmt.setString(3, dto.getR_email());
			pstmt.setString(4, dto.getR_request());
			pstmt.setString(5, dto.getR_date());
			pstmt.setString(6, dto.getR_time());
			pstmt.setString(7, dto.getR_count());
			pstmt.setInt(8, dto.getR_num());

			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}

		}

	}

	public void deleteReserve(int r_num) {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "DELETE FROM reservation WHERE r_num = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, r_num);
			
			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}

		}

	}
	
	
	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT NVL(COUNT(r_num),0) cnt FROM reservation";
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getInt("cnt");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {					
				}
			}
			
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {					
				}
			}
		}
		
		return result;
	}	
	
	public int dataCount(String condition, String keyword) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT NVL(COUNT(*), 0) cnt FROM reservation";
			
			if(condition.equals("r_name")) {
				sql += " WHERE INSTR(r_name, ?) = 1";
			} else if(condition.equals("r_date")) {				
				sql += " WHERE r_date = ? ";
			} 
			System.out.println(sql);
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, keyword);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getInt("cnt");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {					
				}
			}
			
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {					
				}
			}
		}
		
		return result;		
	}	
	
	// 리스트 
	public List<ReserveDTO> listBoard(int offset, int rows) {
		List<ReserveDTO> list = new ArrayList<ReserveDTO>();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT r_num, r_name, r_tel, r_date, r_time, r_count, r_request, r_email");	
			sb.append(" FROM reservation ");			
			sb.append(" ORDER BY r_num DESC");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY"); 	
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, offset);
			pstmt.setInt(2, rows);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ReserveDTO dto = new ReserveDTO();				

				String r_date2 = rs.getString("r_date").replaceAll("-", "");
				dto.setR_num(Integer.parseInt(r_date2+(rs.getString("r_num"))));
				
				dto.setR_name(rs.getString("r_name"));
				dto.setR_tel(rs.getString("r_tel"));
				dto.setR_date(rs.getString("r_date"));
				dto.setR_time(rs.getString("r_time"));
				dto.setR_count(rs.getString("r_count"));
				dto.setR_request(rs.getString("r_request"));
				dto.setR_email(rs.getString("r_email"));
				
				list.add(dto);
			}			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {					
				}
			}
			
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {					
				}
			}
		}		
		return list;
	}
	
	// 검색에서 리스트 
	public List<ReserveDTO> listBoard(int offset, int rows, String condition, String keyword) {
		List<ReserveDTO> list = new ArrayList<ReserveDTO>();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT r_num, r_name, r_tel, r_date, r_time, r_count, r_request, r_email");		
			sb.append(" FROM reservation ");			
			
			if(condition.equalsIgnoreCase("r_name")) {
				sb.append(" WHERE INSTR(r_name, ?) = 1");
			} else if(condition.equalsIgnoreCase("r_date")) {				
				sb.append(" WHERE r_date = ? ");
			}		
			
			sb.append(" ORDER BY r_num DESC");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY"); 
			
			pstmt = conn.prepareStatement(sb.toString());			
			pstmt.setString(1, keyword);
			pstmt.setInt(2, offset);
			pstmt.setInt(3, rows);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ReserveDTO dto = new ReserveDTO();
				
				String r_date2 = rs.getString("r_date").replaceAll("-", "");
				dto.setR_num(Integer.parseInt(r_date2+(rs.getString("r_num"))));
				
				dto.setR_name(rs.getString("r_name"));
				dto.setR_tel(rs.getString("r_tel"));
				dto.setR_date(rs.getString("r_date"));
				dto.setR_time(rs.getString("r_time"));
				dto.setR_count(rs.getString("r_count"));
				dto.setR_request(rs.getString("r_request"));
				dto.setR_email(rs.getString("r_email"));
				
				list.add(dto);
			}			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {					
				}
			}
			
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {					
				}
			}
		}		
		return list;		
	}

}
