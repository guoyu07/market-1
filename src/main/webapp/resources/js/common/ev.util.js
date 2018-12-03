if (typeof ev == "undefined") {
    ev = {};
}


ev.util = {
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


    errorHandler: function (displayId, verifyObj, local) {
        var _message = null;
        if (local == 'en') {
            _message = message.en;
        } else {
            _message = message.cn;
        }

        var _display = $("#" + displayId);
        if (typeof _display == 'undefined') {
            return false;
        }
        var _iconSpan = _display.find('.error_icon');
        var _textSpan = _display.find('.error_msg');
        var _version = 'new';

        if (typeof _textSpan.html() == 'undefined') {
            _textSpan = _display;
            _version = 'old';
        }

        if ('00' == verifyObj.code) {
            if (_version == 'old')_display.empty();
            else _display.hide();
            return true;
        } else {
            var _m = "";
            if (verifyObj.msgCode == 'undefined' || verifyObj.msgCode == null || verifyObj.msgCode == '') {
                _m = verifyObj.msg;
            } else {
                _m = _message[verifyObj.msgCode];
            }
            if (typeof _display == 'undefined') {
                alert(_m);
            } else {
                _textSpan.html(_m);
                _display.show();
            }
            return false;
        }
    },

    errorHandler2: function (formItemDivId, errorDivId, verifyObj, local) {
//        alert("formItemDivId=" + formItemDivId + " , errorDivId" + errorDivId + " , verifyObj=" + verifyObj.code + " , verifyObj=" + verifyObj.msgCode);

        var _message = null;
        if (local == 'en') {
            _message = message.en;
        } else {
            _message = message.cn;
        }

        var _formItem = $("#" + formItemDivId);
        var _errorDiv = $("#" + errorDivId);
        if (typeof _errorDiv == 'undefined' || typeof _formItem == 'undefined') {
            return false;
        }
        var _textSpan = _errorDiv.find('.error_msg');

        var _errorDef = {
            "code": "00",
            "msgCode": "",
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
            if (_errorDef.msgCode == 'undefined' || _errorDef.msgCode == null || _errorDef.msgCode == '') {
                _m = _errorDef.msg;
            } else {
                _m = _message[_errorDef.msgCode];
            }
            _textSpan.html(_m);
            _errorDiv.removeClass("equip_tiptext_warning").removeClass("equip_tiptext_error").addClass(_errorDivClass);
            _formItem.addClass(_formItemClass);
            return false;
        }
    }
};