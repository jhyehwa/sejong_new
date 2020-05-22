package com.menu;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.member.LoginSession;
import com.util.FileManager;
import com.util.MyUploadServlet;
import com.util.MyUtil;

@WebServlet("/foodmenu/*")
@MultipartConfig(maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class FoodMenuServlet extends MyUploadServlet {
	private static final long serialVersionUID = 1L;

	private String pathname;

	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");

		String uri = req.getRequestURI();
		HttpSession session = req.getSession();

		String root = session.getServletContext().getRealPath("/");
		pathname = root + File.separator + "uploads" + File.separator + "f_food";

		if (uri.indexOf("list.do") != -1) {
			// 리스트
			list(req, resp);
		} else if (uri.indexOf("created.do") != -1) {
			// 메뉴 폼
			createdForm(req, resp);
		} else if (uri.indexOf("created_ok.do") != -1) {
			// 메뉴 등록
			createdSubmit(req, resp);
		} else if (uri.indexOf("article.do") != -1) {
			// 메뉴 보기
			article(req, resp);
		} else if (uri.indexOf("update.do") != -1) {
			// 메뉴 수정
			updateForm(req, resp);
		} else if (uri.indexOf("update_ok.do") != -1) {
			// 메뉴 수정 완료
			updateSubmit(req, resp);
		} else if (uri.indexOf("delete.do") != -1) {
			// 메뉴 삭제
			delete(req, resp);
		} else if (uri.indexOf("deleteFile.do") != -1) {
			// 이미지 삭제
			deleteFile(req, resp);
		}
	}

	private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		FoodMenuDAO dao = new FoodMenuDAO();
		MyUtil util = new MyUtil();
		String cp = req.getContextPath();

//		HttpSession session = req.getSession();
//		LoginSession ls = (LoginSession) session.getAttribute("loginMem");

		String page = req.getParameter("page");
		String f_type = req.getParameter("f_type");
		if (f_type == null) {
			f_type = "main";
		}

		int current_page = 1;
		if (page != null) {
			current_page = Integer.parseInt(page);
		}

		int dataCount = dao.dataCount(f_type);

		int rows = 4;
		int total_page = util.pageCount(rows, dataCount);
		if (current_page > total_page) {
			current_page = total_page;
		}

		int offset = (current_page - 1) * rows;
		if (offset < 0) {
			offset = 0;
		}

		List<FoodMenuDTO> list = dao.listMain(offset, rows, f_type);

		String listUrl = cp + "/foodmenu/list.do?f_type=" + f_type;
		String articleUrl = cp + "/foodmenu/article.do?page=" + current_page + "&f_type=" + f_type;
		String paging = util.paging(current_page, total_page, listUrl);

//		req.setAttribute(이름, ls);
		req.setAttribute("list", list);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("articleUrl", articleUrl);
		req.setAttribute("page", current_page);
		req.setAttribute("total_page", total_page);
		req.setAttribute("paging", paging);
		req.setAttribute("f_type", f_type);

		forward(req, resp, "/WEB-INF/views/foodmenu/list.jsp");
	}

	private void createdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		HttpSession session = req.getSession();
//		LoginSession ls = (LoginSession)session.getAttribute("loginMem");

//		if( ls.getLoginId()==null || !ls.getLoginId().equals("admin")) {
//			resp.sendRedirect(req.getContextPath() + "/foodmenu/list.do");
//			return;
//		}

		req.setAttribute("mode", "created");

		forward(req, resp, "/WEB-INF/views/foodmenu/created.jsp");
	}

	private void createdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		/*
		 * HttpSession session = req.getSession(); LoginSession ls = (LoginSession)
		 * session.getAttribute("loginMem");
		 * 
		 * if (!ls.getLoginId().equals("admin")) { resp.sendRedirect(cp +
		 * "/foodmenu/list.do"); return; }
		 */

		FoodMenuDAO dao = new FoodMenuDAO();
		FoodMenuDTO dto = new FoodMenuDTO();

		dto.setF_name(req.getParameter("f_name"));
		dto.setF_price(req.getParameter("f_price"));
		dto.setF_type(req.getParameter("f_type"));
		dto.setF_intro(req.getParameter("f_intro"));
		dto.setF_image(req.getParameter("f_image"));

		System.out.println(dto.getF_name() + ":" + dto.getF_price());

		String fullpath = null;

		Part p = req.getPart("f_food");
		Map<String, String> map = doFileUpload(p, pathname);
		if (map != null) {
			String f_image = map.get("saveFilename");
			fullpath = map.get("fullpath");

			dto.setF_image(f_image);
		}

		System.out.println(fullpath);

		try {
			dao.insertFoodMenu(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}

		resp.sendRedirect(cp + "/foodmenu/list.do");
	}

	private void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		FoodMenuDAO dao = new FoodMenuDAO();

		int f_num = Integer.parseInt(req.getParameter("f_num"));
		String page = req.getParameter("page");

		// 게시물 가져오기
		FoodMenuDTO dto = dao.readFoodMenu(f_num);
		if (dto == null) {
			resp.sendRedirect(cp + "/foodmenu/list.do?=page" + page);
			return;
		}

		dto.setF_intro(dto.getF_intro().replaceAll("\n", "<br>"));

		req.setAttribute("dto", dto);
		req.setAttribute("page", page);

		forward(req, resp, "/WEB-INF/views/foodmenu/article.jsp");
	}

	private void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		HttpSession session = req.getSession();
		LoginSession ls = (LoginSession) session.getAttribute("loginMem");

		FoodMenuDAO dao = new FoodMenuDAO();

		String page = req.getParameter("page");
		int f_num = Integer.parseInt(req.getParameter("f_num"));

		// 게시물 가져오기
		FoodMenuDTO dto = dao.readFoodMenu(f_num);
		if (dto == null) {
			resp.sendRedirect(cp + "/foodmenu/list.do?page=" + page);
			return;
		}

		// 게시물을 올린 사용자가 아니면
		if (!ls.getLoginId().equals("admin")) {
			resp.sendRedirect(cp + "/foodmenu/list.do?page=" + page);
			return;
		}

		req.setAttribute("dto", dto);
		req.setAttribute("page", page);
		req.setAttribute("mode", "update");

		forward(req, resp, "/WEB-INF/views/foodmenu/created.jsp");
	}

	private void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		FoodMenuDAO dao = new FoodMenuDAO();
		FoodMenuDTO dto = new FoodMenuDTO();

		int f_num = Integer.parseInt(req.getParameter("f_num"));
		String page = req.getParameter("page");

		dto.setF_num(f_num);
		dto.setF_name(req.getParameter("f_name"));
		dto.setF_price(req.getParameter("f_price"));
		dto.setF_type(req.getParameter("f_type"));
		dto.setF_image(req.getParameter("f_image"));
		dto.setF_intro(req.getParameter("f_intro"));

		Part p = req.getPart("f_food");
		Map<String, String> map = doFileUpload(p, pathname);
		if (map != null) {
			// 기존파일 삭제
			if (req.getParameter("f_image").length() != 0) {
				FileManager.doFiledelete(pathname, req.getParameter("f_image"));
			}

			// 새로운 파일
			String imageFileName = map.get("saveFilename");
			dto.setF_image(imageFileName);
		}

		dao.updateFoodMenu(dto);

		resp.sendRedirect(cp + "/foodmenu/list.do?page=" + page);
	}

	private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		HttpSession session = req.getSession();
		LoginSession ls = (LoginSession) session.getAttribute("loginMem");

		FoodMenuDAO dao = new FoodMenuDAO();

		String page = req.getParameter("page");
		int f_num = Integer.parseInt(req.getParameter("f_num"));

		FoodMenuDTO dto = dao.readFoodMenu(f_num);
		if (dto == null) {
			resp.sendRedirect(cp + "/foodmenu/list.do?page=" + page);
			return;
		}

		if (!ls.getLoginId().equals("admin")) {
			resp.sendRedirect(cp + "/foodmenu/list.do?page=" + page);
			return;
		}

		FileManager.doFiledelete(pathname, dto.getF_image());

		dao.deleteFoodMenu(f_num);

		resp.sendRedirect(cp + "/foodmenu/list.do?" + page);
	}

	private void deleteFile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		LoginSession ls = (LoginSession) session.getAttribute("loginMem");

		String cp = req.getContextPath();
		FoodMenuDAO dao = new FoodMenuDAO();

		String page = req.getParameter("page");
		int f_num = Integer.parseInt(req.getParameter("f_num"));

		FoodMenuDTO dto = dao.readFoodMenu(f_num);
		if (dto == null) {
			resp.sendRedirect(cp + "/foodmenu/list.do?page=" + page);
			return;
		}

		if (!ls.getLoginId().equals("admin")) {
			resp.sendRedirect(cp + "/foodmenu/list.do?page=" + page);
			return;
		}

		// 파일 삭제
		FileManager.doFiledelete(pathname, dto.getF_image());

		// 파일명과 파일 크기 변경
		dto.setF_image("");
		dao.updateFoodMenu(dto);

		req.setAttribute("dto", dto);
		req.setAttribute("page", page);

		req.setAttribute("mode", "update");

		forward(req, resp, "/WEB-INF/views/foodmenu/created.jsp");
	}
}
