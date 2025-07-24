package org.software.code.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.software.code.common.result.Result;
import org.software.code.dto.AddBankCardDto;
import org.software.code.dto.SendCodeRequestDto;
import org.software.code.dto.VerifyBankCardCodeDto;
import org.software.code.service.CardsService;
import org.software.code.vo.CardsShowVo;
import org.software.code.vo.VerifyCodeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Tag(name = "银行卡相关接口", description = "银行卡展示、添加、删除等操作")
@Validated
@RestController
@RequestMapping("/assets/cards")
public class CardsController {
    @Autowired
    private CardsService cardsService;

    /**
     * 添加银行卡
     *
     * @param addBankCardDto 添加银行卡的请求体
     * @return 操作结果
     */
    @Operation(summary = "添加银行卡", description = "提交持卡人姓名、银行预留手机号、银行名称、银行卡号和类型信息")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "银行卡添加成功"),
            @ApiResponse(responseCode = "400", description = "请求参数不合法"),
            @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping
    public Result<?> addBankCard(
            @RequestHeader("X-User-Id") Long uid,
            @RequestBody AddBankCardDto addBankCardDto
            ) {
        cardsService.addBankCard(addBankCardDto, uid);
        return Result.success("银行卡添加成功");
    }

    /**
     * 发送银行卡验证码
     *
     * @param dto 发送银行卡验证码的请求体
     * @return 操作结果
     */
    @Operation(summary = "发送银行卡验证码", description = "仅在银行卡未被添加时发送短信验证码")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "验证码发送成功"),
            @ApiResponse(responseCode = "400", description = "银行卡已存在或参数无效")
    })
    @PostMapping("/sendverifyCode")
    public Result<VerifyCodeVo> sendBankCardCode(@Valid @RequestBody SendCodeRequestDto dto) {
        VerifyCodeVo codeVo = cardsService.sendBankCardVerifyCode(dto);
        return Result.success(codeVo);
    }

    /**
     * 校验银行卡验证码
     *
     * @param dto 校验银行卡验证码的请求体
     * @return 操作结果
     */
    @Operation(summary = "校验银行卡验证码", description = "在确认添加银行卡前验证短信验证码是否正确")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "验证码校验通过"),
            @ApiResponse(responseCode = "400", description = "验证码错误或已过期")
    })
    @PostMapping("/verifyCode")
    public Result<?> verifyBankCardCode(@Valid @RequestBody VerifyBankCardCodeDto dto) {
        cardsService.verifyBankCardCode(dto);
        return Result.success("验证码校验通过");
    }

    /**
     * 展示银行卡
     *
     * @param uid Header中的uid
     * @return 操作结果
     */
    @Operation(summary = "展示用户银行卡列表", description = "根据请求头中的 Token 查询用户绑定的所有银行卡信息")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "银行卡展示成功"),
            @ApiResponse(responseCode = "401", description = "Token无效或已过期")
    })
    @GetMapping
    public Result<List<CardsShowVo>> listUserBankCards(
            @Parameter(description = "Bearer 类型 Token携带的uid", required = true)
            @RequestHeader("X-User-Id") Long uid) {

        List<CardsShowVo> cardList = cardsService.listUserCards(uid);
        return Result.success(cardList);
    }

    /**
     * 展示银行卡
     *
     * @param id bank_card对应的银行卡标识
     * @return 操作结果
     */
    @Operation(summary = "设置默认银行卡", description = "通过卡片ID设置为当前用户默认卡")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "设置成功"),
            @ApiResponse(responseCode = "400", description = "请求参数错误"),
            @ApiResponse(responseCode = "404", description = "银行卡不存在")
    })
    @PutMapping("/default")
    public Result<?> setDefaultCard(
            @Parameter(description = "银行卡ID", required = true, example = "12345")
            @NotNull @RequestParam Long id) {
        cardsService.setDefaultCard(id);
        return Result.success("设置默认银行卡成功");
    }

    /**
     * 解绑银行卡
     *
     * @param id bank_card对应的银行卡标识
     * @return 操作结果
     */
    @Operation(summary = "解绑银行卡", description = "根据银行卡ID解绑用户银行卡")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "银行卡删除成功"),
            @ApiResponse(responseCode = "404", description = "银行卡不存在或已解绑")
    })
    @DeleteMapping
    public Result<?> unbindBankCard(
            @Parameter(description = "银行卡ID", required = true, example = "12345")
            @RequestParam Long id
    ) {
        cardsService.unbindBankCard(id);
        return Result.success("银行卡删除成功");
    }
}
