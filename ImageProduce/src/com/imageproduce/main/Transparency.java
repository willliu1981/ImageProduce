package com.imageproduce.main;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.imageproduce.bean.ImageData;
import com.imageproduce.component.ImagePanel;

import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.CardLayout;

public class Transparency extends JFrame {

	private JPanel contentPane;

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

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		panel_1.setLayout(new CardLayout(0, 0));

		ImagePanel<ImageData> imagePanel_source = new ImagePanel<ImageData>() {

			@Override
			protected ImageProcess paintImage(Graphics g) {
				return ImageProcess.Default;
			}

		};
		panel_1.add(imagePanel_source, "name_620937426926000");

		JPanel panel_2 = new JPanel();
		panel.add(panel_2);
		panel_2.setLayout(new CardLayout(0, 0));

		ImagePanel<ImageData> imagePanel_destination = new ImagePanel<ImageData>() {

			@Override
			protected ImageProcess paintImage(Graphics g) {
				return ImageProcess.Transparence;
			}

		};
		panel_2.add(imagePanel_destination, "name_613935159229600");

		JPanel panel_3 = new JPanel();
		contentPane.add(panel_3, BorderLayout.SOUTH);

		JButton btnNewButton = new JButton("New button");
		panel_3.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("New button");
		panel_3.add(btnNewButton_1);

	}

}
