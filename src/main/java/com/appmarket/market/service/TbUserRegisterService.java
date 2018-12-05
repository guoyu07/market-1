package com.appmarket.market.service;

import com.appmarket.market.bean.TbUserRegister;

import java.util.List;

/**
 *  新申请用户
 */
public interface TbUserRegisterService {

    /**
     * 申请新用户
     * @param userRegister
     * @return
     */
    boolean add(TbUserRegister userRegister);


    List<TbUserRegister> list();

    /**
     * 新用户通过申请
     * @param id
     * @return
     */
    boolean passRegister(String id);
}
