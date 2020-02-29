package com;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	public static void main(String[] args) throws IOException  {
		// TODO Auto-generated method stub
		for (int i = 2;i<args.length;i++){
			try{
				new Client(args[0],Integer.parseInt(args[1]),args[i]);
			}catch(IOException e){
				System.out.println(e);
			}
		}
//		copy("/home/roc/info.txt", "/home/roc/info2.txt");
	}
	
	Client(String host, int port, String filename) throws IOException{
		
		File f = new File(filename);
		FileInputStream fi  = new FileInputStream(f);
		
//		System.exit(0);
		Socket socket = null;
		socket = new Socket(host, port);
		
		OutputStream outputStream = socket.getOutputStream();
		String []fns = filename.split("/");
		outputStream.write((fns[fns.length-1]+"__FILE__").getBytes());
		outputStream.flush();
		
		int total = fi.available();
		int read_len = 0;
		byte [ ] b = new byte[256];
		int len = 0;
		while((len = fi.read(b))!=-1) {
			outputStream.write(b,0,len);
			outputStream.flush();
			read_len += len;
			float r = (float)read_len/total*100;
			System.out.printf("Send %s %.1f %%\r", filename, r);
		}
		System.out.println();
		outputStream.close();
		socket.close();
	}
	
	public static void copy(String name1, String name2) throws IOException {
		File fi, fo;
		fi = new File(name1);
		fo = new File(name2);
		FileInputStream fis = new FileInputStream(fi);
		FileOutputStream fos = new FileOutputStream(fo);
		byte [] b = new byte[256];
		int off = 0;
		int len = 0;
		while((len = fis.read(b))!=-1) {
			System.out.printf("%d %d\n",off, len);
			fos.write(b,0,len);
			off += len;
		}
		
		fis.close();
		fos.close();
 	}

}
