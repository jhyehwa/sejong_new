package com.qna;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.member.LoginSession;
import com.util.MyServlet;
import com.util.MyUtil;

public class QnAServlet extends MyServlet{
	private static final long serialVersionUID = 1L;

	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String cp = req.getContextPath();
		String uri = req.getRequestURI();
		
		HttpSession session = req.getSession();
		LoginSession login = (LoginSession)session.getAttribute("loginMem");
		
		if(uri.indexOf("list.do")!=-1 && login==null ) {
			resp.sendRedirect(cp+"/");			// 로그인폼으로....
			return;
		}
		
		if(uri.indexOf("list.do")!=-1) {
			list(req, resp);
		} else if(uri.indexOf("created.do")!=-1) {
			createdForm(req, resp);
		} else if(uri.indexOf("created_ok.do")!=-1) {
			createdSubmit(req, resp);
		} else if(uri.indexOf("article.do")!=-1) {
			article(req, resp);
		} else if(uri.indexOf("reply.do")!=-1) {
			replyForm(req, resp);
		} else if(uri.indexOf("reply_ok.do")!=-1) {
			replySubmit(req, resp);
		}
	}
	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글 리스트
		MyUtil util = new MyUtil();
		QnADAO dao = new QnADAO();
		String cp = req.getContextPath();
		
		String page = req.getParameter("page");
		int now_page = 1;
		if(page != null) {
			now_page = Integer.parseInt(page);
		}
		
		String condition = req.getParameter("condition");
		String keyword = req.getParameter("keyword");
		if(condition == null) {
			condition = "";
		}
		
		if(req.getMethod().equalsIgnoreCase("GET")) {
			keyword = URLDecoder.decode(keyword, "UTF-8");
		}
		
		int dataCount;
		if(keyword.length()==0) {
			dataCount = dao.dataCount();
		} else {
			dataCount = dao.dataCount(condition, keyword);
		}
		
		int rows = 10;
		int total_page = util.pageCount(rows, dataCount);
		if(now_page>total_page) {
			now_page = total_page;
		}
		
		int offset = (now_page -1) * rows;
		
		List<QnADTO> list;
		if(keyword.length()==0) {
			list = dao.listQnA(offset, rows);
		} else {
			list = dao.listQnA(offset, rows, condition, keyword);
		}
		
		int listNum, n=0;
		for(QnADTO dto : list) {
			listNum = dataCount - (offset + n);
			dto.setListNum(listNum);
			n++;
		}
		
		String query = "";
		if(keyword.length() != 0) {
			query = "condition = " + condition + "&keyword=" + URLEncoder.encode(keyword, "UTF-8");
		}
		
		String listUrl = cp + "/qna/list.do";
		String articleUrl = cp + "/qna/article.do?page="+ now_page;
		if(query.length()!=0) {
			listUrl += "?" + query;
			articleUrl += "&" + query;
		}
		
		String paging = util.paging(now_page, total_page, listUrl);
		
		req.setAttribute("list", list);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("page", now_page);
		req.setAttribute("total_page", total_page);
		req.setAttribute("articleUrl", articleUrl);
		req.setAttribute("paging", paging);
		req.setAttribute("condition", condition);
		req.setAttribute("keyword", keyword);
		
		forward(req, resp, "/WEB-INF/views/qna/list.jsp");
	}
	
	protected void createdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글등록 폼
		req.setAttribute("mode", "created");
		forward(req, resp, "/WEB-INF/views/qna/created.jsp");
	}
	
	protected void createdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글등록
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		if(req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp+"/qna/list.do");
			return;
		}
		
		HttpSession session = req.getSession();
		LoginSession login = (LoginSession)session.getAttribute("loginMem");
		
		QnADAO dao = new QnADAO();
		QnADTO dto = new QnADTO();
		
		if(!login.getLoginId().equals(dto.getId())) {
			resp.sendRedirect(cp+"/qna/list.do?page="+page);
		}
			
		dto.setTitle(req.getParameter("title"));
		dto.setContent(req.getParameter("content"));
		dto.setId(req.getParameter("id"));
		
		dao.insertQnA(dto, "created");
		
		resp.sendRedirect(cp+"/qna/list.do");
	}
	
	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	}
	
	protected void replyForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("mode", "reply");
		forward(req, resp, "WEB-INF/views/qna/list.created.jsp");
	}
	
	protected void replySubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	}
}
