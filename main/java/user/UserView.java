package user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import dto.Contact;
import dto.User;
import login.LoginView;


public class UserView {
	Scanner scanner = new Scanner(System.in);
	private UserViewModel viewModelCallback;

	public UserView() {
		this.viewModelCallback = new UserViewModel(this);
	}

	public void menu(User user) {
		System.out.println("Welcom back <--" + user.getName() + "-->");
		System.out.println("1.View Contacts\n2.Add new Contact\n3.Delete Contact\n4.LogOut");
		String option = scanner.next();
		switch (option) {
		case "1":
			viewContacts(user);
			break;
		case "2":
			AddnewContact(user);
			break;
		case "3":
			deleteContact(user);
			break;
		case "4":
			logout();
			break;
		default: {
			System.out.println("invalid opiton...");
			menu(user);
		}
			break;
		}

	}

	private void logout() {
		LoginView loginView = new LoginView();
		loginView.create();
	}

	private void deleteContact(User user) {
		System.out.println("Enter statrting letter of the Contact name:");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		String name = null;
		try {
			name = reader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		viewModelCallback.getAndDelete(user, name);
	}

	private void AddnewContact(User user) {
		System.out.println("Enter Mobile Number:");
		long mobileNum = getMobile();
		System.out.println("Enter Contact Name:");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String name = null;
		try {
			name = reader.readLine();
		} catch (IOException e) {

			e.printStackTrace();
		}
		viewModelCallback.addContact(name, mobileNum, user);
	}

	private long getMobile() {
		long num = 0;
		try {
			String val = scanner.next();
			num = Long.parseLong(val);
			if (String.valueOf(num).length() != 10) {
				System.out.println("Enter valid mobile number");
				getMobile();
			} else {
				System.out.println(num);
				return num;
			}
		} catch (NumberFormatException e) {
			System.out.println("Enter valid Input");
			getMobile();
		}
		System.out.println(num);
		return num;
	}

	private void viewContacts(User user) {
		viewModelCallback.getUsers(user);
		menu(user);
	}

	protected void addSuccess(User user, Contact contact) {
		System.out.println("Contact Added Successfully...");
		System.out.printf("%-10s:%s\n", "Name", contact.getName());
		System.out.printf("%-10s:%d\n", "Number", contact.getMobileNumber());
		menu(user);
	}

	protected void noContacts(User user) {
		System.out.println("No contacts Found.....");
		menu(user);
	}

	protected void viewConatcs(User user, ArrayList<Contact> contacts, boolean forDelete) {
		int count = 1;
		for (Contact contact : contacts) {
			System.out.println("------------------------------------------------------------------");
			System.out.printf("%-10s:%d\n", "Contact No", count++);
			System.out.printf("%-10s:%s\n", "Name", contact.getName());
			System.out.printf("%-10s:%d\n", "Number", contact.getMobileNumber());
		}
		System.out.println("------------------------------------------------------------------");

		int option = 0;

		if (forDelete) {
			System.out.println("Select Contacts by Contact No");
			System.out.println("Enter " + (contacts.size() + 1) + ".Menu");
			try {
				option = scanner.nextInt();
			} catch (InputMismatchException e) {
				viewConatcs(user, contacts, forDelete);
			}

			if (option >= (contacts.size() + 1)) {
				menu(user);
			} else {
				Contact contact = contacts.get(option - 1);
				viewContact(contact);
				viewModelCallback.deleteContact(contact, user);
			}

		}
	}

	private void viewContact(Contact contact) {
		System.out.println("-----------------------------------------------------------------------");
		System.out.printf("%-10s:%s\n", "Name", contact.getName());
		System.out.printf("%-10s:%d\n", "Number", contact.getMobileNumber());
		System.out.println("-----------------------------------------------------------------------");
	}

	protected void deletionSuccess(User user) {
		System.out.println("Contact deleted..");
		menu(user);
	}
}
