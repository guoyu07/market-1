/**
 * Created with IntelliJ IDEA.
 * User: Admin
 * Date: 14-12-25
 * Time: ????11:25
 * To change this template use File | Settings | File Templates.
 */

if (typeof ucf == "undefined") {
    ucf = {};
}
ucf.common = {

    getUrlRandom: function () {
        return "?_ur=" + new Date().getTime() + '' + Math.floor(Math.random() * 100);
    },

    formatNum: function (val, length) {
        return parseFloat(val).toFixed(length) == 'NaN' ? '' : parseFloat(val).toFixed(length);
    },

    urlAddRandom: function () {
        var rand = "_ur=" + new Date().getTime() + '' + Math.floor(Math.random() * 100);
        $("a[class='btn btn-primary']").each(function () {
            var href = $(this).attr("href");
            if (href.length == 0 || href.indexOf("javascript") > -1) return;
            else if (href.indexOf("?") > -1) {
                $(this).attr("href", href + "&" + rand);
            } else {
                $(this).attr("href", href + "?" + rand);
            }
        });
    },

    encryptCardNo: function (cardNo) {
        var reg = /^(\d{6})\d+(\d{4})$/;
        var l = cardNo.length - 10;
        var x = "";
        if (l > 0) {
            for (var i = 0; i < l; i++) {
                x = x + "*";
            }
            return cardNo.replace(reg, "$1" + x + "$2");
        }
        return cardNo.replace(reg, "$1****$2");
    },

    formatCardNo: function (cardNo) {
        return cardNo.replace(/\s/g, '').replace(/(\d{4})(?=\d)/g, "$1 ");
    },

// '' 今天    '0' 昨天   '1' 最近7天   '2'  最近一个月   '3'  最近三个月   '4'  最近一年
    getDate: function (type, fmt) {
        var t = new Date();
        if (type == '0')t.setDate(t.getDate() - 1);
        if (type == '1')t.setDate(t.getDate() - 6);
        if (type == '2') {
            t.setMonth(t.getMonth() - 1);
            t.setDate(t.getDate() + 1);
        }
        if (type == '3') {
            t.setMonth(t.getMonth() - 3);
            t.setDate(t.getDate() + 1);
        }
        if (type == '4')t.setFullYear(t.getFullYear() - 1);
        if (undefined == fmt || fmt == '')fmt = 'yyyy-MM-dd';
        return t.Format(fmt)
    },
    getFormJson: function (form) {
        var o = {};
        var a = $(form).serializeArray();
        $.each(a, function () {
            if (o[this.name] !== undefined) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    },
    download: function (url, data, method) {    // Ajax 文件下载  获得url和data
        if($("#btnExport").length>0){
            $("#btnExport").hide();
        }
        if (url && data) {
            // data 是 string 或者 array/object
            data = typeof data == 'string' ? data : jQuery.param(data);        // 把参数组装成 form的  input
            var inputs = '';
            jQuery.each(data.split('&'), function () {
                var pair = this.split('=');
                inputs += '<input type="hidden" name="' + pair[0] + '" value="' + pair[1] + '" />';
            });
            // request发送请求
            jQuery('<form action="' + url + '" method="' + (method || 'post') + '">' + inputs + '</form>')
                .appendTo('body').submit().remove();
        }
        window.setTimeout(function(){ $("#btnExport").show()},10000);

    }

};
Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};
/**
 * 确认对话框
 * @param title 标题
 * @param text 内容
 * @param callback 回调函数
 * @param buttons 按钮数组
 */
function confirm_dialog(title, text, callback, buttons) {
    if (typeof buttons !== "object") {
        buttons = {
            "确定": true,
            "取消": false
        };
    }
    var notice;
    text = $('<div>' + text + '<br style="clear: both;" /><div class="button-container" style="margin-top: 10px; text-align: right;"></div></div>');
    $.each(buttons, function (b, val) {
        text.find("div.button-container").append($('<button style="margin-left: 5px;" class="btn btn-default btn-small">' + b + '</button>').click(function () {
            notice.pnotify_remove();
            callback.call(notice, val);
        }));
    });
    notice = $.pnotify({
        title: title,
        text: text,
        insert_brs: false,
        hide: false,
        icon: 'glyphicon glyphicon-question-sign'
    });

}

