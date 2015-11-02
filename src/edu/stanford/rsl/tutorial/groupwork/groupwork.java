package edu.stanford.rsl.tutorial.groupwork;

import ij.ImageJ;

public class groupwork {
	
	public static void main(String args[]) {
		CustomPhantom phantom1 = new CustomPhantom(400, 400);
		phantom1.setOrigin(-phantom1.getWidth()/2+0.5,phantom1.getHeight()/2-0.5);
		phantom1.setSpacing(1.0f, -1.0f);
		phantom1.show();
		Detector detector = new Detector(500, 500, 1);
		detector.getSinogram(phantom1).show();
		new ImageJ();
		//phantom1.show();
	}
}
