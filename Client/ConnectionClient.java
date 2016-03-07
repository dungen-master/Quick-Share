import java.io.*;
import java.net.*;
import javax.swing.Timer;
import java.awt.event.*;

public class ConnectionClient extends Thread{

	public static Socket socket;
	public static DataOutputStream output;
	public static DataInputStream input;
	public boolean isConnected;
	public static String fileList;
	public static Timer timer;

	public ConnectionClient()
	{
		isConnected=false;

		try{

			socket=new Socket(InetAddress.getByName(ShareIt.ip),ShareIt.port);
			output=new DataOutputStream(socket.getOutputStream());
			input=new DataInputStream(socket.getInputStream());
			isConnected=true;
		}
		catch(Exception e)
		{
			isConnected=false;
			e.printStackTrace();
		}

	}

	public boolean checkConnection()
	{
		return isConnected;
	}

	public int checkUsername(String name)
	{
		try{

			output.writeUTF(name);
			String s=input.readUTF();
			if(s.equals("exists_username"))
			{
				try{

					output.close();
					input.close();
					socket.close();

					return 0;
				}
				catch(Exception e)
				{
					e.printStackTrace();
					return 0;
				}
				
			}
			else if(s.equals("ok_username"))
			{
				return 1;
			}
			else return -1;
		}
		catch(Exception e)
		{
			return -1;
		}
	}

	public static void sendFileData(String file_name)
	{
		try{

			output.writeUTF("update_file_list");
			output.writeUTF(ArticleShare.directory);
			output.writeUTF(file_name);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void getFileList()
	{
		try{

			// System.out.println(ShareIt.username);
			output.writeUTF("get_file_lists");
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
	}

	public static void disconnect()
	{
		try{

			output.writeUTF("connection_cut");
			input.close();
			output.close();
			socket.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void downloadFile(String fileName)
	{
		try{

			output.writeUTF("download_file");
			output.writeUTF(fileName);

			ServerSocket sc=new ServerSocket(5100,1,InetAddress.getByName("127.0.0.1"));
			Socket tempSocket=sc.accept();

			DataInputStream dr=new DataInputStream(tempSocket.getInputStream());

			InputStream in=tempSocket.getInputStream();
			OutputStream out=new FileOutputStream(ArticleShare.directory+"/"+fileName);
			byte[] bytes=new byte[1024];

        	int count,i=1,j=0;
        	String temp=dr.readUTF();
        	long fileLength=Long.parseLong(temp);
        	System.out.println(fileLength);

        	while((count=in.read(bytes))>0 )
        	{
        		j+=count;
        		i=(j/100);
        		DownloadFile.downloadBar.setValue(i);

        		out.write(bytes,0,count);
        	}		

        	
        	System.out.println("Download complete ");

        	tempSocket.close();
        	in.close();
        	out.close();
        	dr.close();
        	sc.close();

        	ShareIt.updateFilesList();
        	DownloadFile.lblDownload.setText("Download Complete");
			// System.out.println(fileName+" "+ArticleShare.directory);

        	
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
	}

	public void run()
	{
		try{

			while(true)
			{
				String temp=input.readUTF();
				if(temp.equals("get_file_lists"))
				{
					fileList="";
					String s=input.readUTF();
					fileList+=s;
					// System.out.println(fileList);

				}
				else if(temp.equals("SEND_FILE"))
				{
					String p=input.readUTF();
					String[] p1=p.split("\\n");
					String temp_ip=p1[0];
					String temp_filename=p1[1];

					try{

						Socket tempSocket=new Socket(InetAddress.getByName(temp_ip),5100);
						InputStream in=new FileInputStream(new File(ArticleShare.directory+"/"+temp_filename));
						OutputStream out=tempSocket.getOutputStream();
						DataOutputStream dr=new DataOutputStream(tempSocket.getOutputStream());

						byte[] bytes=new byte[1024];

						File f=new File(ArticleShare.directory+"/"+temp_filename);
						long l=f.length();
						String t=String.valueOf(l);
						dr.writeUTF(t);

        				int count;
        				while((count=in.read(bytes))>0)
        				{
        					out.write(bytes,0,count);
        				}		

        				System.out.println("File sending complete");

        				in.close();
        				out.close();
        				dr.close();
        				tempSocket.close();
        				

					}
					catch(Exception ep)
					{
						ep.printStackTrace();
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


}