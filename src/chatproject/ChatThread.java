package chatproject;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;


public class ChatThread extends Thread {
    
    private String clientName = null;
    private DataInputStream is = null;
    private PrintStream os = null;
    private Socket clientSocket = null;
    private final ChatThread[] threads;
    private int maxClientsCount;
  
    public ChatThread(Socket clientSocket, ChatThread[] threads) {
        this.clientSocket = clientSocket;
        this.threads = threads;
        maxClientsCount = threads.length;
    }
  
    public void run() {
        int maxClientsCount = this.maxClientsCount;
        ChatThread[] threads = this.threads;

    try {
        is = new DataInputStream(clientSocket.getInputStream());
        os = new PrintStream(clientSocket.getOutputStream());
        String name;
        while (true) {
            os.println("Podaj swój nick");
            name = is.readLine().trim();
            if (name.indexOf('@') == -1) {
                break;
            }
            else {
                os.println("Nick nie może zawierać znaku '@'");
            }
        }

        os.println("Witaj " + name + " w pokoju czatowym. Aby wyjść wpisz /quit.");
        synchronized (this) {
            for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] != null && threads[i] == this) {
                    clientName = "@" + name;
                    break;
                }
            }
            for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] != null && threads[i] != this) {
                    threads[i].os.println("*** Użytkownik " + name + " dołączył do czatu ***");
                }
            }
        }
      
        while (true) {
            String line = is.readLine();
            if (line.startsWith("/quit")) {
                break;
            }
            if (line.startsWith("@")) {
                String[] words = line.split("\\s", 2);
            if (words.length > 1 && words[1] != null) {
                words[1] = words[1].trim();
            if (!words[1].isEmpty()) {
                synchronized (this) {
                    for (int i = 0; i < maxClientsCount; i++) {
                        if (threads[i] != null && threads[i] != this
                            && threads[i].clientName != null
                            && threads[i].clientName.equals(words[0])) {
                            threads[i].os.println("<" + name + "> " + words[1]);
                            this.os.println(">" + name + "> " + words[1]);
                            break;
                        }
                    }
                }
            }
            }
        }
        else {
            synchronized (this) {
                for (int i = 0; i < maxClientsCount; i++) {
                    if (threads[i] != null && threads[i].clientName != null) {
                        threads[i].os.println("<" + name + "> " + line);
                    }
                }
            }
        }
      }
      synchronized (this) {
            for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] != null && threads[i] != this
                    && threads[i].clientName != null) {
                    threads[i].os.println("*** Użytkownik " + name + " opuścił czat ***");
          }
        }
      }
      os.println("*** Żegnaj " + name + " ***");
      
      
      synchronized (this) {
            for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] == this) {
                    threads[i] = null;
                }
            }
      }
      
      is.close();
      os.close();
      clientSocket.close();
    } catch (IOException e) {
    }
  }
    
}
