import java.io.*; 
import java.util.*;
import java.net.*;

class Electric {
	double T1, T2, T3; 
	Integer N;
	// �����������
	Electric() {
		T1 = T2 = T3 = N = 0;
	}
	// ����������� �����������
	Electric(Electric X) {
		T1 = X.T1;
		T2 = X.T2;
		T3 = X.T3;
		N = X.N;
	}
	// ������������ ���������� ���������
	double sum() {
		return T1 * 0.5 + T2 * 0.63 + T3 * 0.8;
	}
	// ������������� �������� ��������
	void set(double T1, double T2, double T3, Integer N) {
		this.T1 = T1;
		this.T2 = T2;
		this.T3 = T3;
		this.N = N;
	}
}

public class Server {
	
	private static Integer port = 30000;
	private static String passwordBD = "TestPassword";
	
	public static void main(String[] args) {
		/* System.out.println("��� ��������� ��������� ��������� ��������� (T1, T2, T3) ��� ������ �������� �� ����� input.txt"
				+ " � ������� ���������� ��������� � ���� output.txt.\n����� ������������� � ������� ���������� ��� ������ �� ������."
				+ "\n��� ������ input.txt � output.txt ���������� ������������ ������� ����������."
				+ "\n������ ������ ����� � input.txt: \"�������� X: T1 T2 T3\""
				+ "\n������ ������ ������ � output.txt: \"�������� X: K ���.\"\n\n");
		*/

		// ������� ������ ���������
		ArrayList<Electric> accountList = new ArrayList<>();
		// ��������� �����, ������� ������ ������
		try(FileReader fileInput = new FileReader("input.txt");
			FileWriter fileOutput = new FileWriter("output.txt");
			BufferedReader bufferedInputFile = new BufferedReader(fileInput);
			BufferedWriter bufferedOutputFile = new BufferedWriter(fileOutput)) {
			// ��������� ������, ��������� �� � �������� ������ T1, T2, T3 � N ��������
			String[] strSub;
			String strInput = bufferedInputFile.readLine();
			while(strInput != null) {
				// System.out.println(strInput);
				strSub = strInput.split(" ");
				Electric importAccount = new Electric();
				// �������� ����� �� ������
				try {
					Integer T1 = new Integer(strSub[2]);
					Integer T2 = new Integer(strSub[3]);
					Integer T3 = new Integer(strSub[4]);
					Integer N = new Integer(strSub[1].substring(0, strSub[1].length() - 1));
					importAccount.set(T1, T2, T3, N);
					accountList.add(importAccount);
					bufferedOutputFile.write(strSub[0] + " " + importAccount.N + ": " + importAccount.sum() + " ���.\n");
					// ���������� � ���� ����������, ��� ����������� ������������� ��������� ����� ����������� ����� try>catch>finally
					bufferedOutputFile.newLine();
				} catch(NumberFormatException e) {
					System.out.println("������. �������� ������ ������.");
				}
				strInput = bufferedInputFile.readLine();
			}
		} catch(IOException e) {
			System.out.println("������ �����-������: " + e);
		}
		try(ServerSocket serverSocket = new ServerSocket(port)) {
			// ��������� ������ � ������ 30000
			System.out.println("������ �������. �������� �����������...");
			Socket clientSocket = serverSocket.accept();
			System.out.println("����������� ������������.");
			try(BufferedReader bufferedChatIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				BufferedWriter bufferedChatOut = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {
				bufferedChatOut.write("�� ������������ � �������.\n\n");
				bufferedChatOut.flush();
				String clientMessage;
				for(int i = 3; i != 0; i--) {
					bufferedChatOut.write("������� ������ ��� ������� � �� (�������� �������: " + i + "): \n\n");
					bufferedChatOut.flush();
					clientMessage = bufferedChatIn.readLine();
					if(clientMessage.equals(passwordBD)) {
						bufferedChatOut.write("\n���������� ������. �����������...\n"); 
						bufferedChatOut.flush(); 
						break;
					} else {
						bufferedChatOut.write("\n������������ ������. ���������� ��� ���.\n");
						bufferedChatOut.flush();
						if(i == 1) {
							bufferedChatOut.write("��������� ���������� ������� �����������. ���������� ���������.\n");
							bufferedChatOut.flush();
							clientSocket.close();
						}
					}
				}
				bufferedChatOut.write("�� ������� ������������ � ��! � ���������, ��� ���� ��� � ��������� �����.\n"
						+ "�� ����� ����� � MySQL! �� ������ � �����������? (Yes/No)\n\n");
				bufferedChatOut.flush();
				clientMessage = bufferedChatIn.readLine();
				if(clientMessage.equals("Yes")) {
					Electric importAccount = new Electric();
					bufferedChatOut.write("\n��������� ���������:\n");
					bufferedChatOut.flush();
					for(int i = 0; i < accountList.size(); i++) {
						importAccount = accountList.get(i);
						bufferedChatOut.write((i + 1) + ". �������� ����� " + importAccount.N + ": " + importAccount.T1 
								+ " " + importAccount.T2 + " " + importAccount.T3 + "\n");
						bufferedChatOut.flush();
					}
					bufferedChatOut.write("\n\n�� ���������. ���������� �������.\n");
					bufferedChatOut.flush();
					System.out.println("������������ �������� �� � ��� ��������.");
					clientMessage = bufferedChatIn.readLine();
					clientSocket.close();
				} else {
					bufferedChatOut.write("����� �� ���� ������ � ����������. ��������, �� ��� ���� ��������...\n");
					bufferedChatOut.flush();
				}
				bufferedChatOut.write("\n���������� �������.\n");
				bufferedChatOut.flush();
				System.out.println("������������ �������� ��� ��������.");
				clientMessage = bufferedChatIn.readLine();
				clientSocket.close();
			} catch(IOException e) {
				System.out.println("������ ���������� � �������������: " + e);
			} finally {
				clientSocket.close();
			}
		} catch(IOException e) {
			System.out.println("������ �������: " + e);
		}
	}
}