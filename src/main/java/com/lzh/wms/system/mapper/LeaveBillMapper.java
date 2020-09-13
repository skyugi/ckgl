package com.lzh.wms.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzh.wms.system.domain.LeaveBill;
import com.lzh.wms.system.vo.LeaveBillVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 刘样
 * @since 2020-03-21
 */
public interface LeaveBillMapper extends BaseMapper<LeaveBill> {
    int deleteByPrimaryKey(Integer id);

//    @Override
//    int insert(LeaveBill record);

    /**
     * leavetime类型应该要改成datetime
     * @param record
     * @return
     */
    int insertSelective(LeaveBill record);

    LeaveBill selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LeaveBill record);

    int updateByPrimaryKey(LeaveBill record);

    List<LeaveBill> queryAllLeaveBill(LeaveBillVo leaveBillVo);
}
