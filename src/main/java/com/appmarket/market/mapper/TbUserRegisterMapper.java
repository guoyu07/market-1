package com.appmarket.market.mapper;

import com.appmarket.market.bean.TbUserRegister;

import java.util.List;

public interface TbUserRegisterMapper {

    int add(TbUserRegister userRegister);

    List<TbUserRegister> list();

    int update(TbUserRegister userRegister);

}
