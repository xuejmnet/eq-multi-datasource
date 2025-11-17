package com.eq.mds.controller;

import com.easy.query.core.basic.api.database.CodeFirstCommand;
import com.easy.query.core.basic.api.database.DatabaseCodeFirst;
import com.eq.mds.annotation.DS;
import com.eq.mds.client.EasyMultiEntityQuery;
import com.eq.mds.entity.SysUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * create time 2025/11/17 15:21
 * 文件说明
 *
 * @author xuejiaming
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("/api/test")
public class TestController {
    private final EasyMultiEntityQuery easyMultiEntityQuery;

    @GetMapping("/ds1")
    public Object ds1() {
        DatabaseCodeFirst databaseCodeFirst = easyMultiEntityQuery.getDatabaseCodeFirst();
        databaseCodeFirst.createDatabaseIfNotExists();
        CodeFirstCommand codeFirstCommand = databaseCodeFirst.syncTableCommand(Arrays.asList(SysUser.class));
        codeFirstCommand.executeWithTransaction(s->s.commit());
        return "ds1-ok";
    }
    @GetMapping("/ds2")
    public Object ds2() {
        try {
            easyMultiEntityQuery.setCurrent("ds2");
            DatabaseCodeFirst databaseCodeFirst = easyMultiEntityQuery.getDatabaseCodeFirst();
            databaseCodeFirst.createDatabaseIfNotExists();
            CodeFirstCommand codeFirstCommand = databaseCodeFirst.syncTableCommand(Arrays.asList(SysUser.class));
            codeFirstCommand.executeWithTransaction(s->s.commit());
            return "ds2-ok";
        }finally {
            easyMultiEntityQuery.clear();
        }
    }
    @GetMapping("/dsc")
    @DS("ds2")
    public Object dsc() {
        List<SysUser> list = easyMultiEntityQuery.queryable(SysUser.class)
                .where(s -> {
                    s.name().like("123");
                }).toList();
        return list;
    }
}
