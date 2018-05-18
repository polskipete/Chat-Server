//Peter Kolesnik
import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer{  
	public static void main(String[] args ){  
		ArrayList<ChatHandler>handlers = new ArrayList<ChatHandler>();
		ArrayList<String>list = new ArrayList<String>();
		try{  
			ServerSocket s = new ServerSocket(3000);
			for(;;){
				Socket incoming = s.accept();
				new ChatHandler(incoming, handlers).start();
			}
		}catch (Exception e){  
			System.out.println(e);
		} 
	} 
}	  
	  
class ChatHandler extends Thread{
	DataObject myObject = null;
	private Socket incoming;
	ArrayList<ChatHandler>handlers;
	ArrayList<String>users;
	ObjectInputStream in;
	ObjectOutputStream out;
	String userName;
	DataObject objIn;
	String temp;
	int ii;
	public String getUserName(){
		//System.out.println("USERNAMES ARE"+userName);
		return userName;
	}
	public ChatHandler(Socket i, ArrayList<ChatHandler>h){
		incoming = i;
		handlers = h;
		handlers.add(this);
	}
	public void run(){
		try{
			in = new ObjectInputStream(incoming.getInputStream());
			out = new ObjectOutputStream(incoming.getOutputStream());

			boolean done = false;
			while (!done){  
				objIn = (DataObject)in.readObject();
				if (objIn == null){
					done = true;
				}else{
					if (userName==null){
						//System.out.println(objIn.getMessage()+" USERNAMES");
						temp= objIn.getMessage();
						if (temp.endsWith("Channel")){
							String[] words = temp.split(" ");
							userName = words[0];
						}

					}
					users=new ArrayList<String>();
					for(ChatHandler h : handlers){
						//System.out.println(h.getUserName());

						users.add(h.getUserName());
						//ii++;
					}
					objIn.setNames(users);

					for(ChatHandler h : handlers){
						//temp= objIn.getMessage();
						//System.out.println(objIn.getMessage()+" MESSAGES");
						//System.out.println("TEMp "+temp);
						if (!temp.endsWith("Disconnected"))
							h.out.writeObject(objIn);
						


					}
					if (objIn.getMessage().trim().equals("BYE")){ 
						done = true;
					}
				}
			}
			incoming.close();
		}catch (Exception e){  
			System.out.println(e);
		}finally{
			System.out.println("finally");
			handlers.remove(this);
				try{
						for(ChatHandler h : handlers){
							System.out.println(objIn.getMessage()+" MESSAGES");

							h.out.writeObject(objIn);
						}
					}catch(Exception e){}


			//When gets here list stops updating on all clients, until another user re runs the RUN method
		} 
	} 
}