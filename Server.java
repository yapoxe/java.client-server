import java.io.*; 
import java.util.*;

class Electric {
	double T1, T2, T3; 
	Integer N;
	// Конструктор
	Electric() {
		T1 = T2 = T3 = N = 0;
	}
	// Конструктор копирования
	Electric(Electric X) {
		T1 = X.T1;
		T2 = X.T2;
		T3 = X.T3;
		N = X.N;
	}
	// Подсчитываем рассчетную стоимость
	double sum() {
		return T1 * 0.5 + T2 * 0.63 + T3 * 0.8;
	}
	// Устанавливаем заданные значения
	void set(double T1, double T2, double T3, Integer N) {
		this.T1 = T1;
		this.T2 = T2;
		this.T3 = T3;
		this.N = N;
	}
}

public class Server {
	public static void main(String[] args) {
		/* System.out.println("Эта программа считывает показания счётчиков (T1, T2, T3) для каждой квартиры из файла input.txt"
				+ " и выводит рассчетную стоимость в файл output.txt.\nПосле подтверждения в консоли пересылает все данные на сервер."
				+ "\nДля файлов input.txt и output.txt существует определенные правила заполнения."
				+ "\nПример строки входа в input.txt: \"Квартира X: T1 T2 T3\""
				+ "\nПример строки выхода в output.txt: \"Квартира X: K руб.\"\n\n");
		*/
		
		// Считываем информацию из input.txt и записываем стоимость в output.txt
		try(FileReader fileInput = new FileReader("input.txt");
			FileWriter fileOutput = new FileWriter("output.txt");
			BufferedReader bufferedInput = new BufferedReader(fileInput);
			BufferedWriter bufferedOutput = new BufferedWriter(fileOutput)) {
			// Создаем массив аккаунтов
			ArrayList<Electric> accountList = new ArrayList<>(100);
			Electric importAccount = new Electric();
			// Считываем строки, разделяем их и получаем нужные T1, T2, T3 и N квартиры
			String[] strSub;
			String strInput = bufferedInput.readLine();
			Integer k = 0;
			while(strInput != null) {
				// System.out.println(strInput);
				strSub = strInput.split(" ");
				// Проверка строк на формат
				try {
					Integer T1 = new Integer(strSub[2]);
					Integer T2 = new Integer(strSub[3]);
					Integer T3 = new Integer(strSub[4]);
					Integer N = new Integer(strSub[1].substring(0, strSub[1].length() - 1));
					// System.out.println(T1 + " " + T2 + " " + T3 + " " + N);
					importAccount.set(T1, T2, T3, N);
					accountList.add(k++, importAccount);
					bufferedOutput.write(strSub[0] + " " + importAccount.N + ": " + importAccount.sum() + " руб.\n");
					bufferedOutput.newLine();
				} catch(NumberFormatException e) {
					System.out.println("Ошибка. Неверный формат строки.");
				}
				strInput = bufferedInput.readLine();
			}
		} catch(IOException e) {
			System.out.println("Ошибка: " + e);
		}		
	}
}