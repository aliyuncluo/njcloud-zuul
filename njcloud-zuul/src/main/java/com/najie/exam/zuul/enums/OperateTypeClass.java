package com.najie.exam.zuul.enums;

import java.util.ArrayList;
import java.util.List;

public class OperateTypeClass {

    private List<String> list;

    public OperateTypeClass() {
        super();
        IniClass();
    }

    private void IniClass() {
        list = new ArrayList<>();
        for (EnumClass.CheckIdentityEnum row : EnumClass.CheckIdentityEnum.values()
                ) {
            list.add(row.toString());
//            if (!row.toString().equals("-nkf-")) {
//                list.add(row.toString());
//            }
        }
    }

    public EnumClass.CheckIdentityEnum GetOperateType(String _myPath) {
        int index = -1;
        EnumClass.CheckIdentityEnum checkIdentityEnum;
        for (int i = 0; i < list.size(); i++) {
            if (_myPath.indexOf(list.get(i)) > -1) {
                index = i;
            }
        }
        switch (index) {
            case 0:
                checkIdentityEnum = EnumClass.CheckIdentityEnum.IS_BS;
                break;
            case 1:
                checkIdentityEnum = EnumClass.CheckIdentityEnum.IS_CS;
                break;
            case 2:
                checkIdentityEnum = EnumClass.CheckIdentityEnum.IS_ANDROID;
                break;
            case 3:
                checkIdentityEnum = EnumClass.CheckIdentityEnum.IS_IOS;
                break;
            case 4:
                checkIdentityEnum = EnumClass.CheckIdentityEnum.IS_WEIXIN_PUBLIC;
                break;
            case 5:
                checkIdentityEnum = EnumClass.CheckIdentityEnum.IS_WEIXIN_SMALLPROGRAME;
                break;
            case 6:
                checkIdentityEnum = EnumClass.CheckIdentityEnum.IS_LOCALREMOTE;
                break;
            default:
                checkIdentityEnum = EnumClass.CheckIdentityEnum.IS_NO;
                break;
        }

        return checkIdentityEnum;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        list.clear();
    }
}
