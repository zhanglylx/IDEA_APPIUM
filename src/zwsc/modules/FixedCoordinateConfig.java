package zwsc.modules;

import AppTest.Devices;

public class FixedCoordinateConfig {
    public static final String[] MODELArr = {"SM_C5000","MI_8_SE"};
    public static final String MODEL =
            Devices.getDevices("获取手机品牌").getDevicesBrand();

    /**
     * 个人中心修改昵称中的X按钮
     *
     * @return
     */
    public static int[] getPersonalCenterUpdateName() {
        switch (MODEL) {
            case "SM_C5000":
                return new int[]{982, 305};
            case "MI_8_SE":
                return new int[]{975, 371};
        }
        return null;
    }

    /**
     * 个人中心修改生日中的年
     *  +1或减一
     * @return
     */
    public static int[] getPersonalCenterUpdateBirthday(int yearRand) {
        switch (MODEL) {
            case "SM_C5000":
                if(yearRand<0){
                    return new int[]{121,1680,121,1783,900};
                }else {
                    return new int[]{121,1680,121,1596,900};
                }

            case "MI_8_SE":
                if(yearRand<0){
                    return new int[]{194,1895,194,1979,700};
                }else {
                    return new int[]{194,1895,194,1816,700};
                }
        }
        return null;
    }

}
