package com.imageproduce.main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FileDialog;
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

import com.imageproduce.bean.GraphicsScale;
import com.imageproduce.component.ImagePanel;
import com.imageproduce.controller.GraphicsManager;
import com.imageproduce.test.TestDialog;

import javax.swing.SwingConstants;

public class Transparency extends JFrame {

	private JPanel contentPane;
	private GraphicsManager manager;
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

		ImagePanel imagePanel_source = new ImagePanel(manager, null) {

		};
		panel_1.add(imagePanel_source, "name_620937426926000");

		JPanel panel_2 = new JPanel();
		panel.add(panel_2);
		panel_2.setLayout(new CardLayout(0, 0));

		ImagePanel imagePanel_destination = new ImagePanel(manager, null) {
			@Override
			protected void paintImage(Graphics g, ImageProcess type) {
				super.paintImage(g, ImageProcess.Transparence);
			}
		};
		panel_2.add(imagePanel_destination, "name_613935159229600");

		JPanel panel_3 = new JPanel();
		contentPane.add(panel_3, BorderLayout.SOUTH);

		JButton btnNewButton_open = new JButton("Open");
		btnNewButton_open.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				FileDialog dialog=new FileDialog(new TestDialog());
				dialog.setVisible(true);
				if(dialog!=null) {
					manager.setSrcFName(dialog.getDirectory()+dialog.getFile());
					manager.repaintComps();
				}
			}
		});
		panel_3.add(btnNewButton_open);

		JButton btnNewButton_output = new JButton("Output");
		btnNewButton_output.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				FileDialog dialog=new FileDialog(new TestDialog());
				dialog.setVisible(true);
				if(dialog!=null) {
					manager.setDesFName(dialog.getDirectory()+dialog.getFile());
					manager.output();
				}
			}
		});
		panel_3.add(btnNewButton_output);

		JPanel panel_4 = new JPanel();
		contentPane.add(panel_4, BorderLayout.NORTH);

		JPanel panel_currentColor = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				GraphicsScale scale = new GraphicsScale(this);
				g.setColor(manager.getInRange().getColor());
				g.fill3DRect(scale.getX1(), scale.getY1(), scale.getW(), scale.getH(), true);
			}
		};
		manager.addComponent(panel_currentColor);
		FlowLayout fl_panel_currentColor = (FlowLayout) panel_currentColor.getLayout();
		fl_panel_currentColor.setVgap(10);
		fl_panel_currentColor.setHgap(10);
		panel_currentColor.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_4.add(panel_currentColor);

		textField_colorInRange = new JTextField() {

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				try {
					if (Integer.valueOf(textField_colorInRange.getText()) != manager.getInRange().getRnage()) {
						textField_colorInRange.setText("" + manager.getInRange().getRnage());
					}
				} catch (NumberFormatException e) {
					textField_colorInRange.setText("" + manager.DefaultRange);
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		textField_colorInRange.setHorizontalAlignment(SwingConstants.CENTER);
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
					manager.setInRange(Integer.valueOf(textField_colorInRange.getText().toString()));
					manager.repaintComps();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});
		manager.addComponent(textField_colorInRange);
		panel_4.add(textField_colorInRange);
		textField_colorInRange.setColumns(2);

	}

	private void init() {
		manager = new GraphicsManager();
		//manager.setSrcFName("data\\n1.jpg");
	}

}
