import java.io.*; 
import java.util.*;
import java.net.*;

class ServerThread implements Runnable {
	Scanner inputStream = new Scanner(System.in);
	String nameThread;
	Thread t;
	ServerThread(String nameThread) {
		this.nameThread = nameThread;
		t = new Thread(this, nameThread);
		t.start();
	}
	public void run() {
		while(true) {
			nameThread = inputStream.nextLine();
			if(nameThread.equals("stop")) {
				System.out.println("Сервер остановлен.");
				System.exit(0);
			}
		}
	}
}

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
	
	private static Integer port = 30000;
	private static String passwordBD = "TestPassword";
	
	public static void main(String[] args) {
		/* System.out.println("Эта программа считывает показания счётчиков (T1, T2, T3) для каждой квартиры из файла input.txt"
				+ " и выводит рассчетную стоимость в файл output.txt.\nПосле подтверждения в консоли пересылает все данные на сервер."
				+ "\nДля файлов input.txt и output.txt существует определенные правила заполнения."
				+ "\nПример строки входа в input.txt: \"Квартира X: T1 T2 T3\""
				+ "\nПример строки выхода в output.txt: \"Квартира X: K руб.\"\n\n");
		*/
		new ServerThread("Waiting process");
		// Создаем массив аккаунтов
		ArrayList<Electric> accountList = new ArrayList<>();
		// Открываем файлы, создаем буферы обмена
		try(FileReader fileInput = new FileReader("input.txt");
			FileWriter fileOutput = new FileWriter("output.txt");
			BufferedReader bufferedInputFile = new BufferedReader(fileInput);
			BufferedWriter bufferedOutputFile = new BufferedWriter(fileOutput)) {
			// Считываем строки, разделяем их и получаем нужные T1, T2, T3 и N квартиры
			String[] strSub;
			String strInput = bufferedInputFile.readLine();
			while(strInput != null) {
				// System.out.println(strInput);
				strSub = strInput.split(" ");
				Electric importAccount = new Electric();
				// Проверка строк на формат
				try {
					Integer T1 = new Integer(strSub[2]);
					Integer T2 = new Integer(strSub[3]);
					Integer T3 = new Integer(strSub[4]);
					Integer N = new Integer(strSub[1].substring(0, strSub[1].length() - 1));
					importAccount.set(T1, T2, T3, N);
					accountList.add(importAccount);
					bufferedOutputFile.write("Квартира " + importAccount.N + ": " + importAccount.sum() + " руб.\n");
					// Записываем в файл информацию, все дескрипторы автоматически закроются после прохождения блока try>catch>finally
					bufferedOutputFile.newLine();
				} catch(NumberFormatException e) {
					System.out.println("Ошибка. Неверный формат строки.");
				}
				strInput = bufferedInputFile.readLine();
			}
		} catch(IOException e) {
			System.out.println("Ошибка ввода-вывода: " + e);
		}
		// Запускаем сервер с портом 30000
		try(ServerSocket serverSocket = new ServerSocket(port)) {
			System.out.println("Сервер запущен. Ожидание подключений...");
			while(true) {
				Socket clientSocket = serverSocket.accept();
				System.out.println("Пользователь подключился.");
				try(BufferedReader bufferedChatIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					BufferedWriter bufferedChatOut = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {
					bufferedChatOut.write("Вы подключились к серверу.\n\n");
					bufferedChatOut.flush();
					String clientMessage;
					for(int i = 3; i != 0; i--) {
						bufferedChatOut.write("Введите пароль для доступа к БД (осталось попыток: " + i + "): \n\n");
						bufferedChatOut.flush();
						clientMessage = bufferedChatIn.readLine();
						if(clientMessage.equals(passwordBD)) {
							bufferedChatOut.write("\nПравильный пароль. Подключение...\n"); 
							bufferedChatOut.flush(); 
							break;
						} else {
							if(i == 1) {
								bufferedChatOut.write("\nИсчерпано количество попыток подключения. Соединение разорвано.\n");
								bufferedChatOut.flush();
								throw new IllegalArgumentException();
							}
							bufferedChatOut.write("\nНеправильный пароль. Попробуйте ещё раз.\n");
							bufferedChatOut.flush();
						}
					}
					bufferedChatOut.write("Вы успешно подключились к БД! К сожалению, она пока что в текстовом файле.\n"
							+ "Но скоро будет в MySQL! Вы хотите её просмотреть? (Yes/No)\n\n");
					bufferedChatOut.flush();
					clientMessage = bufferedChatIn.readLine();
					if(clientMessage.equals("Yes")) {
						Electric importAccount = new Electric();
						bufferedChatOut.write("\nПоказания счётчиков:\n");
						bufferedChatOut.flush();
						for(int i = 0; i < accountList.size(); i++) {
							importAccount = accountList.get(i);
							bufferedChatOut.write((i + 1) + ". Квартира номер " + importAccount.N + ": " + importAccount.T1 
									+ " " + importAccount.T2 + " " + importAccount.T3 + "\n");
							bufferedChatOut.flush();
						}
						bufferedChatOut.write("\nБД загружена. Соединение закрыто.\n");
						bufferedChatOut.flush();
						System.out.println("Пользователь загрузил БД.");
					} else {
						bufferedChatOut.write("\nТогда не вижу смысла в соединении. Простите, но мне пора работать...\n");
						bufferedChatOut.flush();
						System.out.println("Пользователь отказался загрузить БД.");
					}
				} catch(IllegalArgumentException e) {
				} catch(IOException e) {
					System.out.println("Ошибка: " + e);
				} finally {
					System.out.println("Пользователь отключился.");
					clientSocket.close();
				}
			}
		} catch(IOException e) {
			System.out.println("Ошибка сервера: " + e);
		}
	}
}