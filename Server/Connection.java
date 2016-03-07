import java.net.*;
import java.io.*;
import java.util.*;

class Connection extends Thread{
	public String username;
	public DataInputStream input;
	public DataOutputStream output;
	public Socket socket;
	public ArrayList<String> files;
	public String directory;
	public String ip;

	public Connection(Socket soc,String ip_addr)
	{
		try{

			socket=soc;
			input=new DataInputStream(socket.getInputStream());
			output=new DataOutputStream(socket.getOutputStream());
			ip=ip_addr.substring(1,ip_addr.length());

			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public boolean checkUsername()
	{
		try{

			String temp=input.readUTF();
			username=temp;
			
			for(int i=0;i<ServerArticleShare.connectionList.size();i++)
			{
				Connection c=ServerArticleShare.connectionList.get(i);
				if(c.username.equals(temp))
				{
					output.writeUTF("exists_username");
					return false;
				}
			}
			output.writeUTF("ok_username");

			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public void run()
	{
		try{

			while(true)
			{

				String temp=input.readUTF();
				// System.out.println(temp);
				ServerArticleShare.actions+="\nCommand from client: "+this.username+" ------ "+temp;
				ServerArticleShare.addActions();

				if(temp.equals("update_file_list"))
				{
					directory=input.readUTF();
					temp=input.readUTF();

					String[] t=temp.split("\\n");
					files=new ArrayList<String>();
					for(int i=0;i<t.length;i++)
						files.add(t[i]);
					ServerArticleShare.revalidateAreas();
				}

				else if(temp.equals("download_file"))
				{
					
					String filename=input.readUTF();
					ServerArticleShare.actions+="\nCommand from client : "+this.username+" -------- "+"download_file - "+filename;
					for(int i=0;i<ServerArticleShare.connectionList.size();i++)
					{
						Connection con=ServerArticleShare.connectionList.get(i);
						
						for(int j=0;j<con.files.size();j++)
						{
							if(con.files.get(j).equals(filename) && !con.username.equals(this.username))
							{
								sendFile(con,filename,con.ip);
								break;
							}
						}
					}
				}

				else if(temp.equals("connection_cut"))
				{
					try{

						input.close();
						output.close();
						socket.close();
						ServerArticleShare.removeClientList(this.username);
						ServerArticleShare.connectionList.remove(this);
					}
					catch(Exception e)
					{
						ServerArticleShare.removeClientList(this.username);
						ServerArticleShare.connectionList.remove(this);
					}
					
				}

				else if(temp.equals("get_file_lists"))
				{
					String res="";
					
					for(int i=0;i<ServerArticleShare.connectionList.size();i++)
					{
						Connection con=ServerArticleShare.connectionList.get(i);
						
						// System.out.println(con.username);

						if(!con.username.equals(this.username))
						{
							for(int j=0;j<con.files.size();j++)
								res+=con.files.get(j)+"\n";
						}
					}
					output.writeUTF("get_file_lists");
					// System.out.println(res);
					output.writeUTF(res);
				}
				
			 
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
			
	}

	public void closeConnection()
	{
		try{

			input.close();
			output.close();
			socket.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void sendFile(Connection con,String filename,String ip)
	{
		try{

			con.output.writeUTF("SEND_FILE");
			con.output.writeUTF(ip+"\n"+filename);

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}