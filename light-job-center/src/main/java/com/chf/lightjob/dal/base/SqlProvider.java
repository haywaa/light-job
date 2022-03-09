package com.chf.lightjob.dal.base;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.annotations.Param;

public class SqlProvider {

    public String insert(Object bean) {
        Class<?> beanClass = bean.getClass();
        String tableName = getTableName(beanClass);
        Field[] fields = getFields(beanClass);
        StringBuilder insertSql = new StringBuilder();
        List<String> insertParas = new ArrayList<>();
        List<String> insertParaNames = new ArrayList<>();
        insertSql.append("INSERT INTO `").append(tableName).append("` (");
        try {
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                String columnName = UnderlineHumpUtil.HumpToUnderline(field.getName());
                field.setAccessible(true);
                Object object = field.get(bean);
                if (object != null) {
                    insertParaNames.add(columnName);
                    insertParas.add("#{" + field.getName() + "}");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("get insert sql is exception ", e);
        }
        for (int i = 0; i < insertParaNames.size(); i++) {
            insertSql.append("`").append(insertParaNames.get(i)).append("`");
            if (i != insertParaNames.size() - 1) {
                insertSql.append(",");
            }
        }
        insertSql.append(")").append(" VALUES(");
        for (int i = 0; i < insertParas.size(); i++) {
            insertSql.append(insertParas.get(i));
            if (i != insertParas.size() - 1) {
                insertSql.append(",");
            }
        }
        insertSql.append(")");
        return insertSql.toString();
    }

    public String batchInsert(Map map) {
        List<Object> beans = (List<Object>) map.get("list");
        Object bean = beans.get(0);
        Class<?> beanClass = bean.getClass();
        String tableName = getTableName(beanClass);
        Field[] fields = getFields(beanClass);
        StringBuilder insertSql = new StringBuilder();
        List<String> insertParas = new ArrayList<>();
        List<String> insertParaNames = new ArrayList<>();
        insertSql.append("INSERT INTO `").append(tableName).append("`(");
        try {
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                String columnName = UnderlineHumpUtil.HumpToUnderline(field.getName());
                field.setAccessible(true);
                insertParaNames.add(columnName);
                insertParas.add("#{list[index]." + field.getName() + "}");
            }
        } catch (Exception e) {
            throw new RuntimeException("get insert sql is exception:" + e);
        }
        for (int i = 0; i < insertParaNames.size(); i++) {
            insertSql.append("`").append(insertParaNames.get(i)).append("`");
            if (i != insertParaNames.size() - 1) {
                insertSql.append(",");
            }
        }
        insertSql.append(")").append(" VALUES");

        //设值模板
        StringBuilder valueSqlTmpl = new StringBuilder();
        valueSqlTmpl.append("(");
        for (int i = 0; i < insertParas.size(); i++) {
            valueSqlTmpl.append(insertParas.get(i));
            if (i != insertParas.size() - 1) {
                valueSqlTmpl.append(",");
            }
        }
        valueSqlTmpl.append(")");

        for (int i = 0; i < beans.size(); i++) {
            if (i > 0) {
                insertSql.append(",");
            }
            insertSql.append(valueSqlTmpl.toString().replaceAll("index", String.valueOf(i)));
        }

        return insertSql.toString();
    }

    public String update(Object bean) {
        Class<?> beanClass = bean.getClass();
        String tableName = getTableName(beanClass);
        Field[] fields = getFields(beanClass);
        StringBuilder updateSql = new StringBuilder();
        //update field
        updateSql.append(" UPDATE `").append(tableName).append("` SET ");
        appendUpdateFieldSql(fields, updateSql,bean);
        updateSql.append(" WHERE ").append("id =#{id}");
        return updateSql.toString();
    }


    /**
     * 解决myCat 场景非全局唯一Id
     *
     * @param bean 更新字段  beanCondition 更新条件
     * @return sql
     */
    public String updateByCondition(@Param("record") Object bean, @Param("condition") Object beanCondition) {
        Class<?> beanClass = bean.getClass();
        String tableName = getTableName(beanClass);
        Field[] fields = getFields(beanClass);
        StringBuilder updateSql = new StringBuilder();
        updateSql.append(" UPDATE `").append(tableName).append("` SET ");
        //update field
        appendUpdateFieldSql(fields, updateSql,bean, "record");
        //where field
        updateSql.append(" WHERE ");
        Class<?> beanConditionClass = beanCondition.getClass();
        Field[] beanConditionFields = getFields(beanConditionClass);
        appendWhereFieldSql(beanConditionFields, updateSql,beanCondition, "condition");

        return updateSql.toString();
    }


    public String updateForce(Object bean) {
        Class<?> beanClass = bean.getClass();
        String tableName = getTableName(beanClass);
        Field[] fields = getFields(beanClass);
        StringBuilder updateSql = new StringBuilder();
        updateSql.append(" UPDATE `").append(tableName).append("` set ");
        appendUpdateFieldSql(fields, updateSql,bean);
        updateSql.append(" WHERE ").append("id =#{id}");
        return updateSql.toString();
    }

    public String delete(Object bean) {
        Class<?> beanClass = bean.getClass();
        String tableName = getTableName(beanClass);
        StringBuilder deleteSql = new StringBuilder();
        deleteSql.append(" DELETE FROM `").append(tableName).append("` WHERE id =#{id} ");

        return deleteSql.toString();
    }

    public String deleteByRecord(Object bean) {
        Class<?> beanClass = bean.getClass();
        String tableName = getTableName(beanClass);
        //Field[] fields = getFields(beanClass);
        StringBuilder deleteSql = new StringBuilder();
        deleteSql.append(" DELETE FROM `").append(tableName).append("` WHERE  ");

        //where field
        Class<?> beanConditionClass = bean.getClass();
        Field[] beanConditionFields = getFields(beanConditionClass);
        appendWhereFieldSql(beanConditionFields, deleteSql,bean);

        return deleteSql.toString();
    }

    public String find(Object bean) {
        Class<?> beanClass = bean.getClass();
        String tableName = getTableName(beanClass);
        Field[] fields = getFields(beanClass);
        StringBuilder selectSql = new StringBuilder();
        List<String> selectParaNames = new ArrayList<>();
        List<String> selectParas = new ArrayList<>();
        selectSql.append("SELECT ");
        try {
            processField(fields, selectSql, bean, selectParaNames, selectParas);
        } catch (Exception e) {
            new RuntimeException("get select sql is exception ", e);
        }
        selectSql.append(" FROM `").append(tableName).append("`");

        processWhere(selectSql, selectParaNames, selectParas);

        return selectSql.toString();
    }

    public String findAll(Object bean) {
        Class<?> beanClass = bean.getClass();
        String tableName = getTableName(beanClass);
        Field[] fields = getFields(beanClass);
        StringBuilder selectSql = new StringBuilder();
        List<String> selectParaNames = new ArrayList<>();
        List<String> selectParas = new ArrayList<>();
        selectSql.append("SELECT ");
        try {
            processField(fields, selectSql, bean, selectParaNames, selectParas);
        } catch (Exception e) {
            throw new RuntimeException("get all select sql is exception ", e);
        }
        selectSql.append(" FROM `").append(tableName).append("`");

        processWhere(selectSql, selectParaNames, selectParas);

        return selectSql.toString();
    }

    public String countByRecord(Object bean) {
        Class<?> beanClass = bean.getClass();
        String tableName = getTableName(beanClass);
        Field[] fields = getFields(beanClass);
        StringBuilder fieldSql = new StringBuilder();
        StringBuilder selectSql = new StringBuilder();
        List<String> selectParaNames = new ArrayList<>();
        List<String> selectParas = new ArrayList<>();
        selectSql.append("SELECT count(*) ");
        try {
            processField(fields, fieldSql, bean, selectParaNames, selectParas);
        } catch (Exception e) {
            throw new RuntimeException("get select sql is exception ", e);
        }
        selectSql.append(" FROM `").append(tableName).append("`");

        processWhere(selectSql, selectParaNames, selectParas);

        return selectSql.toString();
    }
    /**
     * where value 条件拼接
     *
     */
    private void appendWhereFieldValueSql(Field[] beanConditionFields, StringBuilder updateSql, Object beanCondition) {
        try {
            Boolean setField = false;
            for (int i = 0; i < beanConditionFields.length; i++) {
                Field field = beanConditionFields[i];
                String columnName = UnderlineHumpUtil.HumpToUnderline(field.getName());
                field.setAccessible(true);
                Object beanValue = field.get(beanCondition);
                if (beanValue != null) {
                    if (setField) {
                        updateSql.append(" AND ");
                    }
                    updateSql.append("`").append(columnName).append("`").append("=\"").append(beanValue).append("\"");
                    setField = true;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("get where field sql is exception ", e);
        }
    }

    private void appendWhereFieldSql(Field[] beanConditionFields, StringBuilder updateSql, Object beanCondition) {
        appendWhereFieldSql(beanConditionFields, updateSql, beanCondition, null);
    }

    /**
     * where 条件拼接
     *
     */
    private void appendWhereFieldSql(Field[] beanConditionFields, StringBuilder updateSql, Object beanCondition, String beanPrefix) {
        try {
            Boolean setField = false;
            for (int i = 0; i < beanConditionFields.length; i++) {
                Field field = beanConditionFields[i];
                String columnName = UnderlineHumpUtil.HumpToUnderline(field.getName());
                field.setAccessible(true);
                Object beanValue = field.get(beanCondition);
                if (beanValue != null) {
                    if (setField) {
                        updateSql.append(" AND ");
                    }
                    updateSql.append(columnName).append("=#{");
                    if (beanPrefix != null && beanPrefix.length() > 0) {
                        updateSql.append(beanPrefix).append(".");
                    }
                    updateSql.append(field.getName()).append("}");
                    setField = true;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("get where field sql is exception ", e);
        }
    }


    /**
     * update value字段拼接
     *
     */
    private void appendUpdateFieldValueSql(Field[] fields, StringBuilder updateSql, Object bean) {
        try {
            Boolean setField = false;
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                String columnName = UnderlineHumpUtil.HumpToUnderline(field.getName());
                field.setAccessible(true);
                Object beanValue = field.get(bean);
                if (beanValue != null) {
                    if (setField) {
                        updateSql.append(",");
                    }
                    updateSql.append(columnName).append("=\"").append(beanValue).append("\"");
                    setField = true;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("get update field sql is exception ", e);
        }
    }

    /**
     * update 字段拼接
     *
     */
    private void appendUpdateFieldSql(Field[] fields, StringBuilder updateSql, Object bean) {
        appendUpdateFieldSql(fields, updateSql, bean, null);
    }

    private void appendUpdateFieldSql(Field[] fields, StringBuilder updateSql, Object bean, String beanPrefix) {
        try {
            Boolean setField = false;
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                String columnName = UnderlineHumpUtil.HumpToUnderline(field.getName());
                field.setAccessible(true);
                Object beanValue = field.get(bean);
                if (beanValue != null) {
                    if (setField) {
                        updateSql.append(",");
                    }
                    updateSql.append("`").append(columnName).append("`=#{");
                    if (beanPrefix != null && beanPrefix.length() > 0) {
                        updateSql.append(beanPrefix).append(".");
                    }
                    updateSql.append(field.getName()).append("}");
                    setField = true;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("get update field sql is exception ", e);
        }
    }

    private void processField(Field[] fields, StringBuilder selectSql, Object bean, List<String> selectParaNames, List<String> selectParas) throws IllegalAccessException {
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];

            String columnName = UnderlineHumpUtil.HumpToUnderline(field.getName());

            field.setAccessible(true);
            Object object = field.get(bean);
            selectSql.append("`").append(columnName).append("` `").append(field.getName()).append("`");
            if (object != null) {
                selectParaNames.add(columnName);
                selectParas.add("#{" + field.getName() + "}");
            }
            if (i != fields.length - 1) {
                selectSql.append(",");
            }
        }
    }

    private void processWhere(StringBuilder selectSql, List<String> selectParaNames, List<String> selectParas) {
        if (selectParaNames == null || selectParaNames.size() == 0) {
            throw new RuntimeException("processWhere no param exception");
        }
        for (int i = 0; i < selectParaNames.size(); i++) {
            if (i == 0) {
                selectSql.append(" WHERE ");
            }
            selectSql.append("`").append(selectParaNames.get(i)).append("`").append("=").append(selectParas.get(i));
            if (i != selectParaNames.size() - 1) {
                selectSql.append(" AND ");
            }
        }
    }

    private String getTableName(Class<?> beanClass) {
        String tableName = UnderlineHumpUtil.HumpToUnderline(beanClass.getSimpleName().replaceFirst("DO$", ""));
        return tableName;
    }

    private Field[] getFields(Class<?> beanClass) {
        Field[] beanFields = beanClass.getDeclaredFields();
        Class<?> beanSuperClass = beanClass.getSuperclass();
        Field[] beanSuperFields = beanSuperClass.getDeclaredFields();
        return ArrayUtils.addAll(beanFields, beanSuperFields);
    }
}
