package com.imageproduce.component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import com.imageproduce.bean.GraphicsScale;

public abstract class ImagePanel<T> extends JPanel {
	protected static final int PaneWidth = 10;
	protected static final String OnlyRGB = "rgb";
	protected static final String R = "r";
	protected static final String G = "g";
	protected static final String B = "b";
	protected static final String A = "a";

	protected GraphicsScale scale;
	protected int x, y;
	protected Image sorImage;
	protected Image desImage;
	protected InRange inRange = new InRange(10);

	protected enum ImageProcess {
		Default, Transparence
	}

	protected class InRange {
		private Color color;
		private int rnage = 5;

		protected InRange(int range) {
			this.color = new Color(255, 255, 255);
			this.rnage = range;
		}

		protected int getRnage() {
			return rnage;
		}

		protected void setRnage(int rnage) {
			this.rnage = rnage;
		}

		protected Color getColor() {
			return color;
		}

		protected void setColor(Color color) {
			this.color = color;
		}

		protected boolean inRange(int singleColor, String rgbSign) {
			boolean res = false;
			switch (rgbSign.toLowerCase().trim()) {
			case R:
				res = singleColor >= this.color.getRed() - this.rnage
						&& singleColor <= this.color.getRed() + this.rnage;
				break;
			case G:
				res = singleColor >= this.color.getGreen() - this.rnage
						&& singleColor <= this.color.getGreen() + this.rnage;
				break;
			case B:
				res = singleColor >= this.color.getBlue() - this.rnage
						&& singleColor <= this.color.getBlue() + this.rnage;
				break;
			default:
				res = false;
				break;
			}
			return res;
		}

		protected int getR() {
			return this.color.getRed();
		}

		protected int getG() {
			return this.color.getGreen();
		}

		protected int getB() {
			return this.color.getBlue();
		}
	}

	protected T t;

	/**
	 * Create the panel.
	 */

	public ImagePanel() {
		this.scale = new GraphicsScale(this);
		try {
			this.sorImage = ImageIO.read(new File("data\\nyo.jpg"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				x = e.getX();
				y = e.getY();
			}
		});
		setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
	}

	public T getT() {
		return t;
	}

	public void setT(T t) {
		this.t = t;
	}

	// 繪透明圖
	protected void paintTramsparentImage(Graphics g) {
		BufferedImage bi = new BufferedImage(sorImage.getWidth(null), sorImage.getHeight(null),
				BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2d = bi.createGraphics();
		g2d.drawImage(this.sorImage, 0, 0, null);

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

		this.desImage = bi;
		g.drawImage(bi, this.scale.getX1(), this.scale.getY1(), this.scale.getX2(), this.scale.getY2(), 0, 0,
				this.sorImage.getWidth(null), this.sorImage.getHeight(null), null);
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
		int r = this.getPureColor(color, R);
		r >>= 16;
		int g = this.getPureColor(color, G);
		g >>= 8;
		int b = this.getPureColor(color, B);
		// b >>= 0;
		if (this.inRange.inRange(r, R) && this.inRange.inRange(g, G) && this.inRange.inRange(b, B)) {
			res = true;
		}
		return res;
	}

	protected void paintPrimaryImage(Graphics g) {
		g.drawImage(this.sorImage, this.scale.getX1(), this.scale.getY1(), this.scale.getX2(), this.scale.getY2(), 0, 0,
				this.sorImage.getWidth(null), this.sorImage.getHeight(null), null);

	}

	abstract protected ImageProcess paintImage(Graphics g);

	protected void processImage(Graphics g) {
		if (paintImage(g) == ImageProcess.Default) {
			this.paintPrimaryImage(g);
		} else {
			this.paintTramsparentImage(g);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.mesh(g);
		processImage(g);
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
