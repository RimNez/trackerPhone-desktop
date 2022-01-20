package views;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JCalendar;

import models.User;
import servers.UserRemote;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import com.toedter.calendar.JDateChooser;

public class Main {

	private JFrame frame;
	private JTextField nom;
	private JTextField prenom;
	private JTextField email;
	private JTextField telephone;
	private JTable table;
	private JDateChooser dateChooser;
	DefaultTableModel model;
	private static UserRemote remote;
	int id=-1;
	

	/**
	 * Launch the application.
	 */
	private static UserRemote lookUpUser() throws NamingException {
		Hashtable<Object, Object> config = new Hashtable<Object, Object>();
		config.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
		config.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");
		final Context context = new InitialContext(config);
		return (UserRemote) context.lookup("ejb:/trackerServer/UserService!servers.UserRemote");
				
	}
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	void load() throws RemoteException {
        model.setRowCount(0);
        for(User u : remote.findAll()){
            model.addRow(new Object[]{
                u.getId(),
                u.getNom(),
                u.getPrenom(),
                u.getEmail(),
                u.getTelephone(),
                u.getDateNaissance()
            });
        }
    }
	/**
	 * Create the application.
	 * @throws NamingException 
	 * @throws RemoteException 
	 */
	public Main() throws NamingException, RemoteException {
		initialize();
		remote = lookUpUser();
		load();
		//mainFrame = this;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 873, 623);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 192, 203));
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Nom:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel.setBounds(34, 96, 61, 24);
		panel.add(lblNewLabel);
		
		JLabel lblPrnom = new JLabel("Pr\u00E9nom:");
		lblPrnom.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPrnom.setBounds(34, 134, 82, 24);
		panel.add(lblPrnom);
		
		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblEmail.setBounds(34, 168, 61, 24);
		panel.add(lblEmail);
		
		JLabel lblNewLabel_2_2 = new JLabel("T\u00E9l\u00E9phone:");
		lblNewLabel_2_2.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_2_2.setBounds(34, 209, 130, 24);
		panel.add(lblNewLabel_2_2);
		
		nom = new JTextField();
		nom.setBounds(149, 103, 117, 24);
		panel.add(nom);
		nom.setColumns(10);
		
		prenom = new JTextField();
		prenom.setColumns(10);
		prenom.setBounds(149, 141, 117, 24);
		panel.add(prenom);
		
		email = new JTextField();
		email.setColumns(10);
		email.setBounds(149, 175, 117, 24);
		panel.add(email);
		
		telephone = new JTextField();
		telephone.setColumns(10);
		telephone.setBounds(149, 214, 117, 24);
		panel.add(telephone);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(298, 41, 508, 485);
		panel.add(scrollPane);
		
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				id = Integer.parseInt(model.getValueAt(table.getSelectedRow(), 0).toString());
				nom.setText(model.getValueAt(table.getSelectedRow(), 1).toString());
				prenom.setText(model.getValueAt(table.getSelectedRow(), 2).toString());
				email.setText(model.getValueAt(table.getSelectedRow(), 3).toString());
				telephone.setText(model.getValueAt(table.getSelectedRow(), 4).toString());
				dateChooser.setDate((Date) model.getValueAt(table.getSelectedRow(), 5));
				
			}
		});
		model = new DefaultTableModel();
		Object[] column = {"Id","Nom","Prénom","Email","Téléphone","Date de naissance"};
		final Object[] row = new Object[5];
		model.setColumnIdentifiers(column);
		table.setModel(model);
		scrollPane.setViewportView(table);
		
		JButton btnNewButton = new JButton("Ajouter");
		btnNewButton.setForeground(new Color(0, 0, 0));
		btnNewButton.setBackground(new Color(255, 0, 255));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(nom.getText().equals("") || prenom.getText().equals("") || email.getText().equals("") ||telephone.getText().equals(""))
				{
					JOptionPane.showMessageDialog(null, "Veulliez completer les champs");
				}
				else {
				remote.create(new User(nom.getText(),prenom.getText(),email.getText(),dateChooser.getDate(),telephone.getText()));
		        
				try {
					load();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				nom.setText("");
				prenom.setText("");
				email.setText("");
				telephone.setText("");
				dateChooser.setDate(null);
				JOptionPane.showMessageDialog(null, "Success");
				}
			}
		});
		btnNewButton.setFont(new Font("Georgia", Font.BOLD, 16));
		btnNewButton.setBounds(34, 343, 105, 48);
		panel.add(btnNewButton);
		
		JButton btnSupprimer = new JButton("Supprimer");
		btnSupprimer.setBackground(new Color(255, 255, 0));
		btnSupprimer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				remote.delete(remote.findById(id));
				try {
					load();
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
				nom.setText("");
				prenom.setText("");
				email.setText("");
				telephone.setText("");
				dateChooser.setDate(null);
			}
		});
		btnSupprimer.setFont(new Font("Georgia", Font.BOLD, 14));
		btnSupprimer.setBounds(88, 410, 117, 48);
		panel.add(btnSupprimer);
		
		JButton btnModifier = new JButton("Modifier");
		btnModifier.setBackground(new Color(127, 255, 0));
		btnModifier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(nom.getText().isEmpty() && prenom.getText().isEmpty()&& 
						email.getText().isEmpty()&& telephone.getText().isEmpty()) {
					JOptionPane.showMessageDialog(panel, "Champs vides !!");
				}else {
					remote.update(new User(Integer.valueOf(model.getValueAt(table.getSelectedRow(), 0).toString()),
							nom.getText(),prenom.getText(),email.getText(),
							dateChooser.getDate(),telephone.getText()));
					try {
						load();
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
					nom.setText("");
					prenom.setText("");
					email.setText("");
					telephone.setText("");
					dateChooser.setDate(null);
				}
			}
		});
		btnModifier.setFont(new Font("Georgia", Font.BOLD, 16));
		btnModifier.setBounds(161, 343, 105, 48);
		panel.add(btnModifier);
		
		JButton btnNewButton_1 = new JButton("Gestion phones");
		btnNewButton_1.setBackground(new Color(0, 255, 255));
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PhoneMain phoneFrame;
				try {
					phoneFrame = new PhoneMain();
					phoneFrame.getFrame().setVisible(true);
					phoneFrame.idUser=id;
					phoneFrame.load();
					getFrame().dispose();
					
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NamingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
			}
				
		});
		btnNewButton_1.setFont(new Font("Georgia", Font.BOLD, 18));
		btnNewButton_1.setBounds(571, 536, 200, 37);
		panel.add(btnNewButton_1);
		
		dateChooser = new JDateChooser();
		dateChooser.setBounds(149, 248, 117, 19);
		panel.add(dateChooser);
		
		JLabel lblNewLabel_2_2_1 = new JLabel("Date Naissance:");
		lblNewLabel_2_2_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_2_2_1.setBounds(34, 243, 130, 24);
		panel.add(lblNewLabel_2_2_1);
	}


	public JFrame getFrame() {
		return frame;
	}


	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
}
