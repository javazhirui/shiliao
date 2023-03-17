package com.linghe.shiliao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linghe.shiliao.common.R;
import com.linghe.shiliao.entity.Cases;
import com.linghe.shiliao.entity.dto.CasesDto;
import com.linghe.shiliao.mapper.CasesMapper;
import com.linghe.shiliao.service.CasesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linghe.shiliao.utils.Page;
import com.xxl.tool.excel.ExcelTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhao_qin
 * @since 2023-03-14
 */
@Service
public class CasesServiceImpl extends ServiceImpl<CasesMapper, Cases> implements CasesService {

    @Autowired
    private CasesMapper casesMapper;

    /**
     * 获取客户及病历信息
     *
     * @param phone
     * @param name
     * @param health
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public Page<CasesDto> getCaseList(String phone, String name, String health, Integer currentPage,
                                      Integer pageSize) {
        Integer startSize = null;
        if (null != currentPage && null != pageSize) {
            startSize = (currentPage - 1) * pageSize;
        }
        Page<CasesDto> pageDto = new Page<>();
        pageDto.setTotal(casesMapper.getTotal());
        pageDto.setList(casesMapper.getList(phone, name, health, startSize, pageSize));
        pageDto.setPageSize(pageSize);
        pageDto.setCurrentPage(currentPage);
        return pageDto;
    }

    /**
     * 新增病历信息
     *
     * @param cases
     * @return
     */
    @Override
    public R<String> addCases(Cases cases) {

        if (null == cases) {
            return R.error("数据无效,请重新输入");
        }

        //mybatis-plus自带save方法,成功返回true,反之false
        boolean b = this.save(cases);

        if (!b) {
            return R.error("添加失败");
        }
        return R.success("添加成功");
    }

    /**
     * 编辑修改客户/病历信息
     * @param cases
     * @return
     */
    @Override
    public R<String> updateCases(Cases cases) {

        if (null == cases) {
            return R.error("数据无效,请重新输入");
        }

        boolean b = this.updateById(cases);

        if (!b) {
            return R.error("修改失败");
        }

        return R.success("修改成功");
    }

    @Override
    public void ceshi() {
        LambdaQueryWrapper<Cases> lqw = new LambdaQueryWrapper<>();
//        lqw.orderByAsc(Cases::getCasesId);
//        List<Cases> list = this.list(lqw);
        List<CasesDto> list = casesMapper.getList("", "", "", 2, 100);
        ExcelTool.exportToFile(Collections.singletonList(list),"C:/Users/17966/Desktop/ceshi.xlsx");
    }
}
