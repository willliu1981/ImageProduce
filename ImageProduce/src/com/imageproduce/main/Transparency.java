package com.imageproduce.main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.imageproduce.component.ImagePanel;
import com.imageproduce.controller.GraphicsManager;

public class Transparency extends JFrame {

	private JPanel contentPane;
	private GraphicsManager imgDataManager;
	private JTextField textField_colorInRange;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Transparency frame = new Transparency();
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
	public Transparency() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 737, 379);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		this.init();

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		panel_1.setLayout(new CardLayout(0, 0));

		ImagePanel imagePanel_source = new ImagePanel(imgDataManager, null) {

		};
		panel_1.add(imagePanel_source, "name_620937426926000");

		JPanel panel_2 = new JPanel();
		panel.add(panel_2);
		panel_2.setLayout(new CardLayout(0, 0));

		ImagePanel imagePanel_destination = new ImagePanel(imgDataManager, null) {
			@Override
			protected void paintImage(Graphics g, ImageProcess type) {
				super.paintImage(g, ImageProcess.Transparence);
			}
		};
		panel_2.add(imagePanel_destination, "name_613935159229600");

		JPanel panel_3 = new JPanel();
		contentPane.add(panel_3, BorderLayout.SOUTH);

		JButton btnNewButton = new JButton("New button");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				textField_colorInRange.setText("33");
			}
		});
		panel_3.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("New button");
		panel_3.add(btnNewButton_1);

		JPanel panel_4 = new JPanel();
		contentPane.add(panel_4, BorderLayout.NORTH);

		JPanel panel_currentColor = new JPanel() {

		};
		FlowLayout fl_panel_currentColor = (FlowLayout) panel_currentColor.getLayout();
		fl_panel_currentColor.setVgap(10);
		fl_panel_currentColor.setHgap(10);
		panel_currentColor.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_4.add(panel_currentColor);

		textField_colorInRange = new JTextField();
		textField_colorInRange.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent arg0) {
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				textChange(arg0);
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				textChange(arg0);
			}

			private void textChange(DocumentEvent arg0) {
				try {
					imgDataManager.setInRange(Integer.valueOf(textField_colorInRange.getText().toString()));
					imgDataManager.repaintComps();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});
		panel_4.add(textField_colorInRange);
		textField_colorInRange.setColumns(2);

	}

	private void init() {
		imgDataManager = new GraphicsManager();
		imgDataManager.setSrcFName("data\\n1.jpg");
	}

}
