package com.notice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class NoticeDAO {
	private Connection conn = DBConn.getConnection();
	
	public int insertnotice(NoticeDTO dto) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			if(dto.getEnabled()!=1) {
				sql = "INSERT INTO notice(n_num, n_title, n_content, n_writer, n_enabled) VALUES (seq_not.NEXTVAL,?,?,?,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, dto.getTitle());
				pstmt.setString(2, dto.getContent());
				pstmt.setString(3, dto.getWriter());
				pstmt.setInt(4, dto.getEnabled());
			} else {
				sql = "INSERT INTO notice(n_num, n_title, n_content, n_finishDate, n_writer, n_enabled) VALUES (seq_not.NEXTVAL,?,?,?,?,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, dto.getTitle());
				pstmt.setString(2, dto.getContent());
				pstmt.setString(3, dto.getFinishDate());
				pstmt.setString(4, dto.getWriter());
				pstmt.setInt(5, dto.getEnabled());
			}
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return result;
	}
		
	
	public List<NoticeDTO> listNotice(){					// °øÁö
		List<NoticeDTO> list = new ArrayList<NoticeDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT n_num, n_title, n_content, n_writer, n_hitCount, n_startDate FROM notice WHERE n_enabled = 0 ORDER BY n_num DESC";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				NoticeDTO dto = new NoticeDTO();
				dto.setNum(rs.getInt("n_num"));
				dto.setTitle(rs.getString("n_title"));
				dto.setContent(rs.getString("n_content"));
				dto.setWriter(rs.getString("n_writer"));
				dto.setHitCount(rs.getInt("n_hitcount"));
				dto.setStartDate(rs.getString("n_startDate"));
				
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
		}
		return list;
	}
	
	public List<NoticeDTO> listevent(int offset, int rows){
		List<NoticeDTO> list = new ArrayList<NoticeDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql="SELECT n_num, n_writer, n_title, n_content, n_startDate, n_hitCount FROM notice WHERE n_enabled = 1";
			sql+= " ORDER BY n_num DESC OFFSET ? ROW FETCH FIRST ? ROWS ONLY";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, offset);
			pstmt.setInt(2, rows);

			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				NoticeDTO dto = new NoticeDTO();
				dto.setNum(rs.getInt("n_num"));
				dto.setTitle(rs.getString("n_title"));
				dto.setContent(rs.getString("n_content"));
				dto.setWriter(rs.getString("n_writer"));
				dto.setHitCount(rs.getInt("n_hitcount"));
				dto.setStartDate(rs.getString("n_startDate"));
				
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
		}
		
		
		
		return list;
	}
	
	public List<NoticeDTO> listevent(int offset, int rows, String condition){
		List<NoticeDTO> list = new ArrayList<NoticeDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql="SELECT n_num, n_writer, n_title, n_content, n_startDate, n_hitCount FROM notice WHERE n_enabled = 1 ";
			if(condition.equalsIgnoreCase("created")){
				sql+=" AND n_finishDate>=SYSDATE";
			} else {
				sql+=" AND n_finishDate<SYSDATE";
			} 
			sql+=" ORDER BY n_num DESC";
			sql+=" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, offset);
			pstmt.setInt(2, rows);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				NoticeDTO dto = new NoticeDTO();
				dto.setNum(rs.getInt("n_num"));
				dto.setTitle(rs.getString("n_title"));
				dto.setContent(rs.getString("n_content"));
				dto.setWriter(rs.getString("n_writer"));
				dto.setHitCount(rs.getInt("n_hitcount"));
				dto.setStartDate(rs.getString("n_startDate"));
				
				list.add(dto);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return list;
	}
	
	public NoticeDTO readNotice(int num) {
		NoticeDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT n_num, n_enabled, n_writer, n_title, n_content, n_hitCount, TO_CHAR(n_startDate,'YYYY-MM-DD') startDate";
			sql+= " FROM notice WHERE n_num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new NoticeDTO();
				dto.setNum(rs.getInt("n_num"));
				dto.setEnabled(rs.getInt("n_enabled"));
				dto.setTitle(rs.getString("n_title"));
				dto.setContent(rs.getString("n_content"));
				dto.setWriter(rs.getString("n_writer"));
				dto.setHitCount(rs.getInt("n_hitcount"));
				dto.setStartDate(rs.getString("startDate"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return dto;
	}
	
	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		ResultSet rs = null;
		
		try {
			sql = "SELECT NVL(COUNT(*),0) FROM notice WHERE n_enabled = 1";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return result;
	}
	
	public int dataCount(String condition) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		ResultSet rs = null;
		
		try {
			sql = "SELECT NVL(COUNT(*),0) FROM notice WHERE n_enabled = 1";
			 if(condition.equalsIgnoreCase("created")) {
				sql+=" AND n_finishDate>=SYSDATE";
			} else {
				sql+=" AND n_finishDate<SYSDATE";
			}
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return result;
	}
	
	public int hitCount(int num) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql="UPDATE notice SET n_hitcount = n_hitcount+1 WHERE n_num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return result;
	}
	
	public NoticeDTO prereadNotice(int num, String condition) {
		NoticeDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			if(condition.length()!=0) {
				sql="SELECT n_num, n_title FROM notice ";
				if(condition.equalsIgnoreCase("created")) {
					sql+=" WHERE n_enabled = 1 AND finishDate >= SYSDATE";
				} else if(condition.equalsIgnoreCase("closed")) {
					sql+=" WHERE n_enabled = 1 AND finishDate < SYSDATE";
				} else if(condition.equalsIgnoreCase("notice")){
					sql+=" WHERE n_enabled = 0";
				}
				
				sql+= "AND (n_num>?) ORDER BY n_num ASC FETCH FIRST 1 ROWS ONLY";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, num);
			} else {
				sql="SELECT n_num, n_title FROM notice WHERE n_num > ? ORDER BY n_num ASC FETCH FIRST 1 ROWS ONLY";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, num);
			}
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new NoticeDTO();
				dto.setNum(rs.getInt("n_num"));
				dto.setTitle(rs.getString("n_title"));
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
				
		return dto;
	}
	
	public NoticeDTO nextreadNotice(int num, String condition) {
		NoticeDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			if(condition.length()!=0) {
				sql = "SELECT n_num, n_title FROM notice";
				if(condition.equals("created")) {
					sql+=" WHERE n_enabled = 1 AND finishDate >= SYSDATE";
				} else if(condition.equals("closed")) {
					sql+=" WHERE n_enabled = 1 AND finishDate < SYSDATE";
				} else if(condition.equals("notice")) {
					sql+=" WHERE n_enabled = 0";
				}
				
				sql+= "AND (n_num < ?) ORDER BY n_num DESC FETCH FIRST 1 ROWS ONLY";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, num);
			} else {
				sql = "SELECT n_num, n_title FROM notice WHERE n_num < ? ORDER BY n_num DESC FETCH FIRST 1 ROWS ONLY";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, num);
			}
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new NoticeDTO();
				dto.setNum(rs.getInt("n_num"));
				dto.setTitle(rs.getString("n_title"));
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
		
		return dto;
	}
	
	public int updateNotice(NoticeDTO dto) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql="UPDATE notice SET n_title = ?, n_enabled = ?, n_content = ?, n_startDate = SYSDATE, n_finishDate=? WHERE n_num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getTitle());
			pstmt.setInt(2, dto.getEnabled());
			pstmt.setString(3, dto.getContent());
			pstmt.setString(4, dto.getFinishDate());
			pstmt.setInt(5, dto.getNum());
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return result;
	}
	
	public int deleteNotice(int num) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "DELETE FROM notice WHERE n_num = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return result;
	}
	
}
