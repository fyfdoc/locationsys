package com.dtm.locationsys;

import com.dtm.locationsys.msgDef.EquipmentVerifyReq;
import com.dtm.locationsys.msgDef.EquipmentVerifyRps;
import com.dtm.locationsys.utils.ByteConvert;
import com.dtm.locationsys.utils.SysLog;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * 登录验证测试
 *
 *
 */
public class EquipmentVerifyTest {
    @Test
    public void equipmentVerifyReqTest() {
        // 0xaaaaaaa1: 表示请求用户名密码验证;
        // 0xaaaaaaa2: 表示请求验证码验证;
        // 0xaaaaaaaa: 表示用户名密码和验证码同时验证;
        // Uint
        long msgFlag = 0xaaaaaaa1L;
        // char[32]
        char[] username = new char[32]; //用户名
        username[0] = 'f';
        username[1] = 'y';
        username[2] = 'f';

        // char[32]
        char[] password = new char[32]; //密码
        password[0] = '1';
        password[1] = '2';
        password[2] = '3';

        // char[16]
        char[] phoneNum = new char[16]; //待测手机号
        phoneNum[0] = '1';
        phoneNum[1] = '2';
        phoneNum[2] = '3';
        phoneNum[3] = '4';

//      long scrambleCode; //扰码

        // Uint
        long verifyCode = 66; //验证码


        EquipmentVerifyReq equipmentVerifyReq = new EquipmentVerifyReq(
                  msgFlag
                , username
                , password
                , phoneNum
                , verifyCode

        );

        System.out.println(equipmentVerifyReq.toString());

        EquipmentVerifyReq equipmentVerifyReq2 = new EquipmentVerifyReq(
                equipmentVerifyReq.getBuf());
        System.out.println(equipmentVerifyReq2.toString());

        System.out.println("equipmentVerifyReq.BUF_SIZE=" + equipmentVerifyReq.BUF_SIZE);

    }

    @Test
    public void EquipmentVerifyRpsTest() {
        // Uint
        long pad = 0L;

        // 返回验证结果, 0x00001111:表示用户名密码验证码都验证通过;0xffffffff表示验证失败
        // Uint
        long status = 0x00001111L;

        EquipmentVerifyRps equipmentVerifyRps = new EquipmentVerifyRps(
                  pad
                , status
        );

        System.out.println(equipmentVerifyRps.toString());

        EquipmentVerifyRps equipmentVerifyRps2 = new EquipmentVerifyRps(
                equipmentVerifyRps.getBuf());

        System.out.println(equipmentVerifyRps2.toString());
        System.out.println("equipmentVerifyRps2" + equipmentVerifyRps2.BUF_SIZE);
    }
}

