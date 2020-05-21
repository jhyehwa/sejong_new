package com.notice;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.member.LoginSession;
import com.util.MyServlet;
import com.util.MyUtil;

@WebServlet("/notice/*")
public class NoticeServlet extends MyServlet{
	private static final long serialVersionUID = 1L;

	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String uri = req.getRequestURI();
		
		if(uri.indexOf("created.do")!=-1) {
			// 공지&이벤트 등록 폼
			createdNoticeForm(req, resp);
		} else if(uri.indexOf("created_ok.do")!=-1) {
			// 공지&이벤트 등록
			createdNoticeSubmit(req, resp);
		} else if(uri.indexOf("list.do")!=-1) {
			// 글 리스트
			list(req, resp);
		} else if(uri.indexOf("article.do")!=-1) {
			// 글보기
			article(req, resp);
		} else if(uri.indexOf("update.do")!=-1) {
			// 수정 폼
			updateForm(req, resp);
		} else if(uri.indexOf("update_ok.do")!=-1) {
			// 수정 완료
			updateSubmit(req, resp);
		}
	}
	
	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		NoticeDAO dao = new NoticeDAO();
		MyUtil myUtil = new MyUtil();
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		int now_page = 1;
		if(page != null) {
			now_page = Integer.parseInt(page);
		}
		
		String condition = req.getParameter("condition");
		if(condition == null) condition = "";
		
		List<NoticeDTO> listNotice = null;
		if(condition.length()==0 || condition.equals("notice")) {
			listNotice = dao.listNotice();
			for(NoticeDTO dto : listNotice) {
				dto.setStartDate(dto.getStartDate().substring(0,10));
			}
		}

		int dataCount=0;
		List<NoticeDTO> listEvent = null;
		int rows=10;
		int total_page = 0;
		
		if(condition.length()==0||condition.equals("created")||condition.equals("closed")) {
			if(condition.length()!=0) {
				dataCount = dao.dataCount(condition);
			} else {
				dataCount = dao.dataCount();
			}
		
			total_page = myUtil.pageCount(rows, dataCount);
			if(now_page>total_page) {
				now_page = total_page;
			}
			
			int offset = (now_page -1) * rows;
			if(offset < 0) {
				offset = 0;
			}
			
			if(condition.length() != 0) {
				listEvent = dao.listevent(offset, rows, condition);
			} else {
				listEvent = dao.listevent(offset, rows);
			}
			for(NoticeDTO dto : listEvent) {
				dto.setStartDate(dto.getStartDate().substring(0,10));
			}
			
			long gap;
			Date curDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			// 리스트 글번호 만들기
			int listNum, n = 0;
			for(NoticeDTO dto : listEvent) {
				listNum = dataCount - (offset + n);
				dto.setListNum(listNum);
				
				try {
					Date date = sdf.parse(dto.getStartDate());
					
					gap = (curDate.getTime() - date.getTime()) / (1000*60*60*12);
					dto.setGap(gap);
					
				} catch (Exception e) {
				}
				dto.setStartDate(dto.getStartDate().substring(0, 10));
				n++;
			}
		}
		
		String query = "";
		if(condition.length()!=0) {
			query="condition="+condition;
		}
		
		String listUrl = cp + "/notice/list.do";
		String articleUrl = cp + "/notice/article.do?page=" + now_page;
		if(query.length()!=0) {
			listUrl += "&"+query;
			articleUrl=articleUrl+"&"+query;
		}
		
		String paging = myUtil.paging(now_page, total_page, listUrl);
		
		req.setAttribute("listEvent", listEvent);
		req.setAttribute("listNotice", listNotice);
		req.setAttribute("paging", paging);
		req.setAttribute("total_page", total_page);
		req.setAttribute("page", now_page);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("articleUrl", articleUrl);
		req.setAttribute("condition", condition);

		forward(req, resp, "/WEB-INF/views/notice/list.jsp");
	}
	
	protected void createdNoticeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("mode", "created");
		forward(req, resp, "/WEB-INF/views/notice/created.jsp");
		
	}
	
	protected void createdNoticeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		NoticeDTO dto = new NoticeDTO();
		NoticeDAO dao = new NoticeDAO();
		
		String enabled = req.getParameter("enabled");
		
		dto.setTitle(req.getParameter("title"));
		dto.setWriter(req.getParameter("writer"));
		dto.setContent(req.getParameter("content"));
		if(enabled!=null) {
			dto.setEnabled(Integer.parseInt(enabled));
			dto.setFinishDate(req.getParameter("finishDate"));
		}
		
		dao.insertnotice(dto);
		req.setAttribute("enabled", enabled);
		resp.sendRedirect(cp+"/notice/list.do");
		
	}
	
	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		NoticeDAO dao = new NoticeDAO();
		String cp = req.getContextPath();
		MyUtil myutil = new MyUtil();
		
		int num = Integer.parseInt(req.getParameter("num"));
		String page = req.getParameter("page");
		
		String condition = req.getParameter("condition");
		
		String query = "page=" + page;
		if(condition==null) condition = "";
		if(condition.length() != 0) {
			query +="&condition=" + condition;
		}
		
		dao.hitCount(num);
		
		NoticeDTO dto = dao.readNotice(num);
		if(dto == null) {
			resp.sendRedirect(cp+"/notice/list.do?"+query);
			return;
		}
		
		dto.setContent(myutil.htmlSymbols(dto.getContent()));
		
		NoticeDTO preReadDto = dao.prereadNotice(num, condition);
		NoticeDTO nextReadDto = dao.nextreadNotice(num, condition);
		
		req.setAttribute("dto", dto);
		req.setAttribute("page", page);
		req.setAttribute("query", query);
		req.setAttribute("preReadDto", preReadDto);
		req.setAttribute("nextReadDto", nextReadDto);

		forward(req, resp, "/WEB-INF/views/notice/article.jsp");
	}
	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		NoticeDAO dao = new NoticeDAO();
		String cp = req.getContextPath();
		
		HttpSession session = req.getSession();
		LoginSession login = (LoginSession)session.getAttribute("loginMem");
		
		int num = Integer.parseInt(req.getParameter("num"));
		String page = req.getParameter("page");
		
		NoticeDTO dto = dao.readNotice(num);
		if(dto==null) {
			resp.sendRedirect(cp+"/notice/list.do?page="+page);
			return;
		}
		
		if(!login.getLoginName().equals(dto.getWriter())) {
			resp.sendRedirect(cp+"/notice/list.do?page="+page);
			return;
		}
		
		req.setAttribute("dto", dto);
		req.setAttribute("page", page);
		req.setAttribute("mode", "update");
		
		dao.updateNotice(dto);
		
		forward(req, resp, "/WEB-INF/views/notice/created.jsp");
	}
	
	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		NoticeDAO dao = new NoticeDAO();
		String cp = req.getContextPath();
		
		NoticeDTO dto = new NoticeDTO();
		
		String page = req.getParameter("page");
		int num = Integer.parseInt(req.getParameter("num"));
		
		dto.setNum(num);
		if(req.getParameter("enabled")!=null) {
			dto.setEnabled(Integer.parseInt(req.getParameter("enabled")));
		}
		dto.setTitle(req.getParameter("title"));
		dto.setContent(req.getParameter("content"));
		dto.setFinishDate(req.getParameter("finishDate"));
		
		dao.updateNotice(dto);
		
		resp.sendRedirect(cp+"/notice/list.do?page="+page);
		
	}
		

}
