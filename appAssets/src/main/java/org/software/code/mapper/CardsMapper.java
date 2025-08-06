package org.software.code.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.software.code.entity.BankCard;

@Mapper
public interface CardsMapper extends BaseMapper<BankCard> {
    void clearDefaultCard(Long userId);

    @Update("update bank_card set status = 1 where id = #{cardId}" )
    void updateStatus(Long cardId);
}
