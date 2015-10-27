package edu.stanford.rsl.tutorial.groupwork;

import weka.filters.unsupervised.attribute.Center;
import edu.stanford.rsl.conrad.data.Grid;
import edu.stanford.rsl.conrad.data.numeric.Grid2D;
import edu.stanford.rsl.conrad.data.numeric.InterpolationOperators;

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
		for (int i = 0; i < projections; i++) {
			sinogram.setOrigin(0,pixels/2-0.5);
			sinogram.setSpacing(180.0f/projections, spacing);
		}
		for (int projection = 0; projection < projections; projection++) {
			float angle = 180f * projection / projections;
			double gradient = Math.sin(Math.toRadians(angle + 90))
					/ Math.cos(Math.toRadians(angle + 90));
			for (int pixel = 0; pixel < pixels; pixel++) {
				if (projection == 0) {
					int x = 0;
					x++;
				}
				double[] pixel_pos = {
						Math.cos(Math.toRadians(angle)) * spacing
								* (pixel - pixels / 2) + center[0],
						Math.sin(Math.toRadians(angle)) * spacing
								* (pixel - pixels / 2) + center[1] };
				double[][] intersects = new LineInBox(phantom.getWidth(),
						phantom.getHeight(), gradient, pixel_pos)
						.getBoxIntersects();
				if (intersects[0][0] == -1 && intersects[0][1] == -1
						&& intersects[1][0] == -1 && intersects[1][1] == -1) {
					sinogram.setAtIndex(projection, pixel, 0);
					continue;
				}
				double x_step = Math.cos(Math.toRadians(90 - angle));
				double y_step = Math.sin(Math.toRadians(270 - angle));
				int steps = (int) Math.floor(Math.sqrt(Math.pow(
						intersects[0][0] - intersects[1][0], 2)
						+ Math.pow(intersects[0][1] - intersects[1][1], 2)));
				float value = 0.0f;
				for (int element = 0; element < steps; element++) {
					value += InterpolationOperators.interpolateLinear(phantom,
							intersects[0][0] + element * x_step,
							intersects[0][1] + element * y_step);
				}
				value = value / steps;
				sinogram.setAtIndex(projection, pixel, value);

			}
		}
		return sinogram;
	}
}
