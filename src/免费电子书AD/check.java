package 免费电子书AD;

import net.sf.json.JSONObject;

public class check {
	/**
	 * 检查上报返回的code是否为0
	 * 
	 * @param code
	 * @return
	 */
	public static boolean checkCode(int code) {
		if (0 == code) {
			return true;
		} else {
			requestUrl.errorPopupWindow("上报返回的code不是0");
			return false;
		}
	}

	/**
	 * 检查返回的是否是一个json格式
	 * 
	 * @param response
	 *            返回的json
	 * @return true/false
	 */
	public static boolean checkJson(String response) {
		if (!response.startsWith("{") || !response.endsWith("}")) {
			requestUrl.errorPopupWindow("返回的Json格式不正确");
			return false;
		}
		return true;
	}

	/*
	 * 检查id是否为空
	 */
	public static boolean checkId(JSONObject jsonObj, String response,int i) {
		if (jsonObj.get("id") == null) {
			System.out.println("第"+i+"次id为空" + "json:" + response);
			return false;
		}
		return true;
	}
}
