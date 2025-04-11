package cn.lili.modules.connect.config;


/**
 * 用户信息 枚举
 *
 * @author Chopper
 * @version v4.0
 * @since 2020/12/4 14:10
 */
public enum ConnectAuthEnum implements ConnectAuth {
    /**
     * 新浪微博
     */
    DEMO {
        @Override
        public String authorize() {
            return "https://api.weibo.com/oauth2/authorize";
        }

        @Override
        public String accessToken() {
            return "https://api.weibo.com/oauth2/access_token";
        }

        @Override
        public String userInfo() {
            return "https://api.weibo.com/2/users/show.json";
        }

    }

}
