package org.software.code.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.software.code.common.except.BusinessException;
import org.software.code.common.except.ExceptionEnum;
import org.software.code.common.util.RedisUtil;
import org.software.code.dto.AddBankCardDto;
import org.software.code.dto.SendCodeRequestDto;
import org.software.code.dto.VerifyBankCardCodeDto;
import org.software.code.entity.BankCard;
import org.software.code.mapper.CardsMapper;
import org.software.code.service.CardsService;
import org.software.code.vo.CardsShowVo;
import org.software.code.vo.VerifyCodeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CardsServiceImpl implements CardsService {
    private static final long CODE_EXPIRE_MINUTES = 5; //验证码过期时长
    private static final TimeUnit TIME_UNIT = TimeUnit.MINUTES; //验证码时间单位

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    CardsMapper cardsMapper;

    @Override
    public void addBankCard(AddBankCardDto addBankCardDto, Long userId) {
        BankCard bankCard = new BankCard();
        BeanUtils.copyProperties(addBankCardDto, bankCard);

        // 模拟从安全上下文或网关传入的用户ID（真实业务中应从 token 或上下文中获取）
        bankCard.setUserId(userId);
        bankCard.setBindTime(LocalDateTime.now());
        bankCard.setStatus(2); // 默认为非默认卡

        int rows = cardsMapper.insert(bankCard);
        if (rows <= 0) {
            log.error("添加银行卡失败，数据插入无效，银行卡号: {}", addBankCardDto.getCardNumber());
            throw new RuntimeException("银行卡添加失败，请稍后再试");
        }

        log.info("成功添加银行卡，用户ID: {}, 卡号: {}", userId, addBankCardDto.getCardNumber());
    }

    @Override
    public VerifyCodeVo sendBankCardVerifyCode(SendCodeRequestDto dto) {
        // 检查银行卡是否已存在
        LambdaQueryWrapper<BankCard> query = new LambdaQueryWrapper<>();
        query.eq(BankCard::getCardNumber, dto.getCardNumber());
        BankCard existing = cardsMapper.selectOne(query);
        if (existing != null) {
            throw new BusinessException(ExceptionEnum.CARD_ALREADY_BOUND);
        }

        // 生成6位验证码
        String code = String.valueOf((int) ((Math.random() * 900000) + 100000));

        // 构造 Redis key
        String redisKey = "verify:bankcard:" + dto.getPhone();

        // 存入 Redis，设置有效期

        redisUtil.setValue(redisKey, code);
        redisUtil.setExpire(redisKey, CODE_EXPIRE_MINUTES, TIME_UNIT);

        // 构造返回 VO
        Long expireTime =  CODE_EXPIRE_MINUTES * 60;
        return VerifyCodeVo.builder()
                .code(code)
                .expireTime(expireTime)
                .build();
    }

    @Override
    public void verifyBankCardCode(VerifyBankCardCodeDto dto) {
        String redisKey = "verify:bankcard:" + dto.getPhone();
        String codeInRedis = redisUtil.getValue(redisKey);

        if (codeInRedis == null) {
            throw new BusinessException(ExceptionEnum.VERIFY_CODE_EXPIRED); // 验证码过期
        }

        if (!codeInRedis.equals(dto.getVerifyCode())) {
            throw new BusinessException(ExceptionEnum.VERIFY_CODE_INVALID); // 验证码错误
        }

        // 校验成功后删除验证码，防止重用
        redisUtil.deleteValue(redisKey);
    }

    @Override
    public List<CardsShowVo> listUserCards(Long userId) {
        LambdaQueryWrapper<BankCard> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BankCard::getUserId, userId);

        List<BankCard> cards = cardsMapper.selectList(queryWrapper);

        return cards.stream().map(card -> {
            CardsShowVo vo = new CardsShowVo();
            vo.setId(card.getId());
            vo.setBankName(card.getBankName());
            vo.setType(card.getType());
            vo.setTypeName(card.getType() == 1 ? "储蓄卡" : "信用卡");
            vo.setStatus(card.getStatus());
            vo.setLastFourDigits(card.getLastFourDigits());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public void setDefaultCard(Long cardId) {
        // 查找银行卡记录
        BankCard card = cardsMapper.selectById(cardId);
        if (card == null) {
            throw new BusinessException(ExceptionEnum.DATA_NOT_FOUND); // 银行卡不存在
        }

        Long userId = card.getUserId();

        // 将该用户所有银行卡设为非默认
        cardsMapper.clearDefaultCard(userId);

        // 将当前银行卡设为默认
        card.setStatus(1); // 1 = 默认
        cardsMapper.updateById(card);
    }

    @Override
    public void unbindBankCard(Long id) {
        BankCard card = cardsMapper.selectById(id);
        if (card == null) {
            throw new BusinessException(ExceptionEnum.DATA_NOT_FOUND);
        }

        cardsMapper.deleteById(id);
    }

}
