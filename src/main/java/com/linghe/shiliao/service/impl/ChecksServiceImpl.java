package com.linghe.shiliao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linghe.shiliao.common.R;
import com.linghe.shiliao.entity.Checks;
import com.linghe.shiliao.entity.dto.ChecksDto;
import com.linghe.shiliao.mapper.ChecksMapper;
import com.linghe.shiliao.service.ChecksService;
import com.linghe.shiliao.utils.Page;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhao_qin
 * @since 2023-03-14
 */
@Service
public class ChecksServiceImpl extends ServiceImpl<ChecksMapper, Checks> implements ChecksService {

    @Autowired
    private ChecksMapper checksMapper;

    /**
     * 新增预约
     *
     * @param checks
     * @return
     */
    @Override
    public R<String> addCheck(Checks checks) {
        if (ObjectUtils.isEmpty(checks)) {
            return R.error("预约数据为空");
        }
        boolean save = this.save(checks);
        if (!save) {
            return R.error("添加预约失败");
        }
        return R.success("添加预约成功");
    }

    @Override
    public R<String> updateCheck(Checks checks) {
        if (ObjectUtils.isEmpty(checks)) {
            return R.error("数据为空");
        }
        boolean update = this.updateById(checks);
        if (!update) {
            return R.error("处理预约失败");
        }
        return R.success("处理预约成功");
    }

    /**
     * 获取待处理预约信息
     *
     * @param checksDto
     * @return
     */
    @Override
    public R<Page<ChecksDto>> getChecklist(ChecksDto checksDto) {
        if (ObjectUtils.isEmpty(checksDto)) {
            return R.error("数据为空");
        }
        Page<ChecksDto> pageDto = new Page<>();
        pageDto.setCurrentPage(checksDto.getCurrentPage());
        pageDto.setPageSize(checksDto.getPageSize());
        checksDto.setStartSize((checksDto.getCurrentPage() - 1) * checksDto.getPageSize());
        pageDto.setTotal(checksMapper.getTotal());
        pageDto.setList(checksMapper.getCheckList(checksDto));
        return R.success(pageDto);
    }
}
