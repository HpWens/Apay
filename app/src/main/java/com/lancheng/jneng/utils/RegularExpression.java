package com.lancheng.jneng.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 此类是用于来验证用的。 比如正则表达式
 *
 * @author Zeo
 */
public class RegularExpression {
    public Boolean Emailbox(String text) {
        Pattern pattern = Pattern
                .compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();

    }

    /**
     * 此方法是用来验证 手机号码是否正确
     * <p/>
     * 此参数是表示手机号的字符串
     *
     * @return
     */
    public Boolean PhoneNumber(String text) {

        Pattern pattern = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(14[7])|(18[^4,\\D]))\\d{8}$");
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();

    }

    /**
     * 用以判断 字符串的格式。有数字和字母
     *
     * @param text
     * @return
     */
    public Boolean password(String text) {
        Pattern pattern = Pattern.compile("^(?![^a-zA-Z]+$)(?!\\D+$).{6,15}$");

        Matcher matcher = pattern.matcher(text);
        return matcher.matches();

    }

    /**
     * 用以判读 用户昵称 收货人名字^[\u4E00-\u9FA5A-Za-z0-9_]+$
     */
    public Boolean nickname(String text) {
//^([u4e00\u9fa5]|[azAZ09]|\s|[\x00\xff])+$
        Pattern pattern = Pattern.compile("^([u4e00\\u9fa5]|[azAZ09]|\\s|[\\x00\\xff])+$");
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();

    }

    /**
     * 用以判断 邮政编码
     */
    public Boolean postcode(String text) {

        Pattern pattern = Pattern.compile("[1-9][0-9]{5}");
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();

    }

    /**
     * 此类是用于来验证用身份证的
     *
     * @author Zeo
     */

    public Boolean CardId(String text) {
        Pattern pattern = Pattern.compile("^\\d{15}|\\d{18}$");
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();

    }

    /**
     * 此类是用于来验证邀请码的
     *
     * @author AFLF^[a-zA-Z\d]+$/
     */

    public Boolean FriendCode(String text) {
        Pattern pattern = Pattern.compile("^\\[a-zA-Z\\d]+$");
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();

    }

    /**
     * 判断输入的金额
     *
     * @param money
     * @return
     */
    public Boolean MoneyType(String money) {
        Pattern pattern = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$");
        Matcher matcher = pattern.matcher(money);
        return matcher.matches();
    }

    /**
     * 银行卡卡号验证
     * @param card
     * @return
     */
    public Boolean BankCardType(String card) {
        Pattern pattern = Pattern.compile("^[1-9](\\d{15})$|^[1-9](\\d{18})$");
        Matcher matcher = pattern.matcher(card);
        return matcher.matches();
    }
}
