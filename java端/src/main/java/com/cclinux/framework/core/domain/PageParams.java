package com.cclinux.framework.core.domain;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import lombok.Data;

import java.util.List;
import java.util.Map;


@Data
public class PageParams {

    public final static String[] PAGE_CHECK_RULE = {
            "search:string|name=搜索条件",
            "page:must|int|default=1",
            "size:must|int|size|default=10",
            "isTotal:bool",
            "sortType:string|name=搜索类型",
            "sortVal:string|name=搜索类型值",
            "orderBy:string|name=排序",
            "whereEx:string|name=附加查询条件",
            "oldTotal:int"
    };

    public PageParams(Map<String, Object> map) {

        this.setSearch(MapUtil.getStr(map, "search", null));
        this.setSortType(MapUtil.getStr(map, "sortType", null));
        this.setSortVal(MapUtil.getStr(map, "sortVal", null));
        this.setWhereEx(MapUtil.getStr(map, "whereEx", null));

        this.setPage(MapUtil.getLong(map, "page", 1L));
        this.setSize(MapUtil.getLong(map, "size", 20L));
        this.setOldTotal(MapUtil.getLong(map, "oldTotal", 0L));

        this.setIsTotal(MapUtil.getBool(map, "isTotal", true));

        map.remove("search");
        map.remove("sortType");
        map.remove("sortValue");
        map.remove("whereEx");
        map.remove("page");
        map.remove("size");
        map.remove("isTotal");
        map.remove("oldTotal");
        map.remove("orderBy");
        this.setFilterMap(map);
    }

    /***
     * 搜索条件
     */
    private Map<String, Object> filterMap;

    /***
     * 排序
     */
    private List<OrderItem> orderBy;

    /***
     * 搜索关键字
     */
    private String search;

    /***
     * 搜索分类K
     */
    private String sortType;

    /***
     * 搜索分类V
     */
    private String sortVal;

    /***
     * 附加查询条件
     */
    private String whereEx;

    /***
     * 需显示的页码
     */
    private Long page = 1L;

    /***
     * 每页记录数
     */
    private Long size = 20L;

    /***
     * 是否统计
     */
    private Boolean isTotal = true;

    /***
     * 上次纪录总数
     */
    private Long oldTotal = 0L;

    public int getParamInt(String name) {
        Integer ret = MapUtil.getInt(this.filterMap, name);
        return ObjUtil.isEmpty(ret) ? 0 : ret;
    }

    public long getParamLong(String name) {
        Long ret = MapUtil.getLong(this.filterMap, name);
        return ObjUtil.isEmpty(ret) ? 0 : ret;
    }

    public String getParamStr(String name) {
        return MapUtil.getStr(this.filterMap, name);
    }

}
