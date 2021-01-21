import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

/**
 * Class which adapts Goertzel algorithm for application in DTMF tone detection,
 * as described in ITU-T Recommendation Q.23
 * <p>
 * Once instantiated, a DTMFGoertzel object can be called with the keyFilter(...)
 * method to return a list of detected DTMF keys. The corresponding power levels of
 * each of the constituent frequencies for that DTMF key are reported.
 * <p>
 * Goertzel, G. (1958). An Algorithm for the Evaluation of Finite Trigonometric Series.
 * The American Mathematical Monthly, 65(1), 34-35.
 * <p>
 * @see <a href="https://doi.org/10.2307/2310304">https://doi.org/10.2307/2310304</a>
 * @see <a href="https://www.itu.int/rec/T-REC-Q.23-198811-I">https://www.itu.int/rec/T-REC-Q.23-198811-I</a>
 * <p>
 * @author rlutz
 * @version 0.1
 * @since   2021-01-16
 */
public class DTMFGoertzel {

    private final double powerThreshold, sampleRate;

    /**
     *
     * Class constructor specifying sample rate and minimum detection threshold.
     *
     * @param sampleRate Sampling rate used when recording waveform
     * @param powerThreshold Minimum level needed to accept when analyzing for target frequencies
     */
    public DTMFGoertzel(double sampleRate, double powerThreshold) {
        this.powerThreshold = powerThreshold;
        this.sampleRate = sampleRate;
    }

    /**
     * A convenience method to overload the keyFilter(...) method. This method adapts an incoming
     * array of double values to floats, so that the corresponding keyFilter(...) with the
     * appropriate method signature can be called.
     * <p>
     * This convenience method is needed we prefer to do all calculations in double, but
     * AudioRecord (@see android.media.AudioRecord) loves floats!
     * <p>
     * @param buffer waveform data to be analyzed
     * @return array list containing every dtmf key which exceeds the power threshold
     *
     */
    public ArrayList<PowerLevel> keyFilter(double[] buffer) {
        float[] floatBuffer = new float[buffer.length];
        for (int i=0; i < buffer.length; i++) floatBuffer[i] = (float) buffer[i];
        return keyFilter(floatBuffer);
    }

    /**
     * Method that implements the Goertzel Algorithm.
     *
     *
     * @param buffer waveform data to be analyzed, provided in range from -1 .. 1
     * @param frequencies array of frequencies of interest, in Hz
     * @param sampleRate the sampling rate of the incoming waveform data, in Hz
     * @return array of power levels, one for each frequency provided, in dB
     */
    private double[] goertzel(float[] buffer, Double[] frequencies, double sampleRate) {

        final double[] precalculatedCosines, precalculatedWnk;

        precalculatedCosines = new double[frequencies.length];
        precalculatedWnk = new double[frequencies.length];

        for (int i = 0; i < frequencies.length; i++) {
            precalculatedCosines[i] = 2 * Math.cos(2 * Math.PI * frequencies[i] / sampleRate);
            precalculatedWnk[i] = Math.exp(-2 * Math.PI * frequencies[i] / sampleRate);
        }

        double[] powers = new double[frequencies.length];

        double skn0, skn1, skn2;
        for (int j = 0; j < frequencies.length; j++) {
            skn0 = skn1 = 0;
            for (float v : buffer) {
                skn2 = skn1;
                skn1 = skn0;
                skn0 = precalculatedCosines[j] * skn1 - skn2 + v;
            }
            double wnk = precalculatedWnk[j];
            powers[j] = 20 * Math.log10(Math.abs(skn0 - wnk * skn1)); // dB
        }
        return powers;
    }

    /**
     * Method scans incoming buffer of waveform data and reports a list of DTMF keys that were
     * detected.
     *
     * @param buffer waveform data to be analyzed
     * @return array list containing every dtmf key which exceeds the power threshold
     */
    public ArrayList<PowerLevel> keyFilter(float[] buffer) {

        double[] row_powers = goertzel(buffer, DTMF.ROW_FREQUENCIES, sampleRate);
        double[] col_powers = goertzel(buffer, DTMF.COL_FREQUENCIES, sampleRate);

//      String p = Arrays.stream(powers).mapToObj(d -> String.format("%10.2f", d)).collect(Collectors.joining("  "));
//      System.out.println("powers: " + p); // SOP rather than Log.*, can be called from junit

        ArrayList<PowerLevel> result = new ArrayList<>();

        for (Map.Entry<Character, DTMF> entry : DTMF.getToneMap().entrySet()) {
            DTMF dtmf = entry.getValue();
            int row = dtmf.getPlacement()[0];
            int col = dtmf.getPlacement()[1];
            if (row_powers[row] > powerThreshold && col_powers[col] > powerThreshold)
                result.add(new PowerLevel(dtmf, row_powers[row], col_powers[col]));
        }

        Collections.sort(result); //sorts such that strongest signals appear first
        return result;
    }

    //

    /**
     * Provides a crude scan, intended for unit testing. if we find evidence of the expectedKey
     * anywhere in samples, return true!
     *
     * @param samples waveform data to be analyzed, provided in range from -1 .. 1
     * @param binSize bin size to use when partitioning data for analysis
     * @param expectedKey key that the caller is searching for
     * @return boolean describing if the desired key was found
     */
    public boolean process(double[] samples, int binSize, char expectedKey) {
        boolean found = false;

        for (int i = 0; i < samples.length; i = i + binSize) {
            double[] bin = Arrays.copyOfRange(samples, i, i + binSize);
            ArrayList<PowerLevel> levels = keyFilter(bin);
            for (PowerLevel level : levels) {
                if (level.getDtmf().getKey() == expectedKey) {
                    found = true;
                    break;
                }
                break;
            }
        }
        return found;
    }

    /**
     * Domain object used to associate detected DTMF key with the associate power levels detected
     * in a sample.
     *
     * <p>
     * @see <a href="https://www.itu.int/rec/T-REC-Q.23-198811-I">https://www.itu.int/rec/T-REC-Q.23-198811-I</a>
     * <p>
     * @author rlutz
     * @version 0.1
     * @since   2021-01-16
     */

    static class PowerLevel implements Comparable<PowerLevel> {

        private final DTMF dtmf;
        private final double rowPower, columnPower;

        PowerLevel(DTMF dtmf, double rowPower, double columnPower) {
			this.dtmf = dtmf;
			this.rowPower = rowPower;
			this.columnPower = columnPower;
        }

        public DTMF getDtmf() { return dtmf; }
        public double getRowPower() { return rowPower; }
        public double getColumnPower() { return columnPower; }
        public double getAveragePower() { return (rowPower + columnPower) / 2D;}

        /**
         * The intention of implementing compareTo(...) here is to use the average of the
         * constituent frequency powers to arrive at a scalar which can be used in sorting.
         * In this case, we simplify and use a sum, to alleviate the overhead of dividing by
         * two each time.
         *
         * Note that PowerLevel objects will be sorted such that the strongest signals appear before
         * weaker signals.
         *
         * @param other the other object to be used in comparisons for sorting
         * @return a value of -1, 0 or 1, as governed by the Comparable interface @see java.lang.Comparable
         */
        @Override
        public int compareTo(PowerLevel other) {
            double thisSum = this.rowPower + this.columnPower;
            double otherSum = other.getRowPower() + other.getColumnPower();
            if (thisSum < otherSum)
                return 1;
            else if (thisSum > otherSum)
                return -1;
            return 0;  // they must be equal!
        }
    }
}
