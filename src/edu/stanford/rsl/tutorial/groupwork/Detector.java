package edu.stanford.rsl.tutorial.groupwork;

import com.jgoodies.forms.layout.Size;

import edu.stanford.rsl.conrad.data.numeric.Grid1D;
import edu.stanford.rsl.conrad.data.numeric.Grid1DComplex;
import edu.stanford.rsl.conrad.data.numeric.Grid2D;
import edu.stanford.rsl.conrad.data.numeric.Grid2DComplex;
import edu.stanford.rsl.conrad.data.numeric.InterpolationOperators;
import edu.stanford.rsl.conrad.data.numeric.NumericPointwiseOperators;
import edu.stanford.rsl.conrad.utils.FFTUtil;

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
		Grid2D sinogram = new Grid2D(projections, pixels);
		sinogram.setOrigin(0, (-pixels / 2 + 0.5) * spacing);
		sinogram.setSpacing(180.0f / projections, spacing);
		for (int projection = 0; projection < projections; projection++) {
			float angle = 180f * projection / projections;
			double gradient = Math.sin(Math.toRadians(angle))
					/ Math.cos(Math.toRadians(angle));
			double x_step = Math.abs(Math.sin(Math.toRadians(270 - angle))
					* phantom.getSpacing()[0]);
			double y_step = Math.sin(Math.toRadians(angle + 180))
					* -Math.signum(gradient) * phantom.getSpacing()[1];
			for (double pixel = sinogram.getOrigin()[1]; pixel <= sinogram
					.getOrigin()[1] + pixels * spacing; pixel += spacing) {
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

				int steps = (int) Math.floor(Math.sqrt(Math.pow(
						(intersects[0][0] - intersects[1][0])
								/ phantom.getSpacing()[0], 2)
						+ Math.pow((intersects[0][1] - intersects[1][1])
								/ phantom.getSpacing()[1], 2)));
				float value = 0.0f;
				for (int element = 0; element < steps; element++) {
					double x_real = intersects[0][0] + element * x_step;
					double y_real = intersects[0][1] + element * y_step;
					value += InterpolationOperators.interpolateLinear(phantom,
							phantom.physicalToIndex(x_real, y_real)[0],
							phantom.physicalToIndex(x_real, y_real)[1]);
				}
				value = value / steps;
				sinogram.setAtIndex((int) Math.round(sinogram.physicalToIndex(
						angle, pixel)[0]), (int) Math.round(sinogram
						.physicalToIndex(angle, pixel)[1]), value);
			}
		}
		return sinogram;
	}

	public Grid2D rampFilter(Grid2D sinogram) {

		sinogram = transpose(sinogram);
		System.out.println(sinogram.getHeight() + " " +  sinogram.getWidth());
		Grid1DComplex filter = new Grid1DComplex(sinogram.getWidth(), true);
		int filter_size = filter.getSize()[0];
		System.out.println(filter.getSize()[0] + "   " + filter_size);
		float filter_spacing = 1 / (spacing * filter_size);
		for (int i = 0; i < filter_size / 2; i++) {
			filter.setAtIndex(i, filter_spacing * i);
			filter.setAtIndex(filter_size - i - 1, filter_spacing * i);
		}
		// filter.getRealSubGrid(0, filter_size).show();
		for (int projection = 0; projection < sinogram.getHeight(); projection++) {
			Grid1DComplex projection_complex = new Grid1DComplex(
					sinogram.getSubGrid(projection), true);
			projection_complex.transformForward();
			for (int i = 0; i < filter_size; i++) {
				projection_complex.setRealAtIndex(
						i,
						projection_complex.getRealAtIndex(i)
								* filter.getRealAtIndex(i));
				projection_complex.setImagAtIndex(
						i,
						projection_complex.getImagAtIndex(i)
								* filter.getRealAtIndex(i));
			}
			projection_complex.transformInverse();
			for (int i = 0; i < sinogram.getWidth(); i++) {
				sinogram.setAtIndex(i, projection,
						projection_complex.getRealAtIndex(i));
			}
		}
		sinogram = transpose(sinogram);
		return sinogram;
	}

	public Grid2D transpose(Grid2D grid) {
		Grid2D grid_transposed = new Grid2D(grid.getHeight(), grid.getWidth());
		for (int i = 0; i < grid.getHeight(); i++) {
			for (int j = 0; j < grid.getWidth(); j++) {
				grid_transposed.setAtIndex(i, j, grid.getAtIndex(j, i));
			}
		}
		return grid_transposed;
	}
}
