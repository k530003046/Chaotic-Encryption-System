package client_server;

import java.awt.BorderLayout;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import crypto.CMET_2;
import javax.swing.JTextPane;

public class CMETImageDecryptUI extends JFrame {

	private String KEY = null;
	private JPanel contentPane;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CMETImageDecryptUI frame = new CMETImageDecryptUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CMETImageDecryptUI() {
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 350);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
				
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		contentPane.add(menuBar, BorderLayout.NORTH);
		
		JMenuItem mntmOpen = new JMenuItem("Open");
		fileMenu.add(mntmOpen);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		fileMenu.add(mntmSave);
		
		JButton btnDecrypt = new JButton("Decrypt");
		contentPane.add(btnDecrypt, BorderLayout.SOUTH);
		
		JLabel lbl = new JLabel();
		lbl.setAlignmentX(CENTER_ALIGNMENT);
		lbl.setAlignmentY(CENTER_ALIGNMENT);
		lbl.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.add(lbl, BorderLayout.CENTER);
		setVisible(true);
		setLocationRelativeTo(null);
		
		mntmOpen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFileChooser chooser = new JFileChooser();
				if(chooser.showOpenDialog(mntmOpen) == chooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					try {
						KEY = fileReader(file);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					System.out.println("Successfully");
				}
			}
			
		});
		
		mntmSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFileChooser chooser = new JFileChooser();
				if(chooser.showSaveDialog(mntmSave) == chooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					try {
//						fileWriter(textArea.getText(), file);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			
		});
		
		btnDecrypt.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					JFileChooser chooser = new JFileChooser();
					String noise = CMET_2.noiseGen(KEY);
					System.out.println(noise);
					if(chooser.showOpenDialog(btnDecrypt) == chooser.APPROVE_OPTION) {
						File f = chooser.getSelectedFile();
						CMET_2.decryptWithImage(f, noise);
						lbl.setIcon(SwingUtil.createAutoAdjustIcon("plainI.png", true, 0.90, 0.85));
//						textArea.setText(plainT);
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}					
			}
		});
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
	}
	public static String fileReader(File file) throws Exception {
		FileReader fr = new FileReader(file);
		char[] result = new char[(int) file.length()];
		fr.read(result);
		for(char c : result)
			System.out.print(c);
		System.out.println();
		fr.close();
		System.out.println(new String(result));
		return new String(result);
	}
	public static void fileWriter(String s, File file) throws Exception{
		FileWriter fw = new FileWriter(file);
		fw.write(s);
		fw.close();
	}

}
