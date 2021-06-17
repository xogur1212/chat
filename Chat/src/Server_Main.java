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
import java.util.Scanner;

public class Server_Main {
	public static final int PORT =6077;
	//포트번호 6077
	
	public static void main(String[] args) {
		//소켓 설정
		ServerSocket serverSocket =null;
		
		//입출력 설정
		InputStream is =null;
		InputStreamReader isr=null;
		BufferedReader br= null;
		
		OutputStream os =null;
		OutputStreamWriter osw=null;
		PrintWriter pw =null;
		PrintWriter save =null;
		String babo="babo";
		Scanner sc =new Scanner(System.in);
		
		try {
			//server socket 생성
			serverSocket =new ServerSocket();
			//저장할 save 파일 
			
			//binding socket에 socket addr 바인딩 (ip+port)
			InetAddress inetAddress =InetAddress.getLocalHost();
			String localhost =inetAddress.getHostAddress();
			
			serverSocket.bind(new InetSocketAddress(localhost,PORT));
			
			System.out.println("[server] binding " + localhost);
			
			//accept 클라이언트로부터 연결 기다림
			
			Socket socket =serverSocket.accept();
			InetSocketAddress socketAddress= (InetSocketAddress) socket.getRemoteSocketAddress();
			
			System.out.println("[server] connected by client");
			System.out.println("[server] connect with " + socketAddress.getHostString() + " " +socket.getPort());
		
			while(true) {
				//inputStream 가져와서 StreamReader와 BufferReader로 감싸줌
				is= socket.getInputStream();
				isr= new InputStreamReader(is, "UTF-8");
				br= new BufferedReader(isr);
				//outStream 가져와서 StreamReader와 BufferReader로 감싸줌
				
				os=socket.getOutputStream();
				osw= new OutputStreamWriter(os, "UTF-8");
				pw= new PrintWriter(osw,true);
				
				save =new PrintWriter(new FileWriter("d:/save.txt", true));
				
				String buffer =null;
				buffer =br.readLine(); //blocking
				if (buffer ==null) {
					//정상종료 :remote socket close()
					//메소드를 통해서 정상적으로 소켓을 닫은경우
					System.out.println("[server] closed by client");
					break;
				}
				
				System.out.println("[server] received : " +buffer);
				
				save.println(buffer);
				save.close();
				
				pw.println(buffer);
				
				
			}
				
			//accept(대기중) 
			//blocking 되면서 기다리는중 coonect가 들어오면 block이 풀림
		}catch (IOException e)
		{
			e.printStackTrace(); //에러 근원지를 찾아 단계별로 에러를 출력
		} finally {

            try {
 
                if (serverSocket != null && !serverSocket.isClosed())
                    serverSocket.close();
 
            } catch (Exception e) {
                e.printStackTrace();
            }
            sc.close();
		}
		
		
	}
}
//1. 먼저 서버측 소켓을 만들어 제공할 ip 주소와 포트를 명시한 후에 bind 메소드를 호출합니다. (Line 34~43)
//
//2. bind 되었으면 클라이언트의 요청이 있을 시에 accept 메소드를 호출하게 되고, 클라이언트과 연결되는 소켓을 하나 만듭니다.  (Line 47~51)
//
//3. 연결 이후에 소켓에서 InputStream과 OutputStream을 가져와서 IO를 수행할 준비를 합니다. 보조스트림을 연결합니다. (Line 55~64) 
//
//4. 클라이언트로 부터 온 문자열을 읽습니다. 만약 넘어오는 문자열이 null이면 클라이언트가 소켓을 닫았다는 뜻이므로 while 루프를 탈출하여 서버를 종료합니다. (Line 65~76)
//
//5. 받은 문자열을 그대로 클라이언트에게 전송합니다.
//
//
//
//++ 여기서 유의해야 할 부분은 PrintWriter를 생성할 때 autoFlush 옵션을 true로 주었을 때 println 메소드를 사용해야 autoFlush가 작동된다는 것입니다. 만약 다른 메소드를 사용할 시에 버퍼를 비워주는 메소드를 명시해 주어야합니다.
//
//
//
//++ while문으로 무한루프를 돌리는 이유는 한 번의 요청과 응답이 끝나면 소켓이 종료되기 때문입니다. 요청과 응답을 계속 이어지기 위해 무한루프로 클라이언트가 소켓을 닫을 때 까지 계속 연결을 유지시킵니다.
//
//

