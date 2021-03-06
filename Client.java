import java.io.*;
import java.net.*;

class NewThread implements Runnable {
	String nameThread;
	BufferedReader bufferedChatIn;
	Socket clientSocket;
	Thread t;
	NewThread(String nameThread, Socket clientSocket, BufferedReader bufferedChatIn) {
		this.bufferedChatIn = bufferedChatIn;
		this.clientSocket = clientSocket;
		this.nameThread = nameThread;
		t = new Thread(this, nameThread);
		t.start();
	}
	public void run() {
		try {
			while(true) {
				String serverMessage = bufferedChatIn.readLine();
				if(serverMessage == null) throw new NullPointerException();
				System.out.println(serverMessage);
			}
		} catch (IOException e) {
			System.out.println("Поток прерван: " + nameThread);
		} catch (NullPointerException e) {
			System.out.println("\nСервер закрыл соединение.");
			System.exit(0);
		}
	}
}

public class Client {

	private static String adress = "localhost";
	private static Integer port = 30000;
	
	public static void main(String[] args) {
		System.out.println("\nПодключение к серверу...");
		try(Socket clientSocket = new Socket(adress, port);
			BufferedReader bufferedConsoleInput = new BufferedReader(new InputStreamReader(System.in));
			BufferedReader bufferedChatIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			BufferedWriter bufferedChatOut = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {
			try {
				new NewThread("Второй поток", clientSocket, bufferedChatIn);
				while(true) {
					String clientMessage = bufferedConsoleInput.readLine();
					bufferedChatOut.write(clientMessage + "\n");
					bufferedChatOut.flush();
				}
			} finally {
				clientSocket.close();
			}
		} catch (IOException e) {
			System.out.println("Ошибка соединения: " + e);
		}

	}

}
