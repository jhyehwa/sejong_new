package com.qna;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
			sql="SELECT qna_seq.NEXTVAL FROM dual";
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
			
			sql = "INSERT INTO qna(q_num, q_id, q_title, q_content, q_groupNum, q_depth, q_orderNo, q_parent";
			sql+="VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getNum());
			pstmt.setString(2, dto.getId());
			pstmt.setString(3, dto.getTitle());
			pstmt.setString(4, dto.getContent());
			pstmt.setInt(5, dto.getGroupNum());
			pstmt.setInt(6, dto.getDepth());
			pstmt.setInt(7, dto.getOrderNum());
			pstmt.setInt(8, dto.getParent());
			
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
			sql="UPDATE board SET orderNo=orderNo+1 WHERE groupNum = ? AND orderNo > ?";
			
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
			sql="SELECT NVL(COUNT(*), 0) FROM qun q JOIN member1 m ON m.m_id = m.q_id";
			if(condition.equals("writer")) {
				sql+="WHERE INSTR(q_writer, ?) = 1";
			} else {
				sql+="WHERE INSTR("+condition+", ?)>=1";
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
			sql="SELECT q_num, q_id, q_writer, q_title, q_groupNum, q_orderNo, q_depth, q_hitCount ";
			sql+=" TO_CHAR(q_created, 'YYYY-MM-DD') q_created FROM qna q";
			sql+="JOIN member1 m ON q.id = m.id ORDER BY groupNum DESC, orderNo ASC";
			sql+="OFFSET ? ROWS FETCH FIRST ? ROWS ONLY";
			
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
			sql="SELECT q_num, q_id, q_name, q_title, q_groupNum, q_orderNo, q_depth, q_hitCount ";
			sql+=" TO_CHAR(q_created, 'YYYY-MM-DD') q_created FROM qna q JOIN member1 m ON q.id = m.id";
			if(condition.equals("writer")) {
				sql+=" WHERE INSTR(q_writer, ?) = 1";
			} else {
				sql+=" WHERE INSTR("+condition+", ?) >= 1";
			}
			sql+=" ORDER BY groupNum DESC, orderNo ASC OFFSET ? ROWS FETCH FIRST ? ROWS ONLY";
			
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
			sql="SELECT q_num, q_id, q_name, q_title, q_groupNum, q_orderNo, q_depth, q_hitCount ";
			sql+=" q_content, TO_CHAR(q_created, 'YYYY-MM-DD') q_created FROM qna q JOIN member1 m ON q.id = m.id";
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
}
