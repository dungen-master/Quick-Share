import java.awt.EventQueue;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;


public class ArticleShare {

	public static JFrame frame;
	private JTextField browseField;
	private JLabel lblWarning;
	public static String directory;
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
            java.util.logging.Logger.getLogger(ArticleShare.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ArticleShare.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ArticleShare.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ArticleShare.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ArticleShare window = new ArticleShare();
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
	public ArticleShare() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Quick Share");
		frame.setBounds(100, 100, 529, 100);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);

		frame.addWindowListener(new WindowAdapter()
        {
           @Override
           public void windowClosing(WindowEvent e)
           {
               if(JOptionPane.showConfirmDialog(frame,"Are you sure?")==JOptionPane.YES_OPTION)
				{
					frame.dispose();
				}
           }
        });

        JLabel lblChooseShareDirectory = new JLabel("Choose Shared Directory");
		lblChooseShareDirectory.setBounds(10, 11, 159, 14);
		frame.getContentPane().add(lblChooseShareDirectory);
		
		//browse field
		browseField = new JTextField();
		browseField.setBackground(Color.WHITE);
		browseField.setEditable(false);
		browseField.setBounds(10, 30, 360, 26);
		frame.getContentPane().add(browseField);
		browseField.setColumns(10);
		
		//warning field
		lblWarning=new JLabel("");
		lblWarning.setForeground(Color.RED);
		lblWarning.setBounds(10,53,360,25);
		frame.getContentPane().add(lblWarning);

		//browse button
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.setBounds(380, 29, 89, 23);
		frame.getContentPane().add(btnBrowse);
		
		//save button
		JButton btnSave = new JButton("Save");
		btnSave.setBounds(380, 59, 89, 23);
		frame.getContentPane().add(btnSave);

		btnBrowse.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e)
			{
				 ChooseDirectory cr=new ChooseDirectory();
        		 directory=cr.initialize();
        		 browseField.setText(directory);
			}
		});

       btnSave.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(browseField.getText().toString().equals(""))
				{
					lblWarning.setText("Choose Directory before proceed");
				}
				else
				{
					directory=browseField.getText().toString();
					ShareIt sr=new ShareIt();
					frame.dispose();
				}
			}
		});


	}
}		