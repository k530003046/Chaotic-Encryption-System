package client_server;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Base64;
import java.util.Map;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import crypto.CMET_2;
import crypto.RSA;

public class QQ extends JFrame{

	private JPanel contentPane;
	private JPanel panel_2;
	private JPanel panel_4;
	private JButton b3;
	private JTextPane textPane;
	private JTextArea textArea;
	private ChatClient client;
	private File file;
	private String f_path;
	private String f_name;
	public int size = 0;
	
	private static String PUBLICKEY = null;
	private static String PRIVATEKEY = null;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			QQ frame = new QQ();
			frame.setVisible(true);
			frame.setLocationRelativeTo(null);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Create the frame.
	 */
	public QQ() {
		setTitle("QQ");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 560);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(2,2,2,2));
		
		JMenuBar menubar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		JMenu editMenu = new JMenu("Edit");
		JMenu viewMenu = new JMenu("View");
		JMenu rsaMenu = new JMenu("RSA");
		JMenu cmetMenu = new JMenu("CMET");
		JMenu abouMenu = new JMenu("About QQ");
		menubar.add(fileMenu);
		menubar.add(editMenu);
		menubar.add(viewMenu);
		menubar.add(rsaMenu);
		menubar.add(cmetMenu);
		menubar.add(abouMenu);
		
		contentPane.add(menubar, BorderLayout.NORTH);
		
		JMenuItem newMenuItem = new JMenuItem("New");
		JMenuItem openMenuItem = new JMenuItem("Open");
		JMenuItem quitMenuItem = new JMenuItem("Quit");
		fileMenu.add(newMenuItem);
		fileMenu.add(openMenuItem);
		fileMenu.add(quitMenuItem);
		
		JMenuItem copyMenuItem = new JMenuItem("Copy");
		copyMenuItem.setActionCommand("copy");
		JMenuItem pasteMenuItem = new JMenuItem("Paste");
		pasteMenuItem.setActionCommand("paste");
		editMenu.add(copyMenuItem);
		editMenu.add(pasteMenuItem);
		
		JMenuItem zoomMenuItem = new JMenuItem("Zoom");
		JMenuItem minimizeMenuItem = new JMenuItem("Minimize");
		viewMenu.add(zoomMenuItem);
		viewMenu.add(minimizeMenuItem);
		
		JMenuItem keyPairMenuItem = new JMenuItem("Generate keypair");
		JMenuItem rsaEncryptMenuItem = new JMenuItem("Encrypt");
		JMenuItem rsaDecryptMenuItem = new JMenuItem("Decrypt");

		rsaMenu.add(keyPairMenuItem);
		rsaMenu.add(rsaEncryptMenuItem);
		rsaMenu.add(rsaDecryptMenuItem);
		
		JMenuItem cmetKeyPairMenuItem = new JMenuItem("Generate key");
		JMenuItem cmetEncryptMenuItem = new JMenuItem("Encrypt");
		JMenuItem cmetDecryptMenuItem = new JMenuItem("Decrypt");
		JMenuItem cmetEncryptImgMenuItem = new JMenuItem("Encrypt Image");
		JMenuItem cmetDecryptImgMenuItem = new JMenuItem("Decrypt Image");
		
		cmetMenu.add(cmetKeyPairMenuItem);
		cmetMenu.add(cmetEncryptMenuItem);
		cmetMenu.add(cmetDecryptMenuItem);
		cmetMenu.add(cmetEncryptImgMenuItem);
		cmetMenu.add(cmetDecryptImgMenuItem);
		
		contentPane.add(panel, BorderLayout.WEST);
		
		textPane = new JWarpTextPane();
		//textPane = new JTextPane();
		textPane.setPreferredSize(new Dimension(570, 350));
		textPane.setBorder(new EmptyBorder(0, 10, 10, 10));
		textPane.setEditable(false);
		textPane.setText("\nEnter Your name and press Register: \n");
		textPane.setFont(new Font("", Font.PLAIN, 18));
		JScrollPane pane = new JScrollPane(textPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		panel.add(pane);
		
//		ImageIcon img = new ImageIcon("Lucy.jpg");
//		textPane.insertIcon(img);
		
		panel_2 = new JPanel();
		panel_2.setBorder(new EmptyBorder(2, 40, 2, 20));
		panel_2.setPreferredSize(new Dimension(220, 350));
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.Y_AXIS));
		
		contentPane.add(panel_2, BorderLayout.EAST);
				
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new EmptyBorder(0,0,0,0));
		contentPane.add(panel_3, BorderLayout.SOUTH);
		
		textArea = new JTextArea();
		textArea.setPreferredSize(new Dimension(557, 100));
		textArea.setBorder(new EmptyBorder(10, 10, 10, 10));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setFont(new Font("", Font.PLAIN, 18));
		JScrollPane pane_2 = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panel_3.add(pane_2);
		
		panel_4 = new JPanel();
		panel_4.setBorder(new EmptyBorder(0, 10, 0, 0));
		panel_4.setLayout(new GridLayout(2, 1, 10, 6));
		panel_3.add(panel_4);
		
		JButton btnNewButton = new JButton("Register");
		btnNewButton.setPreferredSize(new Dimension(175, 45));
		Font f = new Font("", Font.BOLD, 20);
		btnNewButton.setFont(f);
		panel_4.add(btnNewButton);
		
		JButton b1 = new JButton("Send");
		b1.setEnabled(false);
		b1.setFont(f);
		panel_4.add(b1);
		
		KeyListener enter = new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyChar() == KeyEvent.VK_ENTER && e.isControlDown()) {
					String temp = textArea.getText();
					textArea.setText(temp + "\r\n");
				}
				else if(e.getKeyChar() == KeyEvent.VK_ENTER && e.isControlDown() == false) {
					try {
						client.sendMessage();
						textArea.setText("");
						if(textArea.getText().trim().isEmpty() && e.getKeyChar() == KeyEvent.VK_ENTER) {
							e.consume();
						}
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				
			}

			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
		};
		
		textArea.addKeyListener(enter);
		
		btnNewButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String user;
				user = textArea.getText();
				try {
					connect(user);
					textArea.setText("");
					//this part to remove register button after the users' register.
					panel_4.remove(btnNewButton);
					
					panel_4.remove(b1);//-----------------------------??????? 
					
					//----------------------------------------!!!!!!!!!!!!!��һ�μӵ��ϴ���ť��
					
					//-------------------------------------------!!!!!!
					
					
					//this part to have a new button quit at the same place as the old register button.
					b3 = new JButton("Quit");
					b3.setPreferredSize(new Dimension(175, 45));
					b3.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							// TODO Auto-generated method stub
							try {
								panel_2.removeAll();//???what does this mean------------------------------------------------------------------
								client.quit();
								//client.newClient();
							} catch (RemoteException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						
					});
					b3.setFont(f);
					panel_4.add(b3);
					panel_4.add(b1);
					b1.setEnabled(true);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NotBoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		});
		b1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					client.sendMessage();
					textArea.setText("");
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		});
		openMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFileChooser chooser = new JFileChooser();
				if(chooser.showOpenDialog(openMenuItem) == JFileChooser.APPROVE_OPTION) {
					try{
						file = chooser.getSelectedFile();
						f_path = file.getAbsolutePath();
					
						f_name = file.getName();
						System.out.println(f_path);
						System.out.println(f_name);
						
						byte[]fileByte=new byte[1024*20];
						fileByte=client.getBytesFromFile(new File(f_path));//input file path here exchanging bear.jpg
						client.updateFile(fileByte,f_name);//input filename here exchanging bear2.jpg
						
						//ImageIcon img = new ImageIcon(file.getPath());
//						ImageIcon img = suitImage(f_path);
						ImageIcon img = SwingUtil.createAutoAdjustIcon(f_path, true, 0.90, 0.85);
						textPane.insertIcon(img);
						
						}catch(IOException ee){
							System.out.print("IOException");
						}
					//System.out.println(f_path);
					//System.out.println(f_name);
				}
			}
			
		});
		keyPairMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e){
				// TODO Auto-generated method stub
				try {
					Map keyMap = RSA.genKeyPair();
					PRIVATEKEY = RSA.getPrivateKey(keyMap);
					PUBLICKEY = RSA.getPublicKey(keyMap);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		});
		rsaEncryptMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				JFileChooser chooser = new JFileChooser();
				if(chooser.showOpenDialog(rsaEncryptMenuItem) == chooser.APPROVE_OPTION) {
					file = chooser.getSelectedFile();
					try {
						PUBLICKEY = fileReader(file);
						byte[] cipher = RSA.encryptByPublicKey(textArea.getText().getBytes(), PUBLICKEY);
						String result = Base64.getEncoder().encodeToString(cipher);
						textArea.setText(result);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			
		});
		rsaDecryptMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
//				JFileChooser chooser = new JFileChooser();
//				if(chooser.showOpenDialog(rsaDecryptMenuItem) == chooser.APPROVE_OPTION) {
//					file = chooser.getSelectedFile();
					try {
//						PRIVATEKEY = fileReader(file);
						RSADecryptUI frame_2 = new RSADecryptUI();
					}catch(Exception e2) {
						e2.printStackTrace();
					}
				}
//			}
			
		});
		
		cmetKeyPairMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				AlertFrame alert = new AlertFrame("How much size of key you want to generate");
				alert.setSize(300, 150);
			}
			
		});
		
		cmetEncryptMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFileChooser chooser = new JFileChooser();
				if(chooser.showOpenDialog(cmetEncryptMenuItem) == chooser.APPROVE_OPTION)
					file = chooser.getSelectedFile();
				try {
					String key = fileReader(file);
					String noise = CMET_2.noiseGen(key);
					String cipher = CMET_2.encryptByNoise(noise, textArea.getText());
					textArea.setText(cipher);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			
		});
		
		cmetDecryptMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				CMETDecryptUI a = new CMETDecryptUI();
			}
			
		});
		
		cmetEncryptImgMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				CMETImageEncryptUI b = new CMETImageEncryptUI();
			}
			
		});
		
		cmetDecryptImgMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				CMETImageDecryptUI c = new CMETImageDecryptUI();
			}
			
		});
	}
	
	public void connect(String name) throws RemoteException, MalformedURLException, NotBoundException {
		try {
			client = new ChatClient(this, name);
			client.startClient();
		}catch(RemoteException e2) {
			e2.printStackTrace();
		}
	}
	public void setTxt(String msg) {
		textPane.setText(textPane.getText() + msg + "\n");
	}
	public String getTxt() {
		return textArea.getText();
	}
	
	//insert a new client name at the right side.
	public void setUser(String name, IChatClient c) {
		DynamicPanel panel_a = new DynamicPanel(name, this, c);
		panel_2.add(panel_a);
		SwingUtilities.updateComponentTreeUI(contentPane);
	}
	
	//remove the customer list at the right side.
	public void removeComponents() {
		panel_2.removeAll();
	}
	public void updateComponents() {//------------------------????????
		SwingUtilities.updateComponentTreeUI(this);
	}
	public ChatClient getClient() {
		return client;
	}
	public void sendPriva(QQ qq, IChatClient a) throws RemoteException {
		qq.getClient().privateSend(qq, a);
	}
	public void emptyText() {
		textArea.setText("");
	}
	public void setImage(String filePath) {
		ImageIcon img = SwingUtil.createAutoAdjustIcon(filePath, true, 0.90, 0.85);
		//ImageIcon img = new ImageIcon(filePath);
		textPane.insertIcon(img);
		System.out.println("Send file successfully!");
	}
	
	public ImageIcon suitImage(String filePath) {
		ImageIcon icon = new ImageIcon(filePath);
		int width = icon.getIconWidth();
		int height = icon.getIconHeight();
		if(width > 550 || height > 300) {
			Image img = icon.getImage();
			img = img.getScaledInstance(520, 300, Image.SCALE_DEFAULT);
			//System.out.println("over");
			icon.setImage(img);
		}
		//System.out.println(width);
		//System.out.println(height);
		return icon;
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
	
	public String getPulicKey() {return PUBLICKEY;}
	public String getPrivateKey() {return PRIVATEKEY;}
}

//this is the class that used to construct a button to show clients names.

/*
class DynamicPanel extends JPanel{
	JButton txt;
	public DynamicPanel(String name) {
		txt = new JButton(name);
		txt.setPreferredSize(new Dimension(220, 20));
		txt.setBorder(new EmptyBorder(5, 5, 5, 5));
	
		add(txt);
		
	}
	public void setTxt(String msg) {
		txt.setText(txt.getText() + msg + "\n");
	}
	public void deleteTxt(String msg) {
		txt.setText(txt.getText().replace(msg, ""));
	}
}*/

class AlertFrame extends JFrame{
	public int result;
	public QQ gui;
	
	public AlertFrame(String msg) {
		setVisible(true);
		setBounds(200, 100, 200, 100);
		setLocationRelativeTo(null);

		
		JLabel lbl = new JLabel(msg);
		add(lbl, BorderLayout.NORTH);
		
		JTextField txtF = new JTextField(10);
		add(txtF, BorderLayout.CENTER);
		
		JButton b1 = new JButton("Confirm");
		add(b1, BorderLayout.SOUTH);
		
		b1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println(txtF.getText());
				result = Integer.parseInt(txtF.getText());
				System.out.println(result);
				try {
					String key = CMET_2.keyGen(result);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
//			public void mousePressed(MouseEvent e) {
//				Component cmp = e.getComponent();
//				while(!(cmp instanceof JFrame) || cmp.getParent() != null) {
//					cmp = cmp.getParent();
//				}
//				((JFrame)cmp).dispose();
//			}
			
		});
	}
}

