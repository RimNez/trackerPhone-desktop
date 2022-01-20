package views;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import models.Phone;
import models.User;
import servers.PhoneRemote;
import servers.UserRemote;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PhoneMain {

	private JFrame frame;
	private JTextField imei;
	private JTextField marque;
	private JTable table;
	DefaultTableModel model;
	private static PhoneRemote remote;
	private static UserRemote remoteUser;
	int idUser;
	int idPhone=-1;
	
	private static PhoneRemote lookUpPhone() throws NamingException {
		Hashtable<Object, Object> config = new Hashtable<Object, Object>();
		config.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
		config.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");
		final Context context = new InitialContext(config);
		return (PhoneRemote) context.lookup("ejb:/trackerServer/PhoneService!servers.PhoneRemote");
				
	}
	private static UserRemote lookUpUser() throws NamingException {
		Hashtable<Object, Object> config = new Hashtable<Object, Object>();
		config.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
		config.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");
		final Context context = new InitialContext(config);
		return (UserRemote) context.lookup("ejb:/trackerServer/UserService!servers.UserRemote");
				
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PhoneMain window = new PhoneMain();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	void load() throws RemoteException {
        model.setRowCount(0);
        for(Phone p : remote.findAll(remoteUser.findById(idUser))){
            model.addRow(new Object[]{
                p.getId(),
                p.getImei(),
                p.getMarque(),
                p.getUser().getId()
            });
        }
    }
	/**
	 * Create the application.
	 * @throws RemoteException 
	 * @throws NamingException 
	 */
	public PhoneMain() throws RemoteException, NamingException {
		initialize();
		remote = lookUpPhone();
		remoteUser = lookUpUser();
		load();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 970, 691);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBackground(new Color(255, 192, 203));
		frame.getContentPane().add(panel_1, BorderLayout.CENTER);
		
		JLabel lblImei = new JLabel("IMEI:");
		lblImei.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblImei.setBounds(34, 96, 61, 24);
		panel_1.add(lblImei);
		
		JLabel lblMarque = new JLabel("Marque:");
		lblMarque.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblMarque.setBounds(34, 134, 82, 24);
		panel_1.add(lblMarque);
		
		imei = new JTextField();
		imei.setColumns(10);
		imei.setBounds(149, 103, 117, 24);
		panel_1.add(imei);
		
		marque = new JTextField();
		marque.setColumns(10);
		marque.setBounds(149, 141, 117, 24);
		panel_1.add(marque);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(298, 41, 508, 485);
		panel_1.add(scrollPane);
		
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				idPhone = Integer.parseInt(model.getValueAt(table.getSelectedRow(), 0).toString());
			    imei.setText(model.getValueAt(table.getSelectedRow(), 1).toString());
				marque.setText(model.getValueAt(table.getSelectedRow(), 2).toString());
			}
		});
		model = new DefaultTableModel();
		Object[] column = {"Id","IMEI","Marque","IdUser"};
		final Object[] row = new Object[5];
		model.setColumnIdentifiers(column);
		table.setModel(model);
		scrollPane.setViewportView(table);
		
		JButton ajouter = new JButton("Ajouter");
		ajouter.setBackground(new Color(0, 255, 0));
		ajouter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(imei.getText().equals("") || marque.getText().equals(""))
				{
					JOptionPane.showMessageDialog(null, "Veulliez completer les champs");
				}
				else {
				remote.create(new Phone(imei.getText(),marque.getText()),/*Integer.parseInt(idU.getText())*/idUser);
		        
				try {
					load();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				imei.setText("");
				marque.setText("");
				JOptionPane.showMessageDialog(null, "Success");
				}
			}
		});
		ajouter.setFont(new Font("Georgia", Font.BOLD, 16));
		ajouter.setBounds(82, 282, 129, 37);
		panel_1.add(ajouter);
		
		JButton supprimer = new JButton("Supprimer");
		supprimer.setBackground(new Color(255, 0, 255));
		supprimer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				remote.delete(remote.findById(idPhone));
				try {
					load();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		supprimer.setFont(new Font("Georgia", Font.BOLD, 16));
		supprimer.setBounds(82, 440, 129, 37);
		panel_1.add(supprimer);
		
		JButton modifier = new JButton("Modifier");
		modifier.setBackground(new Color(255, 255, 0));
		modifier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(imei.getText().isEmpty() && marque.getText().isEmpty()) {
					JOptionPane.showMessageDialog(panel_1, "Champs vides !!");
				}else {
					remote.update(new Phone(Integer.valueOf(model.getValueAt(table.getSelectedRow(), 0).toString()),
							imei.getText(),marque.getText()),Integer.valueOf(model.getValueAt(table.getSelectedRow(), 3).toString()));
					try {
						load();
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		modifier.setFont(new Font("Georgia", Font.BOLD, 16));
		modifier.setBounds(82, 360, 129, 37);
		panel_1.add(modifier);
		
		JButton btnNewButton = new JButton("Revenir");
		btnNewButton.setBackground(new Color(0, 255, 255));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main mainFrame;
				try {
					mainFrame = new Main();
					mainFrame.getFrame().setVisible(true);
					getFrame().dispose();
				} catch (RemoteException | NamingException e1) {
					e1.printStackTrace();
				}
				
			}
		});
		btnNewButton.setFont(new Font("Georgia", Font.BOLD, 16));
		btnNewButton.setBounds(489, 546, 140, 43);
		panel_1.add(btnNewButton);
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
}
