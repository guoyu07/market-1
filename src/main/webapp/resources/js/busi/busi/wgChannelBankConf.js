if (typeof wgChannelBankConfList == 'undefined') {
    wgChannelBankConfList = {};
}
var totalElements=0;

wgChannelBankConfList.list={
    init:function(){
        $("#channelId").select2({data: _wgChannelArr, width: '100%'});
        $("#bankCode").select2({data: _wgBankArr, width: '100%'});
        $("#busiCode").select2({data:_busiCodeArr,minimumResultsForSearch: Infinity, width: '100%'});
        $("#status").select2({minimumResultsForSearch: Infinity, width: '100%'});
        wgChannelBankConfList.list.qryPageData();
        $("#btnQry").on("click", function () {
            $("input[name='page']").val(0);
            wgChannelBankConfList.list.qryPageData();
        });
        $("#frmQry").on("submit",function(){
            wgChannelBankConfList.list.qryPageData();
            return false;
        });
    },
    qryPageData:function(){
        wgChannelBankConfList.list.getRecords();
        $("#Pagination").empty();
        if(totalElements>0){
            $("#Pagination").pagination(totalElements, {
                callback: wgChannelBankConfList.list.PageCallback,
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
        ucf.ajax.commonFn(ctx+"/wgChannelBankConf/list",$("#frmQry").serialize(),function(result){

            if(result.status =='OK'){
                totalElements = result.message.totalElements;
                $('#tblData tbody').empty();//移除所有的数据行
                var html="",defHtml='<tr><td colspan="20" style="text-align: center">没有搜索到数据</td></tr>';
                if (typeof result.message.content != "undefined")
                    $.each(result.message.content,function(index,obj){
                        html += '<tr id="row_'+obj.channelId+'_'+obj.bankCode+'_'+obj.busiCode+'">'
                        +'<td><a href="'+ ctx +'/wgChannelBankConf/'+obj.channelId+'/'+obj.bankCode+'/'+obj.busiCode + ucf.common.getUrlRandom() +'" data-toggle="modal" data-target="#myModal">'+_wgChannel[obj.channelId]+'</a></td>'
                        +'<td>'+_wgBank[obj.bankCode]+'</td>'
                        +'<td>'+obj.channelBankId+'</td>'
                        +'<td>'+obj.rateTypeLable+'</td>'
                        +'<td>'+obj.rateValue+'</td>'
                        +'<td>'+obj.minAmount+'</td>'
                        +'<td>'+obj.maxAmount+'</td>'
                        +'<td>'+_busiCode[obj.busiCode]+'</td>'
                        +'<td>'+obj.remark+'</td>'
                        +'<td>'+obj.statusLabel+'</td>'
                        +'</tr>';
                    });
                $("#tblData tbody").append(html==""?defHtml:html);
            }else{
                $.pnotify({title: result.message,type: 'error'});
            }
        });
    },

    PageCallback:function(pageIndex,jq) {
        $("input[name='page']").val(pageIndex);
        wgChannelBankConfList.list.getRecords();
    }

};
wgChannelBankConfList.info={
    init:function(){
        $("#frmEdit select").select2({minimumResultsForSearch: Infinity,width:'100%'});
        $("#bankCode").select2({width:'100%'});
        $("#delBtn").click(function(){
            wgChannelBankConfList.info.delObj();
        });

        $("select[name=rateType]").change(function(){
            if($(this).val()==1){
                $(this).parent().next().html("费率(%):");
                $("input[name=rateValue]").attr("placeholder","百分比，1%输入1。");
            }else if($(this).val()==2){
                $(this).parent().next().html("金额(元):");
                $("input[name=rateValue]").attr("placeholder","具体的金额。");
            }

        });
        $("#frmEdit").validate({
            rules: {
                channelBankId:{
                    required: true
                },
                rateValue:{
                    required: true,
                    isDecimal:true
                },
                minAmount:{
                    isDecimal:true
                },
                maxAmount:{
                    isDecimal:true,
                    twoNumVerify: function () {
                        return {"num": $("input[name='minAmount']").val(), "type": "L"};
                    }
                }
            },
            messages: {
                maxAmount: {
                    "twoNumVerify": "封顶金额必须大于保底金额"
                }
            },
            submitHandler:function(form){
                ucf.ajax.commonFn($('#frmEdit').attr('action'), $('#frmEdit').serialize(),function(result){
                    if(result.status =='OK'){
                        wgChannelBankConfList.list.getRecords();
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

        var channelId = $("input[name=channelId]").val();
        var bankCode = $("input[name=bankCode]").val();
        var busiCode = $("input[name=busiCode]").val();
        var _param={_method:"delete",channelId:channelId,bankCode:bankCode,busiCode:busiCode};
        confirm_dialog("操作确认","确定删除吗？删除后数据不可恢复！",function(notice, val){
            if(notice){
                ucf.ajax.commonFn($('#frmEdit').attr('action'), _param,function(result){
                    if(result.status =='OK'){
                        $("#row_"+channelId+'_'+bankCode+'_'+busiCode).remove();
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
}