package empire;

import java.util.Random;

public class RandomGeneratorWithRange extends Random 
{
  public int nextInt(int n) 
  {
    if (n<=0) throw new IllegalArgumentException("n must be positive");
    int bits, val;
    do 
    {
      bits = next(31);
	val = bits % n;
    } 
    while(bits - val + (n-1) < 0);
    return val;
  }
}
