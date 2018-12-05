package com.appmarket.market.service.impl;

import com.appmarket.market.bean.TbUserRegister;
import com.appmarket.market.mapper.TbUserRegisterMapper;
import com.appmarket.market.service.TbUserRegisterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TbUserRegisterServiceImpl implements TbUserRegisterService {

    @Autowired
    TbUserRegisterMapper tbUserRegisterMapper;

    @Override
    public boolean add(TbUserRegister userRegister) {
        if(StringUtils.isNotBlank(userRegister.getEmail())
                && StringUtils.isNotBlank(userRegister.getName())
                && StringUtils.isNotBlank(userRegister.getPassword())) {

            userRegister.setCreateTime(new Date());
            userRegister.setStatus(0);
            tbUserRegisterMapper.add(userRegister);
            return true;

        } else
            return false;
    }

    @Override
    public List<TbUserRegister> list() {
        return null;
    }

    @Override
    public boolean passRegister(String id) {
        return false;
    }
}
