//Peter Kolesnik
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.net.*;

public class ChatFrame extends Frame{
	public ChatFrame(){
		
			setSize(500, 500);
			setTitle("Chatster");
			addWindowListener(new WindowAdapter(){
				public void windowClosing(WindowEvent we){
					System.exit(0);
				}
			});
			add(new ChatPanel(this), BorderLayout.CENTER);
			setVisible(true);
		
			
		
	}
	public static void main(String[] args){
		new ChatFrame();
	}

	
}

class ChatPanel extends Panel implements ActionListener, Runnable{  //INCOMPLETE!!!
	TextArea ta;
	TextField tf;
	Button connect, disconnect;
	Thread t;
	java.awt.List list;
	Socket s;
	ObjectOutputStream out;
	ObjectInputStream in;
	DataObject objOut;
	DataObject objIn;

	ChatFrame parent;
	String userName;
	boolean userNamePicked=false;
	boolean removeName= false;
	boolean turnOnList=false;
	
	public ChatPanel(ChatFrame parent){
			this.parent= parent;
			setLayout(new BorderLayout());
			tf = new TextField();
			tf.addActionListener(this);
			ta = new TextArea();
			add(tf, BorderLayout.NORTH);
			add(ta, BorderLayout.CENTER);
			connect = new Button("Connect");
			connect.addActionListener(this);
			disconnect = new Button("Disconnect");
			disconnect.addActionListener(this);
			Panel buttonPanel = new Panel();
			buttonPanel.add(connect);
			buttonPanel.add(disconnect);
			add(buttonPanel, BorderLayout.SOUTH);
			list = new java.awt.List(4, true);
			add(list, BorderLayout.EAST);

			disconnect.setEnabled(false);
			
		
	}
	public void actionPerformed(ActionEvent ae){

		
			
			if(ae.getSource() == connect){	
					userName = tf.getText();					
					
					tf.setText("");
					connect.setEnabled(false);
					disconnect.setEnabled(true);
					parent.setTitle(userName);

					turnOnList=true;
					//ChatFrame.test();
					//userNamePicked=true;

				try{
					s = new Socket("127.0.0.1", 3000);
					System.out.println("Connection Established");
					out = new ObjectOutputStream(s.getOutputStream());
					in = new ObjectInputStream(s.getInputStream());
					t = new Thread(this);
					//out.writeObject(list);
					t.start();
					
					}catch(UnknownHostException uhe){
						System.out.println(uhe.getMessage());
					}catch(IOException ioe){
						System.out.println(ioe.getMessage());		
					}


					//String temp = tf.getText();
				
				try{
					objOut = new DataObject();
					objOut.setMessage(userName+" Has Entered The Channel");
					out.writeObject(objOut);
					out.reset();
				}catch(IOException ioe){
					System.out.println(ioe.getMessage());
				}


				tf.setText("");



			}else if(ae.getSource() == tf){
				
				String temp = tf.getText();
				
				try{
					objOut = new DataObject();
					objOut.setMessage(userName+": "+temp);
					out.writeObject(objOut);
					out.reset();
				}catch(IOException ioe){
					System.out.println(ioe.getMessage());
				}
				tf.setText("");
				
			}else if(ae.getSource() == disconnect){
				removeName = true;
				turnOnList=true;

				try{
					objOut = new DataObject();
					objOut.setMessage(userName+" Has Disconnected");
					out.writeObject(objOut);
					

					System.out.print("CLICKED");
					connect.setEnabled(true);
					disconnect.setEnabled(false);
					//list.remove(userName);
					list.removeAll();
					s.close();
					t=null;


					out.reset();
					}catch(Exception e){}
				
				
			}
		

		
	}
	public void run(){
		try{
			while(t != null){
				DataObject objIn = (DataObject)in.readObject();
				String temp = objIn.getMessage();
				ta.append(temp + "\n");

				
					
					ArrayList<String> users= objIn.getNames();
					if (removeName == true){

						for (int i =0 ; i<users.size(); i++){
							if (users.get(i) == userName)
								list.remove(userName);
							//list.removeAll();						
								
						}
						removeName = false;

					}
					else{
						list.removeAll();
							for (int i =0 ; i<users.size(); i++){
								list.add(users.get(i));
							}
						}			
				
			}
		}catch(ClassNotFoundException cnfe){
			System.out.println(cnfe.getMessage());
		}catch(IOException ioe){
			System.out.println(ioe.getMessage());
		} finally{
			System.out.print("FIANL");
			ta.setText(" ");}



	}
}