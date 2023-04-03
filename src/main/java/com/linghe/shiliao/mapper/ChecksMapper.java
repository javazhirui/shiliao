package com.linghe.shiliao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linghe.shiliao.entity.Checks;
import com.linghe.shiliao.entity.dto.ChecksDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author zhao_qin
 * @since 2023-03-14
 */
@Mapper
public interface ChecksMapper extends BaseMapper<Checks> {

    /**
     * 获取待处理预约信息
     *
     * @param checksDto
     * @return
     */
    List<ChecksDto> getCheckList(ChecksDto checksDto);

    /**
     * 待处理预约信息-总条数
     *
     * @return
     */
    long getTotal();

}
