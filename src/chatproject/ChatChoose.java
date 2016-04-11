package chatproject;

import java.io.IOException;
import javax.swing.JOptionPane;

public class ChatChoose {
    
    public static void main(String [] args) throws IOException{
		
		Object[] selectioValues = { "Serwer","Klient"};
		String initialSection = "Serwer";
		
		Object selection = JOptionPane.showInputDialog(null, "Zaloguj jako: ", "Czat", JOptionPane.QUESTION_MESSAGE, null, selectioValues, initialSection);
		if(selection.equals("Serwer")){
                   String PORTServer = JOptionPane.showInputDialog("Podaj port serwera");
                   if(PORTServer.equals("")){
                       PORTServer = "1201";
                   }
                   String[] arguments = new String[] {PORTServer};
                   new ChatServer().main(arguments);
		}else if(selection.equals("Klient")){
			String IPServer = JOptionPane.showInputDialog("Podaj adres IP serwera");
                        String PORTServer = JOptionPane.showInputDialog("Podaj port serwera");
                        String[] arguments = new String[] {IPServer,PORTServer};
			new ChatClient().main(arguments);
		}
		
	}
    
}
