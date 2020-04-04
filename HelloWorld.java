import java.io.*; 
import java.util.*;

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

public class HelloWorld {
	public static void main(String[] args) {
		/* System.out.println("��� ��������� ��������� ��������� ��������� (T1, T2, T3) ��� ������ �������� �� ����� input.txt"
				+ " � ������� ���������� ��������� � ���� output.txt.\n����� ������������� � ������� ���������� ��� ������ �� ������."
				+ "\n��� ������ input.txt � output.txt ���������� ������������ ������� ����������."
				+ "\n������ ������ ����� � input.txt: \"�������� X: T1 T2 T3\""
				+ "\n������ ������ ������ � output.txt: \"�������� X: K ���.\"\n\n");
		*/
		
		// ��������� ���������� �� input.txt � ���������� ��������� � output.txt
		try(FileReader fileInput = new FileReader("input.txt");
			FileWriter fileOutput = new FileWriter("output.txt");
			BufferedReader bufferedInput = new BufferedReader(fileInput);
			BufferedWriter bufferedOutput = new BufferedWriter(fileOutput)) {
			// ������� ������ ���������
			ArrayList<Electric> accountList = new ArrayList<>(100);
			Electric importAccount = new Electric();
			// ��������� ������, ��������� �� � �������� ������ T1, T2, T3 � N ��������
			String[] strSub;
			String strInput = bufferedInput.readLine();
			Integer k = 0;
			while(strInput != null) {
				// System.out.println(strInput);
				strSub = strInput.split(" ");
				// �������� ����� �� ������
				try {
					Integer T1 = new Integer(strSub[2]);
					Integer T2 = new Integer(strSub[3]);
					Integer T3 = new Integer(strSub[4]);
					Integer N = new Integer(strSub[1].substring(0, strSub[1].length() - 1));
					// System.out.println(T1 + " " + T2 + " " + T3 + " " + N);
					importAccount.set(T1, T2, T3, N);
					accountList.add(k++, importAccount);
					bufferedOutput.write(strSub[0] + " " + importAccount.N + ": " + importAccount.sum() + " ���.\n");
					bufferedOutput.newLine();
				} catch(NumberFormatException e) {
					System.out.println("������. �������� ������ ������.");
				}
				strInput = bufferedInput.readLine();
			}
		} catch(IOException e) {
			System.out.println("������: " + e);
		}		
	}
}