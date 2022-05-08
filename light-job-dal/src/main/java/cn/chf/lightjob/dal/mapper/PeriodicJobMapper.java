package cn.chf.lightjob.dal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.chf.lightjob.dal.base.BaseMapper;
import cn.chf.lightjob.dal.entity.PeriodicJobDO;
import cn.chf.lightjob.dal.qo.PeriodicJobQO;

@Mapper
public interface PeriodicJobMapper extends BaseMapper<PeriodicJobDO> {

    List<PeriodicJobDO> selectNowScheduleJob(@Param("preReadSecond") int preReadSecond, @Param("pagesize") int pagesize);

    int batchUpdateJob(@Param("list") List<PeriodicJobDO> list);

    int updateById(PeriodicJobDO job);

    List<PeriodicJobDO> queryAll();

    int clearInvalidData();

    PeriodicJobDO selectById(@Param("id") long id);

    int countByQuery(PeriodicJobQO qo);

    List<PeriodicJobDO> listByQuery(PeriodicJobQO qo);
}