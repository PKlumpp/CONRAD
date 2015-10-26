package edu.stanford.rsl.tutorial.groupwork;

public class LineInBox {
	private int box_width;
	private int box_height;
	private double gradient;
	private double[] intersect;

	public LineInBox(int box_width, int box_height, double gradient,
			double[] intersect) {
		this.box_width = box_width;
		this.box_height = box_height;
		this.gradient = gradient;
		this.intersect = intersect;
	}

	public double[][] getBoxIntersects() {
		double[][] boxIntersects = { { 0, 0 }, { 0, 0 } };
		double intersect_y = intersect[1] - gradient * intersect[0];

		// First Point
		boxIntersects[0][1] = intersect_y;
		boxIntersects[0][0] = 0;
		if (Double.isInfinite(gradient)) {
			if (intersect[0] >= 0 && intersect[0] <= box_width) {
				boxIntersects[0][0] = intersect[0];
				boxIntersects[0][1] = 0;
				boxIntersects[1][0] = intersect[0];
				boxIntersects[1][1] = box_height;
				return boxIntersects;
			} else {
				return notInBox();
			}
		}
		if (boxIntersects[0][1] < 0 || boxIntersects[0][1] > box_height) {

			if (gradient > 0) {
				boxIntersects[0][0] = (0 - intersect_y) / gradient;
				boxIntersects[0][1] = 0;
			} else {
				boxIntersects[0][0] = (box_height - intersect_y) / gradient;
				boxIntersects[0][1] = box_height;
			}
			if (boxIntersects[0][0] < 0 || boxIntersects[0][0] > box_width) {
				return notInBox();
			}
		}

		// Second Point
		if (boxIntersects[0][0] == 0) {
			if (gradient > 0) {
				if (!((box_height - intersect_y) / gradient < 0 || (box_height - intersect_y)
						/ gradient > box_width)) {
					boxIntersects[1][0] = (box_height - intersect_y) / gradient;
					boxIntersects[1][1] = box_height;
					return boxIntersects;
				} else {
					boxIntersects[1][0] = box_width;
					boxIntersects[1][1] = gradient * box_width + intersect_y;
					return boxIntersects;
				}
			} else {
				if (!((0 - intersect_y) / gradient < 0 || (0 - intersect_y)
						/ gradient > box_width)) {
					boxIntersects[1][0] = (0 - intersect_y) / gradient;
					boxIntersects[1][1] = 0;
				} else {
					boxIntersects[1][0] = box_width;
					boxIntersects[1][1] = gradient * box_width + intersect_y;
				}
			}
		} else {
			if (boxIntersects[0][1] == 0) {
				boxIntersects[1][1] = box_width * gradient + intersect_y;
				boxIntersects[1][0] = box_width;
				if (boxIntersects[1][1] < 0 || boxIntersects[1][1] > box_height) {
					if (gradient > 0) {
						boxIntersects[1][1] = box_height;
						boxIntersects[1][0] = (box_height - intersect_y)
								/ gradient;
					} else {
						boxIntersects[1][1] = 0;
						boxIntersects[1][0] = (0 - intersect_y) / gradient;
					}
				}
			}
		}
		if (boxIntersects[0][1] < 0) {
			int z = 0;
			z++;
		}
		return boxIntersects;
	}

	private double[][] notInBox() {
		double[][] boxIntersects = { { -1, -1 }, { -1, -1 } };
		return boxIntersects;
	}
}
