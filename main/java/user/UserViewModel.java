package user;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.management.loading.PrivateClassLoader;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import main.java.user.ChatMessage;

import dto.Contact;
import dto.User;
import login.LoginViewModel;
import repository.Contactsrepository;

@WebServlet("/userview")
@MultipartConfig
public class UserViewModel extends HttpServlet {

	private UserView viewCallback;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		PrintWriter out = resp.getWriter();
		HttpSession session = req.getSession();
		String cName = req.getParameter("contactnamein");
		String cNumber = req.getParameter("contactnumberin");
		String name = (String) session.getAttribute("name");
		String pass = (String) session.getAttribute("password");
		String type = req.getParameter("type");

		
		if (type != null && type.equals("loginCheck")) {
			if (name != null && pass != null) {
				out.write("alredayLogin");
				return;
			}
			out.write("doLogin");
			return;
		}
		User user = LoginViewModel.getInsatnce().checkAndLogin(name, pass);
		if (type != null && type.equals("editWithImage")){
			Part imgFile =req.getPart("image");
			Long mobNum = Long.parseLong(req.getParameter("contactnumberin"));
			String fileName =imgFile.getSubmittedFileName();
			InputStream stream = imgFile.getInputStream();
			String oldNum = req.getParameter("oldNum");
			out.write(editContact(cName,Long.parseLong(cNumber),stream,user,Long.parseLong(oldNum)));
			return;
		}
		if (type != null && type.equals("editWithoutImage")){
			String oldNum = req.getParameter("oldNum");
			out.write(editContact(cName,Long.parseLong(cNumber),null,user,Long.parseLong(oldNum)));
			return;
		}
		if (name == null || pass == null) {
			JSONObject userJson = new JSONObject();
			userJson.put("message", "Please Login");
			out.println(userJson);
			return;
		}
		if(type!=null && type.equals("addToFavs")){
			out.write(addToFavs(user,cName,Long.parseLong(cNumber)));
			return;
		}
		if (type != null && type.equals("removeContact")) {
			
			out.write(deleteContact(new Contact(cName, Long.parseLong(cNumber),
			cName.charAt(0)+user.getName().charAt(1)+cNumber.substring(3,6)), user));
			return;
		}
		if (type != null && type.equals("addContact")){
			out.println(addContact(cName, Long.parseLong(cNumber), user));
			return;
		}
		if(type!=null && type.equals("addImage")) {
			Part imgFile =req.getPart("image");
			Long mobNum = Long.parseLong(req.getParameter("contactnumberin"));
			String fileName =imgFile.getSubmittedFileName();
			InputStream stream = imgFile.getInputStream();
			out.write(addImage(user,mobNum,stream));
			return;
		}

		List<Contact> contacts = getUsers(user);
		if(type!=null && type.equals("getMessages")){
			List<ChatMessage> messages = getMessages(user.getMobileNumber());
			JSONArray chatArr = new JSONArray();
			for (ChatMessage chatMessage : messages) {
					JSONObject obj = new JSONObject();
					obj.put("message", chatMessage.getText());
					obj.put("sender", chatMessage.getSender());
					obj.put("receiver",chatMessage.getRecipient());
					obj.put("date",chatMessage.getDate());
					obj.put("time",chatMessage.getTime());
					chatArr.add(obj);
			}
			out.append(chatArr.toString());
			return;
		}
		JSONArray arr = new JSONArray();
		JSONObject userJson = createJson(user);
		arr.add(userJson);
		if (type != null && type.equals("search")) {
			String key = req.getParameter("key");
			contacts = contacts.stream().filter((cn) -> {
				return cn.getName().contains(key);
			}).collect(Collectors.toList());
		}
		if (contacts == null) {
			arr.add(new JSONObject().put("result", "No Contacts Foudn"));
			out.print(arr.toString());
		} else {
			for (int i = 0; i < contacts.size(); i++) {
				Contact c = contacts.get(i);
				JSONObject obj = new JSONObject();
				obj.put("name", c.getName());
				obj.put("number", c.getMobileNumber());
				obj.put("favourite",c.getIsFavourite());
				obj.put("image",c.getImage());
				arr.add(obj);
			}
			out.append(arr.toString());
		}

	}

	private String addImage(User user,Long mobNum, InputStream stream) {
		
		return Contactsrepository.getInsatnce().addImage(user,mobNum,stream);
	}

	private String addToFavs(User user, String cName, long number) {
		return Contactsrepository.getInsatnce().getContact(user,number);
	
	}
	private List<ChatMessage> getMessages(Long mobile){
		return Contactsrepository.getInsatnce().getMessages(mobile);
	}
	private JSONObject createJson(User user) {
		JSONObject userjson = new JSONObject();
		userjson.put("name", user.getName());
		userjson.put("mobilenumber", user.getMobileNumber());
		userjson.put("id", user.getUserId());
		return userjson;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public UserViewModel(UserView viewCallback) {
		this.viewCallback = viewCallback;
	}

	public UserViewModel() {
		this.viewCallback = new UserView();
	}

	protected String editContact(String name,Long mobile,InputStream stream,User user,Long oldNum){
		return Contactsrepository.getInsatnce().updateContact(name,mobile,stream,user,oldNum);
	}

	protected String addContact(String name, long mobileNum, User user) {
		Contact contact = new Contact(name, mobileNum,name.charAt(0)+user.getName().charAt(1)+String.valueOf(mobileNum).substring(3,6));
		return Contactsrepository.getInsatnce().addContact(user, contact);
//		viewCallback.addSuccess(user, contact);
	}

	protected void getAndDelete(User user, String name) {
		ArrayList<Contact> contacts = Contactsrepository.getInsatnce().getContacts(user, name);
		if (contacts == null || contacts.size() == 0) {
			viewCallback.noContacts(user);
		} else {
			viewCallback.viewConatcs(user, contacts, true);
		}
	}

	protected ArrayList<Contact> getUsers(User user) {
		ArrayList<Contact> contacts = Contactsrepository.getInsatnce().getContacts(user);
		if (contacts == null || contacts.size() == 0) {
//			viewCallback.noContacts(user);
			return null;
//		} else {
//			viewCallback.viewConatcs(user, contacts, false);
		}
		return contacts;
	}

	protected String deleteContact(Contact contact, User user) {
		Contactsrepository.getInsatnce().deleteConatact(contact, user);
//		viewCallback.deletionSuccess(user);
		return ("success");
	}

}
