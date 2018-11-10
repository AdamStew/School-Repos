import org.junit.Assert;
import org.junit.Test;

import static edu.gvsu.dlunit.DLUnit.*;

/**
 * Test cases for a signed 16-bit ALU.
 * <p>
 * IMPORTANT:  These test cases do *not* thoroughly test the circuit.  You need to
 * re-name this class and add more tests!
 * <p>
 * <p>
 * Created by kurmasz on 8/8/16.
 */
public class ALU16BitTest {

  public static class OpCodes {
    public static final int ADD = 0;
    public static final int MULT = 1;
    public static final int SUB = 4;
    public static final int LTS = 6;
    public static final int LTU = 7;
  }


  @Test
  public void testAddition() {
    setPinSigned("InputA", 23);
    setPinSigned("InputB", 44);
    setPinUnsigned("Op", OpCodes.ADD);
    run();
    Assert.assertEquals("Addition Output", 23 + 44, readPinSigned("Output"));
    Assert.assertEquals("Addition Overflow", false, readPin("Overflow"));
  }

  @Test
  public void testSubtraction() {
    setPinSigned("InputA", 24);
    setPinSigned("InputB", 45);
    setPinUnsigned("Op", OpCodes.SUB);
    run();
    Assert.assertEquals("Subtraction Output", 24 - 45, readPinSigned("Output"));
    Assert.assertEquals("Subtraction Overflow", false, readPin("Overflow"));
  }

  @Test
  public void testSmallMultiplication() {
    setPinSigned("InputA", 51);
    setPinSigned("InputB", 43);
    setPinUnsigned("Op", OpCodes.MULT);
    run();
    Assert.assertEquals("Multiplication Output", 51 * 43, readPinSigned("Output"));
    Assert.assertEquals("Multiplication High", 0, readPinSigned("High"));
    Assert.assertEquals("Multiplication Overflow", false, readPin("Overflow"));
  }

  @Test
  public void testLargeMultiplication() {
    setPinSigned("InputA", 300);
    setPinSigned("InputB", 450);
    setPinUnsigned("Op", OpCodes.MULT);
    run();
    Assert.assertEquals("Multiplication Output", 0x0F58, readPinSigned("Output"));
    Assert.assertEquals("Multiplication High", 0x2, readPinSigned("High"));
    Assert.assertEquals("Multiplication Overflow", true, readPin("Overflow"));
  }


  @Test
  public void ltSigned() {
    setPinSigned("InputA", 5);
    setPinSigned("InputB", 6);
    setPinUnsigned("Op", OpCodes.LTS);
    run();
    Assert.assertEquals("Signed Less Than Output", 1, readPinSigned("Output"));
    Assert.assertEquals("Signed Less Than Overflow", false, readPin("Overflow"));
  }

  @Test
  public void ltSigned2() {
    setPinSigned("InputA", 32767);
    setPinSigned("InputB", -1);
    setPinUnsigned("Op", OpCodes.LTS);
    run();
    Assert.assertEquals("Signed Less Than Output", 0, readPinSigned("Output"));
    Assert.assertEquals("Signed Less Than Overflow", false, readPin("Overflow"));
  }


  public static void verifySigned(long a, long b, boolean checkOverflow) {
    long expected = (a < b) ? 1 : 0;

    setPinSigned("InputA", a);
    setPinSigned("InputB", b);
    setPinUnsigned("Op", OpCodes.LTS);
    run();
    String message = String.format(" of %d < %d (signed) ", a, b);
    Assert.assertEquals("Output" + message, expected, readPinUnsigned("Output"));
    if (checkOverflow) {
      Assert.assertEquals("Overflow" + message, false, readPin("Overflow"));
    }
  }


  @Test
  public void ltSigned_allPairs() {
    long[] values = {-32768, -32767, -22222, -1234, -315, -2, -1, 0, 1, 2, 13, 127, 128, 129, 0x5555, 32766, 32767};
    for (long a : values) {
      for (long b : values) {
        verifySigned(a, b, true);
      }
    }
  }

  public static void verifyUnsigned(long a, long b, boolean checkOverflow) {
    long expected = (a < b) ? 1 : 0;

    setPinUnsigned("InputA", a);
    setPinUnsigned("InputB", b);
    setPinUnsigned("Op", OpCodes.LTU);
    run();
    String message = String.format(" of %d < %d (unsigned) ", a, b);
    Assert.assertEquals("Output" + message, expected, readPinUnsigned("Output"));
    if (checkOverflow) {
      Assert.assertEquals("Overflow" + message, false, readPin("Overflow"));
    }
  }

  @Test
  public void ltUnsigned_allPairs() {
    long[] values = {0, 1, 2, 13, 127, 128, 129, 0x5555, 32766, 32767, 32768};
    for (long a : values) {
      for (long b : values) {
        verifyUnsigned(a, b, true);
      }
    }
  }

}
