package com.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.util.DBConn;

public class MemberDAO {
	private Connection conn = DBConn.getConnection();
	
	public MemberDTO readMember(String m_id) {
		MemberDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT m_num, m1.m_id, m_name, m_password, TO_CHAR(m_created, 'YYYY-MM-DD') m_created, m_birth, m_email, m_tel, m_addr1, m_addr2");
			sb.append(" FROM member1 m1 JOIN member2 m2 ON m1.m_id = m2.m_id");
			sb.append(" WHERE m1.m_id = ?");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setString(1, m_id);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new MemberDTO();
				dto.setNum(rs.getInt("m_num"));
				dto.setId(rs.getString("m_id"));
				dto.setName(rs.getString("m_name"));
				dto.setPassword(rs.getString("m_password"));
				dto.setCreated(rs.getString("m_created"));
				dto.setBirth(rs.getString("m_birth"));
				dto.setEmail(rs.getString("m_email"));
				dto.setTel(rs.getString("m_tel"));
				dto.setAddr1(rs.getString("m_addr1"));
				dto.setAddr2(rs.getString("m_addr2"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return dto;
	}
}
