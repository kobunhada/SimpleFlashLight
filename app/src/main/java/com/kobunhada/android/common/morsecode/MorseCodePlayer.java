package com.kobunhada.android.common.morsecode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

/**
 * Morse code player.
 * <pre>
 * The rules of the International Morse Code are as follows.
 *   - short point "." standard for length
 *   - long point "-" three short points.
 *   - spacing between points one short point.
 *   - spacing between characters 3 short points.
 *   - spacing between words 7 short points
 * </pre>
 *
 * @author kobunhada
 */
public class MorseCodePlayer {
    /** tag.*/
    private static final String TAG = "MorseCode";
    /** short time.*/
    private static final int SHORT = 160;
    /** long time.*/
    private static final int LONG = SHORT * 3;
    /** interval code.*/
    private static final int INTERVAL_CODE = SHORT;
    /** interval character.*/
    private static final int INTERVAL_CHAR = SHORT * 3;
    /** interval word.*/
    private static final int INTERVAL_WORD = SHORT * 7;
    /** character span.*/
    private static final int CHAR_SPAN = -1;
    /** white space span.*/
    private static final int WORD_SPAN = -2;
    /** code list.*/
    private ArrayList<Integer> playList = null;
    /** has only on and off player.*/
    private PlayOnOff playOnOff = null;
    /** This class's instance.*/
    private static final MorseCodePlayer obj = new MorseCodePlayer();
    /** isPlaying.*/
    public boolean isPlaying = false;
    /** alphabet to morse code table.*/
    private static Map<String, String> alphabetToMorseCodeTable;
    static {
        alphabetToMorseCodeTable = new HashMap<String, String>();
        alphabetToMorseCodeTable.put("A", "・－");
        alphabetToMorseCodeTable.put("B", "－・・・");
        alphabetToMorseCodeTable.put("C", "－・－・");
        alphabetToMorseCodeTable.put("D", "－・・");
        alphabetToMorseCodeTable.put("E", "・");
        alphabetToMorseCodeTable.put("F", "・・－・");
        alphabetToMorseCodeTable.put("G", "－－・");
        alphabetToMorseCodeTable.put("H", "・・・・");
        alphabetToMorseCodeTable.put("I", "・・");
        alphabetToMorseCodeTable.put("J", "・－－－");
        alphabetToMorseCodeTable.put("K", "－・－");
        alphabetToMorseCodeTable.put("L", "・－・・");
        alphabetToMorseCodeTable.put("M", "－－");
        alphabetToMorseCodeTable.put("N", "－・");
        alphabetToMorseCodeTable.put("O", "－－－");
        alphabetToMorseCodeTable.put("P", "・－－・");
        alphabetToMorseCodeTable.put("Q", "－－・－");
        alphabetToMorseCodeTable.put("R", "・－・");
        alphabetToMorseCodeTable.put("S", "・・・");
        alphabetToMorseCodeTable.put("T", "－");
        alphabetToMorseCodeTable.put("U", "・・－");
        alphabetToMorseCodeTable.put("V", "・・・－");
        alphabetToMorseCodeTable.put("W", "・－－");
        alphabetToMorseCodeTable.put("X", "－・・－");
        alphabetToMorseCodeTable.put("Y", "－・－－");
        alphabetToMorseCodeTable.put("Z", "－－・・");
        alphabetToMorseCodeTable.put("a", "・－");
        alphabetToMorseCodeTable.put("b", "－・・・");
        alphabetToMorseCodeTable.put("c", "－・－・");
        alphabetToMorseCodeTable.put("d", "－・・");
        alphabetToMorseCodeTable.put("e", "・");
        alphabetToMorseCodeTable.put("f", "・・－・");
        alphabetToMorseCodeTable.put("g", "－－・");
        alphabetToMorseCodeTable.put("h", "・・・・");
        alphabetToMorseCodeTable.put("i", "・・");
        alphabetToMorseCodeTable.put("j", "・－－－");
        alphabetToMorseCodeTable.put("k", "－・－");
        alphabetToMorseCodeTable.put("l", "・－・・");
        alphabetToMorseCodeTable.put("m", "－－");
        alphabetToMorseCodeTable.put("n", "－・");
        alphabetToMorseCodeTable.put("o", "－－－");
        alphabetToMorseCodeTable.put("p", "・－－・");
        alphabetToMorseCodeTable.put("q", "－－・－");
        alphabetToMorseCodeTable.put("r", "・－・");
        alphabetToMorseCodeTable.put("s", "・・・");
        alphabetToMorseCodeTable.put("t", "－");
        alphabetToMorseCodeTable.put("u", "・・－");
        alphabetToMorseCodeTable.put("v", "・・・－");
        alphabetToMorseCodeTable.put("w", "・－－");
        alphabetToMorseCodeTable.put("x", "－・・－");
        alphabetToMorseCodeTable.put("y", "－・－－");
        alphabetToMorseCodeTable.put("z", "－－・・");
        alphabetToMorseCodeTable.put("1", "・－－－－");
        alphabetToMorseCodeTable.put("2", "・・－－－");
        alphabetToMorseCodeTable.put("3", "・・・－－");
        alphabetToMorseCodeTable.put("4", "・・・・－");
        alphabetToMorseCodeTable.put("5", "・・・・・");
        alphabetToMorseCodeTable.put("6", "－・・・・");
        alphabetToMorseCodeTable.put("7", "－－・・・");
        alphabetToMorseCodeTable.put("8", "－－－・・");
        alphabetToMorseCodeTable.put("9", "－－－－・");
        alphabetToMorseCodeTable.put("0", "－－－－－");
        alphabetToMorseCodeTable.put(".", "・－・－・－");
        alphabetToMorseCodeTable.put(",", "－－・・－－");
        alphabetToMorseCodeTable.put("?", "・・－－・・");
        alphabetToMorseCodeTable.put("!", "－・－・－－");
        alphabetToMorseCodeTable.put("-", "－・・・・－");
        alphabetToMorseCodeTable.put("/", "－・・－・");
        alphabetToMorseCodeTable.put("@", "・－－・－・");
        alphabetToMorseCodeTable.put("(", "－・－－・");
        alphabetToMorseCodeTable.put(")", "－・－－・－");


    }

    /**
     * private constructor
     */
    private MorseCodePlayer() {
    }

    /**
     * Get the MorseCode class instance.
     *
     * @return This class's instance
     */
    public static MorseCodePlayer getInstance() {
        return obj;
    }

    /**
     * Set the player. This player has only "on" and "off".
     *
     * @param playOnOff Something to play.
     */
    public void setPlayOnOff(PlayOnOff playOnOff) {
        this.playOnOff = playOnOff;
    }

    /**
     * Set the character string to be played.
     *
     * @param str the character string
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setCode(String str) {
        playList = new ArrayList<Integer>();
        playList.add(new Integer(CHAR_SPAN));

        Log.d("code", str);

        for (int i = 0, len = str.length(); i < len; i++) {
            String code = alphabetToMorseCodeTable.get(str.substring(i, i + 1));

            if (code.equals(" ")) {
                playList.add(new Integer(WORD_SPAN));
                continue;
            }
            if (code != null) {
                for (int j = 0, codeLen = code.length(); j < codeLen; j++) {
                    char c = code.charAt(j);

                    if (c == '－') {

                        playList.add(new Integer(LONG));
                    }
                    else if (c == '・') {
                        playList.add(new Integer(SHORT));
                    }
                }
            }
            playList.add(new Integer(CHAR_SPAN));
        }
//
// Why doesn't this code doing (T_T)
//        String[] characters =  str.split("") ;
//
//        for (String s : characters) {
//            Log.d("s:", "[" + s + "]");
//
//            String code = alphabetToMorseCodeTable.get(s);
//
//            if (code.equals(" ")) {
//                playList.add(new Integer(WORD_SPAN));
//                continue;
//            }
//            if (code != null) {
//                String[] morseCodes = code.split("");
//                for (String c : morseCodes) {
//                    if (c.equals("－")) {
//                        playList.add(new Integer(LONG));
//                    }
//                    else if (c.equals("・")) {
//                        playList.add(new Integer(SHORT));
//                    }
//                }
//            }
//            playList.add(new Integer(CHAR_SPAN));
//        }
    }

    /**
     * Playing the code list.
     * <pre>
     * You have to call (@link #setCode( ) ) method previously when you call this method.
     * </pre>
     */
    public void play() {
        isPlaying = true;

        for (Integer i : playList) {

            Log.d("code", "" + i);

            if (!isPlaying) {
                return;
            }

            if (i.intValue() == CHAR_SPAN) {
                try {
                    Thread.sleep(INTERVAL_CHAR);
                }
                catch (InterruptedException e) {
                    Log.e(TAG, "sleep error", e);
                }
                continue;
            }
            else if (i.intValue() == WORD_SPAN) {
                try {
                    Thread.sleep(INTERVAL_WORD);
                }
                catch (InterruptedException e) {
                    Log.e(TAG, "sleep error", e);
                }
                continue;
            }
            playOnOff.On();

            try {
                Thread.sleep(i);
                playOnOff.Off();
                Thread.sleep(INTERVAL_CODE);
            }
            catch (InterruptedException e) {
                Log.e(TAG, "sleep error", e);
            }
        }
    }

    /**
     * Stop playing.
     */
    public void stop() {
        isPlaying = false;
    }
}


