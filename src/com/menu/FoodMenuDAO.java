package com.menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class FoodMenuDAO {
	private Connection conn = DBConn.getConnection();

	public int insertFoodMenu(FoodMenuDTO dto) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "INSERT INTO foodmenu (f_num, f_name, f_price, f_type, f_intro, f_image) VALUES (seq_food.NEXTVAL, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, dto.getF_name());
			pstmt.setString(2, dto.getF_price());
			pstmt.setString(3, dto.getF_type());
			pstmt.setString(4, dto.getF_intro());
			pstmt.setString(5, dto.getF_image());

			result = pstmt.executeUpdate();
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

		return result;
	}

	// 전체 데이터 개수
	public int dataCount(String type) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT NVL(COUNT(f_num), 0) cnt FROM foodmenu WHERE f_type = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, type);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				result = rs.getInt(1);
			}
			
			/*
			 * FoodMenuDTO dto = new FoodMenuDTO(); dto.setF_type(rs.getString("f_type"));
			 */
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
		}
		return result;
	}

	// 검색에서의 데이터 개수
	public int dataCount(String condition, String keyword) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT NVL(COUNT(*), 0) cnt FROM foodmenu ";

			if (condition.equals("data")) {
				keyword = keyword.replaceAll("-", "");
				sql += " WHERE TO_CHAR(data, 'YYYYMMDD') = ? ";
			} else {
				sql += " WHERE INSERT(" + condition + ", ?) >= 1";
			}

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, keyword);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				result = rs.getInt("cnt");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}

		return result;
	}

	// 게시물 리스트
	public List<FoodMenuDTO> listMain(int offset, int rows, String f_type) {
		List<FoodMenuDTO> list = new ArrayList<FoodMenuDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append("SELECT f_num, f_name, f_price, f_type, f_intro, f_image ");
			sb.append(" FROM foodmenu ");
			sb.append(" WHERE f_type = ? ");
			sb.append(" ORDER BY f_num DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, f_type);
			pstmt.setInt(2, offset);
			pstmt.setInt(3, rows);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				FoodMenuDTO dto = new FoodMenuDTO();
				dto.setF_num(rs.getInt("f_num"));
				dto.setF_name(rs.getString("f_name"));
				dto.setF_price(rs.getString("f_price"));
				dto.setF_type(rs.getString("f_type"));
				dto.setF_intro(rs.getString("f_intro"));
				dto.setF_image(rs.getString("f_image"));

				list.add(dto);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}

			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}

		return list;
	}

	// 게시물 리스트
	public List<FoodMenuDTO> listFoodMenu(int offset, int rows, String condition, String keyword) {
		List<FoodMenuDTO> list = new ArrayList<FoodMenuDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append("SELECT f_num, f_name, f_price, f_type, f_intro, f_image ");
			sb.append(" FROM foodmenu ");

			if (condition.equalsIgnoreCase("date")) {
				keyword = keyword.replaceAll("-", "");
				sb.append(" WHERE TO_CHAR(date, 'YYYYMMDD') = ? ");
			} else if (condition.equalsIgnoreCase("f_name")) {
				sb.append(" WHERE INSTR(f_name, ?) = 1 ");
			} else {
				sb.append(" WHERE INSTR(" + condition + ", ?) >= 1 ");
			}
			sb.append(" ORDER BY f_num DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setString(1, keyword);
			pstmt.setInt(2, offset);
			pstmt.setInt(3, rows);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				FoodMenuDTO dto = new FoodMenuDTO();

				dto.setF_num(rs.getInt("f_num"));
				dto.setF_name(rs.getString("f_name"));
				dto.setF_price(rs.getString("subject"));
				dto.setF_type(rs.getString("f_type"));
				dto.setF_intro(rs.getString("f_intro"));
				dto.setF_image(rs.getString("f_image"));
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return list;
	}

	// 게시물 보기
	public FoodMenuDTO readFoodMenu(int f_num) {
		FoodMenuDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append("SELECT f_num, f_name, f_price, f_intro, f_image ");
			sb.append(" FROM foodmenu");
			sb.append(" WHERE f_num=?");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, f_num);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new FoodMenuDTO();
				dto.setF_num(rs.getInt("f_num"));
				dto.setF_name(rs.getString("f_name"));
				dto.setF_price(rs.getString("f_price"));
				dto.setF_intro(rs.getString("f_intro"));
				dto.setF_image(rs.getString("f_image"));
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}

			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}

		return dto;
	}

	// 게시물 수정
	public int updateFoodMenu(FoodMenuDTO dto) {
		int result = 0;
		PreparedStatement pstmt = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append("UPDATE foodmenu SET f_name=?, f_price=?, f_type=?, f_image=?, f_intro=? ");
			sb.append(" WHERE f_num=?");
			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setString(1, dto.getF_name());
			pstmt.setString(2, dto.getF_price());
			pstmt.setString(3, dto.getF_type());
			pstmt.setString(4, dto.getF_image());
			pstmt.setString(5, dto.getF_intro());
			pstmt.setInt(6, dto.getF_num());

			result = pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.toString());
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

	// 게시물 삭제
	public int deleteFoodMenu(int f_num) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "DELETE FROM foodmenu WHERE f_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, f_num);
			pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.toString());
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
}
