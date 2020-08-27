package client_server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Base64;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import crypto.RSA;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class RSADecryptUI extends JFrame {
	
	QQ gui = new QQ();
	private JPanel contentPane;
	private String PRIVATEKEY = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RSADecryptUI frame = new RSADecryptUI();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public RSADecryptUI() {
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTextArea textArea = new JTextArea();
		textArea.setBorder(new EmptyBorder(10, 10, 10, 10));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setViewportView(textArea);
		contentPane.add(scrollPane, BorderLayout.CENTER);
				
		JMenuBar menuBar = new JMenuBar();
		scrollPane.setColumnHeaderView(menuBar);
		
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		JMenuItem mntmOpen = new JMenuItem("Open");
		fileMenu.add(mntmOpen);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		fileMenu.add(mntmSave);
		
		JButton btnDecrypt = new JButton("Decrypt");
		contentPane.add(btnDecrypt, BorderLayout.SOUTH);
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
						PRIVATEKEY = fileReader(file);
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
						fileWriter(textArea.getText(), file);
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
					byte[] result = RSA.decryptByPrivateKey(Base64.getDecoder().decode(textArea.getText()), PRIVATEKEY);
					textArea.setText(new String(result));
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
