package com;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		new Server(Integer.parseInt(args[0]));
	}
	public Server(int port) throws IOException {
		System.out.println("running ...");
		ServerSocket serverSocket = new ServerSocket(port);
		while(true) {
			Socket clientSocket = serverSocket.accept();
			
			System.out.println("recieved");
			InputStream is = clientSocket.getInputStream();
			byte[] str = new byte[256];
			int len = 0;
			
			if((len = is.read(str))!=-1) {
//				byte[]a  = new byte[len];
//				System.arraycopy(str, 0, a, 0, len);
				String fn = new String(str,"UTF-8");
				if (fn.substring(0,7).equals("__STR__")){
					ArrayList<Byte> byteList = new ArrayList<Byte>();
					for (int i = "__STR__".getBytes().length;i<len;i++){
						byteList.add(str[i]);
					}
					while((len = is.read(str))!=-1){
						for (int i = 0;i<len;i++){
							byteList.add(str[i]);
						}
					}
					byte textBytes[] = new byte[byteList.size()];
					for (int i = 0;i<byteList.size();i++){
						textBytes[i] = byteList.get(i);
					}
					String text = new String(textBytes,"UTF-8");
					System.out.println(text);
				}
				else{
				int ind = fn.indexOf("__FILE__");
				String real_fn = fn.substring(0, ind);
				System.out.println("file name: "+real_fn);
				File f = new File("/home/roc/received/"+real_fn);
				FileOutputStream fos = new FileOutputStream(f);
//				byte [] left = fn.substring(ind+8).getBytes();
				int head_len = (real_fn+"__FILE__").getBytes().length;
				fos.write(str,head_len,len-head_len);
				while((len = is.read(str)) != -1) {
	//				System.out.println(""+len);
					fos.write(str,0,len);
				}
				fos.close();
			}
				
			}
			
			is.close();
			clientSocket.close();
		}
	}

}
