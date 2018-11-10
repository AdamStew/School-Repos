import org.junit.Assert;
import org.junit.Test;

import static edu.gvsu.dlunit.DLUnit.*;

/**
 * Test cases for a signed 16-bit multiplier with overflow.
 * <p>
 * IMPORTANT:  These test cases do *not* thoroughly test the circuit.  You need to
 * re-name this class and add more tests!
 * <p>
 * <p>
 * Created by kurmasz on 8/8/16.
 */
public class Multiplier16BitTest {


  // The complete list of integers to be tests.
  // (IMPORTANT !!! You need to add to this list !!! )
  public static final long testIntegers[] = {-32768, -32767, -22222, -1234, -2, -1,  0, 1, 2, 13, 127, 128, 129, 0x5555, 32766, 32767};

  // Helper method that runs a test for a given pair of integers.
  protected static void verify(long a, long b, boolean checkOverflow) {

    //////////////////////////////////
    //
    // Compute the expected outputs
    //
    /////////////////////////////////
    long expected = a * b;

    // The `overflow` output should be `true` if the expected output is not in the range [-(2^15), (2^15)-1]
    // (In java "1 << 15" takes the bit string 0000000000000001 and shifts it left 15 spaces, effectively
    // generating the value 2^15.)
    boolean expectedOverflow = ((expected >= (1 << 15)) || (expected < -(1 << 15)));

    ////////////////////////////////////////
    //
    // Configure and simulate the circuit
    //
    ///////////////////////////////////////
    setPinSigned("InputA", a);
    setPinSigned("InputB", b);
    run();


    ////////////////////////////////////////
    //
    // Check the correctness of the output
    //
    ///////////////////////////////////////
    String message = String.format("of %d x %d: ", a, b);

    if (expectedOverflow) {
      // If an overflow is expected, then "Low" is just the low 16 bits oof the answer.
      // (expected & 0x0FFFFL) throws away all but the lowest 16 bits.
      // (I used 0x0FFFFL instead of 0xFFFF to make it clear we don't want the mask sign-extended.)

      // "High" is the 2nd group of 16 bits.  expected >> 16 moves those bits from
      // bit positions 16-31 into bit positions 0-15.  The following & 0x0FFFFL assures that bits
      // 16-31 are 0.

      Assert.assertEquals("Low " + message, expected & 0x0FFFFL, readPinUnsigned("Low"));
      Assert.assertEquals("High " + message, (expected >> 16) & 0x0FFFFL, readPinUnsigned("High"));
    } else {
      Assert.assertEquals("Low " + message, expected, readPinSigned("Low"));

      // If there is no overflow, then `High` should be 0000000000000000 for positive numbers
      // and 1111111111111111 for negative numbers.
      long expectedHigh = expected >= 0 ? 0 : -1;
      Assert.assertEquals("High " + message, expectedHigh, readPinSigned("High"));
    }
    if (checkOverflow) {
      Assert.assertEquals("Overflow " + message, expectedOverflow, readPin("Overflow"));
    }
  }

  //
  // Quick tests designed to quickly catch major errors.  (Also serve as example tests)
  //

  @Test
  public void zero_zero() {
    verify(0, 0, true);
  }

  @Test
  public void zero_one() {
    verify(0, 1, true);
  }

  @Test
  public void example_overflow() {
    verify(10000, 10, true);
  }


  // This is actually rather gross; but, it is an effective way to thoroughly test your adder without
  // having to write hundreds of individual methods.
  @Test
  public void testAll() {
    for (long a : testIntegers) {
      for (long b : testIntegers) {
        verify(a, b, true);
        verify(b, a, true);
      }
    }
  } // end testAll
}
