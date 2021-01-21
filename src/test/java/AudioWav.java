import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class AudioWav {

    private String fileName;
    private float[] samples;

    public AudioWav(String fileName) {
        this.fileName = fileName;
        samples = readWav(fileName);
    }

    public double[] getSamples() {
        double[] values = new double[samples.length];
        Arrays.setAll(values, i -> samples[i]);
        return values;
    }

    private float[] concatenate(float[] a, float[] b) {
        float[] c = new float[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

    private float[] readWav(String s) {

        URL u = this.getClass().getClassLoader().getResource(s);

        InputStream is;
        float[] fullResult = null;

        try {
            is = new URL(u.toString()).openStream();
            fullResult = new float[0];
//            while (is.available() > 0) {
                WavHeaderReader wavHeaderReader = new WavHeaderReader(is);
                WavHeader wavHeader = wavHeaderReader.read();
                System.out.println(wavHeader.toString());
                int numSamples = wavHeader.getSubChunk2Size() * 8 / wavHeader.getBitsPerSample();
                float[] result = new float[numSamples];

                float highestVal = 0;
                float lowestVal = 0;

                for (int i = 0; i < wavHeader.getSubChunk2Size(); i += 2) {
                    byte b1 = (byte) is.read();
                    byte b2 = (byte) is.read();
                    ByteBuffer bb = ByteBuffer.allocate(2);
                    bb.put(b2);
                    bb.put(b1);
                    float value = bb.getShort(0) / 32767.0F;
                    result[i / 2] = value;
                    //System.out.println(value);
                    if (value > highestVal) highestVal = value;
                    if (value < lowestVal) lowestVal = value;

                }
                fullResult = concatenate(fullResult, result);
                System.out.println("low, high = " + lowestVal + " " + highestVal);
//            }

            is.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return fullResult;
    }

    class WavHeaderReader {

        private static final int HEADER_SIZE = 44;

        private byte[] buf = new byte[HEADER_SIZE];
        private WavHeader header = new WavHeader();
        private InputStream inputStream;

        public WavHeaderReader(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        public WavHeader read() throws IOException {
            int res = inputStream.read(buf);

            if (res != HEADER_SIZE) {
                throw new IOException("Could not read header.");
            }
            header.setChunkID(Arrays.copyOfRange(buf, 0, 4));
            if (new String(header.getChunkID()).compareTo("RIFF") != 0) {
                throw new IOException("Illegal format.");
            }

            System.out.println("file size = " + toInt(4, false));
            header.setChunkSize(toInt(4, false));
            header.setFormat(Arrays.copyOfRange(buf, 8, 12));
            header.setSubChunk1ID(Arrays.copyOfRange(buf, 12, 16));
            header.setSubChunk1Size(toInt(16, false));
            header.setAudioFormat(toShort(20, false));
            header.setNumChannels(toShort(22, false));
            header.setSampleRate(toInt(24, false));
            header.setByteRate(toInt(24, false));
            header.setBlockAlign(toShort(32, false));
            header.setBitsPerSample(toShort(34, false));
            header.setSubChunk2ID(Arrays.copyOfRange(buf, 36, 40));
            header.setSubChunk2Size(toInt(40, false));
            return header;
        }

    /**
     * Convert byte[] array to int number
     *
     * @param start  start position of the buffer
     * @param endian <code>true</code> for big-endian
     *               <code>false</code> for little-endian
     * @return converted number
     */
    private int toInt(int start, boolean endian) {
        int k = (endian) ? 1 : -1;
        if (!endian) start += 3;

        int i1 = Byte.toUnsignedInt(buf[start]) << 24;
        int i2 = Byte.toUnsignedInt(buf[start + k * 1]) << 16;
        int i3 = Byte.toUnsignedInt(buf[start + k * 2]) << 8;
        int i4 = Byte.toUnsignedInt(buf[start + k * 3]);
//        System.out.println("i1:" + i1);
//        System.out.println("i2:" + i2);
//        System.out.println("i3:" + i3);
//        System.out.println("i4:" + i4);

        int result = (i1+i2+i3+i4);
//        System.out.println("total:" + result);

        return result;

    }

        /**
         * Convert byte[] array to short number
         *
         * @param start  start position of the buffer
         * @param endian <code>true</code> for big-endian
         *               <code>false</code> for little-endian
         * @return converted number
         */
        private short toShort(int start, boolean endian) {
            short k = (endian) ? (short) 1 : -1;
            if (!endian) {
                start++;
            }
            return (short) ((buf[start] << 8) + (buf[start + k * 1]));
        }


    }

    class WavHeader {
        private byte[] chunkID = new byte[4];
        private int chunkSize;
        private byte[] format = new byte[4];
        private byte[] subChunk1ID = new byte[4];
        private int subChunk1Size;
        private short audioFormat;
        private short numChannels;
        private int sampleRate;
        private int byteRate;
        private short blockAlign;
        private short bitsPerSample;
        private byte[] subChunk2ID = new byte[4];
        private int subChunk2Size;

        @Override
        public String toString() {
            return "The RIFF chunk descriptor: " + new String(this.getChunkID()) + "\n" +
                    "Size of this chunk: " + this.getChunkSize() + "\n" +
                    "Format: " + new String(this.getFormat()) + "\n" + "\n" +
                    "fmt subchunk: " + new String(this.getSubChunk1ID()) + "\n" +
                    "Size of this chunk: " + this.getSubChunk1Size() + "\n" +
                    "Audio format: " + this.getAudioFormat() + "\n" +
                    "Number of channels: " + this.getNumChannels() + "\n" +
                    "Sample rate: " + this.getSampleRate() + "\n" +
                    "Byte rate: " + this.getByteRate() + "\n" +
                    "Block align: " + this.getBlockAlign() + "\n" +
                    "Bits per sample: " + this.getBitsPerSample() + "\n" + "\n" +
                    "data subchunk: " + new String(this.getSubChunk2ID()) + "\n" +
                    "Size of this chunk: " + this.getSubChunk2Size();
        }

        public byte[] getChunkID() {
            return chunkID;
        }

        public void setChunkID(byte[] chunkID) {
            this.chunkID = chunkID;
        }

        public int getChunkSize() {
            return chunkSize;
        }

        public void setChunkSize(int chunkSize) {
            this.chunkSize = chunkSize;
        }

        public byte[] getFormat() {
            return format;
        }

        public void setFormat(byte[] format) {
            this.format = format;
        }

        public byte[] getSubChunk1ID() {
            return subChunk1ID;
        }

        public void setSubChunk1ID(byte[] subChunk1ID) {
            this.subChunk1ID = subChunk1ID;
        }

        public int getSubChunk1Size() {
            return subChunk1Size;
        }

        public void setSubChunk1Size(int subChunk1Size) {
            this.subChunk1Size = subChunk1Size;
        }

        public short getAudioFormat() {
            return audioFormat;
        }

        public void setAudioFormat(short audioFormat) {
            this.audioFormat = audioFormat;
        }

        public short getNumChannels() {
            return numChannels;
        }

        public void setNumChannels(short numChannels) {
            this.numChannels = numChannels;
        }

        public int getSampleRate() {
            return sampleRate;
        }

        public void setSampleRate(int sampleRate) {
            this.sampleRate = sampleRate;
        }

        public int getByteRate() {
            return byteRate;
        }

        public void setByteRate(int byteRate) {
            this.byteRate = byteRate;
        }

        public short getBlockAlign() {
            return blockAlign;
        }

        public void setBlockAlign(short blockAlign) {
            this.blockAlign = blockAlign;
        }

        public short getBitsPerSample() {
            return bitsPerSample;
        }

        public void setBitsPerSample(short bitsPerSample) {
            this.bitsPerSample = bitsPerSample;
        }

        public byte[] getSubChunk2ID() {
            return subChunk2ID;
        }

        public void setSubChunk2ID(byte[] subChunk2ID) {
            this.subChunk2ID = subChunk2ID;
        }

        public int getSubChunk2Size() {
            return subChunk2Size;
        }

        public void setSubChunk2Size(int subChunk2Size) {
            this.subChunk2Size = subChunk2Size;
        }

    }
}
