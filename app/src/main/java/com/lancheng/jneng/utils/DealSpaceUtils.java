package com.lancheng.jneng.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by Administrator on 2016/5/13 0013.
 */
public class DealSpaceUtils {

    public static String formateBankno(String source, int tag) {

        StringBuffer buffer = new StringBuffer(source.replace(" ", ""));
        switch (tag) {
            case 0:                 //身份证号
                try {
                    buffer.insert(6, " ");
                    buffer.insert(15, " ");
                    buffer.insert(20, " ");
                    buffer.insert(24, " ");
                } catch (Exception e) {
                    // TODO: handle exception
                }
                return buffer.toString();

            case 1101:                 //身份证号
                try {
                    buffer.insert(3, " ");
                    buffer.insert(7, " ");
                    buffer.insert(12, " ");
                    buffer.insert(17, " ");
                } catch (Exception e) {
                    // TODO: handle exception
                }
                return buffer.toString();

            case 1:
                //卡号
                try {
                    buffer.insert(4, " ");
                    buffer.insert(9, " ");
                    buffer.insert(14, " ");
                    buffer.insert(19, " ");
                } catch (Exception e) {
                    // TODO: handle exception
                }
                return buffer.toString();

            case 2:   //3, 8, 13, 18     手机号

                try {
                    buffer.insert(3, " ");
                    buffer.insert(8, " ");
                } catch (Exception e) {
                    // TODO: handle exception
                }
                return buffer.toString();

            default:
                return null;
        }
    }


    public static void watcherListener(final EditText editText, final int tag) {

        editText.addTextChangedListener(new TextWatcher() {

            int count;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                this.count = count;

            }

            @Override
            public void afterTextChanged(Editable s) {
                String result;
                result = formateBankno(s.toString(), tag);
//
                resultInfo(s, result, count, editText, tag);
            }
        });
    }

    public static void resultInfo(Editable s, String result, int count, EditText editText, int tag) {

        if (result.equals(s.toString())) {

            return;
        }

        int index;
        if (count == 0) {
            index = editText.getSelectionStart();
        } else {

            index = editText.getSelectionStart();

            if (tag == 0) {    //身份证号

                if (index == 7 || index == 16) {
                    index += 1;
                }
            } else if (tag == 1) {    //卡号   4, 9, 14, 19

                if (index == 5 || index == 10 || index == 15 || index == 20) {
                    index += 1;
                }
            } else if (tag == 2) {   //手机号   3, 8, 13, 18

                if (index == 3 || index == 8) {
                    index += 1;
                }
            } else if (tag == 1101) {//  111  111  1111  1111  1111
                if (index == 3 || index == 7 || index == 12 || index == 17) {
                    index += 1;
                }
            }
        }
        editText.setText(result);
        editText.setSelection(index);
    }

    /**
     * 处理中间间隔
     *
     * @param string
     * @return
     */
    public static String handMargin(String string) {

        StringBuffer sbf = new StringBuffer();
        String[] str = string.split(" ");

        for (int i = 0; i < str.length; i++) {

            sbf.append(str[i]);
        }
        return sbf.toString();
    }
}
