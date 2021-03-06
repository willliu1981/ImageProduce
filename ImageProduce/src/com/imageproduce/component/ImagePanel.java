package com.imageproduce.component;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Robot;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import com.imageproduce.bean.GraphicsScale;
import com.imageproduce.controller.GraphicsManager;

public abstract class ImagePanel extends JPanel {
	protected static final int PaneWidth = 15;
	protected static final String OnlyRGB = "rgb";
	protected static final String R = "r";
	protected static final String G = "g";
	protected static final String B = "b";
	protected static final String A = "a";

	protected GraphicsScale scale;
	protected int x, y;
	protected GraphicsManager imgDataManager;

	protected enum ImageProcess {
		Default, Transparence
	}

	/**
	 * Create the panel.
	 */

	public ImagePanel(GraphicsManager manager, Component comp) {
		this.imgDataManager = manager;
		this.imgDataManager.addComponent(comp == null ? this : comp);
		this.scale = new GraphicsScale(this);

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				x = e.getXOnScreen();
				y = e.getYOnScreen();
				try {
					imgDataManager.getInRange().setColor(new Robot().getPixelColor(x, y));
					imgDataManager.repaintComps();
				} catch (AWTException e1) {
					e1.printStackTrace();
				}
			}
		});
		setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
	}

	// 繪透明圖
	protected void paintTramsparentImage(Graphics g) {
		try {
			BufferedImage bi = new BufferedImage(this.imgDataManager.getSrcImage().getWidth(null),
					this.imgDataManager.getSrcImage().getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g2d = bi.createGraphics();
			g2d.drawImage(this.imgDataManager.getSrcImage(), 0, 0, null);

			int alpha = 0;
			for (int y = bi.getMinY(); y < bi.getHeight(); y++) {
				for (int x = bi.getMinX(); x < bi.getWidth(); x++) {
					int color = bi.getRGB(x, y);
					if (this.checkInRange(color)) {
						alpha = 0;
					} else {
						alpha = 255;
					}
					alpha <<= 24;
					color = this.getPureColor(color, OnlyRGB);
					color |= alpha;
					bi.setRGB(x, y, color);
				}
			}

			this.imgDataManager.setDesImage(bi);
			g.drawImage(bi, this.scale.getX1(), this.scale.getY1(), this.scale.getX2(), this.scale.getY2(), 0, 0,
					this.imgDataManager.getSrcImage().getWidth(null), this.imgDataManager.getSrcImage().getHeight(null),
					null);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected int getPureColor(int color, String rgbSign) {
		int c = -1;
		switch (rgbSign.toLowerCase().trim()) {
		case OnlyRGB:
			c = color & 0xffffff;
			break;
		case R:
			c = color & 0xff0000;
			break;
		case G:
			c = color & 0x00ff00;
			break;
		case B:
			c = color & 0x0000ff;
			break;
		case A:
			c = color & 0xff000000;
			break;
		default:
			c = -1;
			break;
		}
		return c;
	}

	protected boolean checkInRange(int color) {
		boolean res = false;
		int a = this.getPureColor(color, A);
		a >>= 24;
		int r = this.getPureColor(color, R);
		r >>= 16;
		int g = this.getPureColor(color, G);
		g >>= 8;
		int b = this.getPureColor(color, B);
		// b >>= 0;
		if (a == 0 || this.imgDataManager.getInRange().inRange(r, R) && this.imgDataManager.getInRange().inRange(g, G)
				&& this.imgDataManager.getInRange().inRange(b, B)) {
			res = true;
		}
		return res;
	}

	protected void paintPrimaryImage(Graphics g) {
		try {
			g.drawImage(this.imgDataManager.getSrcImage(), this.scale.getX1(), this.scale.getY1(), this.scale.getX2(),
					this.scale.getY2(), 0, 0, this.imgDataManager.getSrcImage().getWidth(null),
					this.imgDataManager.getSrcImage().getHeight(null), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void paintImage(Graphics g, ImageProcess type) {
		if (type == ImageProcess.Default) {
			this.paintPrimaryImage(g);
		} else if (type == ImageProcess.Transparence) {
			this.paintTramsparentImage(g);
		}
	}

	protected void processImage(Graphics g) {

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.mesh(g);
		paintImage(g, ImageProcess.Default);
	}

	protected void mesh(Graphics g) {
		int wLen = this.scale.getW() / PaneWidth;
		int hLen = this.scale.getH() / PaneWidth;

		int[] xs = new int[wLen * hLen];
		int[] ys = new int[wLen * hLen];

		int tidx = 0;
		int idx = 0;

		while (true) {
			// 畫U型
			xs[idx] = PaneWidth * 2 * tidx + this.scale.getX1();
			ys[idx] = this.scale.getY1();
			idx++;
			xs[idx] = PaneWidth * 2 * tidx + this.scale.getX1();
			ys[idx] = this.scale.getY2();
			idx++;
			xs[idx] = PaneWidth * 2 * tidx + PaneWidth + this.scale.getX1();
			ys[idx] = this.scale.getY2();
			idx++;
			xs[idx] = PaneWidth * 2 * tidx + PaneWidth + this.scale.getX1();
			ys[idx] = this.scale.getY1();
			idx++;
			if (tidx++ >= wLen / 2) {
				break;
			}
		}

		tidx = 0;
		while (true) {
			// 畫倒C型
			xs[idx] = this.scale.getX1();
			ys[idx] = PaneWidth * 2 * tidx + this.scale.getY1();
			idx++;
			xs[idx] = this.scale.getX2();
			ys[idx] = PaneWidth * 2 * tidx + this.scale.getY1();
			idx++;
			xs[idx] = this.scale.getX2();
			ys[idx] = PaneWidth * 2 * tidx + PaneWidth + this.scale.getY1();
			idx++;
			xs[idx] = this.scale.getX1();
			ys[idx] = PaneWidth * 2 * tidx + PaneWidth + this.scale.getY1();
			idx++;
			if (tidx++ >= hLen / 2) {
				break;
			}
		}

		g.setColor(Color.LIGHT_GRAY);
		g.fillPolygon(xs, ys, xs.length);
	}

}
