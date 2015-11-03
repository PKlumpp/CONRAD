package edu.stanford.rsl.tutorial.groupwork;

import ij.ImageJ;

public class groupwork {
	
	public static void main(String args[]) {
		new ImageJ();
		CustomPhantom phantom1 = new CustomPhantom(400, 400);
		phantom1.setSpacing(2.0f, 2.0f);
		phantom1.setOrigin(-phantom1.getSpacing()[0]*phantom1.getWidth()/2+0.5,-phantom1.getSpacing()[1]*phantom1.getHeight()/2+0.5);
		phantom1.show();
		Detector detector = new Detector(500, 250, 2);
		detector.getSinogram(phantom1).show();
		//phantom1.show();
	}
}
