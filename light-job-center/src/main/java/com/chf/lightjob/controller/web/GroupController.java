package com.chf.lightjob.controller.web;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chf.lightjob.controller.web.request.GroupAddOrUpdateReq;
import com.chf.lightjob.controller.web.request.GroupPageReq;
import com.chf.lightjob.controller.web.response.GroupResp;
import com.chf.lightjob.dal.entity.LightJobGroupDO;
import com.chf.lightjob.dal.qo.GroupQO;
import com.chf.lightjob.model.DataResult;
import com.chf.lightjob.model.PageData;
import com.chf.lightjob.service.GroupService;

/**
 * @description
 * @author: davy
 * @create: 2022-03-06 14:07
 */
@RestController
@RequestMapping("/admin/group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping("/v1/queryList")
    public DataResult<PageData<GroupResp>> queryList(@RequestBody GroupPageReq req) {
        GroupQO qo = new GroupQO();
        qo.setPageNo(Optional.ofNullable(req.getPageNo()).orElse(1));
        qo.setPageSize(Optional.ofNullable(req.getPageSize()).orElse(20));
        int count = groupService.countList(qo);
        if (count == 0) {
            return DataResult.success(PageData.emptyPage(count));
        }
        List<LightJobGroupDO> groupList = groupService.queryList(qo);
        if (CollectionUtils.isEmpty(groupList)) {
            return DataResult.success(PageData.emptyPage(count));
        }

        List<GroupResp> respList = groupList.stream().map(groupDO -> {
            return convertDoToResp(groupDO);
        }).collect(Collectors.toList());
        return DataResult.success(PageData.listPage(count, respList));
    }

    @GetMapping("/v1/queryAll")
    public DataResult<List<GroupResp>> queryAll() {
        List<LightJobGroupDO> groupList = groupService.queryAll();
        if (CollectionUtils.isEmpty(groupList)) {
            return DataResult.success(Collections.emptyList());
        }

        List<GroupResp> respList = groupList.stream().map(groupDO -> {
            return convertDoToResp(groupDO);
        }).collect(Collectors.toList());
        return DataResult.success(respList);
    }

    @PostMapping("/v1/addOrUpdate")
    public DataResult<Long> addOrUpdate(@RequestBody GroupAddOrUpdateReq req) {
        LightJobGroupDO groupDO = new LightJobGroupDO();
        groupDO.setId(req.getId());
        groupDO.setGroupCode(req.getGroupCode());
        groupDO.setGroupName(req.getGroupName());
        Long groupId = groupService.addOrUpdate(groupDO);
        return DataResult.success(groupId);
    }

    private static GroupResp convertDoToResp(LightJobGroupDO groupDO) {
        if (groupDO == null) {
            return null;
        }
        GroupResp groupResp = new GroupResp();
        groupResp.setId(groupDO.getId());
        groupResp.setGroupCode(groupDO.getGroupCode());
        groupResp.setGroupName(groupDO.getGroupName());
        groupResp.setGmtCreate(groupDO.getGmtCreate().getTime());
        groupResp.setGmtModified(groupDO.getGmtModified().getTime());
        return groupResp;
    }
}
