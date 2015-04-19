import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;


public class AliceFrame extends JFrame {

	private JPanel contentPane;
	private JTextArea textArea;
	private JPanel panel;
	private JButton clear;
	private JButton start;
	private JLabel lblNewLabel;
	private JTextField textField;
	
	AliceCore core;
	
	/**
	 * Create the frame.
	 */
	public AliceFrame() {
		setTitle("Alice");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		textArea = new JTextArea();
		JScrollPane scroll = new JScrollPane (textArea, 
				   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		contentPane.add(scroll);
		
		panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new GridLayout(0, 4, 0, 0));
		
		lblNewLabel = new JLabel("Address");
		panel.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setText("localhost");
		panel.add(textField);
		
		start = new JButton("Start");
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (core == null) {
					try {
						log("Client started...");
						core = new AliceCore(AliceFrame.this, textField.getText());
						core.start();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					start.setText("Stop");
				} else {
					core.interrupt();
					stopClient();
				}
			}
		});
		panel.add(start);
		
		clear = new JButton("Clear");
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText("");
			}
		});
		panel.add(clear);
		
	}
	
	public void log(String s) {
		textArea.setText(textArea.getText() + s + "\n");
	}
	
	public void stopClient() {
		core = null;
		start.setText("Start");
		log("Client stopped...");
	}

}
