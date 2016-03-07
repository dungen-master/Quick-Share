import java.awt.EventQueue;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;



public class ChooseDirectory {

	private JFrame frame;
	public JFileChooser chooser;
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
            java.util.logging.Logger.getLogger(ChooseDirectory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChooseDirectory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChooseDirectory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChooseDirectory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
		ChooseDirectory t=new ChooseDirectory();
	}

	
	public ChooseDirectory() {
		//initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public String initialize() {
		
		String res="";

		chooser = new JFileChooser(); 
	    chooser.setCurrentDirectory(new java.io.File("."));
	    chooser.setDialogTitle("Choose Directory");
	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    chooser.setAcceptAllFileFilterUsed(false);
	 	chooser.setApproveButtonText("OK");


	    int n=chooser.showOpenDialog(null);

	    if (n == JFileChooser.APPROVE_OPTION) { 
	      		res=chooser.getSelectedFile().toString();
	      		return res;
	      }
	    else if(n == JFileChooser.CANCEL_OPTION){
	      		res="";
	      		return res;
	      }
  

	    return null; 
	} 
		

}
