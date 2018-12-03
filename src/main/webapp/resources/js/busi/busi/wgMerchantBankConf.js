if (typeof wgMerchantBankConf == 'undefined') {
    wgMerchantBankConf = {};
}
var totalElements=0;

wgMerchantBankConf.list={
    init:function(){

        $("#bankCode").select2({data:_wgBankArr,width:'100%'});
        wgMerchantBankConf.merchantInit();
        wgMerchantBankConf.list.qryPageData();
        $("#btnQry").on("click", function () {
            $("input[name='page']").val(0);
            wgMerchantBankConf.list.qryPageData();
        });
        $("#frmQry").on("submit",function(){
            wgMerchantBankConf.list.qryPageData();
            return false;
        });
    },
    qryPageData:function(){
        wgMerchantBankConf.list.getRecords();
        $("#Pagination").empty();
        if(totalElements>0){
            $("#Pagination").pagination(totalElements, {
                callback: wgMerchantBankConf.list.PageCallback,
                prev_text: '上一页',
                next_text: '下一页',
                items_per_page: $("input[name='size']").val(),
                num_display_entries: 5,
                current_page: $("input[name='page']").val(),
                load_first_page:false
            });
        }
    },

    getRecords:function(){
        ucf.ajax.commonFn(ctx+"/wgMerchantBankConf/list",$("#frmQry").serialize(),function(result){

            if(result.status =='OK'){
                totalElements = result.message.totalElements;
                $('#tblData tbody').empty();//移除所有的数据行
                var _noRecord = $("<tr>").append($("<td>").attr({
                    "colspan": $("#tblData tr th").length,
                    "style": "text-align: center"
                }).html("没有搜索到数据"));
                if (typeof result.message.content == "undefined" || result.message.content.length == 0) {
                    $("#tblData tbody").append(_noRecord);
                } else {
                    $.each(result.message.content, function (index, obj) {

                        var _tr = $("<tr>").attr({id:'row_'+obj.merchantNo+'_'+obj.bankCode});
                        $("<td>").append($("<a>").attr({
                            "href": ctx + "/wgMerchantBankConf/" + obj.merchantNo + '/' + obj.bankCode + ucf.common.getUrlRandom(),
                            "data-toggle": "modal",
                            "data-target": "#myModal"
                        }).html(obj.merchantNo + " | " + obj.merchantName)).appendTo(_tr);
                        $("<td>").html(_wgBank[obj.bankCode]).appendTo(_tr);
                        $("<td>").html(_wgChannel[obj.channelId]).appendTo(_tr);
                        $("<td>").html(obj.remark).appendTo(_tr);
                        $("#tblData tbody").append(_tr);
                    });
                }
            }else{
                $.pnotify({title: result.message,type: 'error'});
            }
        });
    },

    PageCallback:function(pageIndex,jq) {
        $("input[name='page']").val(pageIndex);
        wgMerchantBankConf.list.getRecords();
    }
};
wgMerchantBankConf.info={
    init:function(){
        $("#frmEdit select").select2({minimumResultsForSearch: Infinity,width:'100%'});
        wgMerchantBankConf.merchantInit();
        $("#delBtn").click(function(){
            wgMerchantBankConf.info.delObj();
        });

        $("#frmEdit").validate({
            rules: {
                merchantNo:{
                    required: true
                },
                channelId:{
                    required: true
                },
                bankCode:{
                    required: true
                }
            },
            submitHandler:function(form){
                ucf.ajax.commonFn($('#frmEdit').attr('action'), $('#frmEdit').serialize(),function(result){
                    if(result.status =='OK'){
                        wgMerchantBankConf.list.getRecords();
                        $('[data-dismiss]').click();
                        $.pnotify({title: result.message,type: result.status});
                    }else{
                        $("#saveBtn").attr("data-content",result.message);
                        $("#saveBtn").popover('toggle');
                        setTimeout(function(){$("#saveBtn").popover('toggle');}, 2000);
                    }
                });
            }
        });
    },
    delObj:function(){
        var merchantNo = $("input[name=merchantNo]").val();
        var bankCode = $("input[name=bankCode]").val();
        var _param={_method:"delete",merchantNo:merchantNo,bankCode:bankCode};
        confirm_dialog("操作确认","确定删除吗？",function(notice, val){
            if(notice){
                ucf.ajax.commonFn($('#frmEdit').attr('action'), _param,function(result){
                    if(result.status =='OK'){
                        $('#row_'+merchantNo+'_'+bankCode).remove();
                        $('[data-dismiss]').click();
                    }else{
                        $("#delBtn").attr("data-content",result.message);
                        $("#delBtn").popover('toggle');
                        setTimeout(function(){$("#delBtn").popover('toggle');}, 2000);
                    }
                });
            }
        });
    }
};
wgMerchantBankConf.merchantInit=function(){
    $("#merchantNo").select2({
        width: "100%",
        language: "zh-CN",
        placeholder: "输入业务编码或名称",
        cache: "true",
        allowClear: true,
        ajax: {
            url: ctx + "/merchant/map",
            dataType: 'json',
            type: "POST",
            delay: 250,
            data: function (params) {
                return {
                    likeKey: params.term,
                    page: params.page,
                    size: 20
                };
            },
            processResults: function (data, page) {
                if (data.status == "OK") {
                    return {
                        results: $.map(data.message, function (value, key) {
                            return {id: key, text: value};
                        })
                    };
                } else {
                    alert(data.message);
                }
            }

        },
        escapeMarkup: function (markup) {
            return markup;
        },
        minimumInputLength: varSearchKeyMinLength,
        maximumInputLength: 20
    });
}