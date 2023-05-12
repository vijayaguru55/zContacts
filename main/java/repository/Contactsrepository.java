package repository;

import java.sql.Blob;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;

import javax.imageio.ImageIO;

import main.java.user.ChatMessage;

import dto.Contact;
import dto.Credentials;
import dto.User;

public class Contactsrepository {
	private static Connection database = null;

	private static Contactsrepository contactsrepository;

	public static Contactsrepository getInsatnce() {
		if (contactsrepository == null) {
			String url = "jdbc:mysql://localhost:3306/contacts";
			String username = "root";
			String password = "Guruvijayg@123";
			Connection connection = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection(url, username, password);
				database = connection;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			contactsrepository = new Contactsrepository();
			return contactsrepository;
		}
		return contactsrepository;
	}

	public boolean isExistingUser(String name, String password,long mobileNumber) {
		String query = "select mobileNumber from user where mobileNumber ='" + mobileNumber + "';";
		PreparedStatement preparedStatement = null;
		try {

			preparedStatement = database.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			return resultSet.next();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public void adduser(User user, Credentials credentials) {
		String query = "insert into credential(name,password,userId) values(?,?,?)";
		String query1 = "create table "+user.getUserId()+"con ("+
		"name varchar(30) not null unique,mobileNumber numeric(10) not null unique,userId varchar(15) not null ,contactId varchar(15) not null unique,"+
		"isFavourite bool, image longblob)";
		String name = user.getName();
		String password = credentials.getPassword();
		String userid = user.getUserId();
		long mobile = user.getMobileNumber();
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = database.prepareStatement(query);
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, password);
			preparedStatement.setString(3, userid);
			preparedStatement.executeUpdate();
			query = "insert into user values(?,?,?)";
			preparedStatement = database.prepareStatement(query);
			preparedStatement.setString(1, userid);
			preparedStatement.setString(2, name);
			preparedStatement.setLong(3, mobile);
			preparedStatement.executeUpdate();
			preparedStatement = database.prepareStatement(query1);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public User getuser(String name, String password) {
		String query = "select userId from credential where name = '" + name + "' and password = '" + password + "'";
		PreparedStatement preparedStatement = null;
		String userId = null;
		try {
			preparedStatement = database.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (!resultSet.next()) {
				return null;
			}
			userId = resultSet.getString(1);

			query = "select name,mobileNumber from user where userId = '" + userId + "'";
			preparedStatement = database.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next())
				return new User(resultSet.getString(1), resultSet.getLong(2));
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return null;
	}

	public String addContact(User user, Contact contact) {
		String query = "insert into "+user.getUserId()+"con  (name,mobileNumber,userId,contactId) values(?,?,?,?)";
		String name = contact.getName();
		long mobileNumber = contact.getMobileNumber();
		String userId = user.getUserId();
		String contactId = contact.getcontactID();
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = database.prepareStatement(query);
			preparedStatement.setString(1, name);
			preparedStatement.setLong(2, mobileNumber);
			preparedStatement.setString(3, userId);
			preparedStatement.setString(4, contactId);
			if(preparedStatement.executeUpdate()!=1){
				return "Contact Already Exists...";
			}
		} catch (SQLException e) {
			// return "Contact Already Exists...";
			e.printStackTrace();
		}
		return "Contact Added Success";
	}

	public ArrayList<Contact> getContacts(User user, String name) {
		ArrayList<Contact> contacts = new ArrayList<Contact>();
		String query = "select name,mobileNumber,isFavourite,image from "+user.getUserId()+"con where"
				+ "'name like '" + name + "%'";
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = database.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				Contact newContact = new Contact(resultSet.getString(1), resultSet.getLong(2),
				resultSet.getString(1).charAt(0)+user.getName().charAt(1)+String.valueOf(resultSet.getLong(2)).substring(3,6));
				newContact.setIsFavourite(resultSet.getBoolean(3));
				Blob imageBlob = resultSet.getBlob(4);
				if(imageBlob!=null){
					InputStream inputStream = imageBlob.getBinaryStream();
 					
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					byte[] buffer = new byte[4096];
					int bytesRead = -1;
    				try {
						while ((bytesRead = inputStream.read(buffer)) != -1) {
						    baos.write(buffer, 0, bytesRead);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    				byte[] imageBytes = baos.toByteArray();
    				try {
						baos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String base64Image = Base64.getEncoder().encodeToString(imageBytes);
					newContact.setimage("<img src='data:image/jpeg;base64," + base64Image + "'style= 'margin-left: 17px;'/>");
				
				}
				contacts.add(newContact);
			}

		} catch (SQLException e) {
			return null;
		}
		return contacts;
	}

	public ArrayList<Contact> getContacts(User user) {
		ArrayList<Contact> contacts = new ArrayList<Contact>();
		String query = "select name,mobileNumber,isFavourite,image from "+user.getUserId()+"con order by name asc";
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = database.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				Contact newContact = new Contact(resultSet.getString(1), resultSet.getLong(2),
				resultSet.getString(1).charAt(0)+user.getName().charAt(1)+String.valueOf(resultSet.getLong(2)).substring(3,6));
				newContact.setIsFavourite(resultSet.getBoolean(3));
				Blob imageBlob = resultSet.getBlob(4);
				if(imageBlob!=null){
					InputStream inputStream = imageBlob.getBinaryStream();
 					
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					byte[] buffer = new byte[4096];
					int bytesRead = -1;
    				try {
						while ((bytesRead = inputStream.read(buffer)) != -1) {
						    baos.write(buffer, 0, bytesRead);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    				byte[] imageBytes = baos.toByteArray();
    				try {
						baos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String base64Image = Base64.getEncoder().encodeToString(imageBytes);
					newContact.setimage("<img src='data:image/jpeg;base64," + base64Image +"'style= 'margin-left: 17px;'/>");
					
					
				}
				contacts.add(newContact);
			}

		} catch (SQLException e) {
			return null;
		}

		return contacts;
	}

	public void deleteConatact(Contact contact, User user) {
		
		String query = "delete from "+user.getUserId()+"con where name='" + contact.getName() + "' and mobileNumber = '"
				+ contact.getMobileNumber() + "'";
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = database.prepareStatement(query);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getContact(User user,long mobilenumber){
		String query1 = "select isFavourite from "+user.getUserId()+"con where mobileNumber = "+mobilenumber;
		String query = "insert into favourites values((select contactId from "+user.getUserId()+"con where mobileNumber ="+mobilenumber+"),'"+user.getUserId()+"')";
		String query2 = "update "+user.getUserId()+"con set isFavourite=true where mobileNumber = '"+mobilenumber+"'";
		
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = database.prepareStatement(query1);
			ResultSet res = preparedStatement.executeQuery();
			if(res.next() &&res.getBoolean(1)==false){
			preparedStatement = database.prepareStatement(query);
			if(preparedStatement.executeUpdate()!=1){
				return "Invalid attempt";
			}else{
			preparedStatement = database.prepareStatement(query2);
			preparedStatement.executeUpdate();
		}
		}else{
			return delFav(user,mobilenumber);
		}
		}
		catch(java.sql.SQLIntegrityConstraintViolationException er) {
			return "Exception";
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Contacts added to favourites...";
	}
	
	public String delFav(User user,long mobilenumber) {
		String query = "delete from favourites where contactId = (select contactId from "+user.getUserId()+"con where mobileNumber ='"+mobilenumber+"');";
		String query2 = "update "+user.getUserId()+"con set isFavourite=false where mobileNumber = '"+mobilenumber+"'";
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = database.prepareStatement(query);
			preparedStatement.executeUpdate();
			preparedStatement = database.prepareStatement(query2);
			preparedStatement.executeUpdate();
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Contacts removed from favourites...";
	}

	public String addImage(User user,Long mobNum, InputStream stream) {
		String	query = "update "+user.getUserId()+"con set image=(?) where mobileNumber = "+mobNum+";";
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = database.prepareStatement(query);
			preparedStatement.setBlob(1,stream);
			if(preparedStatement.executeUpdate()!=1){
				return "Contact add failed";
			}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Contact added Success";
	}

	public String updateContact(String name,Long mobile,InputStream stream,User user,Long oldNum){
		String query = null;
		if(stream==null){
			query = "update  "+user.getUserId()+
			"con set name ='"+name+"',mobileNumber ="+mobile+" where mobileNumber ="+oldNum;
		}else{
			query = "update  "+user.getUserId()+
			"con set name =(?),mobileNumber =(?),image = (?) where mobileNumber =(?)";
		}
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = database.prepareStatement(query);
			if(stream==null){preparedStatement.executeUpdate();}else{
				preparedStatement.setString(1,name);
				preparedStatement.setLong(2,mobile);
				preparedStatement.setBlob(3,stream);
				preparedStatement.executeUpdate();
			}
			
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "contact updated...";
	}

	public void addmessage(String text,String sender,String receiver,String date,String time) {
		String query = "insert into messages(message,senderId,receiverId,date,time) values(?,?,?,?,?)";
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = database.prepareStatement(query);
				preparedStatement.setString(1,text);
				preparedStatement.setString(2,sender);
				preparedStatement.setString(3,receiver);
				preparedStatement.setString(4,date);
				preparedStatement.setString(5,time);
				preparedStatement.executeUpdate();
			System.out.println(date+" "+time);
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<ChatMessage>  getMessages(Long mobile){
		String query = "select message,senderId,receiverId,date,time from messages where senderid="+mobile+" or receiverId = "+mobile+"  order by messageId asc";
		PreparedStatement preparedStatement = null;
		ArrayList<ChatMessage> messages = new ArrayList<>();
		try {
			preparedStatement = database.prepareStatement(query);
			ResultSet result = preparedStatement.executeQuery();

			while(result.next()){
				ChatMessage chat = new ChatMessage(result.getString(1),result.getString(2), result.getString(3),result.getString(4),result.getString(5));
				messages.add(chat);
			}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return messages;
	}
}
