package edu.stanford.rsl.tutorial.groupwork;

import edu.stanford.rsl.conrad.data.numeric.Grid2D;

public class DetectorFanBeam extends Detector {
	float distanceSI;
	float distanceSD;

	public DetectorFanBeam(int pixels, int projections, float spacing,
			float distanceSI, float distanceSD) {
		super(pixels, projections, spacing);
		this.distanceSI = distanceSI;
		this.distanceSD = distanceSD;
	}

	public Grid2D getFanogram(CustomPhantom phantom) {
		float angleIncrement = getAngleIncrement360();
		return null;
	}

	private float getAngleIncrement180() {
		float detectorSize = pixels * spacing;
		float fanAngle = (float) Math.toDegrees(Math.atan(detectorSize / 2
				/ distanceSD));
		float angleIncrement = (180 + fanAngle) / projections;
		return angleIncrement;
	}

	private float getAngleIncrement360() {
		float angleIncrement = 360 / projections;
		return angleIncrement;
	}
}
