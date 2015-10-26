package edu.stanford.rsl.tutorial.groupwork;

import ij.ImageJ;

public class groupwork {
	
	public static void main(String args[]) {
		CustomPhantom phantom1 = new CustomPhantom(400, 400);
		phantom1.show();
		Detector detector = new Detector(500, 500, 1);
		detector.getSinogram(phantom1).show();
		new ImageJ();
		//phantom1.show();
	}
}
