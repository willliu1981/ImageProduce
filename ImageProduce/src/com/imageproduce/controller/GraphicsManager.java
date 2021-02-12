package com.imageproduce.controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class GraphicsManager {
	private Image srcImage;
	private Image desImage;
	private String srcFName;
	private String desFName;
	private InRange inRange = new InRange(20);
	private List<Component> comps = new ArrayList<>();

	public void addComponent(Component comp) {
		comps.add(comp);
	}

	public List<Component> getComps() {
		return comps;
	}

	public void repaintComps() {
		for (Component comp : comps) {
			if (comp != null) {
				comp.repaint();
			}
		}
	}

	public InRange getInRange() {
		return inRange;
	}

	public void setInRange(int range) {
		this.inRange.setRnage(range);
	}

	public String getSrcFName() {
		return srcFName;
	}

	public void setSrcFName(String srcFName) {
		this.srcFName = srcFName;
		try {
			this.setSrcImage(ImageIO.read(new File(srcFName)));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public String getDesFName() {
		return desFName;
	}

	public void setDesFName(String desFName) {
		this.desFName = desFName;
	}

	public Image getSrcImage() {
		return srcImage;
	}

	public void setSrcImage(Image srcImage) {
		this.srcImage = srcImage;
	}

	public Image getDesImage() {
		return desImage;
	}

	public void setDesImage(Image desImage) {
		this.desImage = desImage;
	}

	public class InRange {
		public static final String R = "r";
		public static final String G = "g";
		public static final String B = "b";
		private Color color;
		private int rnage;

		public InRange(int range) {
			this.color = new Color(255, 255, 255);
			this.rnage = range;
		}

		public int getRnage() {
			return rnage;
		}

		public void setRnage(int rnage) {
			this.rnage = rnage;
		}

		public Color getColor() {
			return color;
		}

		public void setColor(Color color) {
			this.color = color;
		}

		public boolean inRange(int singleColor, String rgbSign) {
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

		public int getR() {
			return this.color.getRed();
		}

		public int getG() {
			return this.color.getGreen();
		}

		public int getB() {
			return this.color.getBlue();
		}
	}

}
