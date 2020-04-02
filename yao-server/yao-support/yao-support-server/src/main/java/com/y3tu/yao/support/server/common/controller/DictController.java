package com.y3tu.yao.support.server.common.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.y3tu.tool.web.base.pojo.Page;
import com.y3tu.tool.core.pojo.R;

import com.y3tu.yao.support.client.common.entity.Dict;
import com.y3tu.yao.support.client.common.entity.DictData;
import com.y3tu.yao.support.client.common.service.DictDataService;
import com.y3tu.yao.support.client.common.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * 字典Controller
 *
 * @author y3tu
 */
@RestController
@RequestMapping("/dict")
@Slf4j
public class DictController {
    @Autowired
    private DictService dictService;

    @Autowired
    private DictDataService dictDataService;

    @PostMapping("/dictPage")
    public R dictPage(@RequestBody Page Page) {
        return R.success(dictService.findDictPage(Page));
    }

    @PostMapping("/dictDataPage")
    public R dictDataPage(@RequestBody Page Page) {
        return R.success(dictDataService.findDictDataPage(Page));
    }

    @PostMapping("/createDict")
    public R createDict(@Valid Dict dict) {
        Dict dictOne = dictService.getOne(new LambdaQueryWrapper<Dict>().eq(Dict::getCode, dict.getCode()));
        if (dictOne != null) {
            return R.error("字典编码已存在");
        }
        dictService.createDict(dict);
        return R.success();
    }

    @PostMapping("/createDictData")
    public R createDictData(@Valid DictData dictData) {
        Dict dict = dictService.getById(dictData.getDictId());
        if (dict == null) {
            return R.error("字典类型id不存在");
        }
        dictDataService.createDictData(dictData);
        return R.success();
    }

    @PutMapping("/updateDict")
    public R updateDict(@Valid Dict dict) {
        Dict old = dictService.getById(dict.getId());
        // 若type修改判断唯一
        if (!old.getCode().equals(dict.getCode()) && dictService.list(new LambdaQueryWrapper<Dict>().eq(Dict::getCode, dict.getCode())).size() > 0) {
            return R.error("字典编码已存在");
        }
        dictService.updateDict(dict);
        return R.success();
    }

    @PutMapping("/updateDictData")
    public R updateDictData(@Valid DictData dictData) {
        dictDataService.updateDictData(dictData);
        return R.success();
    }

    @DeleteMapping("/deleteDict")
    public R deleteDict(@RequestBody String[] ids) {
        dictService.deleteDict(ids);
        return R.success();
    }

    @DeleteMapping("/deleteDictData")
    public R deleteDictData(@RequestBody String[] ids) {
        dictDataService.deleteDictData(ids);
        return R.success();
    }


    @GetMapping("/getDataByDictCode/{code}")
    public R getDictDataByCode(@PathVariable String code) {
        Dict dict = dictService.getOne(new LambdaQueryWrapper<Dict>().eq(Dict::getCode, code));
        if (dict == null) {
            return R.error("字典编码不存在");
        }
        return R.success(dictDataService.list(new LambdaQueryWrapper<DictData>().eq(DictData::getDictId, dict.getId())));
    }

    @GetMapping("/getDataByDictName/{name}")
    public R getDictDataByDictName(@PathVariable String name){
        Dict dict = dictService.getOne(new LambdaQueryWrapper<Dict>().eq(Dict::getName, name));
        if (dict == null) {
            return R.error("字典编码不存在");
        }
        return R.success(dictDataService.list(new LambdaQueryWrapper<DictData>().eq(DictData::getDictId, dict.getId())));
    }


    @GetMapping("/search")
    public R search(@RequestParam String key) {
        List<Dict> list = dictService.list(new QueryWrapper<Dict>().like("title", key).or().like("code", key));
        return R.success(list);
    }


}
