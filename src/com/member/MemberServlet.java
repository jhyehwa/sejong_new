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
			insert(req, resp);
		} else if (uri.indexOf("insertSubmit.mem") != -1) {
//			
		} else if (uri.indexOf("update.mem") != -1) {

		} else if (uri.indexOf("updateSubmit.mem") != -1) {

		} else if (uri.indexOf("delete.mem") != -1) {

		} else if (uri.indexOf("logout.mem") != -1) {
			logout(req, resp);
		} else if(uri.indexOf("idCheck.mem") != -1) {
			
		} else if(uri.indexOf("idCheck.mem") != -1) {
			
		}

	}

	protected void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String path = "/WEB-INF/views/memberSj/login.jsp";

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

	protected void logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();

		HttpSession session = req.getSession();
		session.invalidate();
		
		resp.sendRedirect(cp);
	}
	
	protected void insert(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("subject", "회원 가입");
		req.setAttribute("mode", "created");
		
		String path = "/WEB-INF/views/memberSj/create.jsp";
		
		forward(req, resp, path);
	}
	protected void insertSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		MemberDAO dao = new MemberDAO();
		MemberDTO dto = new MemberDTO();
		
		dto.setId(req.getParameter("memberId"));
		dto.setPassword(req.getParameter("memberPassword"));
		dto.setName(req.getParameter("memberName"));
		dto.setBirth(req.getParameter("memberBirth"));
		String email1 = req.getParameter("email1");
		String email2 = req.getParameter("email2");
		if(email1.length() != 0 && email2.length() != 0) {
			dto.setEmail(email1 + "@" + email2);
		}
		String tel1 = req.getParameter("tel1");
		String tel2 = req.getParameter("tel2");
		String tel3 = req.getParameter("tel3");
		
		if(tel1.length() != 0 && tel2.length() != 0 &&tel3.length() != 0) {
			dto.setTel(tel1 + "-" + tel2 + "-" + tel3);
		}
		
		dto.setAddr1(req.getParameter("addr1"));
		dto.setAddr2(req.getParameter("addr2"));
		
		if(dao.readMember(dto.getId()) != null) {
			String message = "사용할 수 없는 아이디입니다.";
			
			
			return;
		}
		
	}
	protected void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	}
	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	}
	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	}
	protected void idCheck(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	}
	protected void pwdCheck(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	}
}
