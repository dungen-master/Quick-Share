import java.awt.EventQueue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class ShareIt {

	public static JFrame frame;
	private static JTextField nameField;
	public static JTextArea filesArea;
	public static JButton btnConnect,btnShow,btnLogout;
	public static String ip="127.0.0.1";
	public static int port=5000;
	public JLabel lblWarning,lblName,lblLogName;
	public Timer timer;
	public static String username;
	public JPanel tempPanel,actionPanel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Metal".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ShareIt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ShareIt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ShareIt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ShareIt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ShareIt window = new ShareIt();
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
	public ShareIt() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */

	public static void enableField(boolean flag)
	{
		nameField.setEnabled(flag);
		btnConnect.setEnabled(flag);
		btnShow.setEnabled(flag);
		filesArea.setEnabled(flag);
		btnLogout.setEnabled(flag);
	}

	public void updateWarning(String msg)
	{
		lblWarning.setText(msg);
		timer=new Timer(2000,new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent ae)
		{
			lblWarning.setText("");
			timer.stop();
		}
		});

		// timer.setRepeats(false);
		timer.start();
	}

	public void LogoutPanel(boolean f)
	{
		lblName.setVisible(!f);
		nameField.setVisible(!f);
		btnConnect.setVisible(!f);
		btnShow.setVisible(f);
		lblLogName.setVisible(f);
		btnLogout.setVisible(f);
	}

	public void initialize() {

		frame = new JFrame();
		frame.setTitle("Quick Share");
		frame.setResizable(false);
		frame.setBounds(100, 100, 679, 474);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);


		frame.addWindowListener(new WindowAdapter()
        {
           @Override
           public void windowClosing(WindowEvent e)
           {
           		if(DownloadFile.open==0)
           		{
           			if(JOptionPane.showConfirmDialog(frame,"Are you sure?")==JOptionPane.YES_OPTION)
					{
						ConnectionClient.disconnect();
						frame.dispose();
						System.exit(0);
					}
           		}
               	
           }
        });


		//username
		lblName = new JLabel("Username");
		lblName.setBounds(190, 16, 77, 14);
		frame.getContentPane().add(lblName);
		
		nameField = new JTextField();
		nameField.setBounds(260, 11, 150, 25);
		frame.getContentPane().add(nameField);
		nameField.setColumns(10);
		

		//log out
		lblLogName=new JLabel("");
		lblLogName.setBounds(260, 11, 150, 25);
		frame.getContentPane().add(lblLogName);

		btnLogout=new JButton("Logout");
		btnLogout.setBounds(575, 12, 89, 23);
		frame.getContentPane().add(btnLogout);

		btnLogout.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e)
			{
				ConnectionClient.disconnect();
				LogoutPanel(false);
				filesArea.setText("");
				nameField.setText("");
			}
		});

		//warning label
		lblWarning=new JLabel("");
		lblWarning.setForeground(Color.RED);
		lblWarning.setBounds(260, 35, 160, 25);
		frame.getContentPane().add(lblWarning);

		
		//action panel
		actionPanel = new JPanel();
		actionPanel.setBackground(new Color(46, 139, 87));
		actionPanel.setBounds(10, 62, 653, 373);
		frame.getContentPane().add(actionPanel);
		actionPanel.setLayout(null);

		//files area
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(297, 27, 346, 335);
		actionPanel.add(scrollPane);
		
		filesArea = new JTextArea();
		scrollPane.setViewportView(filesArea);
		filesArea.setEditable(false);
		
		JLabel lblFiles = new JLabel("LOCAL FILES");
		lblFiles.setForeground(new Color(255, 255, 255));
		lblFiles.setBounds(297, 11, 116, 14);
		actionPanel.add(lblFiles);
		
		//files button
		btnShow = new JButton("Show Files");
		btnShow.setBounds(92, 170, 100, 23);
		actionPanel.add(btnShow);
		btnShow.setVisible(false);

		btnShow.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e)
			{
				
				enableField(false);
				DownloadFile.open=1;
				ConnectionClient.getFileList();
				timer=new Timer(2000,new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e2)
				{
					timer.stop();
					new DownloadFile();
				}

				});
				timer.start();

			}
		});

		//connect button
		btnConnect = new JButton("Connect");
		btnConnect.setBounds(420, 12, 89, 23);
		frame.getContentPane().add(btnConnect);

		btnConnect.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e)
			{
				if(nameField.getText().equals(""))
				{
					updateWarning("Username needed");
					
				}
				else
				{

					username=nameField.getText();

					ConnectionClient cc=new ConnectionClient();
					boolean isConnection=cc.checkConnection();

					if(!isConnection)
						updateWarning("Cannot connect to server");
					else
					{
						int p=cc.checkUsername(username);
						if(p==1)
						{
							lblLogName.setText(username);
							LogoutPanel(true);
							cc.start();
							updateFilesList();
							
						}
						else if(p==0)
						{
							updateWarning("Username exists..");
						}
						else
						{
							updateWarning("Connection problem..\n");
						}
					}
				}
			}
		});
		

		LogoutPanel(false);
		
		
	}

	public static void updateFilesList()
	{
		File[] files=new File(ArticleShare.directory).listFiles();
		String temp="";
		for(int i=0;i<files.length;i++)
			temp+=files[i].getName()+"\n";


		filesArea.setText(temp);
		ConnectionClient.sendFileData(temp);
	}
}
