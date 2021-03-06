package com.bestomb.sevice;

import com.bestomb.common.exception.EqianyuanException;
import com.bestomb.common.response.systemUser.SystemUserVo;
import com.bestomb.common.response.systemMenu.SystemMenuBo;
import com.bestomb.common.response.systemUser.SystemUserBo;
import com.bestomb.common.util.SessionUtil;
import com.bestomb.common.util.yamlMapper.SystemConf;
import com.bestomb.service.ISystemMenuService;
import com.bestomb.service.ISystemUserService;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统用户授权登录操作数据转换及业务主方法调用实现类
 * 主要功能接口
 * 登录
 * 登出
 * Created by jason on 2016-05-18.
 */
@Service
public class AuthorizationService {

    private Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private ISystemMenuService systemMenuService;

    /**
     * 带验证码的登录
     *
     * @param userName 用户名
     * @param password 密码
     * @param code     登录验证码
     * @throws EqianyuanException
     */
    public void login(String userName, String password, String code) throws EqianyuanException {
        SystemUserBo systemUserBo = systemUserService.login(userName, password, code);

        SystemUserVo systemUserVo = new SystemUserVo();
        BeanUtils.copyProperties(systemUserBo, systemUserVo);

        /**
         * 根据用户角色获取用户菜单集合
         */
        List<SystemMenuBo> systemMenuBos = systemMenuService.getListBySystemRole(systemUserBo.getRoleId());
        systemUserVo.setSystemMenuBos(systemMenuBos);

        /**
         * 将用户VO对象写入session
         */
        SessionUtil.setAttribute(SystemConf.SYSTEM_SESSION_USER.toString(), systemUserVo);
    }
}
