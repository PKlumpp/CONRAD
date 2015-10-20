package edu.stanford.rsl.tutorial.groupwork;

import ij.ImageJ;

public class groupwork {
	
	public static void main(String args[]) {
		CustomPhantom phantom1 = new CustomPhantom(400, 400);
		new ImageJ();
		phantom1.show();
	}
}
