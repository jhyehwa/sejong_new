package com.qna;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class QnADAO {
	private Connection conn = DBConn.getConnection();
	
	public int insertQnA(QnADTO dto, String mode) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		int seq;
		
		try {
			sql="SELECT seq_qna.NEXTVAL FROM dual";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			seq=0;
			if(rs.next()) {
				seq=rs.getInt(1);
			}
			rs.close();
			pstmt.close();
			rs=null;
			pstmt=null;
			
			dto.setNum(seq);
			if(mode.equals("created")) {
				// 글쓰기일때
				dto.setGroupNum(seq);
				dto.setOrderNum(0);
				dto.setDepth(0);
				dto.setParent(0);
			} else if(mode.equals("reply")) {
				// 답변 일때
				updateOrderNo(dto.getGroupNum(), dto.getOrderNum());
				
				dto.setDepth(dto.getDepth()+1);
				dto.setOrderNum(dto.getOrderNum()+1);
			}
			
			sql = "INSERT INTO qna(q_num, q_id, q_title, q_content, q_groupNum, q_depth, q_orderNo, q_parent, q_writer) ";
			sql+="VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getNum());
			pstmt.setString(2, dto.getId());
			pstmt.setString(3, dto.getTitle());
			pstmt.setString(4, dto.getContent());
			pstmt.setInt(5, dto.getGroupNum());
			pstmt.setInt(6, dto.getDepth());
			pstmt.setInt(7, dto.getOrderNum());
			pstmt.setInt(8, dto.getParent());
			pstmt.setString(9, dto.getName());
			
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
	
	// 답변일 경우 orderNo 변경
	public int updateOrderNo(int groupNum, int orderNo) {
		int result = 0;
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			sql="UPDATE board SET q_orderNo=q_orderNo+1 WHERE q_groupNum = ? AND q_orderNo > ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, groupNum);
			pstmt.setInt(2, orderNo);
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
	
	public int dataCount() {
		int result=0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
	
		
		try {
			sql="SELECT NVL(COUNT(*),0) FROM qna";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getInt(1);
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
		int result=0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql="SELECT NVL(COUNT(*), 0) FROM qna q JOIN member1 m ON m.m_id = q.q_id";
			if(condition.equalsIgnoreCase("q_writer")) {
				sql+=" WHERE INSTR(q_writer, ?) = 1";
			} else if(condition.equalsIgnoreCase("q_created")) {
				keyword.replaceAll("-", "");
				sql+=" WHERE (TO_CHAR(q_created, 'YYYYMMDD' ) = ?)";
			} else {
				sql+=" WHERE (INSTR("+condition+", ?)>=1)";
			}
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, keyword);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getInt(1);
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
	public List<QnADTO> listQnA(int offset, int rows){
		List<QnADTO> list = new ArrayList<QnADTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql="SELECT q_num, q.q_id, q_writer, q_title, q_groupNum, q_orderNo, q_depth, q_hitCount, ";
			sql+=" TO_CHAR(q_created, 'YYYY-MM-DD') q_created FROM qna q ";
			sql+=" JOIN member1 m ON q.q_id = m.m_id ORDER BY q_groupNum DESC, q_orderNo ASC ";
			sql+=" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, offset);
			pstmt.setInt(2, rows);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				QnADTO dto = new QnADTO();
				dto.setNum(rs.getInt("q_num"));
				dto.setId(rs.getString("q_id"));
				dto.setName(rs.getString("q_writer"));
				dto.setTitle(rs.getString("q_title"));
				dto.setGroupNum(rs.getInt("q_groupNum"));
				dto.setOrderNum(rs.getInt("q_orderNo"));
				dto.setDepth(rs.getInt("q_depth"));
				dto.setHitCount(rs.getInt("q_hitCount"));
				dto.setCreated(rs.getString("q_created"));
				
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
	
	// 클라이언트 : 자신이 쓴 QnA보기	관리자 : 내용 및 작성자로 검색
	public List<QnADTO> listQnA(int offset, int rows, String condition, String keyword){
		List<QnADTO> list = new ArrayList<QnADTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql="SELECT q_num, q.q_id, q_writer, q_title, q_groupNum, q_orderNo, q_depth, q_hitCount,";
			sql+=" TO_CHAR(q_created, 'YYYY-MM-DD') q_created FROM qna q JOIN member1 m ON q.q_id = m.m_id";
			if(condition.equals("q_writer")) {
				sql+=" WHERE INSTR(q_writer, ?) = 1";
			} else if(condition.equalsIgnoreCase("q_created")) {
				keyword = keyword.replaceAll("-", "");
				sql+=" WHERE (TO_CHAR(q_created, 'YYYYMMDD' ) = ?)";
			} else {
				sql+=" WHERE (INSTR("+condition+", ?) >= 1)";
			}
			sql+=" ORDER BY q_groupNum DESC, q_orderNo ASC OFFSET ? ROWS FETCH FIRST ? ROWS ONLY";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, keyword);
			pstmt.setInt(2, offset);
			pstmt.setInt(3, rows);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				QnADTO dto = new QnADTO();
				dto.setNum(rs.getInt("q_num"));
				dto.setId(rs.getString("q_id"));
				dto.setName(rs.getString("q_writer"));
				dto.setTitle(rs.getString("q_title"));
				dto.setGroupNum(rs.getInt("q_groupNum"));
				dto.setOrderNum(rs.getInt("q_orderNo"));
				dto.setDepth(rs.getInt("q_depth"));
				dto.setHitCount(rs.getInt("q_hitCount"));
				dto.setCreated(rs.getString("q_created"));
				
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
	
	public QnADTO readQnA(int num) {
		QnADTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql="SELECT q_num, q.q_id, q_writer, q_title, q_groupNum, q_orderNo, q_depth, q_hitCount, ";
			sql+=" q_content, TO_CHAR(q_created, 'YYYY-MM-DD') q_created FROM qna q JOIN member1 m ON q.q_id = m.m_id";
			sql+=" WHERE q_num = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new QnADTO();
				dto.setNum(rs.getInt("q_num"));
				dto.setId(rs.getString("q_id"));
				dto.setName(rs.getString("q_writer"));
				dto.setTitle(rs.getString("q_title"));
				dto.setGroupNum(rs.getInt("q_groupNum"));
				dto.setOrderNum(rs.getInt("q_orderNo"));
				dto.setDepth(rs.getInt("q_depth"));
				dto.setHitCount(rs.getInt("q_hitCount"));
				dto.setContent(rs.getString("q_content"));
				dto.setCreated(rs.getString("q_created"));
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
	
	public QnADTO preReadQnA(int num, String condition, String keyword) {
		QnADTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			if(keyword.length() !=0 ) {
				sql="SELECT q_num, q_title FROM qna";
				if(condition.equalsIgnoreCase("created")) {
					keyword = keyword.replaceAll("-", "");
					sql+=" WHERE (TO_CHAR(q_created,'YYYYMMDD') = ?)";
				} else if(condition.equalsIgnoreCase("writer")) {
					sql+=" WHERE (INSTR (q_name, ? ) = 1)";
				} else {
					sql+=" WHERE (INSTR (q_content, ? ) >= 1)";
				}
				sql+="AND(num > ?) ORDER BY q_num ASC FETCH FIRST 1 ROWS ONLY";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, keyword);
				pstmt.setInt(2, num);
			} else {
				sql="SELECT q_num, q_title FROM qna q WHERE q_num > ? ORDER BY q_num ASC FETCH FIRST 1 ROWS ONLY";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, num);
			}
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new QnADTO();
				dto.setNum(rs.getInt("q_num"));
				dto.setTitle(rs.getString("q_title"));
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
	
	public QnADTO nextReadQnA(int num, String condition, String keyword) {
		QnADTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			if(keyword.length() != 0 ) {
				sql="SELECT q_num, q_title FROM qna";
				if(condition.equalsIgnoreCase("created")) {
					sql+="WHERE (TO_CHAR(created,'YYYYMMDD') = ?)";
				} else if(condition.equalsIgnoreCase("writer")) {
					sql+="WHERE (INSTR(q_name, ?) = 1)";
				} else {
					sql+="WHERE (INSTR(q_content, ?) >=1)";
				}
				sql+="AND(q_num < ?) ORDER BY q_num DESC FETCH FIRST 1 ROWS ONLY";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, keyword);
				pstmt.setInt(2, num);
			} else {
				sql="SELECT q_num, q_title FROM qna WHERE q_num < ? ORDER BY q_num DESC FETCH FIRST 1 ROWS ONLY";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, num);
			}
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new QnADTO();
				dto.setNum(rs.getInt("q_num"));
				dto.setTitle(rs.getString("q_title"));
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
	
	public int updateHitCount(int num) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql="UPDATE qna SET q_hitCount = q_hitCount + 1 WHERE q_num = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
		
		return result;
	}
	
	public int updateqna(QnADTO dto, String userId) {
		int result = 0;
		PreparedStatement pstmt =null;
		String sql;
		
		try {
			sql = "UPDATE qna SET q_title = ?, q_content = ? WHERE q_num = ? AND q_id = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getContent());
			pstmt.setInt(3, dto.getNum());
			pstmt.setString(4, userId);
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
	
	public int delete(int num) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql="DELETE FROM qna WHERE q_num IN (SELECT q_num FROM qna START WITH q_num = ? CONNECT BY PRIOR q_num = q_parent)";
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
