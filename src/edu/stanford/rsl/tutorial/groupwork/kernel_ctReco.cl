__kernel void add_Grids(__global float* input_1, __global float* input_2, __global float* output, int size){
  size_t i = get_global_id(0);
  if (i > size){
    return;
  }
  output[i] = input_1[i] + input_2[i];
}

__kernel void backprojectParallel(__global float* sinogram, __global float* result, int projections, int pixels, int cols_rows) {
	size_t index = get_global_id(0);
	if (index >= (cols_rows * cols_rows)){
		return;
	}
	
	//Indices in result image
	float x = index % cols_rows;
	float y = index / cols_rows;
	float rw_isocenter_offset = -((float) cols_rows - 1) / 2;
	float value = 0;
	
	for (int projection = 0; projection < projections; projection++){
		float angle = 180.0 *  (float) projection / projections;
		float rw_x = x + rw_isocenter_offset;
		float rw_y = y + rw_isocenter_offset;
		float rw_pixel = rw_x * cos(radians(angle)) + rw_y * sin(radians(angle));
		float pixel = rw_pixel + (pixels - 1) / 2;
		if (pixel < 0 || pixel > (pixels - 1)){
			continue;
		}
		
		// Linear Interpolation
		float value_1 = sinogram[(int) (trunc(pixel) * (float) projections + (float) projection)];
		float weight_1 = 1.0 - (pixel - trunc(pixel));
		
		float value_2 = sinogram[(int) (floor(pixel) * (float) projections + (float) projection)];
		float weight_2 = (pixel - trunc(pixel));
		
		value += weight_1 * value_1 + weight_2 * value_2;
	}
	result[index] = value;
}