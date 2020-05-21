package com.member;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.util.MyServlet;

@WebServlet("/memberSj/*")
public class MemberServlet extends MyServlet {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		
		String uri = req.getRequestURI();

		if (uri.indexOf("login.mem") != -1) {
			login(req, resp);
		} else if (uri.indexOf("loginSubmit.mem") != -1) {
			loginSubmit(req, resp);
		} else if (uri.indexOf("insert.mem") != -1) {

		} else if (uri.indexOf("insertSubmit.mem") != -1) {

		} else if (uri.indexOf("update.mem") != -1) {

		} else if (uri.indexOf("updateSubmit.mem") != -1) {

		} else if (uri.indexOf("delete.mem") != -1) {

		} // else if(uri.indexOf("insert.mem") != -1){
//			
//		}

	}

	protected void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String path="/WEB-INF/views/memberSj/login.jsp";

		forward(req, resp, path);
	}

	protected void loginSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String cp = req.getContextPath();
		MemberDAO dao = new MemberDAO();

		String m_id = req.getParameter("userId");
		String m_pwd = req.getParameter("userPwd");

		MemberDTO dto = dao.readMember(m_id);

		if (!dto.getPassword().equals(m_pwd)) {
			String arr = "아이디 또는 패스워드가 일치하지 않습니다.";
			req.setAttribute("msg", arr);
			forward(req, resp, "/WEB-INF/views/memberSj/login.jsp");
			return;
		}
		HttpSession session = req.getSession(); // 로그인 성공
//		// 세션 기본 로그인 시간 30분

		LoginSession ls = new LoginSession();
		
		ls.setLoginId(dto.getId());
		ls.setLoginName(dto.getName());

		session.setAttribute("loginMem", ls);

		resp.sendRedirect(cp);
	}
}
