package mm.technomation.alistin;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Alistin {

    private Context context;
    private ArrayList<String> words = new ArrayList<>();

    private byte[] headers = {0x23, 0x21, 0x41, 0x4d, 0x52, 0x0a};
    private byte[] emptySound = {60, -26, 105, 109, -32, -52, -63, -125, -64, -114, 80, 122, -62, 18, 116, 80, -64, 0, 54, -34, -66, 60, -101, 24, 0, 4, -118, 2, 17, 113, 26, 64, 60, 32, 100, 121, -99, -26, 28, 5, -32, 31, 66, -38, 82, -42, -26, -127, -50, -47, 123, -38, 59, -20, 22, 87, -14, -98, 19, 24, 37, 74, 18, -112, 60, 26, 114, 99, 85, -14, -84, 10, 64, 82, 117, -102, -3, 37, -41, 110, -7, -102, 64, 13, -56, -34, -79, 52, -70, 9, 90, -24, 92, 104, 5, 16, 60, -34, 92, -123, -111, -11, -32, -127, 96, -108, -89, -22, -125, -20, -104, -67, -15, -4, 114, 115, 19, 70, -87, 83, -23, 85, -127, 119, -65, -36, 62, -64, 60, 20, 127, 95, 70, -93, -88, -124, 97, 45, -32, -118, -120, 9, 120, -67, -35, -26, 59, 15, 70, -103, 78, -78, -124, 50, 40, 108, -16, 51, -40, 0, 60, -32, 86, -125, 67, 81, 96, 9, 0, 28, -29, -22, 123, 111, 82, -119, -43, 37, -30, 115, -61, 44, 40, 113, -28, -16, 86, 119, -37, -82, 20, 64, 60, 53, 5, 87, -115, -38, -40, 1, -31, 15, -90, -81, 107, 41, -82, 48, -88, 84, 27, 38, 59, 17, 107, -72, 103, -44, 91, 86, 77, 107, 115, -128, 60, 26, 93, 109, -114, -84, -38, 0, 64, 89, -90, -53, -47, 40, -71, 42, 81, -66, 114, -74, -65, 32, 84, 98, -128, 18, 25, 53, -26, 4, -123, 64, 60, 68, 112, 116, -118, -46, -127, 1, 65, 14, -106, -103, 85, 24, 49, 16, -3, 80, -72, 86, -4, -11, 41, 42, 44, -44, -47, -36, 43, -97, 56, -64, 60, 6, 103, 103, -114, 23, 0, 1, 32, -61, -90, -104, 72, 96, -87, -77, 14, -21, 103, -120, -91, -31, 3, -3, -47, -6, -76, 39, 50, -117, 74, -64, 60, 66, 110, 99, 65, -5, 40, 3, -128, 120, 78, 90, 112, 38, -106, 16, -60, -1, 0, 34, 72, 10, -48, 47, -71, 15, -51, -73, 61, 68, -68, 0, 60, -32, 92, -128, -117, -72, 74, 3, 64, 91, -56, -86, 80, -122, 102, -48, -22, 48, -112, 12, 24, -26, -16, -101, -54, -69, 118, 60, -1, 4, 82, -96, 60, 30, 118, 100, -118, -68, -14, 5, 1, 44, -9, -102, -80, -84, 124, 102, -6, -92, -81, -68, 13, -39, -87, 10, 110, 17, -46, -65, -114, -79, -17, -64, 60, 26, 84, -123, 49, 124, 122, 4, 64, 83, -13, -6, -60, -40, 106, 47, -13, -86, 68, -57, 110, -3, 95, -108, -59, -41, 54, -85, -119, 103, -93, 96, 60, -40, 102, 118, 77, 34, -16, 24, -96, -109, -103, -54, 114, -94, 120, 29, -126, -37, -124, -11, 123, 70, 113, 51, -122, 114, 102, -40, 40, 13, 80, -128, 60, 12, 111, 119, -107, 115, -103, 0, -96, -106, -45, -55, 122, -86, 106, -56, -27, 62, -39, 84, 43, 42, -48, 83, 59, 47, 115, 26, 61, 4, 91, 64};

    private ArrayList<Byte> tempBytes = new ArrayList<>();

    private static String root = "CoreSpeech/";

    /**
     * Constructor for Alistin
     *
     * @param context Context for Alistin
     */
    public Alistin(Context context) {
        this.context = context;
    }


    private void appendBytes(String file, long from, long to) throws IOException {
        InputStream is = context.getAssets().open(file);
        is.skip(from);
        byte[] mByteArray = new byte[(int) (to - from)];
        is.read(mByteArray);
        is.close();
        for (byte mByte : mByteArray) {
            tempBytes.add(mByte);
        }
        for (byte mByte : emptySound) {
            tempBytes.add(mByte);
        }
    }

    /**
     * start the tts to speak
     *
     * @param paragraph text to read in Myanmar Unicode String
     */
    public void speak(String paragraph) {
        tempBytes.clear();
        words.clear();
        paragraph = breaker(paragraph);
        if (paragraph.charAt(0) == ' ') {
            paragraph = paragraph.substring(1);
        }
        boolean isFirstWordMM = isMM(paragraph.charAt(0));
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < paragraph.length(); i++) {
            char c = paragraph.charAt(i);
            switch (c) {
                case ' ':
                case '\u200b':
                    if (sb.length() > 0) {
                        words.add(sb.toString());
                    }
                    sb = new StringBuffer();
                    break;
                default:
                    if (isFirstWordMM != isMM(c)) {
                        if (sb.length() > 0) {
                            words.add(sb.toString());
                        }
                        sb = new StringBuffer();
                    } else {
                        sb.append(c);
                    }
                    break;
            }
        }

        playWords();
    }

    private void playSound(String path) {

        mp = new MediaPlayer();

        try {
            mp.setDataSource(path);
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            Trace(e.toString());
        }

    }

    /**
     * stop the tts if speaking. (rude though)
     */
    public void stop() {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }

    MediaPlayer mp = null;

    private void createFile() {
        byte[] allBytes = new byte[tempBytes.size()];
        for (int i = 0; i < tempBytes.size(); i++) {
            allBytes[i] = tempBytes.get(i);
        }
        try {
            FileOutputStream outputStream = context.openFileOutput("sound.amr", Context.MODE_PRIVATE);
            outputStream.write(headers);
            outputStream.write(allBytes);
            outputStream.close();

            if (mp != null) {
                mp.reset();
                mp.release();
            }
            playSound(context.getFilesDir() + "/sound.amr");

        } catch (Exception e) {

        }
    }

    private void playWords() {
        for (String word : words) {
            boolean isNumber = false;
            for (int i = 0; i < word.length(); i++) {
                if (!(4160 <= word.charAt(i) && word.charAt(i) <= 4169)) {
                    isNumber = false;
                    break;
                }
                isNumber = true;
            }

            if (isNumber) {
                numWords.clear();
                playNumber(word);
                for (String numWord : numWords) {
                    playAWord(numWord);
                }

            } else {
                playAWord(word);
            }
        }
        createFile();
    }

    private ArrayList<String> numWords = new ArrayList<>();
    private boolean toAdd = false;

    private void playNumber(String word) {
        String[] ex = {"ဆယ်", "ရာ", "ထောင်", "သောင်း", "သိန်း", "သန်း"};
        if (word.length() > 7) {
            toAdd = true;
            playNumber(word.substring(0, word.length() - 6));
            word = word.substring(word.length() - 6);
        }
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) != '၀') {
                numWords.add(word.charAt(i) + "");
                if (word.length() - i - 2 >= 0) {
                    numWords.add(ex[word.length() - i - 2]);
                }
            }
        }
        if (toAdd) {
            numWords.add(ex[ex.length - 1]);
            toAdd = !toAdd;
        }
    }

    private void playAWord(String word) {
        long[] toFrom;
        if (isMM(word.charAt(0))) {
            toFrom = getMMIndex(word);
        } else {
            toFrom = getEngIndex(word);
        }
        if (toFrom == null) {
            for (byte mByte : emptySound) {
                tempBytes.add(mByte);
            }
            return;
        }
        String fName;
        if (word.charAt(0) >= 0x1000 && word.charAt(0) <= 0x1021) {
            fName = String.valueOf((int) word.charAt(0)) + ".big";
        } else {
            fName = "4095.big";
        }
        try {
            appendBytes(root + fName, toFrom[0], toFrom[1]);
        } catch (IOException e) {
            Trace(e.toString());
        }
    }

    private long[] getMMIndex(String word) {
        try {
            String fname;
            if (word.charAt(0) >= 0x1000 && word.charAt(0) <= 0x1021) {
                fname = ((int) word.charAt(0)) + "";
            } else {
                fname = "4095";
            }
            InputStream is;
            is = context.getAssets().open(root + fname + ".snd");


            boolean bx = false;
            StringBuffer sb = new StringBuffer();
            int i = 0;
            WHILE:
            while (true) {
                int x = is.read();
                if (x == -1) {
                    is.close();
                    return null;
                }
                switch (x) {
                    case 1:
                        String st = sb.toString();
                        if (word.equals(st)) {
                            break WHILE;
                        }
                        bx = false;
                        sb = new StringBuffer();
                        i++;
                        break;
                    case 2:
                    case 3:
                        break;
                    default:
                        if (bx) {
                            x -= 3;
                        } else {
                            x -= 7;
                        }
                        x += 4094;
                        sb.append((char) x);
                        bx ^= true;
                        break;
                }
            }
            is.close();
            is = context.getAssets().open(root + fname + ".vol");
            DataInputStream dis = new DataInputStream(is);
            int unit = 8;
            long[] data = new long[2];
            int toskip = unit * i;
            dis.skip(toskip);
            data[0] = (long) dis.readInt();
            data[1] = (long) dis.readInt();
            dis.close();
            return data;
        } catch (IOException e) {
            Trace(e.toString());
            return null;
        }
    }

    private long[] getEngIndex(String word) {
        try {
            word = word.toLowerCase();
            InputStream is = context.getAssets().open(root + word.substring(0, 1).toLowerCase() + ".snd");
            boolean bx = false;
            StringBuffer sb = new StringBuffer();
            int i = 0;
            boolean iseng = false;
            WHILE:
            while (true) {
                int x = is.read();
                if (x == -1) {
                    is.close();
                    return null;
                }
                switch (x) {
                    case 1:
                        String st = sb.toString().toLowerCase();
                        if (word.equals(st)) {
                            break WHILE;
                        }
                        bx = false;
                        sb = new StringBuffer();
                        i++;
                        break;
                    case 2:
                        iseng = true;
                        break;
                    case 3:
                        iseng = false;
                        break;
                    default:
                        if (bx) {
                            x -= 3;
                        } else {
                            x -= 7;
                        }
                        if (!iseng) {
                            x += 4094;
                        }
                        sb.append((char) x);
                        bx ^= true;
                        break;
                }
            }
            is.close();
            is = context.getAssets().open(root + word.substring(0, 1).toLowerCase() + ".vol");
            DataInputStream dis = new DataInputStream(is);
            int unit = 8;
            long[] data = new long[2];
            int toskip = unit * i;
            dis.skip(toskip);
            data[0] = (long) dis.readInt();
            data[1] = (long) dis.readInt();
            dis.close();
            return data;
        } catch (Exception ex) {
            Trace(ex.getMessage());
        }
        return null;
    }


    private static boolean isMM(char c) {
        return (c >= 4096 && c <= 4255);
    }

    private static char[] ak = {4135, 4172, 4173, 4175};

    private static String breaker(String st) {
        ArrayList<Character> alak = new ArrayList<>();
        for (char c : ak) {
            alak.add(c);
        }
        String returnSt = "";
        for (int i = 0; i < st.length(); i++) {
            if (4096 <= st.charAt(i) && st.charAt(i) <= 4129 && i + 2 < st.length() && st.charAt(i + 1) == 4151 && st.charAt(i + 2) == 4154) {
                returnSt += st.charAt(i);
            } else if ((4096 <= st.charAt(i) && st.charAt(i) <= 4129 && i + 1 < st.length() && st.charAt(i + 1) != 4154) || alak.contains(st.charAt(i))) {
                returnSt += " " + st.charAt(i);
            } else if (4160 <= st.charAt(i) && st.charAt(i) <= 4169 && i != 0 && !(4160 <= st.charAt(i - 1) && st.charAt(i - 1) <= 4169)) {
                returnSt += " " + st.charAt(i);
            } else if (4096 <= st.charAt(i) && st.charAt(i) <= 4129 && i + 1 == st.length()) {
                returnSt += " " + st.charAt(i);
            } else {
                returnSt += st.charAt(i);
            }
        }

        returnSt = returnSt.replaceAll("ဿ", " သ");
        returnSt = returnSt.replaceAll("ဪ", " အော်");
        returnSt = returnSt.replaceAll("ဉာဏ်", " ညန်");
        returnSt = returnSt.replaceAll("ဦ", " အူ");
        returnSt = returnSt.replaceAll("့်", "့်");
        returnSt = returnSt.replaceAll("\u104A", " \u104A");
        returnSt = returnSt.replaceAll("\u104B", " \u104B");

        return returnSt + " ";
    }

    private void Trace(String s) {
        Log.e("Trace", s);
    }


}
