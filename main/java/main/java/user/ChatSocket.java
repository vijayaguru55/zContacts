package main.java.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.json.simple.JSONObject;

import repository.Contactsrepository;


@ServerEndpoint(value = "/chat/{userId}")
public class ChatSocket extends HttpServlet{
    private static List<Session> sessions = new ArrayList<Session>();


    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        
        session.getUserProperties().put("userId", userId);
        List<Session> userIds =new  ArrayList<>();
        userIds = this.sessions.stream().filter((x)->{return x.getUserProperties().get("userId").equals(userId);}).collect(Collectors.toList());
        if(userIds.size()!=0) {
        	System.out.println("User rejecteed "+userId);
        	return;
        }
        System.out.println("WebSocket opened for " + userId);
        this.sessions.add(session);
    }
    

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        System.out.println("User disconnected: " + session.getId());
        
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("userId") String userId) throws IOException {
        System.out.println("Message received from " + userId + ": " + message);
        String[] ar = message.split(",");
        System.out.println(ar.length);
        if(ar.length==5) {
            System.out.println("inside");
         
        ChatMessage chatMessage =  new ChatMessage(ar[0], ar[1], ar[2],ar[3]=","+ar[4].substring(0,4),ar[4].substring(5));
        for (Session s : sessions) {
            String recipientId = (String) s.getUserProperties().get("userId");
            System.out.println(recipientId);
            if (recipientId != null && recipientId.equals(chatMessage.getRecipient())) {
              System.out.println("receiver found");
              	JSONObject send = new JSONObject();
              	send.put("sender",chatMessage.getSender());
              	send.put("message",chatMessage.getText());
                send.put("date",chatMessage.getDate());
                send.put("time",chatMessage.getTime());
                s.getBasicRemote().sendText(send.toString());
                Contactsrepository.getInsatnce().addmessage(chatMessage.getText(),chatMessage.getSender(),chatMessage.getRecipient(),chatMessage.getDate(),chatMessage.getTime());
            }
        }}
      }
}
