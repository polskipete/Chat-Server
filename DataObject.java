//Peter Kolesnik
import java.io.*;
import java.util.*;

public class DataObject implements Serializable{

	private String message;
	private ArrayList<String> names;
	private String to;

	DataObject(){
		message = "";
		names = new ArrayList<String>();
	}

	public String getMessage(){
		return message;
	}

	public void setMessage(String inMessage){
		message = inMessage;
	}
	public ArrayList<String> getNames(){
		return names;
	}
	public void setNames(ArrayList<String> names){
		this.names = names;
	}

	public void setTo(String name){
		to = name;
		//private msg
	}
	public String getTo(){
		return to;
	}
}