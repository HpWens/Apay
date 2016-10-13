package com.lancheng.jneng.base;

/**
 * Created by AFLF on 2016/7/4.
 */
public interface Constant {
    //    测试服
    String SERVER_HOST = "http://002.600i.com.cn/api/";
    //    正式服
    // String SERVER_HOST = "http://s-281945.abc188.com/api/";
    String SERVER_IMAGE = "http://002.600i.com.cn";
//图片服务地址
    // String SERVER_IMAGE = "http://s-281945.abc188.com";
    /**
     * 首页广告列表
     */
    String HOME_ADLIST = SERVER_HOST + "Home/adlist";
    /**
     * 新闻列表
     */
    String ARTICK_LIST = SERVER_HOST + "Article/list";

    /**
     * 登陆
     */
    String LOGIN = SERVER_HOST + "Member/login";
    /**
     * 注册
     */
    String REGISTER = SERVER_HOST + "Member/registration";
    /**
     * 验证用户是否可以注册Member/checkuser
     */
    String REGISTER_CHECKUSER = SERVER_HOST + "Member/checkuser";
    /**
     * 发送验证码     Member/sendcode
     */
    String REGISTER_SENDCODE = SERVER_HOST + "Member/sendcode";
    /**
     * 发送验证码  Member/changepassword
     */
    String CHANGE_PASSWORD = SERVER_HOST + "Member/changepassword";
    /**
     * 资金详情列表   Member/FundingDetails
     */
    String MONEY_LIST = SERVER_HOST + "Member/FundingDetails";
    /**
     * 投资列表   Service/list
     */
    String Investment_LIST = SERVER_HOST + "Service/list";
    /**
     * 投资详情   Service/infor
     */
    String Investment_INFO = SERVER_HOST + "Service/infor";

    /**
     * 提交投资服务Service/Subinvest
     */
    String Investment_SUBINEVST = SERVER_HOST + "Service/Subinvest";

    /**
     * Member/Index 我的页面数据
     */
    String MYZOE_INDEX = SERVER_HOST + "Member/Index";
    /**
     * Member/Payback     回报明细
     */
    String RETURN_DETAILS_LIST = SERVER_HOST + "Member/Payback";

    /**
     * Service/Recharge 提现
     */
    String SERVICE_RECHARGE = SERVER_HOST + "Service/Recharge";
    /**
     * Member/Fundlist    回报列表
     */
    String MEMBER_FUNDLIST = SERVER_HOST + "Member/Fundlist";
    /**
     * Member/FindPassword     找回密码保存新密码
     */
    String FIND_PASSWORD = SERVER_HOST + "Member/FindPassword";
    /**
     * Member/infor    个人资料查询
     */
    String MEMBER_INFO = SERVER_HOST + "Member/infor";
    /**
     * Service/Transfer 股权转让
     */
    String SERVICE_TRANSFER = SERVER_HOST + "Service/Transfer";
    /**
     * Member/edinfor 修改个人资料
     */
    String MEMBER_EDINFOR = SERVER_HOST + "Member/edinfor";
    /**
     * Member/MyCode 邀请码
     */
    String MEMBER_MYCODE = SERVER_HOST + "Member/MyCode";
    /**
     * Member/Uploads 文件上传
     */
    String MEMBER_UPLOADS = SERVER_HOST + "Member/Uploads";
    /**
     * Service/withdraw 充值
     */
    String SERVICE_WITHDRAW = SERVER_HOST + "Service/withdraw";
    /**
     * Article/Sortlist新闻分类
     */
    String ARTICLE_SORTLIST = SERVER_HOST + "Article/Sortlist";
    /**
     * 意见反馈 Service/Messages
     */
    String SERVICE_MESSAGES = SERVER_HOST + "Service/Messages";

    /**
     * 倒计时的时间设定
     */
    long COUNT_DOWN_TIME = 30000;

    int LIST_LOAD_FIRST = 0;//默认第一次访问数据
    int LIST_REFRESH = 1;//刷新
    int LIST_LOAD_MORE = 2;//加载更多
    int LIST_REFRSH_SELCT = 3;//按搜索条件获取数据


}
