package edu.stanford.rsl.tutorial.groupwork;

import weka.filters.unsupervised.attribute.Center;
import edu.stanford.rsl.conrad.data.numeric.Grid2D;

public class Detector {
	private int pixels;
	private int projections;
	private float spacing;

	public Detector(int pixels, int projections, float spacing) {
		this.setPixels(pixels);
		this.setProjections(projections);
		this.setSpacing(spacing);
	}

	public int getPixels() {
		return pixels;
	}

	public void setPixels(int pixels) {
		this.pixels = pixels;
	}

	public int getProjections() {
		return projections;
	}

	public void setProjections(int projections) {
		this.projections = projections;
	}

	public float getSpacing() {
		return spacing;
	}

	public void setSpacing(float spacing) {
		this.spacing = spacing;
	}

	public Grid2D getSinogram(CustomPhantom phantom) {
		float[] center = { phantom.getWidth() / 2, phantom.getHeight() / 2 };
		Grid2D sinogram = new Grid2D(projections, pixels);
		for (int projection = 0; projection < projections; projection++) {
			float angle = 180f * projection / projections;
			double gradient = Math.cos(angle) / Math.sin(angle);
			for (int pixel = 0; pixel < pixels; pixel++) {
				double[] pixel_pos = {
						Math.cos(angle) * spacing * (pixel - pixels / 2)
								+ center[0],
						Math.sin(angle) * spacing * (pixel - pixels / 2)
								+ center[1] };
				double step_width = Math.sin(angle) * spacing;
				double step_height = Math.cos(angle) * spacing;
				int elements = 0; // Calculate # of elements in line integral
				double[][] intersects = new LineInBox(phantom.getWidth(),
						phantom.getHeight(), gradient, pixel_pos)
						.getBoxIntersects();
				double x_step = 
				for (int element = 0; element < elements; element++) {

				}
			}
		}
		return sinogram;
	}
}
