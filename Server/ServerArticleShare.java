import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.net.*;
import java.util.*;

public class ServerArticleShare extends Thread{

	private JFrame frame;
	public static JTextArea actionArea;
	public static JList<String> fileArea,clientArea;
	public static DefaultListModel fileList,clientList;

	public final String ip="127.0.0.1";
	public final int port=5000;
	public static ArrayList<Connection> connectionList;
	public static String actions="";
	public static ArrayList<String> str;
	public static JScrollPane scrollPane_1,scrollPane_2;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ServerArticleShare.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ServerArticleShare.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ServerArticleShare.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ServerArticleShare.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerArticleShare window = new ServerArticleShare();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ServerArticleShare() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle("ArticleShare --- Server");
		frame.setBounds(100, 100, 746, 450);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		frame.addWindowListener(new WindowAdapter()
        {
           @Override
           public void windowClosing(WindowEvent e)
           {
               if(JOptionPane.showConfirmDialog(frame,"Are you sure?")==JOptionPane.YES_OPTION)
				{
					frame.dispose();
					System.exit(0);
				}
           }
        });
		
		//CLIENTS
		JLabel lblClients = new JLabel("Clients");
		lblClients.setBounds(10, 11, 46, 14);
		frame.getContentPane().add(lblClients);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 32, 200, 262);
		frame.getContentPane().add(scrollPane);

		str=new ArrayList<String>();
		
		clientList=new DefaultListModel();
		clientArea = new JList<String>(clientList);
		// clientArea.setModel(clientList);
		clientArea.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(clientArea);

		clientArea.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                	if(clientArea.getSelectedIndex()!=-1)
                	{
                		String temp=clientArea.getSelectedValue().toString();
                  		
                  		showFiles(temp);
                	}
                  
                }
            }
        });
		
		//FILES
		JLabel lblFiles = new JLabel("Files");
		lblFiles.setBounds(232, 11, 46, 14);
		frame.getContentPane().add(lblFiles);

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(232, 32, 488, 262);
		frame.getContentPane().add(scrollPane_1);
		
		fileList=new DefaultListModel();
		fileArea = new JList<String>(fileList);
		fileArea.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane_1.setViewportView(fileArea);

		//ACTION
		scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(10, 313, 710, 111);
		frame.getContentPane().add(scrollPane_2);
		
		actionArea = new JTextArea();
		actionArea.setEditable(false);
		scrollPane_2.setViewportView(actionArea);

		connectionList=new ArrayList<Connection>();
		
		this.start();
		
	}

	public static void addActions()
	{
		actionArea.setText(actions);
	}

	public static void validateFileArea()
	{
		if(clientArea.getSelectedIndex()==-1)
		{
			showFiles("");
		}
	}

	public static void revalidateAreas()
	{
		scrollPane_2.revalidate();
		scrollPane_2.repaint();
	}

	public static void addElement()
	{
		clientList=new DefaultListModel();
		
		for(int i=0;i<str.size();i++)
		{
			clientList.addElement(str.get(i));
		}
		clientArea.setModel(clientList);

		scrollPane_1.revalidate();
		scrollPane_1.repaint();

		validateFileArea();

	}

	public static void removeClientList(String s)
	{
		actions+="\nConnection removed : "+s;
		addActions();
		str.remove(s);
		addElement();
	}

	public void run()
	{
		try{

			ServerSocket sc=new ServerSocket(port,1,InetAddress.getByName(ip));
			while(true)
			{
				Socket socket=sc.accept();
				Connection con=new Connection(socket,socket.getInetAddress().toString());
				boolean ck=con.checkUsername();

				if(ck==false)
				{
					con.closeConnection();
				}
				else
				{
					// System.out.println("Connection added "+con.username+" ip: "+con.ip);
					actions+="\nConnection added " +con.username+" ip: "+con.ip;
					addActions();

					connectionList.add(con);
					clientList.addElement(con.username);
					str.add(con.username);
					addElement();
					con.start();
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void showFiles(String name)
	{
		fileList.removeAllElements();
		if(name.equals(""))
			return;

		for(int i=0;i<connectionList.size();i++)
		{
			Connection con=connectionList.get(i);
			if(name!=null && con.username.equals(name))
			{
				for(int j=0;j<con.files.size();j++)
				{
					fileList.addElement(con.files.get(j));
				}
				// System.out.println(name);

				break;
			}
		}
	}
}
