package org.software.code.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.software.code.service.CardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "银行卡相关接口", description = "银行卡展示、添加、删除等操作")
@Validated
@RestController
@RequestMapping("/assets/cards")
public class CardsController {
    @Autowired
    private CardsService cardsService;


}
