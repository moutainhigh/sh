package com.ldz.biz.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ldz.dao.biz.bean.ClLsGjInfo;
import com.ldz.dao.biz.bean.CsTxTj;
import com.ldz.dao.biz.bean.SafedrivingModel;
import com.ldz.dao.biz.bean.gpsSJInfo;
import com.ldz.util.commonUtil.HttpUtil;
import com.ldz.util.exception.RuntimeCheck;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ldz.dao.biz.model.ClGpsLs;
import com.ldz.dao.biz.model.ClSbyxsjjl;
import com.ldz.service.biz.interfaces.SbyxsjjlService;
import com.ldz.sys.base.BaseController;
import com.ldz.sys.base.BaseService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.bean.TrackPointsForReturn.Point;

@RestController
@RequestMapping("api/clsbyxsjjl")
public class ClSbyxsjjlCtrl extends BaseController<ClSbyxsjjl, String> {
	@Autowired
	private SbyxsjjlService service;
	@Value("${shipApi.ip}")
	private String shipip;

	@Override
	protected BaseService<ClSbyxsjjl, String> getBaseService() {
		return service;
	}

	/*
	 *
	 * 获取该终端所有历史轨迹数据
	 *  入参每个必填 点火熄火固定值
	 */
	@RequestMapping("/history")
	public ApiResponse<List<ClLsGjInfo>> historyTrajectory(gpsSJInfo gpssjinfo) {
		return service.historyTrajectory(gpssjinfo);
	}

	/*
	 *
	 * 获取该终端某段时间内gps坐标
	 * 入参 开始时间  结束时间 终端编号
	 */
	@PostMapping("/historygps")
	public ApiResponse<List<ClGpsLs>>  getGuiJiGps(gpsSJInfo gpssjinfo) {
		return service.getGuiJiGps(gpssjinfo);
	}

	@PostMapping("/baiduGuiJi")
	public ApiResponse<List<Point>>  getBaiDuGuiJi(gpsSJInfo gpssjinfo) {

		return service.baiduGuiJi(gpssjinfo);

	}



	@RequestMapping("/Safedriving")
	public ApiResponse<List<SafedrivingModel>> getSafeDrivig(){
		return service.getSafeDrivig();
	}

	/*
	 * 获取时间段内超数值
	 */
	@PostMapping("/csxxtj")
	public ApiResponse<CsTxTj> getcs(String cph, String day){

		return service.getcs(cph,day);

	}

	/**
	 * 查询本地库中的百度鹰眼历史数据
	 * @param gpssjinfo
	 * @return
	 */
	@PostMapping("/yyguiji")
	public ApiResponse<List<com.ldz.util.bean.Point>> getYyGuiJi(gpsSJInfo gpssjinfo){

		return service.getYyGuiJi(gpssjinfo);

	}

	@PostMapping("/historyTrack")
	public ApiResponse<JSONArray> getHistoryTrack(String zdbh, String start, String end) {
		RuntimeCheck.ifBlank(zdbh, "请选择船舶");
		RuntimeCheck.ifBlank(start, "请选择轨迹时间");
		RuntimeCheck.ifBlank(end, "请选择轨迹时间");
		String url = shipip + "/v1/GetHistoryTrack";
		Map<String,String> params = new HashMap<>();
		params.put("shipid", zdbh);
		params.put("startUtcTime", DateTime.parse(start, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate().getTime()/1000 + "");
		params.put("endUtcTime", DateTime.parse(end, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate().getTime()/1000 + "");
		String res = HttpUtil.get(url, params);
		JSONObject object = JSON.parseObject(res);
		RuntimeCheck.ifFalse(StringUtils.equals(object.getString("Status"), "0"), "请求异常， 请稍后再试");
		JSONArray array = object.getJSONArray("Result");
		return ApiResponse.success(array);
	}

}
