package cn.chf.lightjob.dal;

import java.net.URL;
import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.ShellRunner;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaElement;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.Context;

public class MbgGenerator extends PluginAdapter {

    public static void main(String[] args) {
        URL resUrl = MbgGenerator.class.getClassLoader().getResource("mybatis-generator/mybatis-generator.xml");
        String config = resUrl.getFile();
        String[] arg = {"-configfile", config, "-overwrite"};
        ShellRunner.main(arg);
    }

    private static final String BASE_MAPPER_TYPE = "BaseMapper";
    private static final String BASE_MODEL_TYPE = "BaseEntity";
    private static final String MAPPER_ANNOTATION = "@Mapper";
    private static final String MAPPER_IMPORT_TYPE = "org.apache.ibatis.annotations.Mapper";

    public MbgGenerator() {
        super();
    }

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        super.initialized(introspectedTable);
        // 实体类名后面追加DO
        introspectedTable.setBaseRecordType(introspectedTable.getBaseRecordType() + "DO");
    }

    @Override
    public void setContext(Context context) {
        super.setContext(context);
        // 支持mysql获取注释
        context.getJdbcConnectionConfiguration().addProperty("useInformationSchema", "true");
    }

    @Override
    public boolean clientGenerated(Interface interfaze,
                                   TopLevelClass topLevelClass,
                                   IntrospectedTable introspectedTable) {
        final String baseMapperClassPath = getClass().getPackage().getName() + ".base.BaseMapper";
        FullyQualifiedJavaType baseMapperType = new FullyQualifiedJavaType(BASE_MAPPER_TYPE + "<" + introspectedTable.getBaseRecordType() + ">");
        FullyQualifiedJavaType mapperAnnotationImportType = new FullyQualifiedJavaType(MAPPER_IMPORT_TYPE);
        FullyQualifiedJavaType baseMapperImportType = new FullyQualifiedJavaType(baseMapperClassPath);

        try {
            Class curr = Class.forName(interfaze.getType().getFullyQualifiedNameWithoutTypeParameters());
            if (curr != null) {
                // Java Mapper 类不覆盖
                return false;
            }
        } catch (ClassNotFoundException e) {
            // do nothing
        }

        interfaze.addSuperInterface(baseMapperType);
        interfaze.addAnnotation(MAPPER_ANNOTATION);
        interfaze.getMethods().clear();
        interfaze.addImportedType(mapperAnnotationImportType);
        interfaze.addImportedType(baseMapperImportType);
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass,
                                                 IntrospectedTable introspectedTable) {
        final String baseEntityClassPath = getClass().getPackage().getName() + ".base.BaseEntity";
        FullyQualifiedJavaType baseModelType = new FullyQualifiedJavaType(BASE_MODEL_TYPE);
        FullyQualifiedJavaType baseModelImportType = new FullyQualifiedJavaType(baseEntityClassPath);


        topLevelClass.addImportedType(baseModelImportType);
        topLevelClass.setSuperClass(baseModelType);

        topLevelClass.addImportedType("lombok.Setter");
        topLevelClass.addAnnotation("@Setter");
        topLevelClass.addImportedType("lombok.Getter");
        topLevelClass.addAnnotation("@Getter");

        //region 文档注释
        String tableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();
        String remarks = introspectedTable.getRemarks();
        topLevelClass.addJavaDocLine("/**");
        topLevelClass.addJavaDocLine(" * 表名：" + tableName);
        if (remarks != null) {
            remarks = remarks.trim();
        }
        if (remarks != null && remarks.trim().length() > 0) {
            String[] lines = remarks.split("\\r?\\n");
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                if (i == 0) {
                    topLevelClass.addJavaDocLine(" * 表注释：" + line);
                } else {
                    topLevelClass.addJavaDocLine(" *         " + line);
                }
            }
        }
        topLevelClass.addJavaDocLine(" */");
        return true;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        this.comment(field, introspectedTable, introspectedColumn);
        return true;
    }



    /**
     * 生成Getter注解，就不需要生成get相关代码了
     */
    @Override
    public boolean modelGetterMethodGenerated(Method method,
                                              TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
                                              IntrospectedTable introspectedTable,
                                              ModelClassType modelClassType)  {

        return false;
    }

    /**
     * 生成Setter注解，就不需要生成set相关代码了
     */
    @Override
    public boolean modelSetterMethodGenerated(Method method,
                                              TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
                                              IntrospectedTable introspectedTable,
                                              ModelClassType modelClassType) {
        return false;
    }

    private void comment(JavaElement element, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        element.getJavaDocLines().clear();
        element.addJavaDocLine("/**");
        String remark = introspectedColumn.getRemarks();
        if (remark != null && remark.length() > 1) {
            element.addJavaDocLine(" * " + remark);
            //element.addJavaDocLine(" *");
        }

        //element.addJavaDocLine(" * Table:     " + introspectedTable.getFullyQualifiedTable());
        //element.addJavaDocLine(" * Column:    " + introspectedColumn.getActualColumnName());
        element.addJavaDocLine(" * Nullable:  " + introspectedColumn.isNullable());
        element.addJavaDocLine(" */");
    }

    @Override
    public boolean sqlMapDeleteByPrimaryKeyElementGenerated(XmlElement element,
                                                            IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapInsertElementGenerated(XmlElement element,
                                                IntrospectedTable introspectedTable) { return false;
    }

    @Override
    public boolean sqlMapSelectByPrimaryKeyElementGenerated(XmlElement element,
                                                            IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapSelectAllElementGenerated(XmlElement element,
                                                   IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeySelectiveElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeyWithBLOBsElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapResultMapWithoutBLOBsElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable) {
        return true;
    }

    @Override
    public boolean sqlMapResultMapWithBLOBsElementGenerated(XmlElement element,
                                                            IntrospectedTable introspectedTable) {
        return true;
    }
}
