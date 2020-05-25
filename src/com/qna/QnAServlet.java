package com.qna;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.member.LoginSession;
import com.util.MyServlet;
import com.util.MyUtil;

@WebServlet("/qna/*")
public class QnAServlet extends MyServlet{
	private static final long serialVersionUID = 1L;

	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String cp = req.getContextPath();
		String uri = req.getRequestURI();
		
		HttpSession session = req.getSession();
		LoginSession login = (LoginSession)session.getAttribute("loginMem");
		
		if(login==null ) {
			resp.sendRedirect(cp+"/memberSj/login.mem");			// 로그인폼으로....
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
		} else if(uri.indexOf("update.do")!=-1) {
			updateForm(req, resp);
		} else if(uri.indexOf("update_ok.do")!=-1) {
			updateSubmit(req, resp);
		} else if(uri.indexOf("delete.do")!=-1) {
			deleteQnA(req, resp);
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
			condition = "q_title";			
		}
		int dataCount;
		if(keyword==null || keyword.length()!=0) {
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
		if(keyword==null || keyword.length()==0) {
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
		if(keyword!=null && keyword.length() != 0) {
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
		
//		if(!login.getLoginId().equals(dto.getId())) {
//			resp.sendRedirect(cp+"/qna/list.do?page="+page);
//			return;
//		}
		
		if(login==null || login.getLoginId()==null) {
			resp.sendRedirect(cp+"/qna/list.do?page="+page);
			return;
		}

			
		dto.setTitle(req.getParameter("title"));
		dto.setContent(req.getParameter("content"));
		dto.setId(login.getLoginId());
		dto.setName(login.getLoginName());
		dao.insertQnA(dto, "created");
		
		resp.sendRedirect(cp+"/qna/list.do");
	}
	
	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글보기
		QnADAO dao = new QnADAO();
		String cp = req.getContextPath();
		MyUtil util = new MyUtil();
		
		int num = Integer.parseInt(req.getParameter("num"));
		String page = req.getParameter("page");
		
		String condition = req.getParameter("condition");
		String keyword = req.getParameter("keyword");
		if(condition == null) {
			condition = "title";
			keyword = "";
		}
		keyword = URLDecoder.decode(keyword, "UTF-8");
		
		String query = "page="+page;
		if(keyword.length() != 0) {
			query += "&condition="+condition+"&keyword="+URLEncoder.encode(keyword, "UTF-8");
		}
		
		dao.updateHitCount(num);
		
		QnADTO dto = dao.readQnA(num);
		if(dto==null) {
			resp.sendRedirect(cp+"/qna/list.do?"+query);
			return;
		}
		
		dto.setContent(util.htmlSymbols(dto.getContent()));
		
		QnADTO preReadDto = dao.preReadQnA(dto.getNum(), condition, keyword);
		QnADTO nextReadDto = dao.nextReadQnA(dto.getNum(), condition, keyword);
		
		req.setAttribute("dto", dto);
		req.setAttribute("preReadDto", preReadDto);
		req.setAttribute("nextReadDto", nextReadDto);
		req.setAttribute("query", query);
		req.setAttribute("page", page);

		forward(req, resp, "/WEB-INF/views/qna/article.jsp");
	}
	
	protected void replyForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 답변 폼
		QnADAO dao = new QnADAO();
		String cp = req.getContextPath();
		
		int num = Integer.parseInt(req.getParameter("num"));
		String page = req.getParameter("page");
		
		QnADTO dto = dao.readQnA(num);
		if(dto==null) {
			resp.sendRedirect(cp+"/qna/list.do?page="+page);
			return;
		}
		
		String s = "["+dto.getTitle()+"] 에 대한 답변입니다.\n";
		dto.setContent(s);
		
		req.setAttribute("mode", "reply");
		req.setAttribute("dto", dto);
		req.setAttribute("page", page);
		
		forward(req, resp, "/WEB-INF/views/qna/created.jsp");
	}
	
	protected void replySubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 답변 완료
		String cp = req.getContextPath();
		if(req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp+"/qna/list.do");
			return;
		}
		
		HttpSession session = req.getSession();
		LoginSession login = (LoginSession)session.getAttribute("loginMem");
		
		QnADAO dao = new QnADAO();
		QnADTO dto = new QnADTO();
		
		dto.setTitle(req.getParameter("title"));
		dto.setContent(req.getParameter("content"));
		dto.setGroupNum(Integer.parseInt(req.getParameter("q_groupNum")));
		dto.setOrderNum(Integer.parseInt(req.getParameter("q_orderNum")));
		dto.setDepth(Integer.parseInt(req.getParameter("q_depth")));
		dto.setParent(Integer.parseInt(req.getParameter("q_parent")));
		dto.setName(login.getLoginName());
		
		dto.setId(login.getLoginId());
		
		dao.insertQnA(dto, "reply");
		
		String page = req.getParameter("page");
		
		resp.sendRedirect(cp+"/qna/list.do?page="+page);
		
	}
	
	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정 폼
		HttpSession session = req.getSession();
		LoginSession login = (LoginSession)session.getAttribute("loginMem");
		
		String cp = req.getContextPath();
		QnADAO dao = new QnADAO();
		
		String page = req.getParameter("page");
		String condition = req.getParameter("condition");
		String keyword = req.getParameter("keyword");
		if(condition == null) {
			condition ="q_title";
			keyword="";
		}
		keyword = URLDecoder.decode(keyword, "UTF-8");
		String query = "page="+page;
		if(keyword.length() != 0) {
			query+="&condition="+condition+"&keyword="+URLEncoder.encode(keyword, "UTF-8");
		}
		
		int num = Integer.parseInt(req.getParameter("num"));
		QnADTO dto = dao.readQnA(num);
		
		if(dto==null) {
			resp.sendRedirect(cp+"/qna/list.do?"+query);
			return;
		}
		
		if(!dto.getId().equals(login.getLoginId())) {
			resp.sendRedirect(cp+"/qna/list.do?"+query);
			return;
		}
		
		req.setAttribute("dto", dto);
		req.setAttribute("page", page);
		req.setAttribute("condition", condition);
		req.setAttribute("keyword", keyword);
		req.setAttribute("mode", "update");
		
		forward(req, resp, "/WEB-INF/views/qna/created.jsp");
	}
	
	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정 완료
		HttpSession session = req.getSession();
		LoginSession login = (LoginSession)session.getAttribute("loginMem");
		
		String cp = req.getContextPath();
		if(req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp+"/qna/list.do");
			return;
		}
		
		QnADAO dao = new QnADAO();
		
		String page = req.getParameter("page");
		String condition = req.getParameter("condition");
		String keyword = req.getParameter("keyword");
		String query = "page="+page;
		if(keyword.length()!=0) {
			query+="&condition="+condition+"&keyword="+URLEncoder.encode(keyword, "UTF-8");
		}
		
		QnADTO dto = new QnADTO();
		dto.setNum(Integer.parseInt(req.getParameter("num")));
		dto.setTitle(req.getParameter("title"));
		dto.setContent(req.getParameter("content"));
		
		dao.updateqna(dto, login.getLoginId());
		
		resp.sendRedirect(cp+"/qna/list.do?"+query);
	}
	
	protected void deleteQnA(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		LoginSession login = (LoginSession)session.getAttribute("loginMem");
		
		String cp = req.getContextPath();
		QnADAO dao = new QnADAO();
		
		String page = req.getParameter("page");
		String condition = req.getParameter("condition");
		String keyword = req.getParameter("keyword");
		if(condition==null) {
			condition = "q_title";
			keyword="";
		}
		keyword = URLDecoder.decode(keyword, "UTF-8");
		String query = "page="+page;
		if(keyword.length()!=0) {
			query+="&condition="+condition+"&keyword="+URLEncoder.encode(keyword, "UTF-8");
		}
		
		int num = Integer.parseInt(req.getParameter("num"));
		QnADTO dto = dao.readQnA(num);
		
		if(dto == null) {
			resp.sendRedirect(cp+"/qna/list.do?"+query);
			return;
		}
		
		if(!dto.getId().equals(login.getLoginId()) && ! login.getLoginId().equals("admin")) {
			resp.sendRedirect(cp+"/qna/list.do?"+query);
			return;
		}
		
		dao.delete(num);
		resp.sendRedirect(cp+"/qna/list.do?"+query);
	}
	
}
