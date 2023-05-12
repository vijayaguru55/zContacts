package login;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import dto.Credentials;
import dto.User;
import repository.Contactsrepository;
@WebServlet("/loginPage")
public class LoginViewModel extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private static LoginViewModel loginViewModel;
	private LoginView loginView;

	public static LoginViewModel getInsatnce() {
		if (loginViewModel == null) {
			loginViewModel = new LoginViewModel();
			return loginViewModel;
		}
		return loginViewModel;
	}

	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		PrintWriter out = resp.getWriter();
		String type = req.getParameter("type");
		HttpSession session = req.getSession();
		String name = req.getParameter("username");
		String mobilenumber = req.getParameter("mobilenumber");
		String pass = req.getParameter("password");
		User user;
		resp.setContentType("application/json");
		if (type!=null &&type.equals("logout")) {
			session.removeAttribute("name");
			session.removeAttribute("password");	
			session.removeAttribute("id");
			session.invalidate();
			out.print("Success");
		}
		if (type!=null && type.equals("login") || mobilenumber == null) {
			user = checkAndLogin(name, pass);
			if (user == null) {
				out.write("Invalid Credentials");
				return;
			}
		} else {
			String result = addUser(name, Long.parseLong(mobilenumber), pass);
			user = new User(name, Long.parseLong(mobilenumber));
			if (result.equals("user Aready exists...")) {
				System.out.println("Exist.....................");
				out.write("User Alreday Exists..");
				return;
			}
			
		}
		JSONObject userjson = new JSONObject();
		userjson = createJson(user);

		session.setAttribute("name", name);
		session.setAttribute("password", pass);
		session.setAttribute("id", user.getUserId());
		out.write("success");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private JSONObject createJson(User user) {
		
		JSONObject userjson = new JSONObject();
		userjson.put("name", user.getName());
		userjson.put("mobilenumber", user.getMobileNumber());
		userjson.put("id", user.getUserId());
		return userjson;
	}

	public LoginViewModel(LoginView view) {
		this.loginView = view;
	}

	public LoginViewModel() {
		this.loginView = new LoginView();
	}

	interface LoginModelControllerCallback {

		void loginFailed(String string);

		void loginSuccess(User user, String message);

	}

	protected String addUser(String name, long mobileNumber, String password) {

		User user = new User(name, mobileNumber);

		if (Contactsrepository.getInsatnce().isExistingUser(name, password,mobileNumber)) {

//			loginView.loginfailed("user Aready exists...");
			return "user Aready exists...";
		} else {
			Credentials credentials = new Credentials(password, user.getUserId(), user.getName());
			Contactsrepository.getInsatnce().adduser(user, credentials);

//			loginView.loginSuccess(user, "user Added Success...");
			return "user Added Success...";
		}
	}

	public User checkAndLogin(String name, String password) {
		User user = null;
//		if (Contactsrepository.getInsatnce().isExistingUser(name, password,mobileNumber)) {
			user = Contactsrepository.getInsatnce().getuser(name, password);
//			if (user != null)
//				loginView.loginSuccess(user, "Login Success..");
//			else
//				loginView.loginfailed("Invalid Credentiasl");
//		} else {
//
//			loginView.loginfailed("No user found..");
//		}
		return user;
	}
}
