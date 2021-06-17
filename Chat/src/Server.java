import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {
	public static final int PORT = 6077;
	ServerSocket serverSocket=null ;
	

	// 입출력 설정
	InputStream is = null;
	InputStreamReader isr = null;
	BufferedReader br = null;

	OutputStream os = null;
	OutputStreamWriter osw = null;
	PrintWriter pw = null;
	PrintWriter save = null;
	
	Scanner sc = new Scanner(System.in);

	List<Thread> list;

	public Server() {
		list = new ArrayList<Thread>();
		System.out.println("서버 시작");
	}

	public void Connect() {
		try {
			// server socket 생성
			serverSocket = new ServerSocket();
			

			// binding socket에 socket addr 바인딩 (ip+port)
			InetAddress inetAddress = InetAddress.getLocalHost();
			String localhost = inetAddress.getHostAddress();

			serverSocket.bind(new InetSocketAddress(localhost, PORT));

			System.out.println("[server] binding " + localhost);
				
			
			while (true) {
			

				// accept 클라이언트로부터 연결 기다림

				Socket socket = serverSocket.accept();
				//accept 한후에 리스트에 쓰레드 저장
				ServerSocketThread thread =new ServerSocketThread(this,socket);
				addClient(thread);
				thread.start();


		
			} // while 종료

		} // try 종료
		  catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {

				if (serverSocket != null && !serverSocket.isClosed())
					serverSocket.close();

			} catch (Exception e) {
				e.printStackTrace();
			}//try catch  end
			sc.close();
		}//finally end

	}//public Connect end
	
	// 퇴장시 호출 쓰레드 배열에서 제거 synchronized 스레드간 동기화 
	// data의 thread-safe 가능케함
//	스레드간 서로 공유하고 수정할 수 있는 data가 있는데 스레드간 동기화가 되지 않은 상태에서 
//
//	멀티스레드 프로그램을 돌리면, data의 안정성과 신뢰성을 보장할 수 없습니다.

	// 클라이언트가 입장 시 호출되며, 리스트에 클라이언트 담당 쓰레드 저장
	//접속은 서버에서만가능해야하므로 private형 
		private synchronized void addClient(ServerSocketThread thread) {
			// 리스트에 ServerSocketThread 객체 저장
			list.add(thread);
			System.out.println("Client 1명 입장. 총 " + list.size() + "명");
		}		


	
	public synchronized void removeClient(Thread thread) {
		list.remove(thread);
		System.out.println("Client 1명 퇴장. 총" +list.size() +"명");
	}
	
	
	
	// 모든 클라이언트에게 채팅 내용 전달
	public synchronized void broadCasting(String str) {
		for(int i=0; i<list.size();i++) {
			ServerSocketThread thread =(ServerSocketThread)list.get(i);
			thread.sendMessage(str);
		}
	}

}


