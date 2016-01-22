package edu.stanford.rsl.tutorial.groupwork;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.jogamp.opencl.CLBuffer;
import com.jogamp.opencl.CLCommandQueue;
import com.jogamp.opencl.CLContext;
import com.jogamp.opencl.CLDevice;
import com.jogamp.opencl.CLKernel;
import com.jogamp.opencl.CLMemory.Mem;
import com.jogamp.opencl.CLProgram;

import edu.stanford.rsl.conrad.data.numeric.Grid2D;
import edu.stanford.rsl.conrad.data.numeric.NumericGridOperator;
import edu.stanford.rsl.conrad.data.numeric.NumericPointwiseOperators;
import edu.stanford.rsl.conrad.data.numeric.opencl.OpenCLGrid2D;
import edu.stanford.rsl.conrad.opencl.OpenCLUtil;
import ij.ImageJ;

public class groupwork {

	public static void main(String args[]) {
		new ImageJ();
		CustomPhantom phantom1 = new CustomPhantom(400, 400);
		phantom1.setSpacing(1.0f, 1.0f);
		phantom1.setOrigin(-phantom1.getSpacing()[0] * phantom1.getWidth() / 2
				+ 0.5, -phantom1.getSpacing()[1] * phantom1.getHeight() / 2
				+ 0.5);

		CustomPhantom phantom2 = new CustomPhantom(400, 400);
		phantom2.configure(new int[] { 100, 100 },
				new float[] { 0.2f, 0.6f, 1f }, 200, 50, new int[] { 50, 50 });

		OpenCLGrid2D ocl_phantom = new OpenCLGrid2D(phantom1);

		long time = System.currentTimeMillis();
		/*
		 * for (int i = 0; i < 1000; i++) {
		 * NumericPointwiseOperators.addBy(phantom1, phantom1); }
		 * System.out.println(System.currentTimeMillis() - time); time =
		 * System.currentTimeMillis(); for (int i = 0; i < 1000; i++) {
		 * NumericPointwiseOperators.addBy(ocl_phantom, ocl_phantom); //
		 * ocl_phantom.getGridOperator().addBy(ocl_phantom, ocl_phantom); }
		 */
		Detector detector = new Detector(500, 300, 1f);
		Grid2D sinogram = detector.getSinogram(phantom1);
		sinogram = detector.ramLakFilter(sinogram);
		Grid2D result = new Grid2D(400, 400);
		result.setSpacing(1f, 1f);
		result.setOrigin(-result.getSpacing()[0] * result.getWidth() / 2 + 0.5,
				-result.getSpacing()[1] * result.getHeight() / 2 + 0.5);
		result = detector.clBackprojection(sinogram, result, 16);
		//result = detector.backproject(sinogram, result);
		phantom1.show();
		result.show();

		/*
		 * CLContext context = OpenCLUtil.createContext(); CLDevice[] devices =
		 * context.getDevices(); CLDevice device = context.getMaxFlopsDevice();
		 * System.out.println("Device: " + device);
		 * 
		 * CLProgram program = null; try { program = context.createProgram(
		 * groupwork.class.getResourceAsStream("kernel_ctReco.cl")) .build(); }
		 * catch (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); System.exit(-1); } int size =
		 * phantom2.getHeight() * phantom2.getWidth();
		 * 
		 * CLBuffer<FloatBuffer> input1 = context.createFloatBuffer(
		 * phantom1.getHeight() * phantom1.getWidth(), Mem.READ_ONLY); for (int
		 * i = 0; i < size; i++) {
		 * input1.getBuffer().put(phantom1.getBuffer()[i]); }
		 * input1.getBuffer().rewind();
		 * 
		 * CLBuffer<FloatBuffer> input2 = context.createFloatBuffer(
		 * phantom2.getHeight() * phantom2.getWidth(), Mem.READ_ONLY); for (int
		 * i = 0; i < size; i++) {
		 * input2.getBuffer().put(phantom2.getBuffer()[i]); }
		 * input2.getBuffer().rewind();
		 * 
		 * CLBuffer<FloatBuffer> output = context.createFloatBuffer(
		 * phantom2.getHeight() * phantom2.getWidth(), Mem.WRITE_ONLY);
		 * 
		 * int localWorkSize = Math.min(device.getMaxWorkGroupSize(), 16); int
		 * globalWorkSize = OpenCLUtil.roundUp(localWorkSize, size);
		 * 
		 * CLKernel kernel = program.createCLKernel("add_Grids"); CLCommandQueue
		 * queue = device.createCommandQueue();
		 * kernel.putArg(input1).putArg(input2).putArg(output).putArg(size);
		 * queue.putWriteBuffer(input1, true).finish() .putWriteBuffer(input2,
		 * true).finish() .put1DRangeKernel(kernel, 0, globalWorkSize,
		 * localWorkSize) .finish().putReadBuffer(output, true).finish(); Grid2D
		 * result = new Grid2D(phantom1.getWidth(), phantom1.getHeight()); for
		 * (int i = 0; i < size; i++) { result.getBuffer()[i] =
		 * output.getBuffer().get(); } result.show();
		 * 
		 * queue.release(); input1.release(); input2.release();
		 * output.release(); kernel.release(); program.release();
		 * context.release();
		 * 
		 * System.out.println(System.currentTimeMillis() - time);
		 */

		/*
		 * phantom1.show(); Detector detector = new Detector(500, 300, 1f);
		 * Grid2D sinogram = detector.getSinogram(phantom1); DetectorFanBeam
		 * fanDetector = new DetectorFanBeam(500, 300, 1f, 2000, 2200); Grid2D
		 * fanogram = fanDetector.getFanogram(phantom1); fanogram.show(); Grid2D
		 * sinogram2 = fanDetector.rebinning180(fanogram); sinogram2.show();
		 * sinogram.show(); // fanogram.show();
		 * 
		 * //sinogram2 = detector.rampFilter(sinogram2); sinogram2 =
		 * detector.ramLakFilter(sinogram2); sinogram2.show(); Grid2D result =
		 * new Grid2D(400, 400); result.setSpacing(1f, 1f);
		 * result.setOrigin(-result.getSpacing()[0] * result.getWidth() / 2 +
		 * 0.5, -result.getSpacing()[1] * result.getHeight() / 2 + 0.5); Grid2D
		 * reconstruction2 = detector.backproject(sinogram2, result);
		 * reconstruction2.show();
		 */

		// System.out.println(sinogram.getSpacing()[1]);
		// Grid2D result = new Grid2D(400, 400);
		// sinogram = detector.rampFilter(sinogram);
		// System.out.println(sinogram.getSpacing()[1]);
		// sinogram.show();
		// Grid2D result = new Grid2D(400, 400);
		// Grid2D result2 = new Grid2D(400, 400);
		// sinogram = detector.getSinogram(phantom1);
		// sinogram = detector.ramLakFilter(sinogram);
		// //sinogram = detector.rampFilter(sinogram);
		// System.out.println(sinogram.getSpacing()[1]);
		// sinogram.show();
		// result2.setSpacing(5f,5f);
		// result2.setOrigin(-result2.getSpacing()[0] * result2.getWidth() / 2 +
		// 0.5,
		// -result2.getSpacing()[1] * result2.getHeight() / 2 + 0.5);
		// Grid2D reconstruction2 = detector.backproject(sinogram, result2);
		// reconstruction.show();
		// reconstruction2.show();
		// //phantom1.show();
		//
		// DetectorFanBeam detectorFanBeam = new DetectorFanBeam(500, 300, 1,
		// 750, 1500);
		// detectorFanBeam.getFanogram(phantom1);
	}
}
