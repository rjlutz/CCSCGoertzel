import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DTMFGoertzelTest {

    private int binSize = 256;
    private double powerThreshold = 25.0D;

    @Test
    @DisplayName("Key1")
    void filterTestKey1() {
        float sampleRate = 8000;
        DTMFGoertzel goertzel = new DTMFGoertzel(sampleRate, powerThreshold);
        double[] samples = DTMF.generateCompositeTone(sampleRate, 1024,  697D, 1209D);
        assertTrue(goertzel.process(samples, binSize, '1'));
    }

    @Test
    @DisplayName("Key2")
    void filterTestKey2() {
        float sampleRate = 8000;
        DTMFGoertzel goertzel = new DTMFGoertzel(sampleRate, powerThreshold);
        double[] samples = DTMF.generateCompositeTone(sampleRate, 1024,  697D, 1336D);
        assertTrue(goertzel.process(samples, binSize, '2'));
    }

    @Test
    @DisplayName("Key3")
    void filterTestKey3() {
        float sampleRate = 8000;
        DTMFGoertzel goertzel = new DTMFGoertzel(sampleRate, powerThreshold);
        double[] samples = DTMF.generateCompositeTone(sampleRate, 1024, 697D, 1477D);
        assertTrue(goertzel.process(samples, binSize, '3'));
    }

    @Test
    @DisplayName("KeyA")
    void filterTestKeyA() {
        float sampleRate = 8000;
        DTMFGoertzel goertzel = new DTMFGoertzel(sampleRate, powerThreshold);
        double[] samples = DTMF.generateCompositeTone(sampleRate, 1024,  697D, 1633D);
        assertTrue(goertzel.process(samples, binSize, 'A'));
    }

    @Test
    @DisplayName("Key4")
    void filterTestKey4() {
        float sampleRate = 8000;
        DTMFGoertzel goertzel = new DTMFGoertzel(sampleRate, powerThreshold);
        double[] samples = DTMF.generateCompositeTone(sampleRate, 1024, 770D, 1209D);
        assertTrue(goertzel.process(samples, binSize, '4'));
    }

    @Test
    @DisplayName("Key5")
    void filterTestKey5() {
        float sampleRate = 8000;
        DTMFGoertzel goertzel = new DTMFGoertzel(sampleRate, powerThreshold);
        double[] samples = DTMF.generateCompositeTone(sampleRate, 1024, 770D, 1336D);
        assertTrue(goertzel.process(samples, binSize, '5'));
    }

    @Test
    @DisplayName("Key6")
    void filterTestKey6() {
        float sampleRate = 8000;
        DTMFGoertzel goertzel = new DTMFGoertzel(sampleRate, powerThreshold);
        double[] samples = DTMF.generateCompositeTone(sampleRate, 1024, 770D, 1477D);
        assertTrue(goertzel.process(samples, binSize, '6'));
    }

    @Test
    @DisplayName("KeyB")
    void filterTestKeyB() {
        float sampleRate = 8000;
        DTMFGoertzel goertzel = new DTMFGoertzel(sampleRate, powerThreshold);
        double[] samples = DTMF.generateCompositeTone(sampleRate, 1024, 770D, 1633D);
        assertTrue(goertzel.process(samples, binSize, 'B'));
    }

    @Test
    @DisplayName("Key7")
    void filterTestKey7() {
        float sampleRate = 8000;
        DTMFGoertzel goertzel = new DTMFGoertzel(sampleRate, powerThreshold);
        double[] samples = DTMF.generateCompositeTone(sampleRate, 1024, 852D, 1209D);
        assertTrue(goertzel.process(samples, binSize, '7'));
    }

    @Test
    @DisplayName("Key8")
    void filterTestKey8() {
        float sampleRate = 8000;
        DTMFGoertzel goertzel = new DTMFGoertzel(sampleRate, powerThreshold);
        double[] samples = DTMF.generateCompositeTone(sampleRate, 1024, 852D, 1336D);
        assertTrue(goertzel.process(samples, binSize, '8'));
    }

    @Test
    @DisplayName("Key9")
    void filterTestKey9() {
        float sampleRate = 8000;
        DTMFGoertzel goertzel = new DTMFGoertzel(sampleRate, powerThreshold);
        double[] samples = DTMF.generateCompositeTone(sampleRate, 1024, 852D, 1477D);
        assertTrue(goertzel.process(samples, binSize, '9'));
    }

    @Test
    @DisplayName("KeyC")
    void filterTestKeyC() {
        float sampleRate = 8000;
        DTMFGoertzel goertzel = new DTMFGoertzel(sampleRate, powerThreshold);
        double[] samples = DTMF.generateCompositeTone(sampleRate, 1024, 852D, 1633D);
        assertTrue(goertzel.process(samples, binSize, 'C'));
    }

    @Test
    @DisplayName("KeyS")
    void filterTestKeyS() {
        float sampleRate = 8000;
        DTMFGoertzel goertzel = new DTMFGoertzel(sampleRate, powerThreshold);
        double[] samples = DTMF.generateCompositeTone(sampleRate,1024, 941D, 1209D);
        assertTrue(goertzel.process(samples, binSize, '*'));
    }

    @Test
    @DisplayName("Key0")
    void filterTestKey0() {
        float sampleRate = 8000;
        DTMFGoertzel goertzel = new DTMFGoertzel(sampleRate, powerThreshold);
        double[] samples = DTMF.generateCompositeTone(sampleRate, 1024, 941D, 1336D);
        assertTrue(goertzel.process(samples, binSize, '0'));
    }

    @Test
    @DisplayName("KeyP")
    void filterTestKeyP() {
        float sampleRate = 8000;
        DTMFGoertzel goertzel = new DTMFGoertzel(sampleRate, powerThreshold);
        double[] samples = DTMF.generateCompositeTone(sampleRate, 1024, 941D, 1477D);
        assertTrue(goertzel.process(samples, binSize, '#'));
    }

    @Test
    @DisplayName("KeyD")
    void filterTestKeyD() {
        float sampleRate = 8000;
        DTMFGoertzel goertzel = new DTMFGoertzel(sampleRate, powerThreshold);
        double[] samples = DTMF.generateCompositeTone(sampleRate, 1024, 941D, 1633D);
        assertTrue(goertzel.process(samples, binSize, 'D'));
    }

    @Test
    @DisplayName("Key1-11025")
    void filterTestKey1_11025() {
        float sampleRate = 11025;
        DTMFGoertzel goertzel = new DTMFGoertzel(sampleRate, powerThreshold);
        double[] samples = DTMF.generateCompositeTone(sampleRate, 1024, 697D, 1209D);
        assertTrue(goertzel.process(samples, binSize, '1'));
    }

    @Test
    @DisplayName("Key1-22050")
    void filterTestKey1_22050() {
        float sampleRate = 22050;
        DTMFGoertzel goertzel = new DTMFGoertzel(sampleRate, powerThreshold);
        double[] samples = DTMF.generateCompositeTone(sampleRate, 1024, 697D, 1209D);
        assertTrue(goertzel.process(samples, binSize, '1'));
    }

    @Test
    @DisplayName("Key1-44100")
    void filterTestKey1_44100() {
        float sampleRate = 44100;
        DTMFGoertzel goertzel = new DTMFGoertzel(sampleRate, powerThreshold);
        double[] samples = DTMF.generateCompositeTone(sampleRate, 11025, 697D, 1209D);
        assertTrue(goertzel.process(samples, binSize, '1'));
    }

    @Test
    @DisplayName("S/Key1/S 250/500/250 msecs")
    void filterTestKey1Embedded() {

        DTMFGoertzel goertzel = new DTMFGoertzel(8000, 37.0D);
        AudioWav wav = new AudioWav("dtmf-8000-16-mono-key-1-8000samples.wav");
        double[] samples = wav.getSamples();

        ArrayList<DTMFGoertzel.PowerLevel> powerLevelsFound;
        boolean found = false;
        for (int i = 0; i < samples.length; i = i + binSize) {
            powerLevelsFound = goertzel.keyFilter(Arrays.copyOfRange(samples, i, i + binSize));
            for (DTMFGoertzel.PowerLevel level : powerLevelsFound)
                if (level.getDtmf().getKey() == '1') found |= true;
        }
        assertTrue(found);
    }

    @Test
    @DisplayName("DTMF 1-2")
    void filterTestDTMF_1_2() {

        boolean found_1 = false;
        boolean found_2 = false;

        DTMFGoertzel goertzel = new DTMFGoertzel(8000, 25.0D); // threshold lowered b/c wav volume
        AudioWav wav = new AudioWav("dtmf-1-2-#.wav");
        double[] samples = wav.getSamples();
        System.out.println("samples length: " + samples.length);

        ArrayList<DTMFGoertzel.PowerLevel> powerLevelsFound;
        for (int i = 0; i < samples.length; i = i + binSize) {
            powerLevelsFound = goertzel.keyFilter(Arrays.copyOfRange(samples, i, i + binSize));
            for (DTMFGoertzel.PowerLevel level : powerLevelsFound) {
                if (level.getDtmf().getKey() == '1') found_1 |= true;
                if (level.getDtmf().getKey() == '2') found_2 |= true;
            }
        }
        assertTrue(found_1 && found_2);
    }

    @Test
    @DisplayName("DTMF 1-2-#-normalized")
    void filterTestDTMF_1_2_P() {

        boolean found_1 = false, found_2 = true, found_P = true;

        DTMFGoertzel goertzel = new DTMFGoertzel(8000, 25.0D); // threshold lowered b/c wav volume
        AudioWav wav = new AudioWav("dtmf-1-2-#-normalized.wav");
        double[] samples = wav.getSamples();
        System.out.println("samples length: " + samples.length);

        ArrayList<DTMFGoertzel.PowerLevel> powerLevelsFound;
        for (int i = 0; i < samples.length; i = i + binSize) {
            powerLevelsFound = goertzel.keyFilter(Arrays.copyOfRange(samples, i, i + binSize));
            for (DTMFGoertzel.PowerLevel level : powerLevelsFound) {
                if (level.getDtmf().getKey() == '1') found_1 |= true;
                if (level.getDtmf().getKey() == '2') found_2 |= true;
                if (level.getDtmf().getKey() == '#') found_P |= true;
            }
        }
        assertTrue(found_1 && found_2 && found_P);
    }

    @Test
    @DisplayName("DTMF 8-1-#-normalized")
    void filterTestDTMF_8_1_P_normalized() {

        boolean found_8 = false, found_1 = false, found_P = false;

        DTMFGoertzel goertzel = new DTMFGoertzel(8000, 25.0D); // threshold lowered b/c wav volume
        AudioWav wav = new AudioWav("dtmf-8-1-#-normalized.wav");
        double[] samples = wav.getSamples();
        System.out.println("samples length: " + samples.length);

        ArrayList<DTMFGoertzel.PowerLevel> powerLevelsFound;
        for (int i = 0; i < samples.length; i = i + binSize) {
            powerLevelsFound = goertzel.keyFilter(Arrays.copyOfRange(samples, i, i + binSize));
            for (DTMFGoertzel.PowerLevel level : powerLevelsFound) {
                if (level.getDtmf().getKey() == '8') found_8 |= true;
                if (level.getDtmf().getKey() == '1') found_1 |= true;
                if (level.getDtmf().getKey() == '#') found_P |= true;
            }
        }
        assertTrue(found_8 && found_1 && found_P);
    }

    @Test
    @DisplayName("DTMF 8-1-#")
    void filterTestDTMF_8_1_P() {

        boolean found_8 = false, found_1 = false, found_P = false;

        DTMFGoertzel goertzel = new DTMFGoertzel(8000, 10.0D); // threshold lowered b/c wav volume
        AudioWav wav = new AudioWav("dtmf-8-1-#.wav");
        double[] samples = wav.getSamples();
        System.out.println("samples length: " + samples.length);

        ArrayList<DTMFGoertzel.PowerLevel> powerLevelsFound;
        for (int i = 0; i < samples.length; i = i + binSize) {
            powerLevelsFound = goertzel.keyFilter(Arrays.copyOfRange(samples, i, i + binSize));
            for (DTMFGoertzel.PowerLevel level : powerLevelsFound) {
                if (level.getDtmf().getKey() == '8') found_8 |= true;
                if (level.getDtmf().getKey() == '1') found_1 |= true;
                if (level.getDtmf().getKey() == '#') found_P |= true;
            }
        }
        assertTrue(found_8 && found_1 && found_P);
    }

    @Test
    @DisplayName("Silence")
    void filterTestSilence() {

        boolean found = false;
        DTMFGoertzel goertzel = new DTMFGoertzel(8000, 37.0D);
        AudioWav wav = new AudioWav("silence-8000-16-mono-200samples.wav");
        double[] samples = wav.getSamples();

        ArrayList<DTMFGoertzel.PowerLevel> powerLevelsFound;
        for (int i = 0; i < samples.length; i = i + 20) {
            powerLevelsFound = goertzel.keyFilter(Arrays.copyOfRange(samples, i, i + 20));
            if (powerLevelsFound.size() > 0) found |= true;
        }
        assertFalse(found);
    }

    @Test
    @DisplayName("Silence Long")
    void filterTestSilenceLong() {

        boolean found = false;
        DTMFGoertzel goertzel = new DTMFGoertzel(8000, 37.0D);
        double[] samples = new double[8000];
        for (int i=0; i < 8000; i++) {
            samples[i] = 0.0F;
        }

        ArrayList<DTMFGoertzel.PowerLevel> powerLevelsFound;
        for (int i = 0; i < samples.length; i = i + binSize) {
            powerLevelsFound = goertzel.keyFilter(Arrays.copyOfRange(samples, i, i + binSize));
            if (powerLevelsFound.size() > 0) found |= true;
        }
        assertFalse(found);
    }

    @Test
    @DisplayName("Square")
    void filterTestSquare() {

        boolean found = false;
        DTMFGoertzel goertzel = new DTMFGoertzel(8000, 37.0D);
        AudioWav wav = new AudioWav("square-8000-16-mono-200samples.wav");
        double[] samples = wav.getSamples();

        ArrayList<DTMFGoertzel.PowerLevel> powerLevelsFound;
        for (int i = 0; i < samples.length; i = i + 20) {
            powerLevelsFound = goertzel.keyFilter(Arrays.copyOfRange(samples, i, i + 20));
            if (powerLevelsFound.size() > 0) found |= true;
        }
        assertFalse(found);
    }

    @Test
    @DisplayName("Power Level Comparison 1")
    void sortPowerLevels1() {

        ArrayList<DTMFGoertzel.PowerLevel> powers = new ArrayList<>();
        powers.add(new DTMFGoertzel.PowerLevel(DTMF.getToneMap().get('*'), 0, 0));
        powers.add(new DTMFGoertzel.PowerLevel(DTMF.getToneMap().get('#'), 0, 0));
        powers.add(new DTMFGoertzel.PowerLevel(DTMF.getToneMap().get('1'), 25, 26));
        Collections.sort(powers);
        assertTrue(powers.get(0).getDtmf().getKey() == '1');
        assertEquals(25.0D, powers.get(0).getRowPower());
        assertEquals(26.0D, powers.get(0).getColumnPower());
    }

    @Test
    @DisplayName("Power Level Comparison 2")
    void sortPowerLevels2() {

        ArrayList<DTMFGoertzel.PowerLevel> powers = new ArrayList<>();
        powers.add(new DTMFGoertzel.PowerLevel(DTMF.getToneMap().get('*'), 0, 0));
        powers.add(new DTMFGoertzel.PowerLevel(DTMF.getToneMap().get('#'), 30, 31));
        powers.add(new DTMFGoertzel.PowerLevel(DTMF.getToneMap().get('1'), 25, 26));
        Collections.sort(powers);
        assertTrue(powers.get(0).getDtmf().getKey() == '#');
        assertEquals(30.0D, powers.get(0).getRowPower());
        assertEquals(31.0D, powers.get(0).getColumnPower());

    }

    @Test
    @DisplayName("Power Level Comparison 3")
    void sortPowerLevels3() {

        ArrayList<DTMFGoertzel.PowerLevel> powers = new ArrayList<>();
        powers.add(new DTMFGoertzel.PowerLevel(DTMF.getToneMap().get('*'), 20, 30));
        powers.add(new DTMFGoertzel.PowerLevel(DTMF.getToneMap().get('#'), 10, 10));
        powers.add(new DTMFGoertzel.PowerLevel(DTMF.getToneMap().get('1'), 0, 0));
        Collections.sort(powers);
        assertTrue(powers.get(0).getDtmf().getKey() == '*');
        assertEquals(20D, powers.get(0).getRowPower());
        assertEquals(30D, powers.get(0).getColumnPower());

    }

    private void print(ArrayList<Character> keys) {
        //String s = Arrays.stream(freqs).mapToObj(d -> String.format("%6.2f", d)).collect(Collectors.joining(","));
        //System.out.println(Arrays.toString(keys.toArray()));
    }

}
