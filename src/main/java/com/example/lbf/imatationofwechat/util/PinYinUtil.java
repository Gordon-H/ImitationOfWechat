package com.example.lbf.imatationofwechat.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Created by lbf on 2016/6/29.
 */
public class PinYinUtil {
    public static String toPinYin(String hanzi){
        HanyuPinyinOutputFormat hanyuPinyin = new HanyuPinyinOutputFormat();
//        设置拼音格式
        hanyuPinyin.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        hanyuPinyin.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        hanyuPinyin.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
        StringBuilder builder = new StringBuilder();
        String[] pinyinArray=null;
        try {
            for(int i = 0;i<hanzi.length();i++){
                char c = hanzi.charAt(i);
//                是否在汉字范围内
                if(c>=0x4e00 && c<=0x9fa5){
                    pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c, hanyuPinyin);
                    for(String str:pinyinArray){
                        builder.append(str);
                    }
                }else{
//                    若为小写字母则转为大写
                    if(c >= 'a'&&c<='z'){
                        c = (char) (c+('A'-'a'));
                    }
                    builder.append(c);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        //将获取到的拼音返回
        return builder.toString();
    }
}
