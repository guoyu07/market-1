if (typeof ucf == "undefined") {
    ucf = {};
}

function timeoutDeal(XMLHttpRequest, textStatus) {
    var sessionstatus = XMLHttpRequest.getResponseHeader("sessionstatus"); //通过XMLHttpRequest取得响应头，sessionstatus，
    if (sessionstatus == "timeout") {
        alert("登录超时,请重新登录！");
        //如果超时就处理 ，指定要跳转的页面
        window.location.href = 'sys/toLogin';
    }
    if (textStatus == 'timeout') {//超时,status还有success,error等值的情况
        alert("访问时间太长，系统超时了");
    }
}

$.ajaxSetup({
    timeout: 180000,
    complete: function (XMLHttpRequest, textStatus) {
        timeoutDeal(XMLHttpRequest, textStatus);
    }
});

ucf.ajax = {
    commonFn: function (url, params, callbackFn) {
        if (url.endWith("/list"))
            ucf.ajax.loadingAjax(url, params, callbackFn,
                function () {
                    $("#btnQry").attr("disabled", "disabled");
                },
                function () {
                    $("#btnQry").removeAttr("disabled");
                });
        else
            $.ajax({
                type: "POST",
                url: url,
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                dataType: 'json',
                async: false,
                data: params,
                /*beforeSend:function(){
                 if($("#saveBtn").length>0){
                 $("#saveBtn").attr("disabled","disabled");
                 }
                 $("#cover").show();
                 },
                 complete:function(data){
                 if($("#saveBtn").length>0){
                 $("#saveBtn").removeAttr("disabled");

                 }
                 setTimeout(function () {
                 $("#cover").hide();
                 }, 500);

                 },*/
                success: function (data) {
                    callbackFn(data);
                }
            });
    },
    loadingAjax: function (url, params, callbackFn, showLoadingFn, closeLoadingFn) {
        $.ajax({
            type: "POST",
            url: url,
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            dataType: 'json',
            async: false,
            data: params,
            beforeSend: function () {
                showLoadingFn();//打开加载层
            },
            complete: function (XMLHttpRequest, textStatus) {
                timeoutDeal(XMLHttpRequest, textStatus);
                closeLoadingFn();//关闭加载层
            },
            success: function (data) {
                callbackFn(data);
            }
        });
    }
};
String.prototype.endWith = function (s) {
    if (s == null || s == "" || this.length == 0 || s.length > this.length)
        return false;
    if (this.substring(this.length - s.length) == s)
        return true;
    else
        return false;
    return true;
};
String.prototype.startWith = function (s) {
    if (s == null || s == "" || this.length == 0 || s.length > this.length)
        return false;
    if (this.substr(0, s.length) == s)
        return true;
    else
        return false;
    return true;
};