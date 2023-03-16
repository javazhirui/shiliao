package com.linghe.shiliao.mapper;

import com.linghe.shiliao.entity.Cases;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linghe.shiliao.entity.dto.CasesDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhao_qin
 * @since 2023-03-14
 */
@Mapper
public interface CasesMapper extends BaseMapper<Cases> {

    //获取总条数
    long getTotal();

    //多条件分页查询
    List<CasesDto> getList(@Param("phone") String phone, @Param("name") String name, @Param("health") String health, @Param("startSize") Integer startSize, @Param("pageSize") Integer pageSize);
}
