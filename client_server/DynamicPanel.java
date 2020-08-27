package client_server;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

class DynamicPanel extends JPanel{
	JButton txt;
	QQ gui = new QQ();
	IChatClient client;
	public DynamicPanel(String name, QQ qq, IChatClient remote) {
		gui = qq;
		client = remote;
		txt = new JButton(name);
		txt.setPreferredSize(new Dimension(175, 30));
		txt.setBorder(new EmptyBorder(5, 5, 5, 5));
		txt.setFont(new Font("", Font.PLAIN, 20));
		txt.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					gui.sendPriva(gui, client);
					gui.emptyText();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		});
	
		add(txt);
		
	}
	public void setTxt(String msg) {
		txt.setText(txt.getText() + msg + "\n");
	}
	public void deleteTxt(String msg) {
		txt.setText(txt.getText().replace(msg, ""));
	}
}
