
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


class DownloadFile {

	private JFrame frame;
	public static int open;
	public static JProgressBar downloadBar;
	public static JLabel lblDownload;
	public static JComboBox listFileBox ;
	public Timer timer;
	public String fileList;

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
            java.util.logging.Logger.getLogger(DownloadFile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DownloadFile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DownloadFile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DownloadFile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DownloadFile window = new DownloadFile();
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
	public DownloadFile() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Remote Files");
		frame.setBounds(100, 100, 450, 207);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);


		// ConnectionClient.getFileList();
		
		fileList=ConnectionClient.fileList;
		// System.out.println("file Request: "+fileList);

		frame.addWindowListener(new WindowAdapter()
        {
           @Override
           public void windowClosing(WindowEvent e)
           {
               if(JOptionPane.showConfirmDialog(frame,"Are you sure?")==JOptionPane.YES_OPTION)
				{
					open=0;
					ShareIt.enableField(true);
					frame.dispose();
				}
           }
        });

		//downlaod bar
		downloadBar = new JProgressBar();
		downloadBar.setForeground(new Color(0, 128, 0));
		downloadBar.setBounds(90, 79, 255, 18);
		downloadBar.setStringPainted(true);
		downloadBar.setValue(0);
		frame.getContentPane().add(downloadBar);
		
		//download label
		lblDownload = new JLabel("");
		lblDownload.setBounds(171, 104, 180, 20);
		frame.getContentPane().add(lblDownload);


		//list of files
		listFileBox = new JComboBox();
		listFileBox.addItem("None");
		listFileBox.setBounds(10, 11, 216, 20);
		frame.getContentPane().add(listFileBox);

		// System.out.println("asd");
		if(fileList!=null && !fileList.isEmpty() && !fileList.equals(""))
		{
			String[] temp=fileList.split("\\n");
			for(int i=0;i<temp.length;i++)
				listFileBox.addItem(temp[i]);
		}
		
		
		//Download button
		JButton btnDownload = new JButton("Download");
		btnDownload.setBounds(236, 10, 89, 23);
		frame.getContentPane().add(btnDownload);

		btnDownload.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(listFileBox.getSelectedItem().toString().equals("None"))
				{
					lblDownload.setVisible(true);
					lblDownload.setText("No files selected");

					timer=new Timer(2000,new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent ae)
					{
						lblDownload.setText("");
						timer.stop();
					}
					});

					timer.start();
		
				}
				else
				{
					lblDownload.setText("Downloading.....");
					downloadBar.setVisible(true);
					ConnectionClient.downloadFile(listFileBox.getSelectedItem().toString());
				}
			}
		});
		
		//Details button
		JButton btnDetails = new JButton("Details....");
		btnDetails.setBounds(335, 10, 89, 23);
		frame.getContentPane().add(btnDetails);
		
		
		downloadBar.setVisible(false);
		lblDownload.setText("");
	}
}
