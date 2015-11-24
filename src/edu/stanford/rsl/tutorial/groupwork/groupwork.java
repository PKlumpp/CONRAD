package edu.stanford.rsl.tutorial.groupwork;

import edu.stanford.rsl.conrad.data.numeric.Grid2D;
import ij.ImageJ;

public class groupwork {

	public static void main(String args[]) {
		new ImageJ();
		CustomPhantom phantom1 = new CustomPhantom(400, 400);
		phantom1.setSpacing(1.0f, 1.0f);
		phantom1.setOrigin(-phantom1.getSpacing()[0] * phantom1.getWidth() / 2
				+ 0.5, -phantom1.getSpacing()[1] * phantom1.getHeight() / 2
				+ 0.5);
		//phantom1.show();
		//Detector detector = new Detector(500, 300, 1f);
		//Grid2D sinogram = detector.getSinogram(phantom1);
		//System.out.println(sinogram.getSpacing()[1]);
		//Grid2D result = new Grid2D(400, 400);
		//sinogram = detector.rampFilter(sinogram);
		//System.out.println(sinogram.getSpacing()[1]);
		//sinogram.show();
		//result.setSpacing(2f,2f);
		//result.setOrigin(-result.getSpacing()[0] * result.getWidth() / 2 + 0.5,
		//		-result.getSpacing()[1] * result.getHeight() / 2 + 0.5);
		//Grid2D reconstruction = detector.backproject(sinogram, result);
		//Grid2D result2 = new Grid2D(400, 400);
		//sinogram = detector.getSinogram(phantom1);
		//sinogram = detector.ramLakFilter(sinogram);
		//sinogram = detector.rampFilter(sinogram);
		//System.out.println(sinogram.getSpacing()[1]);
		//sinogram.show();
		//result2.setSpacing(5f,5f);
		//result2.setOrigin(-result2.getSpacing()[0] * result2.getWidth() / 2 + 0.5,
		//		-result2.getSpacing()[1] * result2.getHeight() / 2 + 0.5);
		//Grid2D reconstruction2 = detector.backproject(sinogram, result2);
		//reconstruction.show();
		//reconstruction2.show();
		//phantom1.show();

		DetectorFanBeam detectorFanBeam = new DetectorFanBeam(500, 300, 1, 750, 1500);
		detectorFanBeam.getFanogram(phantom1);
	}
}
