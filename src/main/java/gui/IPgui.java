package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.*;

import thread.Worker;

public class IPgui extends JFrame {
	
	private static final String INPUT = "Input";
	private static final String OUTPUT = "Output";
	
	private JButton start;
	private JTextArea inputArea;
	private JTextArea statusArea;
	
	
	public String getClipboardContents() {
        String result = "";
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        //odd: the Object param of getContents is not currently used
        Transferable contents = null;
        try {
            contents = clipboard.getContents(null);
        } catch(IllegalStateException ise) {
            ise.printStackTrace();
        }
        boolean hasTransferableText =
          (contents != null) &&
          contents.isDataFlavorSupported(DataFlavor.stringFlavor)
        ;
        if ( hasTransferableText ) {
          try {
            result = (String)contents.getTransferData(DataFlavor.stringFlavor);
          }
          catch (UnsupportedFlavorException ex){
            //highly unlikely since we are using a standard DataFlavor
            System.out.println(ex);
            ex.printStackTrace();
          }
          catch (IOException ex) {
            System.out.println(ex);
            ex.printStackTrace();
          }
        }
        return result;
      }
	
	public IPgui(){
		super("IP Locator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BoxLayout(super.getContentPane(), BoxLayout.PAGE_AXIS));
		
		//the text panel1
		JPanel textPanel1 = new JPanel();
		inputArea = new JTextArea(20, 50);
        inputArea.setEditable(true);
        inputArea.setLineWrap(true);
        JScrollPane scrollPane1 = new JScrollPane(inputArea);
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;     
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        textPanel1.add(scrollPane1, c);
		add(textPanel1);
		
		//the text panel2
		JPanel textPanel2 = new JPanel();
		statusArea = new JTextArea(10, 50);
        statusArea.setEditable(false);
        statusArea.setLineWrap(true);
        statusArea.setBackground(new Color(232, 232, 237));
        JScrollPane scrollPane2 = new JScrollPane(statusArea);
        textPanel2.add(scrollPane2, c);
		add(textPanel2);
        
        //the button panel
		JPanel cPanel = new JPanel();
		cPanel.setLayout(new BoxLayout(cPanel, BoxLayout.Y_AXIS));
		//init button
		start = new JButton("Start");
		//and its action listener
		start.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				fetchModeOn();
				Worker w = new Worker(inputArea.getText(), IPgui.this);
				w.start();
			}
		});
		
		cPanel.add(start);
		cPanel.add(Box.createRigidArea(new Dimension(0,10)));
		
		add(cPanel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		super.setVisible(true);
	}
	
	public void fetchModeOn(){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				start.setEnabled(false);
			}
		});
	}
	
	public void fetchModeOff(){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				start.setEnabled(true);
			}
		});
	}
		
	
	public void addStatusLine(final String status){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				statusArea.append(status);
			}
		});
	}
	
	public void clearStatusArea(){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				statusArea.setText("");
			}
		});
	}
	
	
	public static void main(String[] args){
		//set nimbus, if possible
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		}
		//launch
		IPgui frame = new IPgui();
	}

}