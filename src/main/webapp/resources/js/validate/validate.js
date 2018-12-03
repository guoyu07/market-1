if (typeof validate == "undefined") {
    validate = {};
}


validate = {
    rule: {
        isEmpty: function (val) {
            return !!!val;
        },

        isNumber: function (val) {
            return !(isNaN(val));
        },

        mobilePhoneVerify: function (val) {
            var re = new RegExp(/^1\d{10}$/);
            return re.test(val);
        },
        accountVerify: function (val) {
            var re = new RegExp(/^[a-zA-z]\w{5,19}$/);
            return re.test(val);
        },

        emailVerify: function (val) {
            var re = new RegExp(/\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/);
            return re.test(val);
        },

        lengthVerify: function (maxLength, minLength, val) {
            if (val.length > maxLength) {
                return false;
            } else if (val.length < minLength) {
                return false;
            } else {
                return true;
            }
        },

        lengthEqual: function (defLength, val) {
            return val.length == defLength;
        },

        domNumber: function (dom) {
            return dom.length;
        },

        isPositiveInteger: function (val) {
            var re = new RegExp(/^[0-9]*[1-9][0-9]*$/);
            return re.test(val);
        },

        isMoney: function (val) {
            var re = new RegExp(/^(([1-9]\d*)|0)(\.\d{1,2})?$/);
            return re.test(val);
        },

        capitalLetterVerify: function (val) {
            var re = new RegExp(/^[A-Z]+$/);
            return re.test(val);
        },

        letterVerify: function (val) {
            var re = new RegExp(/^[A-Za-z]+$/);
            return re.test(val);
        },

        bankCardVerify: function (val) {
            var re = new RegExp(/^[0-9\-]+$/);
            return re.test(val);
        },

        nameVerify: function (val) {
            var re = new RegExp(/^[a-zA-Z\u4e00-\u9fa5]+$/);
            return re.test(val);
        },

        chineseVerify: function (val) {
            var re = new RegExp(/^[\u4e00-\u9fa5]{0,}$/);
            return re.test(val);
        },

        rateVerify: function (val) {
            var re = new RegExp(/^\d+(\.\d{1,2})?$/);
            return re.test(val);
        },

        cByteLenGBK: function (val, len) {
            var sum = 0;
            var strTemp = val;
            for (var i = 0; i < strTemp.length; i++) {
                if ((strTemp.charCodeAt(i) >= 0) && (strTemp.charCodeAt(i) <= 255)) {
                    sum = sum + 1;
                } else {
                    sum = sum + 2;
                }
            }
            if (sum > len) {
                return true;
            } else {
                return false;
            }
        }
    },
    /*errorHandler: function (formItemDivId, errorDivId, verifyObj) {
        var _formItem = $("#" + formItemDivId);
        var _errorDiv = $("#" + errorDivId);
        if (typeof _errorDiv == 'undefined' || typeof _formItem == 'undefined') {
            return false;
        }
        var _textSpan = _errorDiv.find('.error_msg');
        var _errorDef = {
            "code": "00",
            "msg": "",
            "level": "W",  // W:warning E:error
            "errorControlClass": "warning_tips",   //error_tips
            "errorInfoClass": "equip_tiptext_warning"  //equip_tiptext_error
        };

        if (typeof verifyObj != "undefined") {
            _errorDef = $.extend(_errorDef, verifyObj);
        }
        var _formItemClass = _errorDef.errorControlClass, _errorDivClass = _errorDef.errorInfoClass;
        if ("E" == _errorDef.level) {
            _formItemClass = "error_tips";
            _errorDivClass = "equip_tiptext_error";
        }
        if ('00' == _errorDef.code) {
            _textSpan.empty();
            _formItem.removeClass(_formItemClass);
            return true;
        } else {
            var _m = "";
            _m = _errorDef.msg;
            _textSpan.html(_m);
            _errorDiv.removeClass("equip_tiptext_warning").removeClass("equip_tiptext_error").addClass(_errorDivClass);
            _formItem.addClass(_formItemClass);
            return false;
        }
    },*/
    errorHandler: function (errorDivId, verifyObj) {
        var _errorDiv = $("#" + errorDivId);
        if (typeof _errorDiv == 'undefined') {
            return false;
        }
        var _textSpan = _errorDiv.find('.error_msg');
        var _errorDef = {
            "code": "00",
            "msg": ""
        };
        if (typeof verifyObj != "undefined") {
            _errorDef = $.extend(_errorDef, verifyObj);
        }
        _textSpan.empty();
        if ('00' == _errorDef.code) {
            _errorDiv.hide();
            return true;
        } else {
            var _m = _errorDef.msg;
            _textSpan.html(_m);
            _errorDiv.show();
            return false;
        }
    }
};