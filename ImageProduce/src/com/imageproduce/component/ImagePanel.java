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

public abstract class ImagePanel extends JPanel {
	protected final int PaneWidth = 10;
	protected final String OnlyRGB = "rgb";
	protected final String R = "r";
	protected final String G = "g";
	protected final String B = "b";
	protected final String A = "a";
	protected GraphicsScale scale;
	protected int x, y;
	protected Image sorImage;
	protected Image desImage;
	protected int colorRange = 210;

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
				if (this.checkRange(color)) {
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

	protected int getPureColor(int color, String rgbStr) {
		int c = -1;
		switch (rgbStr.toLowerCase().trim()) {
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

	protected boolean checkRange(int color) {
		boolean res = false;
		int r = this.getPureColor(color, R);
		r >>= 16;
		int g = this.getPureColor(color, G);
		g >>= 8;
		int b = this.getPureColor(color, B);
		// b >>= 0;
		if (r >= this.colorRange && g >= this.colorRange && b >= this.colorRange) {
			res = true;
		}
		return res;
	}

	protected void paintPrimaryImage(Graphics g) {
		g.drawImage(this.sorImage, this.scale.getX1(), this.scale.getY1(), this.scale.getX2(), this.scale.getY2(), 0, 0,
				this.sorImage.getWidth(null), this.sorImage.getHeight(null), null);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.mesh(g);
//		this.paintPrimaryImage(g);
//		this.paintTramsparentImage(g);
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
