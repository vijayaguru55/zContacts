package login;

import java.util.InputMismatchException;
import java.util.Scanner;

import dto.User;
import user.UserView;


//@WebServlet("/contactlogin")
public class LoginView {
	
	/**
	 * 
	 */
//	private static final long serialVersionUID = 2L;
//
//	public void doGet(HttpServletRequest req,HttpServletResponse resp) {
//		String type = req.getParameter("type");
//		String name = req.getParameter("username");
//		String mobilenumber = req.getParameter("mobilenumber");
//		String pass = req.getParameter("password");
////		loginViewModel.checkAndLogin(name, pass);
//		if(type.equals("login")) {
//			loginViewModel.checkAndLogin(name, pass);
//		}else {
//			loginViewModel.addUser(name, Long.parseLong(mobilenumber),pass);
//		}
//	}
	Scanner scanner = new Scanner(System.in);
	private LoginViewModel loginViewModel;

	public LoginView() {
		this.loginViewModel = new LoginViewModel(this);
	}

	public static void main(String[] args) {
		LoginView loginView = new LoginView();
		loginView.create();
	}

	public void create() {
		menu();
	}

	private void menu() {
		System.out.println("1.Login\n2.SignUp\n3.Exit");
		String option = scanner.next();
		switch (option) {
		case "1":
			login();
			break;
		case "2":
			signUp();
			break;
		case "3":
			System.out.println("Thank You....");
			System.exit(0);
			break;
		default: {
			System.out.println("Invalid input");
			menu();
		}
			break;
		}
	}

	private void login() {
		System.out.println("Enter user Name :");
		String name = scanner.next();
		System.out.println("Enter Password");
		String password = scanner.next();
		loginViewModel.checkAndLogin(name, password);
	}

	private void signUp() {
		System.out.println("Enter Your Name :");
		String name = scanner.next();
		System.out.println("Enter Mobile Number:");
		long mobileNumber = 0;
		try {
			mobileNumber = getMobile();
		} catch (InputMismatchException e) {
			System.out.println("Invalid Input");
			signUp();
		}
		System.out.println("Set Password");
		String password = scanner.next();

		loginViewModel.addUser(name, mobileNumber, password);
	}

	private long getMobile() {
		long num = 0;
		try {
			num = scanner.nextLong();
			while (num == 0 || String.valueOf(num).length() != 10) {
				System.out.println("Enter valid mobile number");
				num = scanner.nextLong();
			}
		} catch (InputMismatchException e) {
			System.out.println("Enter valid Input");
			getMobile();
		}

		return num;
	}

	protected void loginfailed(String message) {
		System.out.println(message);
		menu();
	}

	protected void loginSuccess(User user, String message) {
		System.out.println(message);
		UserView userView = new UserView();
		userView.menu(user);
	}
}
