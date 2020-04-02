import request from '@/utils/request'

/**
 * 获取数据源中的所有表信息
 * @param data
 */
export function getTables(data) {
    return request.get('support/common/generator/getTables', data);
}

/**
 * 获取表的字段
 * @param tableName
 */
export function getTableColumn(data) {
    return request.get('support/common/generator/getTableColumn', data);
}

/**
 * 获取代码生成配置
 */
export function getGeneratorConfig() {
    return request.get('support/common/generator/getGeneratorConfig');
}

/**
 * 获取数据源配置
 * @returns {*}
 */
export function getDataSourceConfig() {
    return request.get('support/common/dataSourceConfig/getAll');
}

/**
 * 更新代码生成配置
 */
export function updateGeneratorConfig(data) {
    return request.put('support/common/generator/updateGeneratorConfig', data)
}

export function genPreview(data) {
    return request.postData('support/common/generator/genPreview',data);
}

/**
 * 代码生成打包下载
 */
export function genZip(data) {
    let fileName = data.tableName + '.zip';
    return  request.download('support/common/generator/genZip',data,fileName);
}