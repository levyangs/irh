package top.imuster.user.provider.dao;


import top.imuster.common.base.dao.BaseDao;
import top.imuster.user.api.pojo.WxAppLoginInfo;

/**
 * WxAppLoginDao 接口
 * @author 黄明人
 * @since 2020-05-19 15:28:28
 */
public interface WxAppLoginDao extends BaseDao<WxAppLoginInfo, Long> {
    //自定义扩展
    /**
     * @Author hmr
     * @Description
     * @Date: 2020/5/19 15:35
     * @param openId
     * @reture: java.lang.Long
     **/
    Long selectUserIdByOpenId(String openId);

}