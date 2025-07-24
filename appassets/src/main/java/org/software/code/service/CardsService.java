package org.software.code.service;

import org.software.code.dto.AddBankCardDto;
import org.software.code.dto.SendCodeRequestDto;
import org.software.code.dto.VerifyBankCardCodeDto;
import org.software.code.vo.CardsShowVo;
import org.software.code.vo.VerifyCodeVo;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface CardsService {

    /**
     * 添加银行卡
     *
     * @param addBankCardDto 添加银行卡请求数据
     * @param uid
     */
    void addBankCard(AddBankCardDto addBankCardDto, Long uid);

    /**
     * 银行卡验证码发送
     *
     * @param dto 银行卡验证码发送请求数据
     */
    VerifyCodeVo sendBankCardVerifyCode(@Valid SendCodeRequestDto dto);

    /**
     * 添加银行卡
     *
     * @param dto 银行卡验证码验证请求数据
     */
    void verifyBankCardCode(@Valid VerifyBankCardCodeDto dto);

    /**
     * 展示银行卡
     *
     * @param uid 用户的id
     */
    List<CardsShowVo> listUserCards(Long uid);

    /**
     * 展示银行卡
     *
     * @param id 银行卡的id
     */
    void setDefaultCard(@NotNull Long id);

    /**
     * 解绑银行卡
     *
     * @param id 银行卡的id
     */
    void unbindBankCard(Long id);
}
