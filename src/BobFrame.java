import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;


public class BobFrame extends JFrame {

	private JPanel contentPane;
	private JTextArea textArea;
	private JPanel panel;
	private JButton clear;
	private JButton start;
	
	BobCore core;
	
	/**
	 * Create the frame.
	 */
	public BobFrame() {
		setTitle("Bob");
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
		
		start = new JButton("Start");
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (core == null) {
					try {
						log("Server started...");
						core = new BobCore(BobFrame.this);
						core.start();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					start.setText("Stop");
				} else {
					core.interrupt();
					core = null;
					start.setText("Start");
					log("Server stopped...");
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

}
