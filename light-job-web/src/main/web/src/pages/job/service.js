// @ts-ignore

/* eslint-disable */
import { request } from 'umi';

/** 获取规则列表 GET /api/rule */
export async function rule(params, options) {
  return request('/api/rule', {
    method: 'GET',
    params: { ...params },
    ...(options || {}),
  });
}

export async function queryPeriodicJobList(params) {
  const result = await request('/admin/periodicjob/v1/queryList', {
    method: 'POST',
    data: {...params, pageNo : params.current},
  });

  if (result.success) {
    if (result.data) {
      // console.log(`querySchemaList => ${ JSON.stringify(result.data.list)}`)
      return {'success': true, 'total': result.data.totalSize, 'data': result.data.list ? result.data.list : []};
    }
    return {'success': true, 'total': 0, 'data': []};
  }
  message.warning(result.errorMsg);
  return {'success': true, 'total': 0, 'data': []};
}

/** 新建规则 PUT /api/rule */
export async function updateRule(data, options) {
  return request('/api/rule', {
    data,
    method: 'PUT',
    ...(options || {}),
  });
}
/** 新建规则 POST /api/rule */

export async function addPeriodicJob(data, options) {
  return request('/admin/periodicjob/v1/addOrUpdate', {
    data,
    method: 'POST',
    ...(options || {}),
  });
}
/** 删除规则 DELETE /api/rule */

export async function removeRule(data, options) {
  return request('/api/rule', {
    data,
    method: 'DELETE',
    ...(options || {}),
  });
}

export async function queryAllGroup(callback) {
  const result = await request('/admin/group/v1/queryAll');
  if (result?.success) {
    callback(result.data || []);
  }
}
