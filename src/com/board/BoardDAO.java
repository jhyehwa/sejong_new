package com.board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class BoardDAO {
	private Connection conn = DBConn.getConnection();
	
	public BoardDTO readBoard(int num) {
		BoardDTO dto =null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "SELECT b_num, b_title, b_content, b_id, b_writer FROM board where b_num = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, num);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new BoardDTO();
				dto.setNum(rs.getInt("b_num"));
				dto.setTitle(rs.getString("b_title"));
				dto.setContent(rs.getString("b_content"));
				dto.setId(rs.getString("b_id"));
				dto.setWriter(rs.getString("b_writer"));
			}
			
		}  catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
		}
		return dto;
	}
	
	public int dataCnt() {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		ResultSet rs = null;
		
		try {
			sql = "SELECT NVL(COUNT(*),0) FROM board";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getInt(1);
			}
		}  catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
		}
		return result;
	}
	
	
	public int dataCnt(String type, String keyword) {
		int result = 0;
		String sql;
		PreparedStatement pstmt= null;
		ResultSet rs = null;
		
		try {
			if(type.equals("created")) {
				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				sql = "SELECT NVL(COUNT(*), 0) FROM board b  JOIN member1 m1 ON b.b_id = m1.m_id WHERE TO_CHAR(b_created, 'YYYYMMDD') = ?";
			}else if(type.equals("writer")) {
				sql = "SELECT NVL(COUNT(*), 0) FROM board b  JOIN member1 m1 ON b.b_id = m1.m_id WHERE INSTR(m_name, ?) = 1";
			}else {
				sql = "SELECT NVL(COUNT(*), 0) FROM board b  JOIN member1 m1 ON b.b_id = m1.m_id WHERE INSTR(" + type +", ?) >= 1";
			}
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, keyword);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getInt(1);
			}
		}   catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
		}
		return result;
	}
	
	public List<BoardDTO> listBoard(int startNum, int rows){
		List<BoardDTO> list = new ArrayList<BoardDTO>();
		BoardDTO dto = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT b_num, b_title,m_name ,b_content,b.b_id, b_writer, b_hitCount,");
			sb.append(" TO_CHAR(b_created, 'YYYY-MM-DD') created");
			sb.append(" FROM board b");
			sb.append(" JOIN member1 m1 ON b.b_id = m1.m_id");
			sb.append(" ORDER BY b_num DESC");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, startNum);
			pstmt.setInt(2, rows);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				dto = new BoardDTO();
				dto.setNum(rs.getInt("b_num"));
				dto.setTitle(rs.getString("m_name"));
				dto.setTitle(rs.getString("b_title"));
				dto.setContent(rs.getString("b_content"));
				dto.setId(rs.getString("b_id"));
				dto.setWriter(rs.getString("b_writer"));
				dto.setHitCount(rs.getInt("b_hitCount"));
				dto.setCreated(rs.getString("created"));
				
				list.add(dto);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
		}
		return list;
	}
	
	public List<BoardDTO> listBoard(int startNum, int rows, String type, String keyword){
		List<BoardDTO> list = new ArrayList<BoardDTO>();
		BoardDTO dto = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT b_num, b_title,m_name ,b_content,b.b_id, b_writer, b_hitCount,");
			sb.append(" TO_CHAR(b_created, 'YYYY-MM-DD') created");
			sb.append(" FROM board b");
			sb.append(" JOIN member1 m1 ON b.b_id = m1.m_id");
			
			if(type.equals("created")) {
				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				sb.append(" WHERE TO_CHAR(b_created, 'YYYY-MM-DD') = ?");
			}else if(type.equals("writer")) {
				sb.append(" WHERE INSTR(m_name, ?) = 1");
			}else {
				sb.append(" WHERE INSTR(" + type + ", ?) >= 1");
			}
			
			sb.append(" ORDER BY b_num DESC");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setString(1, keyword);
			pstmt.setInt(2, startNum);
			pstmt.setInt(3, rows);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				dto = new BoardDTO();
				dto.setNum(rs.getInt("b_num"));
				dto.setTitle(rs.getString("m_name"));
				dto.setTitle(rs.getString("b_title"));
				dto.setContent(rs.getString("b_content"));
				dto.setId(rs.getString("b_id"));
				dto.setWriter(rs.getString("b_writer"));
				dto.setHitCount(rs.getInt("b_hitCount"));
				dto.setCreated(rs.getString("created"));
				
				list.add(dto);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
		}
		return list;
	}
	

	public int insertBoard(BoardDTO dto) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql; // sql ¹®
		

		try {
			sql = "INSERT INTO board(b_num, b_title, b_content, b_id, b_writer) VALUES(seq_board.NEXTVAL,?,?,?,?)";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getId());
			pstmt.setString(4, dto.getWriter());
			
			result = pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
		return result;
	}
	
	public BoardDTO preBoard(int num, String type, String keyword) {
		BoardDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			if(keyword.length() != 0) {
			sql = "SELECT b_num, b_title FROM board b JOIN member1 m1 ON m1.m_id = b.b_id";
			if(type.equals("writer")) {
				sql += " WHERE(INSTR(m_name, ?) = 1)";
			}else if(type.equals("created")) {
				keyword = keyword.replaceAll("-", "");
				sql += " WHERE(TO_CHAR(created, 'YYYY-MM-DD') = ?)";
			}else {
				sql += " WHERE (INSTR(" + type + ", ? ) > 0";
			}
			sql += " AND(b_num > ? ) ";
			sql += " ORDER BY b_num ASC";
			sql += " FETCH FIRST 1 ROWS ONLY";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, keyword);
			pstmt.setInt(2, num);
			}else {
				sql = "SELECT b_num, b_title FROM board WHERE b_num > ? ORDER BY b_num ASC FETCH FIRST 1 ROWS ONLY";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, num);
			}
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new BoardDTO();
				dto.setNum(rs.getInt("b_num"));
				dto.setTitle(rs.getString("b_title"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
		}
		return dto;
	}
	public BoardDTO nextBoard(int num, String type, String keyword) {
		BoardDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			if(keyword.length() != 0) {
				sql = "SELECT b_num, b_title FROM board b JOIN member1 m1 ON m1.m_id = b.b_id";
				if(type.equals("writer")) {
					sql += " WHERE(INSTR(m_name, ?) = 1)";
				}else if(type.equals("created")) {
					keyword = keyword.replaceAll("-", "");
					sql += " WHERE(TO_CHAR(created, 'YYYY-MM-DD') = ?)";
				}else {
					sql += " WHERE (INSTR(" + type + ", ? ) > 0";
				}
				sql += " AND(b_num < ? ) ";
				sql += " ORDER BY b_num DESC";
				sql += " FETCH FIRST 1 ROWS ONLY";
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, keyword);
				pstmt.setInt(2, num);
			}else {
				sql = "SELECT b_num, b_title FROM board WHERE b_num < ? ORDER BY b_num DESC FETCH FIRST 1 ROWS ONLY";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, num);
			}
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new BoardDTO();
				dto.setNum(rs.getInt("b_num"));
				dto.setTitle(rs.getString("b_title"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
		}
		return dto;
	}
}
