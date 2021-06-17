import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;


public class ServerSocketThread extends Thread{
	Socket socket;
	Server server;
	BufferedReader in;
	PrintWriter out;
	String name;
	String threadName;
	
	public ServerSocketThread(Server server,Socket socket) {
		this.server=server;
		this.socket=socket;
		threadName=super.getName();
		System.out.println(socket.getInetAddress() + "님이 입장했습니다.");
		System.out.println("Thread Name : " + threadName);

	}
	
	//클라이언트로 메시지 출력
	public void sendMessage (String str) {
		out.println(str);
	}
	// 쓰레드override
	@Override
	public void run() {
		try {
			in= new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			//true autoflash 설정
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			

			sendMessage("대화자 이름을 넣으세요");
			name=in.readLine();
			server.broadCasting("[" + name+"]님이 입장하셨습니다.");
			
			while(true) {
				String str_in =in.readLine();
				server.broadCasting("[" + name + "] "+str_in);
				
			}
		}catch (IOException e) {
			System.out.println(threadName + "퇴장했습니다.");
			server.removeClient(this);
			
			
		}finally {
			try {
				//버전에따라 객체 클로즈안해도괜찮음 알아서닫힐수도잇긴하지만 보험~~
				socket.close();
				
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
	
}
