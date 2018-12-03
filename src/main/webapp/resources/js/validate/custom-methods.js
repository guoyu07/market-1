$(function () {

    // 判断中文字符 
    jQuery.validator.addMethod("isChinese", function (value, element) {
        return this.optional(element) || /^[\u0391-\uFFE5]+$/.test(value);
    }, "只能输入中文字符。");

    // 判断英文字符 
    jQuery.validator.addMethod("isEnglish", function (value, element) {
        return this.optional(element) || /^[A-Za-z]+$/.test(value);
    }, "只能输入英文字符。");

    jQuery.validator.addMethod("isDecimal", function (value, element) {
        return this.optional(element) || /^-?\d+(\.\d{1,2})?$/.test(value);
    }, "请输入不超过两位小数的数字。");
    // 手机号码验证    
    jQuery.validator.addMethod("isMobile", function (value, element,param) {
        if(param){
            var length = value.length;
            return this.optional(element) || (length == 11 && /^1[3-9]\d{9}$/.test(value));
        }else{
            return true;
        }

    }, "请正确填写您的手机号码。");

    // 电话号码验证    
    jQuery.validator.addMethod("isPhone", function (value, element,param) {
        if(param){
            var tel = /^(0\d{2,3}-\d{7,8})$/;
            return this.optional(element) || (tel.test(value));
        }else{
            return true;
        }
    }, "请正确填写您的电话号码。如：020-87554433");

    jQuery.validator.addMethod("isTel", function(value, element,param) {
        if(param){
            var tel = /^((0\d{2,3}-\d{7,8})|(1[3-9]\d{9}))$/;
            return this.optional(element) || tel.test(value);
        }else{
            return true;
        }
    },  "请正确填写您的联系电话。如：020-87554433或13956788765");

    // 邮政编码验证
    jQuery.validator.addMethod("isZipCode", function (value, element) {
        var zip = /^[0-9]{6}$/;
        return this.optional(element) || (zip.test(value));
    }, "请正确填写您的邮政编码。");

    // 匹配密码，以字母开头，长度在6-12之间，只能包含字符、数字和下划线。      
    jQuery.validator.addMethod("isPwd", function (value, element) {
        return this.optional(element) || /^[a-zA-Z0-9]\w{5,17}$/.test(value);
    }, "以字母或数字开头，长度在6-18之间，只能包含字符、数字和下划线。");

    // 身份证号码验证
    jQuery.validator.addMethod("isIdCardNo", function (value, element) {
        return this.optional(element) || IdCardValidate(value);
    }, "请输入正确的身份证号码。");

    jQuery.validator.addMethod("isIdCardNo", function (value, element, param) {
        if (param) {
            return this.optional(element) || IdCardValidate(value);
        } else {
            return true;
        }
    }, "请输入正确的身份证号码。");

    // 判断是否为合法字符(a-zA-Z0-9-_)
    jQuery.validator.addMethod("isRightfulString", function (value, element) {
        return this.optional(element) || /^[A-Za-z][A-Za-z0-9_-]+$/.test(value);
    }, "必须字母、数字、下划线组成，字母开头。");

    jQuery.validator.addMethod("isNumbersAndLetters", function (value, element) {
        return this.optional(element) || /^[a-zA-Z0-9]+$/.test(value);
    }, "必须英文或数字。");

    jQuery.validator.addMethod("maxLengthVerify", function (value, element, param) {
        var _max = param;
        var sum = 0;
        var strTemp = value;
        for (var i = 0; i < strTemp.length; i++) {
            if ((strTemp.charCodeAt(i) >= 0) && (strTemp.charCodeAt(i) <= 255)) {
                sum = sum + 1;
            } else {
                sum = sum + 2;
            }
        }
        return sum <= _max;
    }, "填写的字符过长");

    jQuery.validator.addMethod("twoNumVerify", function (value, element, param) {
        var _num = param.num;
        var _type = param.type;
        var _flag = true;
        if (_type == 'L' && parseFloat(value) < parseFloat(_num)) { //当前数必须要大于比较的数
            _flag = false;
        } else if (_type == 'S' && parseFloat(value) > parseFloat(_num)) {//当前数必须要小于比较的数
            _flag = false;
        }
        return _flag;
    }, "数字大小不正确");

    jQuery.validator.addMethod("dateVerify", function (value, element, param) {
        var _flag = true;
        var _beginDate = param.beginDate;
        var _endDate = param.endDate;

        _beginDate = _beginDate.replace("-", "");
        _endDate = _endDate.replace("-", "");

        if (_beginDate > _endDate) {
            _flag = false;
        }
        return _flag;
    }, "结束日期不能早于开始日期");

    jQuery.validator.addMethod("isIp", function (value, element) {
        return this.optional(element) || /^(0|[1-9]?|1\d\d?|2[0-4]\d|25[0-5])\.(0|[1-9]?|1\d\d?|2[0-4]\d|25[0-5])\.(0|[1-9]?|1\d\d?|2[0-4]\d|25[0-5])\.(0|[1-9]?|1\d\d?|2[0-4]\d|25[0-5])$/.test(value);
    }, "只能输入IP");

    jQuery.validator.addMethod("isUrl", function (value, element) {
        return this.optional(element) || /^(https?|s?ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/.test(value);
    }, "只能输入URL");

});