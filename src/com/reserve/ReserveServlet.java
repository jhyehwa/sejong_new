package com.reserve;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.util.MyServlet;
import com.util.MyUtil;
import com.member.LoginSession;

@WebServlet("/reserve/*")
public class ReserveServlet extends MyServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String cp = req.getContextPath();
		String uri = req.getRequestURI();
		
		
		
		// 예약 리스트 (관리자)
		if(uri.indexOf("list.do") != -1) {			
			list(req, resp);
		// 예약하기 폼 
		} else if(uri.indexOf("reserve.do") != -1) {	
			reserveForm(req, resp);
	    // 예약 하기 
		} else if(uri.indexOf("reserve_ok.do") != -1) {	
			reserveSubmit(req, resp);
	   // 예약 확인 
		} else if(uri.indexOf("checked.do") != -1) {	
			reserveCheck(req, resp);
	   // 예약 수정 
		} else if(uri.indexOf("update.do") != -1) {		
			updateForm(req, resp);
	   // 예약 수정 완료 
		} else if(uri.indexOf("update_ok.do") != -1) {	
			updateSubmit(req, resp);
	   // 예약 취소 
		} else if(uri.indexOf("delete.do") != -1) {		
			delete(req, resp);
		}		
		
	}

	private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ReserveDAO dao = new ReserveDAO();
		MyUtil util = new MyUtil();
		String cp = req.getContextPath();		
		
		String page = req.getParameter("page");
		int current_page = 1;
		if(page!=null) {
			current_page = Integer.parseInt(page);
		}
		
		String condition = req.getParameter("condition");
		String keyword = req.getParameter("keyword");
		if(condition == null) {
			condition = "r_name";
			keyword = "";
		}
		
		if(req.getMethod().equalsIgnoreCase("GET")) {
			keyword = URLDecoder.decode(keyword, "UTF-8");
		}
		
		int dataCount;	
		if(keyword.length() == 0) {
			dataCount = dao.dataCount();		
		} else {
			dataCount = dao.dataCount(condition, keyword);
		}
		
		int rows = 10;
		int total_page = util.pageCount(rows, dataCount);		
		if(current_page > total_page) {
			current_page = total_page;
		}
		
		int offset = (current_page-1) * rows;
		if(offset < 0) {
			offset = 0;
		}
		
		List<ReserveDTO> list;	
		if(keyword.length() == 0) {
			list = dao.listBoard(offset, rows);
		} else {
			list = dao.listBoard(offset, rows, condition, keyword);
		}
		
		int r_listNum, n=0;
		for(ReserveDTO dto : list) {
			r_listNum = dataCount - (offset+n);
			dto.setR_listNum(r_listNum);
			n++;
		}
		
		String query = "";
		if(keyword.length()!=0) {
			query = "condition=" + condition + "&keyword=" + URLEncoder.encode(keyword, "UTF-8");
		}
	
		String listUrl = cp+"/reserve/list.do";	
		if(query.length()!=0) {
			listUrl += "?" + query;
		}
		
		String paging = util.paging(current_page, total_page, listUrl);
		
		req.setAttribute("list", list);
		req.setAttribute("paging", paging);		
		req.setAttribute("page", current_page);		
		req.setAttribute("total_page", total_page);		
		req.setAttribute("dataCount", dataCount);	
		req.setAttribute("condition", condition);
		req.setAttribute("keyword", keyword);
		
		forward(req, resp, "/WEB-INF/views/reserve/list.jsp");
	}
	
	private void reserveForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		req.setAttribute("mode", "reserve");	
		forward(req, resp, "/WEB-INF/views/reserve/reserve.jsp");	
	}
	
	private void reserveSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		if(req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp+"/reserve/reserve.do");
			return;
		}
		
		HttpSession session=req.getSession();
		LoginSession info=(LoginSession)session.getAttribute("loginMem");
		
		ReserveDAO dao = new ReserveDAO();
		ReserveDTO dto = new ReserveDTO();		
		
		if(info == null) {
			dto.setR_id("guest");
		} else {
			dto.setR_id(info.getLoginId());
		}	
		
		String r_month = req.getParameter("r_month");
		
		if(Integer.parseInt(r_month)<10) {
			r_month = "0" + r_month;
		}
		
		dto.setR_date(req.getParameter("r_year")+"-"+r_month+"-"+req.getParameter("r_day"));
		dto.setR_time(req.getParameter("r_time"));
		dto.setR_count(req.getParameter("r_count"));
		dto.setR_name(req.getParameter("r_name"));
		dto.setR_tel(req.getParameter("r_tel"));
		dto.setR_email(req.getParameter("r_email"));
		dto.setR_request(req.getParameter("r_request"));
		
		dao.insertReserve(dto, "reserve");		
		
		resp.sendRedirect(cp+"/reserve/reserve.do");		
	}
	
	private void reserveCheck(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ReserveDAO dao = new ReserveDAO();		
		
		String r_name = req.getParameter("r_name");
		String r_tel = req.getParameter("r_tel");		
		
		if(r_name != null || r_tel != null) {
 		
			ReserveDTO dto = dao.checkReserve(r_name, r_tel);	
			
			if(dto==null) {
				req.setAttribute("msg","예약 정보가 없습니다! <br> 정보를 다시 확인 해 주세요.");
			}
			
			req.setAttribute("dto", dto);	
		}
		
		forward(req, resp, "/WEB-INF/views/reserve/checked.jsp");		
	}
	
	private void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ReserveDAO dao = new ReserveDAO();		
	
		int r_num = Integer.parseInt(req.getParameter("r_num"));
		
		ReserveDTO dto = dao.checkReserve(r_num);
		
		req.setAttribute("dto", dto);			
		
		forward(req, resp, "/WEB-INF/views/reserve/update.jsp");	
	}
	
	private void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		ReserveDAO dao = new ReserveDAO();
		
		/*
		 * if(req.getMethod().equalsIgnoreCase("GET")) {
		 * resp.sendRedirect(cp+"/reserve/checked.do"); return; }
		 */
		
		ReserveDTO dto = new ReserveDTO();
		
		String r_month = req.getParameter("r_month");
		
		if(Integer.parseInt(r_month)<10) {
			r_month = "0" + r_month;
		}
		
		dto.setR_name(req.getParameter("r_name"));
		dto.setR_tel(req.getParameter("r_tel"));
		dto.setR_email(req.getParameter("r_email"));
		dto.setR_request(req.getParameter("r_request"));
		dto.setR_date(req.getParameter("r_year")+"-"+r_month+"-"+req.getParameter("r_day"));
		dto.setR_time(req.getParameter("r_time"));
		dto.setR_count(req.getParameter("r_count"));
		dto.setR_num(Integer.parseInt(req.getParameter("r_num")));
		
		dao.updateReserve(dto);
		
		resp.sendRedirect(cp+"/reserve/checked.do");	
	}
	
	private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		ReserveDAO dao = new ReserveDAO();
		
		int r_num = Integer.parseInt(req.getParameter("r_num"));
		
		dao.deleteReserve(r_num);	
		
		resp.sendRedirect(cp+"/reserve/checked.do");	
	}
}
