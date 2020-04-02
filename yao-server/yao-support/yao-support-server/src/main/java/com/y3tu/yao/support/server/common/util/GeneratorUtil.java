package com.y3tu.yao.support.server.common.util;

import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import com.y3tu.tool.core.db.DataTypeEnum;
import com.y3tu.tool.core.exception.ToolException;
import com.y3tu.tool.core.io.IoUtil;
import com.y3tu.tool.core.util.CharsetUtil;
import com.y3tu.tool.core.util.StrUtil;
import com.y3tu.yao.support.client.common.entity.GeneratorConfig;
import com.y3tu.yao.support.client.common.pojo.ColumnInfo;
import com.y3tu.yao.support.client.common.pojo.TableInfo;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.StringWriter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成工具类
 *
 * @author y3tu
 */
@Slf4j
public class GeneratorUtil {

    private static final String ENTITY_JAVA_VM = "Entity.java.vm";
    private static final String MAPPER_JAVA_VM = "Mapper.java.vm";
    private static final String SERVICE_JAVA_VM = "Service.java.vm";
    private static final String SERVICE_IMPL_JAVA_VM = "ServiceImpl.java.vm";
    private static final String CONTROLLER_JAVA_VM = "Controller.java.vm";
    private static final String MAPPER_XML_VM = "Mapper.xml.vm";
    private static final String INDEX_VUE_VM = "index.vue.vm";
    private static final String FORM_VUE_VM = "form.vue.vm";

    /**
     * 获取后端代码模板名称
     */
    private static List<String> getBackTemplateNames() {
        List<String> templateNames = new ArrayList<>();
        templateNames.add("Entity.java");
        templateNames.add("Mapper.java");
        templateNames.add("Mapper.xml");
        templateNames.add("Service.java");
        templateNames.add("ServiceImpl.java");
        templateNames.add("Controller.java");
        return templateNames;
    }

    /**
     * 获取前端代码模板名称
     */
    private static List<String> getFrontTemplateNames() {
        List<String> templateNames = new ArrayList<>();
        templateNames.add("index.vue");
        templateNames.add("form.vue");
        return templateNames;
    }

    /**
     * 获取模板数据
     */
    private static Map<String, Object> getGenMap(TableInfo tableInfo, GeneratorConfig genConfig) {
        //是否有BigDecimal类型
        boolean hasBigDecimal = false;
        //是否有date类型
        boolean hasDate = false;

        Map<String, Object> map = new HashMap();
        map.put("package", genConfig.getPack());
        map.put("moduleName", genConfig.getModuleName());
        map.put("author", genConfig.getAuthor());
        map.put("tableName", tableInfo.getTableName());
        map.put("apiPrefix", genConfig.getApiPrefix());
        if (StrUtil.isNotEmpty(tableInfo.getRemarks())) {
            map.put("remarks", tableInfo.getRemarks());
        } else {
            map.put("remarks", tableInfo.getTableName());
        }

        String className = StrUtil.toCamelCase(tableInfo.getTableName());
        className = StrUtil.upperFirst(className);
        String caseClassName = StrUtil.toCamelCase(tableInfo.getTableName());

        // 判断是否去除表前缀
        if (StrUtil.isNotEmpty(genConfig.getPrefix())) {
            caseClassName = StrUtil.toCamelCase(StrUtil.removePrefix(tableInfo.getTableName(), genConfig.getPrefix()));
            className = StrUtil.upperFirst(caseClassName);
        }

        map.put("className", className);
        map.put("caseClassName", caseClassName);

        List<Map<String, Object>> columns = new ArrayList<>();
        for (ColumnInfo column : tableInfo.getColumns()) {
            Map<String, Object> columnMap = new HashMap();

            if (StrUtil.isNotEmpty(column.getComment())) {
                columnMap.put("comment", column.getComment());
            } else {
                columnMap.put("comment", column.getName());
            }

            //列的数据类型，转换成Java类型
            String attrType = DataTypeEnum.getJavaType(column.getTypeName());
            if (!hasBigDecimal && "BigDecimal".equals(attrType)) {
                hasBigDecimal = true;
            }
            if (!hasDate && "Date".equals(attrType)) {
                hasDate = true;
            }

            String caseColumnName = StrUtil.toCamelCase(column.getName());
            String capitalColumnName = StrUtil.upperFirst(caseClassName);

            columnMap.put("columnType", attrType);
            columnMap.put("columnName", column.getName());
            columnMap.put("isNullable", column.isNullable());
            columnMap.put("enableShow", column.isEnableShow());
            columnMap.put("enableEdit", column.isEnableEdit());
            columnMap.put("formType", column.getFormType());
            columnMap.put("enableValidate", column.isEnableValidate());
            columnMap.put("enableSearch", column.isEnableSearch());
            columnMap.put("searchType", column.getSearchType());
            columnMap.put("enableSort", column.isEnableSort());

            columnMap.put("caseColumnName", caseColumnName);
            columnMap.put("capitalColumnName", capitalColumnName);

            //判断是否为主键
            if (column.getName().equals(tableInfo.getPk().getName())) {
                map.put("pk", columnMap);
            }
            columns.add(columnMap);
        }

        map.put("hasBigDecimal", hasBigDecimal);
        map.put("hasDate", hasDate);
        map.put("columns", columns);
        return map;
    }

    /**
     * 代码生成预览
     *
     * @param tableInfo 表信息，包括字段数据
     * @param genConfig 生成代码的参数配置，如包路径，作者
     * @return 生成的代码字符串
     */
    public static List<Map<String, Object>> genPreview(TableInfo tableInfo, GeneratorConfig genConfig) {
        Map genMap = getGenMap(tableInfo, genConfig);
        List<Map<String, Object>> genList = new ArrayList<>();
        //创建模板引擎
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig(null, TemplateConfig.ResourceMode.CLASSPATH));

        //生成后端代码
        List<String> templates = getBackTemplateNames();
        for (String templateName : templates) {
            Map<String, Object> map = new HashMap<>(1);
            Template template = engine.getTemplate("generator/back/" + templateName + ".vm");
            map.put("content", template.render(genMap));
            map.put("name", templateName);
            genList.add(map);
        }

        //生成前端代码
        templates = getFrontTemplateNames();
        for (String templateName : templates) {
            Map<String, Object> map = new HashMap<>(1);
            Template template = engine.getTemplate("generator/front/" + templateName + ".vm");
            map.put(templateName, template.render(genMap));
            map.put("content", template.render(genMap));
            map.put("name", templateName);
            genList.add(map);
        }
        return genList;
    }

    /**
     * 打包生成代码
     *
     * @param tableInfo 表信息，包括字段数据
     * @param genConfig 生成代码的参数配置，如包路径，作者
     * @param zip       压缩
     */
    public static void genZip(TableInfo tableInfo, GeneratorConfig genConfig, ZipOutputStream zip) {
        Map genMap = getGenMap(tableInfo, genConfig);
        //创建模板引擎
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig(null, TemplateConfig.ResourceMode.CLASSPATH));

        //生成后端代码
        List<String> templates = getBackTemplateNames();
        for (String templateName : templates) {
            StringWriter sw = new StringWriter();
            Template template = engine.getTemplate("generator/back/" + templateName + ".vm");
            template.render(genMap, sw);
            String className = genMap.get("className").toString();
            buildZip(getBackFilePath(templateName, genConfig, className), className, sw, zip);
        }

        //生成前端代码
        templates = getFrontTemplateNames();
        for (String templateName : templates) {
            StringWriter sw = new StringWriter();
            Template template = engine.getTemplate("generator/front/" + templateName + ".vm");
            template.render(genMap, sw);
            String className = genMap.get("className").toString();
            String caseClassName = genMap.get("caseClassName").toString();
            buildZip(getFrontFilePath(templateName, genConfig, caseClassName), className, sw, zip);
        }
    }

    private static void buildZip(String path, String className, StringWriter sw, ZipOutputStream zip) {
        try {
            //添加到zip
            zip.putNextEntry(new ZipEntry(Objects.requireNonNull(path)));
            IoUtil.write(zip, CharsetUtil.UTF_8, false, sw.toString());
            IoUtil.close(sw);
            zip.closeEntry();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ToolException("渲染模板失败，表名：" + className, e);
        }
    }


    /**
     * 定义后端文件路径和名称
     */
    private static String getBackFilePath(String templateName, GeneratorConfig genConfig, String className) {

        String packagePath = "back" + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator;
        if (StrUtil.isNotBlank(genConfig.getPack())) {
            packagePath += genConfig.getPack().replace(".", File.separator) + File.separator + genConfig.getModuleName() + File.separator;
        }

        if (ENTITY_JAVA_VM.contains(templateName)) {
            return packagePath + "client" + File.separator + "entity" + File.separator + className + ".java";
        }

        if (MAPPER_JAVA_VM.contains(templateName)) {
            return packagePath + "client" + File.separator + "mapper" + File.separator + className + "Mapper.java";
        }

        if (SERVICE_JAVA_VM.contains(templateName)) {
            return packagePath + "client" + File.separator + "service" + File.separator + className + "Service.java";
        }

        if (SERVICE_IMPL_JAVA_VM.contains(templateName)) {
            return packagePath + "server" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
        }

        if (CONTROLLER_JAVA_VM.contains(templateName)) {
            return packagePath + "server" + File.separator + "controller" + File.separator + className + "Controller.java";
        }

        if (MAPPER_XML_VM.contains(templateName)) {
            return "back" + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "mapper" + File.separator + genConfig.getModuleName() + File.separator + className + "Mapper.xml";
        }

        return null;
    }

    /**
     * 定义前端文件路径和名称
     */
    private static String getFrontFilePath(String templateName, GeneratorConfig genConfig, String className) {

        if (INDEX_VUE_VM.contains(templateName)) {
            return "front" + File.separator + "src" + File.separator + "views" +
                    File.separator + genConfig.getModuleName() + File.separator + className + File.separator + "Index.vue";
        }
        if (FORM_VUE_VM.contains(templateName)) {
            return "front" + File.separator + "src" + File.separator + "views" +
                    File.separator + genConfig.getModuleName() + File.separator + className + File.separator + "Form.vue";
        }
        return null;
    }
}
