import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Domain class used to represent a Dual Tone Multi-Frequency signal.
 * <p>
 * @see <a href="https://www.itu.int/rec/T-REC-Q.23-198811-I">https://www.itu.int/rec/T-REC-Q.23-198811-I</a>
 * <p>
 * @author rlutz
 * @version 0.1
 * @since   2021-01-16
 */
public class DTMF {

	public static Map<Character, DTMF> getToneMap() {
		return TONE_MAP;
	}

	private static final Map<Character, DTMF> TONE_MAP;

	public static final Double[] ROW_FREQUENCIES = {697D, 770D, 852D, 941D};
	public static final Double[] COL_FREQUENCIES = {1209D, 1336D, 1477D, 1633D};

	private final int[] placement; // placement of keys on the touchpad
	                               // 0,0 upper left, 3,3 lower right
	private final char key;        // the key: 0-9, #, *, A-D

	static {

		TONE_MAP = new HashMap<>(); // from ITU-T Recommendation Q.23
		TONE_MAP.put('1', new DTMF('1', 0, 0));
		TONE_MAP.put('2', new DTMF('2', 0, 1));
		TONE_MAP.put('3', new DTMF('3', 0, 2));
		TONE_MAP.put('A', new DTMF('A', 0, 3));

		TONE_MAP.put('4', new DTMF('4', 1, 0));
		TONE_MAP.put('5', new DTMF('5', 1, 1));
		TONE_MAP.put('6', new DTMF('6', 1, 2));
		TONE_MAP.put('B', new DTMF('B', 1, 3));

		TONE_MAP.put('7', new DTMF('7', 2, 0));
		TONE_MAP.put('8', new DTMF('8', 2, 1));
		TONE_MAP.put('9', new DTMF('9', 2, 2));
		TONE_MAP.put('C', new DTMF('C', 2, 3));

		TONE_MAP.put('*', new DTMF('*', 3, 0));
		TONE_MAP.put('0', new DTMF('0', 3, 1));
		TONE_MAP.put('#', new DTMF('#', 3, 2));
		TONE_MAP.put('D', new DTMF('D', 3, 3));

	}

	public DTMF(char key, int row, int col) {
		this.key = key;
		placement = new int[2];
		placement[0] = row;
		placement[1] = col;
	}

	public static short[] generateTone(int sampleRate, char c , int t) {
		DTMF dtmf = TONE_MAP.get(c);
		double[] f = Objects.requireNonNull(dtmf).getFrequencies();
		double [] resultD = generateCompositeTone(sampleRate, t,  f[0], f[1]);
		short[] resultS = new short[resultD.length]; /* convert double [] to short [] */

		for (int i = 0; i < resultD.length; i++)
			resultS[i] = (short)(Math.round(resultD[i]*32767.0D));
		return resultS;
	}

	public static double[] generateCompositeTone(final float sampleRate, int msecs, double... frequencies) {
		final double scale = 1.0D / frequencies.length;
		return multiplyTones(generateToneSum(sampleRate, msecs, frequencies),  scale);
	}

	// take any number of frequencies, generate sin waves and add them together
	private static double[] generateToneSum(final float sampleRate, int msecs, double... frequencies) {

		double[] buffer = new double[(int) (msecs / 1000D * sampleRate)];

		if (frequencies.length == 0) return buffer; //base case

		for (int sample = 0; sample < buffer.length; sample++) // get signal for the last element in frequencies
			buffer[sample] = Math.sin(2 * Math.PI * frequencies[frequencies.length-1] * sample / sampleRate);

		// call simpler case, recursively
		double[] rest = generateToneSum(sampleRate, msecs, Arrays.copyOfRange(frequencies, 0, frequencies.length - 1));
		buffer = addTones(buffer, rest);

		return buffer;
	}

	private static double[] addTones(double[] tones1, double[] tones2) {
		double[] result = new double[tones1.length];
		for (int i = 0; i < tones2.length; i++)
			result[i] = tones1[i] + tones2[i]; // aggregate results
		return result;
	}

	private static double[] multiplyTones(double[] tones, double factor) {
		double[] result = new double[tones.length];
		for (int i = 0; i < tones.length; i++)
			result[i] = tones[i] * factor;
		return result;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) return true;
		if (other == null || getClass() != other.getClass()) return false;
		DTMF dtmf = (DTMF) other;
		if (key != dtmf.key) return false;
		return Arrays.equals(placement, dtmf.placement);
	}

	@Override
	public int hashCode() {
		int result = Arrays.hashCode(placement);
		result = 31 * result + (int) key;
		return result;
	}

	// getters
	public double[] getFrequencies() {
		return new double[]{ROW_FREQUENCIES[placement[0]], COL_FREQUENCIES[placement[1]]};
	}
	public char getKey() {
		return key;
	}
	public int[] getPlacement() { return placement; }

}

