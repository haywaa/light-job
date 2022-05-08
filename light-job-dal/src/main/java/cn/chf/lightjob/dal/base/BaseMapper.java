package cn.chf.lightjob.dal.base;


import java.util.List;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface BaseMapper<T extends BaseEntity> {

    @InsertProvider(type = SqlProvider.class, method = "insert")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insert(T record);

    @InsertProvider(type = SqlProvider.class, method = "batchInsert")
    int batchInsert(@Param("list") List<T> list);

    @SelectProvider(type = SqlProvider.class, method = "find")
    T selectByRecord(T record);

    @SelectProvider(type = SqlProvider.class, method = "findAll")
    List<T> selectAll(T record);

    @UpdateProvider(type = SqlProvider.class, method = "update")
    int updateByPrimaryKey(T record);

    @UpdateProvider(type = SqlProvider.class, method = "updateByCondition")
    int updateByCondition(@Param("record") T record, @Param("condition") T condition);

    @SelectProvider(type = SqlProvider.class, method = "countByRecord")
    int countByRecord(T record);

    @DeleteProvider(type = SqlProvider.class, method = "delete")
    int deleteByPrimaryKey(T record);

    @DeleteProvider(type = SqlProvider.class, method = "deleteByRecord")
    int deleteByRecord(T record);

}