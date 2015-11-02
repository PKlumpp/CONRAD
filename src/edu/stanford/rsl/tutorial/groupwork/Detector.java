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
		long time = System.currentTimeMillis() + 7000;
		Grid2D sinogram = new Grid2D(projections, pixels);
		sinogram.setOrigin(0, (-pixels / 2 + 0.5) * spacing);
		sinogram.setSpacing(180.0f / projections, spacing);
		for (int projection = 0; projection < projections; projection++) {
			float angle = 180f * projection / projections;
			double gradient = Math.sin(Math.toRadians(angle))
					/ Math.cos(Math.toRadians(angle));
			for (double pixel = sinogram.getOrigin()[1]; pixel <= sinogram
					.getOrigin()[1] + pixels * spacing; pixel++) {
				if (System.currentTimeMillis() > time) {
					int x = 2;
					x++;
				}
				double[] pixel_pos = { Math.sin(Math.toRadians(angle)) * pixel,
						Math.cos(Math.toRadians(angle)) * pixel * -1 };
				double[][] intersects = new LineInBox(phantom.indexToPhysical(
						phantom.getWidth() - 1, phantom.getHeight() - 1)[0],
						phantom.indexToPhysical(phantom.getWidth() - 1,
								phantom.getHeight() - 1)[1],
						phantom.indexToPhysical(0, 0)[0],
						phantom.indexToPhysical(0, 0)[1], gradient, pixel_pos)
						.getBoxIntersects();
				if (intersects[0][0] == -1 && intersects[0][1] == -1
						&& intersects[1][0] == -1 && intersects[1][1] == -1) {
					sinogram.setAtIndex(
							(int) sinogram.physicalToIndex(angle, pixel)[0],
							(int) sinogram.physicalToIndex(angle, pixel)[1], 0);
					continue;
				}
				double x_step = Math.cos(Math.toRadians(90 - angle))
						* phantom.getSpacing()[0];
				double y_step = Math.sin(Math.toRadians(270 - angle))
						* phantom.getSpacing()[1];
				int steps = (int) Math.floor(Math.sqrt(Math.pow(
						intersects[0][0] - intersects[1][0], 2)
						+ Math.pow(intersects[0][1] - intersects[1][1], 2)));
				float value = 0.0f;
				for (int element = 0; element < steps; element++) {
					double x_real = intersects[0][0] + element * x_step;
					double y_real = intersects[0][1] + element * y_step;
					value += InterpolationOperators.interpolateLinear(phantom,
							phantom.physicalToIndex(x_real, y_real)[0],
							phantom.physicalToIndex(x_real, y_real)[1]);
				}
				value = value / steps;
				sinogram.setAtIndex(
						(int) sinogram.physicalToIndex(angle, pixel)[0],
						(int) sinogram.physicalToIndex(angle, pixel)[1], value);

			}
		}
		return sinogram;
	}
}
