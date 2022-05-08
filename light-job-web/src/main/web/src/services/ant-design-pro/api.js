// @ts-ignore

/* eslint-disable */
import { request } from 'umi';
/** 获取当前的用户 GET /api/currentUser */

export async function currentUser() {
  const result = await request('/admin/user/v1/currentUser', {
    method: 'GET'
  });
  if (result.success) {
    return {
      success: true,
      data:{
        name: result.data.userName,
        avatar: 'https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png',
        userid: result.data.usercode,
        email: '',
        signature: '',
        title: '',
        group: '',
        tags: [
        ],
        notifyCount: 0,
        unreadCount: 0,
        // country: 'China',
        access: "admin",
        // geographic: {
        //   province: {
        //     label: '浙江省',
        //     key: '330000',
        //   },
        //   city: {
        //     label: '杭州市',
        //     key: '330100',
        //   },
        // },
        // address: '西湖区工专路 77 号',
        // phone: '0752-268888888',
      }
    }
  }
}
/** 退出登录接口 POST /api/login/outLogin */

export async function outLogin(options) {
  return request('/api/login/outLogin', {
    method: 'POST',
    ...(options || {}),
  });
}
/** 登录接口 POST /api/login/account */

export async function login(body, options) {
  // return request('/api/login/account', {
  //   method: 'POST',
  //   headers: {
  //     'Content-Type': 'application/json',
  //   },
  //   data: body,
  //   ...(options || {}),
  // });
  const { type } = body;
  const result = await request('/admin/user/v1/loginWithPassword', {
    method: 'POST',
    params: body,
    // data: params, // application/json
    // requestType: 'form' // default json
  });
  if (result.success) {
    return {
      status: 'ok',
      type,
      currentAuthority: result.data.usercode,
    }
  }
}
/** 此处后端没有提供注释 GET /api/notices */

export async function getNotices(options) {
  return request('/api/notices', {
    method: 'GET',
    ...(options || {}),
  });
}
/** 获取规则列表 GET /api/rule */

export async function rule(params, options) {
  return request('/api/rule', {
    method: 'GET',
    params: { ...params },
    ...(options || {}),
  });
}
/** 新建规则 PUT /api/rule */

export async function updateRule(options) {
  return request('/api/rule', {
    method: 'PUT',
    ...(options || {}),
  });
}
/** 新建规则 POST /api/rule */

export async function addRule(options) {
  return request('/api/rule', {
    method: 'POST',
    ...(options || {}),
  });
}
/** 删除规则 DELETE /api/rule */

export async function removeRule(options) {
  return request('/api/rule', {
    method: 'DELETE',
    ...(options || {}),
  });
}
