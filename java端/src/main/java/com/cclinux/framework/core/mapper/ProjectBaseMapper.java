package com.cclinux.framework.core.mapper;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cclinux.framework.core.domain.PageResult;
import com.cclinux.framework.helper.TimeHelper;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ProjectBaseMapper<T> extends BaseMapper<T>, MPJBaseMapper<T> {

    default T getOne(@Param("ew") Where<T> where) {
        where.last("limit 1");
        List<T> list = this.selectList(where);

        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    default T getOne(@Param("ew") long id) {
        return this.selectById(id);
    }


    default Map<String, Object> getOneMap(@Param("ew") Where<T> where, String fields) {

        fields = this._fmtFields(fields);
        if (StringUtils.isNotEmpty(fields)) where.select(fields);

        where.last("limit 1");

        List<T> list = this.selectList(where);

        if (list.size() > 0) {
            Map<String, Object> ret = BeanUtil.beanToMap(list.get(0), this._fmtFieldsCamel(fields));
            if (ObjectUtil.isEmpty(ret)) return null;

            if (ret.containsKey("addTime")) TimeHelper.db2Time(ret, "addTime");

            if (ret.containsKey("editTime")) TimeHelper.db2Time(ret, "editTime");

            return ret;

        } else {
            return null;
        }
    }

    default Map<String, Object> getOneMap(@Param("ew") Where<T> where) {
        return this.getOneMap(where, "");
    }

    default Map<String, Object> getOneMap(@Param("ew") long id, String fields) {

        Object o = this.selectById(id);

        Map<String, Object> ret = BeanUtil.beanToMap(o, this._fmtFieldsCamel(fields));

        if (ObjectUtil.isEmpty(ret)) return null;

        if (ret.containsKey("addTime")) TimeHelper.db2Time(ret, "addTime");

        if (ret.containsKey("editTime")) TimeHelper.db2Time(ret, "editTime");

        return ret;
    }

    default Map<String, Object> getOneMap(@Param("ew") long id) {

        Object o = this.selectById(id);

        Map<String, Object> ret = BeanUtil.beanToMap(o);

        if (ret.containsKey("addTime")) TimeHelper.db2Time(ret, "addTime");

        if (ret.containsKey("editTime")) TimeHelper.db2Time(ret, "editTime");

        return ret;
    }


    default PageResult getPageJoinList(Page page, WhereJoin<T> where, String fields) {
        fields = this._fmtFields(fields);

        if (fields.equals("") || fields.equals("*")) fields = "t.*,t1.*";

        if (StrUtil.isNotEmpty(fields)) where.select(fields);

        IPage<Map<String, Object>> ret = this.selectJoinMapsPage(page, where);

        PageResult pageResult = new PageResult();

        List<Map<String, Object>> retList = new ArrayList<>();

        List<Map<String, Object>> list = ret.getRecords();

        for (Map<String, Object> t : list) {

            Map<String, Object> newMap = new HashMap<>();
            for (Map.Entry entry : t.entrySet()) {
                newMap.put(StrUtil.toCamelCase(entry.getKey().toString()), entry.getValue());
            }

            retList.add(newMap);

        }
        pageResult.setList(retList);

        for (Map<String, Object> record : retList) {

            if (record.containsKey("addTime")) TimeHelper.db2Time(record, "addTime");

            if (record.containsKey("editTime")) TimeHelper.db2Time(record, "editTime");
        }


        pageResult.setSize(ret.getSize());
        pageResult.setCount(ret.getPages());
        pageResult.setTotal(ret.getTotal());
        pageResult.setPage(ret.getCurrent());


        return pageResult;
    }

    default PageResult getPageList(Page page, Where<T> where, String fields) {
        fields = this._fmtFields(fields);
        if (StrUtil.isNotEmpty(fields)) where.select(fields);

        IPage<T> ret = this.selectPage(page, where);

        PageResult pageResult = new PageResult();

        List<Map<String, Object>> retList = new ArrayList<>();

        List<T> list = ret.getRecords();

        for (T t : list) {
            retList.add(BeanUtil.beanToMap(t, this._fmtFieldsCamel(fields)));

        }
        pageResult.setList(retList);

        for (Map<String, Object> record : retList) {
            if (record.containsKey("addTime")) TimeHelper.db2Time(record, "addTime");

            if (record.containsKey("editTime")) TimeHelper.db2Time(record, "editTime");
        }


        pageResult.setSize(ret.getSize());
        pageResult.setCount(ret.getPages());
        pageResult.setTotal(ret.getTotal());
        pageResult.setPage(ret.getCurrent());


        return pageResult;
    }

    default PageResult getPageList(Page page, Where<T> where) {
        return this.getPageList(page, where, "");
    }

    default long count(Where<T> where) {
        return this.selectCount(where);
    }

    default long max(Where<T> where, String fields) {
        where.select(" IFNULL(MAX(" + fields + "),0) as total ");
        List<Map<String, Object>> list = this.selectMaps(where);

        if (list.size() > 0) {
            return MapUtil.getLong(list.get(0), "total");
        }
        return 0;
    }

    default long min(Where<T> where, String fields) {
        where.select(" IFNULL(MIN(" + fields + "),0) as total ");
        List<Map<String, Object>> list = this.selectMaps(where);

        if (list.size() > 0) {
            return MapUtil.getLong(list.get(0), "total");
        }

        return 0;
    }

    default long sum(Where<T> where, String fields) {
        where.select(" IFNULL(SUM(" + fields + "),0) as total ");
        List<Map<String, Object>> list = this.selectMaps(where);

        if (list.size() > 0) {
            return MapUtil.getLong(list.get(0), "total");
        }

        return 0;
    }

    default List<Map<String, Object>> getAllListMap(Where<T> where, String fields) {
        fields = this._fmtFields(fields);
        if (StrUtil.isNotEmpty(fields)) where.select(fields);

        List<T> list = this.selectList(where);

        List<Map<String, Object>> retList = new ArrayList<>();
        for (T t : list) {
            retList.add(BeanUtil.beanToMap(t, this._fmtFieldsCamel(fields)));
        }

        for (Map<String, Object> record : retList) {
            if (record.containsKey("addTime")) TimeHelper.db2Time(record, "addTime");

            if (record.containsKey("editTime")) TimeHelper.db2Time(record, "editTime");
        }

        return retList;
    }

    default List<T> getAllList(Where<T> where, String fields) {
        fields = this._fmtFields(fields);
        if (StrUtil.isNotEmpty(fields)) where.select(fields);

        return this.selectList(where);

    }

    default List<Map<String, Object>> getAllListMap(Where<T> where) {

        return this.getAllListMap(where, "");
    }

    /***
     * 单个字段去重
     */
    default <X> List<X> distinct(Where<T> where, String field, Class<X> classType) {
        List<X> arr = new ArrayList<>();

        if (StrUtil.isEmpty(field)) return arr;

        where.select(" DISTINCT " + field);

        List<T> list = this.selectList(where);

        String CamelKey = CharSequenceUtil.toCamelCase(field);

        for (T t : list) {
            Map<String, Object> map = BeanUtil.beanToMap(t, this._fmtFieldsCamel(field));
            arr.add(MapUtil.get(map, CamelKey, classType));
        }
        return arr;

    }

    default int add(T entity) {
        // 新入库自动填充
        long time = TimeHelper.timestamp();
        BeanUtil.setFieldValue(entity, "addTime", time);
        BeanUtil.setFieldValue(entity, "editTime", time);

        return insert(entity);
    }

    default int delete(Serializable id) {
        return this.deleteById(id);
    }

    // 部分字段修改更新
    default int edit(UpdateWhere<T> uw) {
        uw.set("EDIT_TIME", TimeHelper.timestamp());

        return this.update(null, uw);
    }

    default int edit(long id, UpdateWhere<T> uw) {
        uw.set("EDIT_TIME", TimeHelper.timestamp());

        return this.update(null, uw);
    }

    default int inc(UpdateWhere<T> uw, String field, int count) {
        uw.set("EDIT_TIME", TimeHelper.timestamp());

        uw.setSql(" " + field + " = " + field + " + " + count);

        return this.update(null, uw);
    }

    default int inc(UpdateWhere<T> uw, String field) {
        return this.inc(uw, field, 1);
    }

    // 查询字段格式化
    default String _fmtFields(String fields) {
        if (StrUtil.isEmpty(fields)) return "";

        fields = fields.trim();
        fields = StrUtil.replace(fields," ","");

        if (StrUtil.isEmpty(fields) || fields.equals("*")) return "";
        else return fields;
    }

    // 查询字段格式化为驼峰
    default String[] _fmtFieldsCamel(String fields) {
        if (StrUtil.isEmpty(fields)) return new String[0];

        fields = fields.trim();
        if (StrUtil.isEmpty(fields) || fields.equals("*")) return new String[0];

        String[] arr = fields.split(",");
        for (int i = 0; i < arr.length; i++) {
            if (StrUtil.isEmpty(arr[i])) {
                arr[i] = "";
            } else {
                arr[i] = StrUtil.toCamelCase(arr[i]);
            }
        }

        return arr;
    }

}
