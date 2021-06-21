package cc.i9mc.bungeemanagement.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class HanyuPinyinHelperUtil {

    public static void main(String[] args) {
        System.out.println(toHanyuPinyin("憨批污点咕咕咕"));
    }

    public static String toHanyuPinyin(String ChineseLanguage) {
        char[] cl_chars = ChineseLanguage.trim().toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);

        try {
            for (char cl_char : cl_chars) {
                if (String.valueOf(cl_char).matches("[一-龥]+")) {
                    stringBuilder.append(PinyinHelper.toHanyuPinyinStringArray(cl_char, defaultFormat)[0]);
                } else {
                    stringBuilder.append(cl_char);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            System.out.println("字符不能转成汉语拼音");
        }

        return stringBuilder.toString();
    }

    public static String toHanyuPinyinE(String ChineseLanguage) {
        char[] cl_chars = ChineseLanguage.trim().toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);

        try {
            for (char cl_char : cl_chars) {
                if (String.valueOf(cl_char).matches("[一-龥]+")) {
                    stringBuilder.append(PinyinHelper.toHanyuPinyinStringArray(cl_char, defaultFormat)[0]);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            System.out.println("字符不能转成汉语拼音");
        }

        return stringBuilder.toString();
    }

    public static String getFirstLettersUp(String ChineseLanguage) {
        return getFirstLetters(ChineseLanguage, HanyuPinyinCaseType.UPPERCASE);
    }

    public static String getFirstLettersLo(String ChineseLanguage) {
        return getFirstLetters(ChineseLanguage, HanyuPinyinCaseType.LOWERCASE);
    }

    public static String getFirstLetters(String ChineseLanguage, HanyuPinyinCaseType caseType) {
        char[] cl_chars = ChineseLanguage.trim().toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(caseType);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        try {
            for (char cl_char : cl_chars) {
                String str = String.valueOf(cl_char);
                if (str.matches("[一-龥]+")) {
                    stringBuilder.append(PinyinHelper.toHanyuPinyinStringArray(cl_char, defaultFormat)[0].substring(0, 1));
                } else if (str.matches("[0-9]+")) {
                    stringBuilder.append(cl_char);
                } else if (str.matches("[a-zA-Z]+")) {
                    stringBuilder.append(cl_char);
                } else {
                    stringBuilder.append(cl_char);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination var7) {
            System.out.println("字符不能转成汉语拼音");
        }

        return stringBuilder.toString();
    }

    public static String getPinyinString(String ChineseLanguage) {
        char[] cl_chars = ChineseLanguage.trim().toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        try {
            for(int i = 0; i < cl_chars.length; ++i) {
                String str = String.valueOf(cl_chars[i]);
                if (str.matches("[一-龥]+")) {
                    stringBuilder.append(PinyinHelper.toHanyuPinyinStringArray(cl_chars[i], defaultFormat)[0]);
                } else if (str.matches("[0-9]+")) {
                    stringBuilder.append(cl_chars[i]);
                } else if (str.matches("[a-zA-Z]+")) {
                    stringBuilder.append(cl_chars[i]);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination var6) {
            System.out.println("字符不能转成汉语拼音");
        }

        return stringBuilder.toString();
    }

    public static String getFirstLetter(String ChineseLanguage) {
        char[] cl_chars = ChineseLanguage.trim().toCharArray();
        String hanyupinyin = "";
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        try {
            String str = String.valueOf(cl_chars[0]);
            if (str.matches("[一-龥]+")) {
                hanyupinyin = PinyinHelper.toHanyuPinyinStringArray(cl_chars[0], defaultFormat)[0].substring(0, 1);
            } else if (str.matches("[0-9]+")) {
                hanyupinyin = hanyupinyin + cl_chars[0];
            } else if (str.matches("[a-zA-Z]+")) {
                hanyupinyin = hanyupinyin + cl_chars[0];
            }
        } catch (BadHanyuPinyinOutputFormatCombination var5) {
            System.out.println("字符不能转成汉语拼音");
        }

        return hanyupinyin;
    }
}
