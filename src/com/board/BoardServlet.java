package com.board;

import com.util.MyServlet;
import com.util.MyUtil;

import com.member.LoginSession;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/board/*")
public class BoardServlet extends MyServlet {
	private static final long serialVersionUID = 1L;

		@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String cp = req.getContextPath();

		HttpSession session = req.getSession();
		LoginSession ls = (LoginSession)session.getAttribute("loginMem");
		
		if(ls == null) {
			resp.sendRedirect(cp+"/memberSj/login.mem");
			return;
		}
		
		String uri = req.getRequestURI();

		if (uri.indexOf("list.board") != -1) {
			list(req, resp);
		} else if (uri.indexOf("insert.board") != -1) {
			insert(req, resp);
		} else if (uri.indexOf("insertSubmit.board") != -1) {
			insertSubmit(req, resp);
		} else if (uri.indexOf("update.board") != -1) {

		} else if (uri.indexOf("updateSubmit.board") != -1) {

		} else if (uri.indexOf("delete.board") != -1) {

		} else if (uri.indexOf("article.board") != -1) {
			article(req, resp);
		}
	}

	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		MyUtil mu = new MyUtil();
		BoardDAO dao = new BoardDAO();
		
		String page = req.getParameter("page");
		
		int currentPage = 1;
		if(page != null) {
			currentPage = Integer.parseInt(page);
		}
		
		String type = req.getParameter("type");
		String keyword = req.getParameter("keyword");
		
		if(type == null) {
			type = "title";
			keyword = "";
		}
		
		if(req.getMethod().equalsIgnoreCase("GET")) {
			keyword = URLDecoder.decode(keyword,"utf-8");
		}
		
		int dataCnt;
		if(keyword.length() == 0) {
			dataCnt = dao.dataCnt();
		}else {
			dataCnt = dao.dataCnt(type, keyword);
		}
		
		int rows = 10;
		int totalPage = mu.pageCount(rows, dataCnt);
		if(currentPage > totalPage) {
			currentPage = totalPage;
		}
		
		int startNum = (currentPage -1 )* rows;
		
		
		List<BoardDTO> list;
		if(keyword.length() == 0) {
			list = dao.listBoard(startNum, rows);
		}else {
			list = dao.listBoard(startNum, rows, type, keyword);
		}
		
		int listNum, n = 0;
		for(BoardDTO dto : list) {
			listNum = dataCnt - (startNum + n);
			dto.setNum(listNum);
			n++;
		}
		
		String query = "";
		if(keyword.length() != 0) {
			query = "type=" + type + "&keyword=" + URLEncoder.encode(keyword, "utf-8");
		}
		
		String listUrl  = cp + "/board/list.board";
		String articleUrl = cp  + "/board/article.board?page=" + currentPage;
		if(query.length() != 0) {
			listUrl += "?" + query;
			articleUrl += "&" + query;
		}
		
		String paging = mu.paging(currentPage, totalPage, listUrl);
		
		req.setAttribute("list", list);
		req.setAttribute("dataCnt", dataCnt);
		req.setAttribute("page", currentPage);
		req.setAttribute("totalPage", totalPage);
		req.setAttribute("articleUrl", articleUrl);
		req.setAttribute("paging", paging);
		req.setAttribute("type", type);
		req.setAttribute("keyword", keyword);
		
		String path = "/WEB-INF/views/board/list.jsp";
		
		forward(req, resp, path);
	}

	protected void insert(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("mode", "insert");
		
		String path = "/WEB-INF/views/board/create.jsp";
		forward(req, resp, path);
	}

	protected void insertSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		BoardDAO dao = new BoardDAO();
		
		if(req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp+"/board/list.jsp");
			return;
		}
		
		HttpSession session = req.getSession();
		LoginSession ls = (LoginSession)session.getAttribute("loginMem");
		
		BoardDTO dto = new BoardDTO();
		
		dto.setId(ls.getLoginId());
		dto.setTitle(req.getParameter("title"));
		dto.setContent(req.getParameter("content"));
		dto.setWriter(ls.getLoginName());
		
		dao.insertBoard(dto);
		
		resp.sendRedirect(cp+"/board/list.board");
	}

	protected void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	}

	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	}

	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	}
	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BoardDAO dao = new BoardDAO();
		String cp = req.getContextPath();
		
		int num = Integer.parseInt(req.getParameter("num"));
		String page = req.getParameter("page");
		String rows = req.getParameter("rows");
		String type = req.getParameter("type");
		String keyword = req.getParameter("keyword");
		MyUtil mu = new MyUtil();
		
		if(type == null) {
			type = "title";
			keyword = "";
		}
		keyword = URLDecoder.decode(keyword, "utf-8");
		
		String query = "page=" + page + "&rows=" + rows;
		if(keyword.length() != 0) {
			query += "&type=" + type+ "&keyword=" + URLEncoder.encode(keyword,"utf-8");
		}
		
		/*
		 * 히드카운트
		 * */
		
		BoardDTO dto = dao.readBoard(num);
		if(dto == null) {
			resp.sendRedirect(cp+"/board/list.board" + query);
			return;
		}
		
		
		dto.setContent(mu.htmlSymbols(dto.getContent()));
		
		BoardDTO preDTO = dao.preBoard(num, type, keyword);
		BoardDTO nextDTO = dao.nextBoard(num, type, keyword);
		
		req.setAttribute("dto", dto);
		req.setAttribute("page", page);
		req.setAttribute("query", query);
		req.setAttribute("preDTO", preDTO);
		req.setAttribute("nextDTO", nextDTO);
		req.setAttribute("rows", rows);
		
		
		String path = "/WEB-INF/views/board/article.jsp";
		forward(req, resp, path);
	}
}
