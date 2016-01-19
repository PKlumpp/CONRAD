__kernel void add_Grids(__global float* input_1, __global float* input_2, __global float* output, int size){
  size_t i = get_global_id(0);
  if(i > size){
    return;
  }
  output[i] = input_1[i] + input_2[i];
}